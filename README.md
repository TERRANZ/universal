# Universal

A multiplayer game server and client framework built in Java with JMonkeyEngine, supporting real-time 3D world traversal, player communication, and persistent game state.

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Building](#building)
- [Running](#running)
- [Networking Protocol](#networking-protocol)
- [Entities](#entities)
- [Modules](#modules)
- [Persistence](#persistence)
- [Client](#client)
- [Configuration](#configuration)
- [Docker Deployment](#docker-deployment)

## Overview

Universal is a client-server multiplayer game system designed for 3D world exploration and interaction. It features:

- **Real-time multiplayer**: Multiple players can traverse shared 3D worlds simultaneously
- **Modular server architecture**: Separate services for login, characters, chat, and world management
- **3D client with physics**: Powered by JMonkeyEngine with Bullet physics engine integration
- **Persistent data**: Account and character data stored in JSON format
- **Custom network protocol**: Efficient packet-based communication using Netty

## Architecture

The system follows a multi-service architecture where each server component runs as an independent process:

```
┌─────────────┐     ┌──────────────┐     ┌──────────────────────────────────────┐
│   Client    │────▶│   Frontend   │────▶│            Server Layer              │
│  (JME 3D)   │     │  (ports      │     │                                      │
│             │◀────│  12345,12346) │     │  ┌──────────┬──────────┬──────────┐ │
│             │     │              │◀────▶│  │ Login    │  Char    │  World   │ │ │
└─────────────┘     └──────────────┘     │  │ Server   │ Server │  Server  │ │ │
                                          │  └──────────┴──────────┴──────────┘ │ │
                                          │  ┌──────────┐                          │ │
                                          │  │ Chat     │                          │ │
                                          │  │ Server   │                          │ │
                                          │  └──────────┘                          │ │
                                          └──────────────────────────────────────┘
```

### Service Roles

| Service | Port | Responsibility |
|---------|------|----------------|
| **Frontend** | 12345 (client), 12346 (interserver) | Entry point for client connections, routes packets to backend services |
| **Login Server** | — | Authenticates accounts, manages sessions, handles account registration |
| **Character Server** | — | Manages character boot (load), world assignment, character state |
| **World Server** | — | Runs game worlds, manages entities, broadcasts player movement and actions |
| **Chat Server** | — | Handles chat messages (say, wisp) between players |

### Communication Layers

- **Client-Frontend**: TCP connections on ports 12345 (direct) and 12346 (interserver), using custom binary protocol
- **Frontend-Backend**: TCP connections between services, using the same custom binary protocol
- **All communication**: Packet-based with operation codes (OpCodes) for message type identification

## Project Structure

```
universal/
├── client/                          # Java client (Maven project)
│   ├── pom.xml
│   ├── lib/                         # JMonkeyEngine and dependency JARs
│   ├── jmeproj/                     # Legacy JME project assets
│   └── src/main/java/ru/terra/universal/client/
│       ├── Main.java                # Client entry point
│       ├── game/                    # Game logic (GameManager, GameState)
│       ├── gui/                     # UI layers (LoginWindow, GameWindow)
│       │   └── jmonkey/             # JME3 rendering implementation
│       ├── network/                 # Client network worker
│       └── test/                    # Client test utilities
├── server/                          # Java server (Gradle multi-project)
│   ├── shared/                      # Shared code (packets, entities, config)
│   │   └── src/main/java/ru/terra/universal/shared/
│   │       ├── entity/              # AccountInfo, PlayerInfo, AbstractEntity
│   │       ├── packet/              # Network packet definitions
│   │       │   ├── client/          # Client-to-server packets
│   │       │   ├── server/          # Server-to-client packets
│   │       │   ├── interserver/     # Inter-service packets
│   │       │   └── movement/        # Movement-related packets
│   │       ├── world/               # World abstractions
│   │       ├── persistance/         # JSON file-based data storage
│   │       ├── config/              # Configuration loader
│   │       ├── constants/           # OpCodes and constants
│   │       └── util/                # Utilities (CryptoUtil)
│   ├── frontend/                    # Frontend server
│   ├── loginserver/                 # Login authentication service
│   ├── charserver/                  # Character management service
│   ├── worldserver/                 # World/game loop service
│   └── chatserver/                  # Chat messaging service
├── saves/                           # Persistent data directory
│   ├── accounts.json                # Account storage
│   └── characters.json              # Character storage
├── server.properties                # Server configuration
├── docker-compose.yml               # Docker deployment
├── liblwjgl64.so                    # Linux LWJGL native library
├── libopenal64.so                   # Linux OpenAL native library
├── lwjgl64.dll                      # Windows LWJGL native library
└── OpenAL64.dll                     # Windows OpenAL native library
```

## Prerequisites

- **Java Development Kit 8** (JDK 8) — both client and server compile with `sourceCompatibility = 1.8`
- **Maven 3.x** — for building the client
- **Gradle** — for building the server
- **LWJGL native libraries** — included for Linux (`*.so`) and Windows (`*.dll`); macOS `.jnilib` files should be added if needed

## Building

### Server

Navigate to the server directory and use Gradle:

```bash
cd server2
./gradlew build
```

The server uses Netty for networking and includes these key dependencies:
- `org.jboss.netty:netty:3.2.9.Final` — network I/O framework
- `log4j:log4j:1.2.17` — logging
- `org.reflections:reflections:0.9.8` — runtime class scanning for modules and packets
- `net.sf.flexjson:flexjson:2.1` — JSON serialization
- `commons-configuration:commons_configuration:1.9` — properties configuration

### Client

Navigate to the client directory and use Maven:

```bash
cd client
mvn package
```

This produces a fat JAR with all dependencies bundled, using the `maven-assembly-plugin` with the `jar-with-dependencies` descriptor. The client depends on the shared server module and uses JMonkeyEngine (jME3) for rendering.

## Running

### Server

Build the server JAR (which is shaded with all dependencies via Gradle's `jar` block configuration). Run each service by specifying its main class:

```bash
# Frontend (ports 12345 for clients, 12346 for interserver communication)
java -cp server.jar ru.terra.universal.frontend.Main

# Login Server
java -cp server.jar ru.terra.universal.loginserver.Main

# Character Server
java -cp server.jar ru.terra.universal.charserver.Main

# World Server
java -cp server.jar ru.terra.universal.worldserver.Main

# Chat Server
java -cp server.jar ru.terra.universal.chatserver.Main
```

### Client

Run the built JAR (after building with Maven):

```bash
java -jar client/target/client-1-jar-with-dependencies.jar
```

The client will display a login window. Upon successful login, it connects to the frontend server (default `127.0.0.1:12345`) and launches the JME3 3D game view when a world is selected.

**Note**: Ensure LWJGL native libraries are in the library path. Set `java.library.path` if needed:

```bash
java -Djava.library.path=client/lib/lwjgl/native/linux -jar client/target/client-1-jar-with-dependencies.jar
```

## Networking Protocol

The custom binary protocol uses operation codes to identify message types. All packets are serialized/deserialized through `AbstractPacket` using Netty's `ChannelBuffer`.

### Packet Format

```
[OpCode: 2 bytes] [Sender GUID: 8 bytes] [Payload: variable]
```

### Operation Code Ranges

| Range | Service |
|-------|---------|
| 1–10 | Login server (authentication) |
| 11–100 | Character server |
| 101–200 | World server (gameplay) |
| 201–300 | Chat server |
| 501–507 | Server response codes |
| 10001–10011 | Inter-service communication |

### Client Packets

| OpCode | Description |
|--------|-------------|
| `CMSG_LOGIN (2)` | Send credentials (login + password) to login server |
| `CMSG_LOGOUT (3)` | Logout from server |
| `CSMG_BOOT_ME (4)` | Request character boot (load all characters for this account) |
| `CMSG_SELECT_SERVER (11)` | Select a world to enter |
| `CMSG_SAY (202)` | Send chat message to all nearby players |
| `CMSG_WISP (203)` | Send private chat message to a specific player |

### Server Packets

| OpCode | Description |
|--------|-------------|
| `SMSG_OK (501)` | General acknowledgment |
| `SMSG_CHAR_BOOT (502)` | Send list of player's characters and available worlds |
| `SMSG_UPDATE (503)` | Generic update packet |
| `SMSG_WORLD_STATE (505)` | Initial world state with all entities and players |
| `SMSG_LOGIN_FAILED (506)` | Login rejection with reason |
| `SMSG_WORLD_NOT_AVAIL (507)` | Selected world is unavailable |

### Movement Packets

| OpCode | Description |
|--------|-------------|
| `MSG_MOVE (106)` | Broadcast player movement direction/position to other players in the world |
| `MSG_MOVE_TELEPORT (107)` | Player teleportation event |

### Inter-Server Packets

| OpCode | Description |
|--------|-------------|
| `ISMSG_HELLO (10001)` | Service registration handshake |
| `ISMSG_REG (10002)` | Register service with frontend |
| `ISMSG_UNREG (10003)` | Unregister service |
| `ISMSG_BOOT_CHAR (10004)` | Request character boot for a user |
| `ISMSG_CHAR_REG (10005)` | Register character with frontend |
| `ISMSG_CHAR_IN_WORLD (10006)` | Notify that a character is in a world |
| `ISMSG_REG_WORLD (10007)` | Register world with frontend |
| `ISMSG_UPDATE_CHAR (10010)` | Update character state |
| `ISMSG_IS_WORLD_AVAIL (10011)` | Check world availability |

## Entities

### AbstractEntity

The base class for all in-world objects. Contains:

- `x, y, z` — 3D position coordinates
- `h, r` — heading and roll rotation values
- `map` — map/scene identifier
- `UID` — unique identifier (Long)
- `model` — model name string
- `state` — entity state integer

### PlayerInfo

Extends `AbstractEntity` with game-specific attributes:

- `name` — player's display name
- `world` — current world/scene name
- `hp` — health points
- `mp` — mana points
- `xp` — experience points
- `level` — player level (default: 1)

### AccountInfo

Authentication and account management:

- `login` — username
- `pass` — password (MD5 hashed)
- `banned` — ban status flag
- `lastLogin` — timestamp of last login
- `banTime` — ban duration
- `attemptsToLogin` — failed login attempt counter
- `uid` — unique account identifier

## Modules

The server uses a plugin-style module system powered by the `@Module` annotation and `ClassSearcher` reflection utility. Modules are automatically discovered at runtime:

- **MobsModule** — Non-player monster/enemy management in worlds
- **StatisticModule** — Player and world statistics tracking

World modules extend `WorldModule` and interface through `WorldControlInterface`, loaded by `WorldWorker` via reflection scanning.

## Persistence

All game data is stored in JSON files in the `saves/` directory using FlexJSON serialization:

| File | Contents |
|------|----------|
| `saves/accounts.json` | All registered accounts |
| `saves/characters.json` | All player characters with their stats and positions |

### Persistence Interfaces

- `CharLoader` / `JsonCharLoaderImpl` — Load accounts and characters
- `CharSaver` / `JsonCharSaverImpl` — Save accounts and characters
- `WorldLoader` / `JsonWorldLoaderImpl` — Load world state
- `WorldSaver` / `JsonWorldSaverImpl` — Save world state
- `FilePersister` — Base interface for file-based persistence

Accounts are auto-created on first login with an MD5-hashed password. World state is loaded from scene files (`.j3o` format) in the client assets.

## Client

### Game Engine

The client uses **JMonkeyEngine 3** (jME3) for 3D rendering and **Bullet Physics** for collision detection and character movement.

### Key Client Components

- **`Main.java`** — Entry point; initializes logging and starts the login window
- **`GuiManager`** — Manages UI states (login screen, world selection, game view)
- **`LoginWindow`** — Authentication UI for entering credentials
- **`GameManager`** — Client-side game state management; handles movement packet sending
- **`GameStateHolder`** — Tracks connection state machine (INIT → LOGIN → LOGGED_IN → CHAR_BOOT → SERVER_SELECTED → IN_WORLD)
- **`JMEGameViewImpl`** — Full 3D game implementation extending `SimpleApplication`

### Controls (WASD)

| Key | Action |
|-----|--------|
| W | Move forward |
| A | Move left |
| S | Move back |
| D | Move right |
| Z | Zoom out camera |
| Q | Zoom in camera |

### World Loading

Worlds are loaded from `.j3o` scene files in the client assets directory (`assets/Scenes/`). The project includes sample scenes:
- `newScene.j3o` / `newScene.j3odata`
- `map_0.j3o` / `map_0.j3odata`

Terrain textures (`.jpg`) and the water filter (`waterFilter.j3f`) define visual appearance.

## Configuration

Server settings are managed through `server.properties` using `Commons Configuration`:

```properties
frontend.ip=127.0.0.1
```

The `Config` class provides a singleton accessor (`Config.getConfig()`) for reading and writing configuration values with auto-save enabled.

## Docker Deployment

The project includes Docker Compose configuration for deploying all services:

```bash
docker-compose -f docker-compose.yml up
```

The compose file defines these services:
- `charserver` — Character management
- `chatserver` — Chat messaging
- `loginserver` — Authentication
- `worldserver` — World/game loop
- `frontend` — Entry point (ports 12345 and 12346 published)

Docker images are expected at `universal/charserver:latest`, `universal/loginserver:latest`, etc. Individual service Dockerfiles are available in the `server2/docker-compose.yml` directory.

## Development

### Adding a New Packet

1. Define the OpCode in `OpCodes.java` interface
2. Create a new class extending `AbstractPacket` with `@Packet(opCode = X)` annotation
3. Implement `get(ChannelBuffer buffer)` for deserialization
4. Implement `send(ChannelBuffer buffer)` for serialization
5. Register packet handling in the appropriate `Worker.acceptPacket()` method

### Adding a New World

1. Create a scene file (`.j3o`) and place it in client `assets/Scenes/`
2. Add terrain textures to `assets/Textures/`
3. Register the world via `ISMSG_REG_WORLD` packet in the world server module
