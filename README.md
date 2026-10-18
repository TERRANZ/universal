# Universal

Universal is an experimental, multiplayer 3D game prototype written in Java. It pairs a
**JMonkeyEngine 3** desktop client with a set of **Netty 4** backend services that handle
login, characters, chat and world simulation behind a single routing front end.

The codebase is intentionally small and low-level: the network protocol, packet
serialization, entity model and persistence are all hand-rolled rather than built on a
game framework or engine networking layer.

> Status: prototype / research project. Expect rough edges (see
> [Known issues](#known-issues)).

## Table of contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Repository layout](#repository-layout)
- [Prerequisites](#prerequisites)
- [Building](#building)
- [Running](#running)
- [Configuration](#configuration)
- [Networking protocol](#networking-protocol)
- [Entities](#entities)
- [World modules](#world-modules)
- [Persistence](#persistence)
- [Client](#client)
- [Docker](#docker)
- [Development](#development)
- [Known issues](#known-issues)

## Overview

- **One entry point for clients.** All client traffic lands on the *frontend*, which
  forwards packets to the backend service that owns the packet's operation code.
- **Service per domain.** Login, character boot, chat and world simulation each run as a
  separate process and register their opcode range with the frontend on startup.
- **Custom binary protocol.** Packets are identified by a 2-byte opcode and carry the
  sender's GUID plus a packet-specific payload.
- **JSON persistence.** Accounts and characters are stored as JSON files, with passwords
  hashed using MD5.
- **JME3 client.** The client renders the world with JMonkeyEngine and Bullet physics, and
  streams movement to the world server.

## Architecture

```
        TCP 12345                         TCP 12346
 ┌──────────────┐                  ┌──────────────────┐
 │    Client    │                  │  Backend service │  (login / char / chat / world)
 │  (JME3 3D)   │                  │   InterserverWorker
 └──────┬───────┘                  └────────┬─────────┘
        │                                   │  registers opcode range
        │ connect                           │  (ISMSG_REG)
        ▼                                   ▼
 ┌──────────────────────────────────────────────────────┐
 │                      Frontend                        │
 │  FrontEndServerWorker :12345  (clients)              │
 │  InterserverFEWorker  :12346  (backend services)     │
 │                                                      │
 │  Opcode router: 1-10 login · 11-100 char             │
 │                 101-200 world · 201-300 chat         │
 └──────────────────────────────────────────────────────┘
```

The frontend never interprets payloads. It reads only the opcode (and, for world opcodes,
the target world name) and routes the raw packet to the owning backend channel.

### Services

| Module | Opcode range | Responsibility |
|--------|--------------|----------------|
| `frontend` | - | Listens on `12345` (clients) and `12346` (interservers), routes packets by opcode, tracks connected characters and assigns temporary GUIDs |
| `loginserver` | 1-10 | Authenticates accounts, auto-registers unknown logins, hashes passwords, assigns a permanent GUID |
| `charserver` | 11-100 | Loads/creates the player character and its world, validates the chosen world, pushes updates back to persistence |
| `worldserver` | 101-200 | Runs a world tick, tracks players, broadcasts movement/teleports, spawns entities |
| `chatserver` | 201-300 | Relays "say" and "wisp" (private) chat messages |

### Connection flow

1. Client connects to the frontend on `12345` and receives `SMSG_OK` carrying a temporary GUID.
2. Client sends `CMSG_LOGIN`; login server verifies or creates the account and issues the real GUID (`ISMSG_CHAR_REG` + `SMSG_OK`).
3. Client requests `CSMG_BOOT_ME`; the character server returns `SMSG_CHAR_BOOT` with the player and the list of worlds.
4. Client sends `CMSG_SELECT_SERVER`; the character server asks the frontend whether the world is live (`ISMSG_IS_WORLD_AVAIL`) and, if so, sends `ISMSG_CHAR_IN_WORLD`.
5. The world server adds the player and replies with `SMSG_WORLD_STATE` (all entities and players). Movement is broadcast via `MSG_MOVE` / `MSG_MOVE_TELEPORT`.

## Repository layout

```
universal/
├── client/                       # Desktop client (Maven)
│   ├── pom.xml
│   ├── lib/                      # Bundled JME3 / LWJGL / Netty JARs (added via addjars plugin)
│   │   ├── assets.jar            # Scenes + textures, packaged as a classpath resource
│   │   └── lwjgl/native/         # Linux + Windows LWJGL natives
│   ├── jmeproj/                  # Legacy NetBeans JME project + raw assets (Scenes, Textures)
│   └── src/main/java/ru/terra/universal/client/
│       ├── Main.java             # Entry point (starts the Swing login window)
│       ├── game/                 # GameManager, GameView, GameStateHolder
│       ├── gui/                  # GuiManager, LoginWindow
│       │   └── jmonkey/          # JMEGameViewImpl (SimpleApplication)
│       ├── network/              # ClientWorker, GUIDHOlder
│       └── test/                 # Headless client test harness
├── server2/                      # Backend (Gradle multi-project)
│   ├── settings.gradle           # shared, frontend, loginserver, charserver, worldserver, chatserver
│   ├── build.gradle              # Common deps + fat-jar config for all subprojects
│   ├── shared/                   # Packets, entities, persistence, config, network core
│   ├── frontend/
│   ├── loginserver/
│   ├── charserver/
│   ├── worldserver/
│   ├── chatserver/
│   ├── saves/                    # Runtime character/account storage (created on demand)
│   ├── Dockerfile                # Shared image definition for all services
│   ├── docker-compose.yml
│   └── build.sh                  # Builds all service images locally
├── docker-compose.yml            # Top-level compose (uses universal/<service>:latest)
├── server.properties             # Default frontend host for the root run
├── liblwjgl64.so / libopenal64.so    # Linux natives (repo root copies)
└── lwjgl64.dll / OpenAL64.dll        # Windows natives (repo root copies)
```

## Prerequisites

- **JDK 21** - the server builds and runs on Java 21 (configured as a Gradle toolchain);
  the Maven client still targets the older Java 8 level.
- **Gradle** - a wrapper is included in `server2/` (Gradle 9.8.1), so no local install is required.
- **Maven 3.x** - for building the client.
- **A desktop environment with 3D acceleration** - required to run the JME3 client.
- **LWJGL / OpenAL natives** - bundled for Linux and Windows under `client/lib/lwjgl/native/`.

## Building

### Server (Gradle)

```bash
cd server2
./gradlew build
```

Each module produces a **fat JAR** (all compile dependencies shaded in) named
`<module>-1.0.0.jar` under `<module>/build/libs/`, e.g.:

```
server2/frontend/build/libs/frontend-1.0.0.jar
server2/loginserver/build/libs/loginserver-1.0.0.jar
server2/charserver/build/libs/charserver-1.0.0.jar
server2/worldserver/build/libs/worldserver-1.0.0.jar
server2/chatserver/build/libs/chatserver-1.0.0.jar
```

Key dependencies (declared once in `server2/build.gradle` and inherited by every module):

| Dependency | Version | Purpose |
|------------|---------|---------|
| `io.netty:netty-all` | 4.2.19.Final | NIO networking + codecs |
| `org.apache.logging.log4j:log4j-api` / `log4j-core` | 2.26.1 | Logging (Log4j 2) |
| `org.reflections:reflections` | 0.10.2 | Runtime scanning of `@Packet` / `@Module` classes |
| `net.sf.flexjson:flexjson` | 3.3 | JSON serialization |
| `org.apache.commons:commons-configuration2` | 2.15.1 | `server.properties` loading |
| `org.junit.jupiter:junit-jupiter` | 5.14.4 | Tests (test scope) |

### Client (Maven)

```bash
cd client
mvn package
```

This compiles against `ru.terra.universal:shared:1` and bundles everything
(`lib/*.jar` via `addjars-maven-plugin`, plus `maven-assembly-plugin`'s
`jar-with-dependencies` descriptor) into:

```
client/target/client-1-jar-with-dependencies.jar
```

> The `shared` artifact is consumed from your local Maven repository. If Maven cannot
> resolve `ru.terra.universal:shared:1`, install a matching JAR first
> (`mvn install:install-file ...`).

## Running

### Server

Run the frontend first, then any combination of backend services. Each service is a fat
JAR whose manifest `Main-Class` is `ru.terra.universal.server.Main`:

```bash
java -jar server2/frontend/build/libs/frontend-1.0.0.jar       # binds 0.0.0.0:12345 and 0.0.0.0:12346
java -jar server2/loginserver/build/libs/loginserver-1.0.0.jar
java -jar server2/charserver/build/libs/charserver-1.0.0.jar
java -jar server2/worldserver/build/libs/worldserver-1.0.0.jar
java -jar server2/chatserver/build/libs/chatserver-1.0.0.jar
```

Alternatively, `./gradlew :<module>:run` from `server2/`.

Each backend service reads `frontend.ip`/`persistence.*` from a `server.properties` on the
classpath (or the working directory), then opens a single persistent client connection to
the frontend on port `12346` (`NetworkThread` -> `NetworkManager`).

### Client

```bash
java -jar client/target/client-1-jar-with-dependencies.jar
```

If the LWJGL natives are not found, point the JVM at them:

```bash
java -Djava.library.path=client/lib/lwjgl/native/linux \
     -jar client/target/client-1-jar-with-dependencies.jar
```

The client opens a Swing login window (Russian labels) where you enter the **server IP**,
click **Вход** to connect, pick a world from the list and click **Вход в мир** to launch
the 3D view. See [Client](#client) for details.

## Configuration

Server settings are read through `ru.terra.universal.shared.config.Config`, a singleton
wrapper around Apache Commons Configuration backed by `server.properties`
(auto-save enabled).

| Key | Default | Meaning |
|-----|---------|---------|
| `frontend.ip` | `frontend` | Host the backend service connects to |
| `persistence.dir` | `saves/` | Directory for JSON storage |
| `persistence.characters.filename` | `characters.json` | Characters file name |
| `persistence.accounts.filename` | `accounts.json` | Accounts file name |

The inter-server port is a constant: `ConfigConstants.FRONTEND_PORT = 12346`.

Each module ships its own `src/main/resources/server.properties`. The root
`server.properties` sets `frontend.ip=127.0.0.1` for local runs.

## Networking protocol

All traffic uses the same framing; only the opcode ranges differ between services.
Serialization is done directly against Netty's `ByteBuf` from `AbstractPacket`.

### Frame format

```
+--------+---------------------+-------------------------------+
| opcode |     sender GUID     |            payload            |
| 2 byte |        8 byte       |           variable            |
+--------+---------------------+-------------------------------+
```

- **opcode** - written with `writeChar` / read with `readUnsignedShort`.
- **sender GUID** - a `long` identifying the player the packet belongs to (`0` means
  "broadcast to all players").
- **strings** - encoded as a `short` length followed by UTF-16 code units
  (`writeString` / `readString`). Note: empty strings write no length prefix.

### Opcode map

Ranges are declared in `shared/.../constants/OpCodes.java`:

| Range | Owner |
|-------|-------|
| 1-10 | Login server |
| 11-100 | Character server |
| 101-200 | World server |
| 201-300 | Chat server |
| 501-507 | Server responses (routed by sender) |
| 10001+ | Inter-server control |

#### Client to server

| Opcode | Value | Packet | Description |
|--------|-------|--------|-------------|
| `CMSG_LOGIN` | 2 | `LoginPacket` | Login + password |
| `CMSG_LOGOUT` | 3 | `LogoutPacket` | Logout |
| `CSMG_BOOT_ME` | 4 | `BootMePacket` | Request character boot |
| `CMSG_SELECT_SERVER` | 11 | `SelectServerPacket` | Choose a world to enter |
| `MSG_MOVE` | 106 | `MovementPacket` | Movement direction/vector |
| `MSG_MOVE_TELEPORT` | 107 | `PlayerTeleportPacket` | Absolute position update |
| `CMSG_SAY` | 202 | `ChatSayPacket` | Chat message |
| `CMSG_WISP` | 203 | `WispSayPacket` | Private message (see [Known issues](#known-issues)) |

#### Server to client

| Opcode | Value | Packet | Description |
|--------|-------|--------|-------------|
| `PLAYER_IN` | 102 | `PlayerLoggedInPacket` | Another player entered the world |
| `PLAYER_OUT` | 103 | `PlayerLoggedOutPacket` | Another player left the world |
| `ENTITY_ADD` | 104 | `EntityAddPacket` | Entity added to the world |
| `ENTITY_DEL` | 105 | `EntityDelPacket` | Entity removed from the world |
| `SMSG_OK` | 501 | `OkPacket` | Generic acknowledgement / GUID assignment |
| `SMSG_CHAR_BOOT` | 502 | `CharBootPacket` | Player character + available worlds |
| `SMSG_UPDATE` | 503 | `UpdatePacket` | Field update (`UpdateField.HP/MP/XP/LEVEL`) |
| `SMSG_WORLD_STATE` | 505 | `WorldStatePacket` | Full snapshot of entities and players |
| `SMSG_LOGIN_FAILED` | 506 | `LoginFailedPacket` | Login rejected (+ reason) |
| `SMSG_WORLD_NOT_AVAIL` | 507 | `WorldIsNotAvail` | Selected world is offline |

#### Inter-server

| Opcode | Value | Packet | Description |
|--------|-------|--------|-------------|
| `ISMSG_HELLO` | 10001 | `HelloPacket` | Greeting |
| `ISMSG_REG` | 10002 | `RegisterPacket` | Register an opcode range (and world name for world servers) |
| `ISMSG_UNREG` | 10003 | - | Unregister (not implemented) |
| `ISMSG_BOOT_CHAR` | 10004 | `BootCharPacket` | Character boot request |
| `ISMSG_CHAR_REG` | 10005 | `CharRegPacket` | Rebind temporary GUID to real character GUID |
| `ISMSG_CHAR_IN_WORLD` | 10006 | `CharInWorldPacket` | Player entered a world |
| `ISMSG_REG_WORLD` | 10007 | `RegWorldPacket` | Register a world |
| `ISMSG_UNREG_CHAR` | 10008 | `UnregCharPacket` | Character disconnected |
| `ISMSG_UPDATE_CHAR` | 10010 | `UpdatePlayerPacket` | Persist updated character |
| `ISMSG_IS_WORLD_AVAIL` | 10011 | `ISWorldAvaiPacket` | World availability query/response |

Packets are discovered at runtime by `PacketFactory`, which scans
`ru.terra.universal.shared.packet` for classes annotated with `@Packet(opCode = ...)`.

## Entities

### `AbstractEntity`

Base class for anything placeable in a world:

| Field | Type | Description |
|-------|------|-------------|
| `x`, `y`, `z` | `Float` | Position |
| `h`, `r` | `Integer` | Heading / roll |
| `map` | `Integer` | Map id |
| `UID` | `Long` | Unique id |
| `model` | `String` | Model name (`"default"`) |
| `state` | `Integer` | State flag |

### `PlayerInfo`

Extends `AbstractEntity` and adds `name`, `world`, `hp`, `mp`, `xp` and `level`
(default `1`). It is `Serializable` and provides `writePlayerInfo` / `readPlayerInfo`
helpers alongside the entity helpers.

### `AccountInfo`

| Field | Description |
|-------|-------------|
| `login` | Username |
| `pass` | MD5 hash of the password |
| `banned` | Ban flag |
| `lastLogin` | Last login `Date` |
| `banTime` | Ban timestamp |
| `attemptsToLogin` | Failed attempt counter |
| `uid` | Permanent account GUID |

## World modules

Worlds tick through `WorldThread` (every 1 ms), which drives the `World.tick()` loop.
Modules are discovered via `ClassSearcher` scanning `ru.terra.universal.worldserver.module`
for classes annotated with `@Module("name")`:

- **`MobsModule`** (`@Module("mobs")`) - placeholder for non-player entity logic.
- **`StatisticModule`** (`@Module("statistic")`) - tracks tick timing.

A module extends `WorldModule` and implements `tick(World world)`.

## Persistence

`FilePersister` resolves the storage directory from config and creates it if necessary.
Two implementations use FlexJSON:

| Interface | Implementation | Stores |
|-----------|----------------|--------|
| `CharLoader` / `CharSaver` | `JsonCharLoaderImpl` / `JsonCharSaverImpl` | Accounts and characters |
| `WorldLoader` / `WorldSaver` | `JsonWorldLoaderImpl` / `JsonWorldSaverImpl` | World entities |

- Accounts are **auto-created on first login** with an MD5-hashed password
  (`CryptoUtil.encryptMD5`).
- Writes are read-modify-write: the saver loads the current file, replaces the matching
  entry (by `uid` for players, by `login` for accounts) and rewrites the file.
- Default location is `saves/` relative to the working directory, containing
  `accounts.json` and `characters.json`.

A sample `server2/saves/accounts.json` shows the on-disk shape (FlexJSON adds a `class`
field).

## Client

### Frameworks

- **JMonkeyEngine 3** for rendering (`SimpleApplication`), terrain, and water filter.
- **jBullet** for physics: a `CharacterControl` per player and a `RigidBodyControl` for the
  scene mesh.

### Flow and classes

- `Main` -> `GuiManager.startLoginWindow()`.
- `LoginWindow` (Swing) collects the server IP, connects via `NetworkManager`, and lets the
  player pick a world and enter it.
- `ClientWorker` handles incoming packets and drives the
  `GameStateHolder.GameState` machine: `INIT -> LOGIN -> LOGGED_IN -> CHAR_BOOT ->
  SERVER_SELECTED -> IN_WORLD`.
- `GameManager` tracks the local `PlayerInfo` and sends movement/teleport packets.
- `GameView` -> `JMonkeyGameView` -> `JMEGameViewImpl` wraps the JME app and exposes
  `entityAdd`, `enemyLoggedIn`, `entityVectorMove`, etc.

> Credentials are currently hard-coded in `ClientWorker` (`mylogin` / `mypass`); the login
> window only sends the server IP.

### Controls

| Key | Action |
|-----|--------|
| `W` | Move forward |
| `S` | Move back |
| `A` | Move left |
| `D` | Move right |
| `Q` | Raise camera |
| `Z` | Lower camera |

The scene is loaded from `Scenes/<world>.j3o`. `assets.jar` bundles two sample scenes
(`map_0`, `newScene`), terrain textures and `waterFilter.j3f`; the raw files also live in
`client/jmeproj/assets`.

## Docker

There are two compose files:

- **`server2/docker-compose.yml`** - builds/uses local images tagged `frontend:1.0.0`,
  `loginserver:1.0.0`, `charserver:1.0.0`, `worldserver:1.0.0`, `chatserver:1.0.0`. The
  frontend publishes `12345` and `12346`; backends `depends_on` it and reach it by the
  `frontend` hostname.
- **Root `docker-compose.yml`** - the same topology but referencing
  `universal/<service>:latest` images.

The shared `server2/Dockerfile` runs on `eclipse-temurin:21-jre` and launches
`/app/app.jar`. Build all images locally with:

```bash
cd server2
./build.sh
```

or with the Palantir Gradle Docker plugin configured in `server2/docker.gradle`.

## Development

### Add a packet

1. Add the opcode constant to `OpCodes.java` (in the right range).
2. Create a class extending `AbstractPacket` annotated with `@Packet(opCode = ...)`.
3. Implement `get(ByteBuf)` for deserialization and `send(ByteBuf)` for
   serialization.
4. Handle it in the owning `Worker.acceptPacket(...)` (e.g. `LoginWorker`, `CharWorker`,
   `WorldserverWorker`, `ChatWorker`, or `FrontEndServerWorker`).

`PacketFactory` picks it up automatically via reflection.

### Add a world module

1. Create a class in `ru.terra.universal.worldserver.module` annotated with
   `@Module("your-name")` extending `WorldModule`.
2. Implement `tick(World world)`.

### Add a world

1. Add a `.j3o` scene (plus its `.j3odata` and terrain-alpha textures) to the client
   assets (and repackage `assets.jar`, or point the asset manager at a directory).
2. Register the world name from the world server's `ISMSG_REG` (`RegisterPacket.setWorld`) and
   make sure the character server offers it in `SMSG_CHAR_BOOT`.

## Known issues

These are real behaviors visible in the current code and worth knowing before extending it:

- **`WispSayPacket` is annotated with `CMSG_SAY` (202) instead of `CMSG_WISP` (203).** It
  therefore collides with `ChatSayPacket` in `PacketFactory`, so private messages cannot be
  distinguished from public ones. (The chat server also logs the wrong opcode path.)
- **Empty strings are not length-prefixed.** `writeString` returns early for empty values,
  so a reader expecting a `short` length would desynchronize.
- **The frontend routes world packets only by opcode and world name**, and
  `WorldServersChannelsHolder` is keyed by world, so multiple worlds per server are not
  modelled yet.
- **Client login is hard-coded** (`mylogin`/`mypass` in `ClientWorker`); the Swing window
  only captures the server IP.
- **`WorldThread` ticks every millisecond** with no fixed timestep, so it is CPU-heavy for
  what it does.
- **The client Maven build depends on `ru.terra.universal:shared:1`**, which is not produced
  by the Gradle build (which versions everything `1.0.0`); it must be present in the local
  Maven repository.
- **Native libraries are checked into the repo root and duplicated under `client/lib`** for
  convenience rather than resolved per-platform.
