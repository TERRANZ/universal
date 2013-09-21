package ru.terra.universal.client.gui.jmonkey;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Box;
import com.jme3.terrain.geomipmap.TerrainQuad;
import org.apache.log4j.Logger;
import ru.terra.universal.client.game.GameManager;

import java.util.HashMap;

public class JMEGameViewImpl extends SimpleApplication implements ActionListener {

    private Spatial sceneModel;
    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
    private CharacterControl playerControl;
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;
    private TerrainQuad terrain;
    Material mat_terrain;
    private float currY = 500;

    private CameraNode camNode;
    private float diffY = 50;
    private Geometry controlCube;
    private HashMap<Long, Geometry> entities = new HashMap<>();
    private HashMap<Long, CharacterControl> playerControls = new HashMap<>();
    private Material mat1;
    private boolean isMoving = false;
    private CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
    private Logger logger = Logger.getLogger(getClass());
    private int pressed = 0;
    private BitmapText hudText;

    public void simpleInitApp() {
        /**
         * Set up Physics
         */
        setPauseOnLostFocus(false);
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        // bulletAppState.getPhysicsSpace().enableDebug(assetManager);

        // We re-use the flyby camera for rotation, while positioning is handled
        // by physics
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        // flyCam.setMoveSpeed(100);
        setUpKeys();
        setUpLight();

        // We load the scene from the zip file and adjust its size.
        // assetManager.registerLocator("town.zip", ZipLocator.class);
        sceneModel = assetManager.loadModel("Scenes/newScene.j3o");
        sceneModel.setLocalScale(2f);

        // We set up collision detection for the scene by creating a
        // compound collision shape and a static RigidBodyControl with mass
        // zero.
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) sceneModel);
        landscape = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(landscape);

        // We set up collision detection for the player by creating
        // a capsule collision shape and a CharacterControl.
        // The CharacterControl offers extra settings for
        // size, stepheight, jumping, falling, and gravity.
        // We also put the player in its starting position.

        // We attach the scene and the player to the rootNode and the physics
        // space,
        // to make them appear in the game world.
        rootNode.attachChild(sceneModel);
        bulletAppState.getPhysicsSpace().add(landscape);

        playerControl = addCharacterControl();

        Box b = new Box(5, 5, 5);
        controlCube = new Geometry("Box", b);
        mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        controlCube.setMaterial(mat1);
        controlCube.setLocalTranslation(10, 10, 10);
        rootNode.attachChild(controlCube);
        // bulletAppState.getPhysicsSpace().add(red);
        flyCam.setEnabled(false);
        camNode = new CameraNode("Camera Node", cam);
        // This mode means that camera copies the movements of the target:
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);

        // Move camNode, e.g. behind and above the target:
        camNode.setLocalTranslation(new Vector3f(0, currY, 0));
        // Rotate the camNode to look at the target:
        camNode.lookAt(controlCube.getLocalTranslation(), Vector3f.UNIT_Z);
        // Attach the camNode to the target:
        rootNode.attachChild(camNode);
        controlCube.addControl(playerControl);
        GameManager.getInstance().login();
    }

    private CharacterControl addCharacterControl() {
        CharacterControl ret = new CharacterControl(capsuleShape, 0.05f);
        // player.setJumpSpeed(20);
        // player.setFallSpeed(30);
        ret.setGravity(30);
        ret.setPhysicsLocation(new Vector3f(10, 10, 0));
        bulletAppState.getPhysicsSpace().add(ret);
        return ret;
    }

    private void setUpLight() {
        // We add light so we see the scene
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(dl);
    }

    /**
     * We over-write some navigational key mappings here, so we can add
     * physics-controlled walking and jumping:
     */
    private void setUpKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        // inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Z", new KeyTrigger(KeyInput.KEY_Z));
        inputManager.addMapping("Q", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Q");
        inputManager.addListener(this, "Z");
        // inputManager.addListener(this, "Jump");
    }

    /**
     * These are our custom actions triggered by key presses. We do not walk
     * yet, we just keep track of the direction the user pressed.
     */

    public void onAction(String binding, boolean value, float tpf) {
        if (value == true) {
            pressed++;
        } else {
            pressed--;
        }

        if (pressed == 1 && value && !isMoving) {
            isMoving = true;
            logger.info("Character walking init.");
        } else if (pressed == 0 & !value) {
            isMoving = false;
            logger.info("Character walking end.");
            Vector3f lastPos = playerControl.getPhysicsLocation();
            //GameManager.getInstance().sendPlayerMove(Client.CMSG_MOVE_STOP, lastPos.getX(), lastPos.getY(), lastPos.getZ(), 0);
            // GameManager.getInstance().sendPlayerCurrPos(lastPos.getX(),
            // lastPos.getY(), lastPos.getZ(), 0);
            logger.info("sending STOP");
        }

        if (binding.equals("Left")) {
            left = value;
        } else if (binding.equals("Right")) {
            right = value;
        } else if (binding.equals("Up")) {
            up = value;
        } else if (binding.equals("Down")) {
            down = value;
        } else if (binding.equals("Jump")) {
            // player.jump();
        } else if (binding.equals("Q")) {
            currY = currY + diffY;
            camNode.setLocalTranslation(new Vector3f(0, currY, 0));
        } else if (binding.equals("Z")) {
            currY = currY - diffY;
            camNode.setLocalTranslation(new Vector3f(0, currY, 0));
        }
    }

    /**
     * This is the main event loop--walking happens here. We check in which
     * direction the player is walking by interpreting the camera direction
     * forward (camDir) and to the side (camLeft). The setWalkDirection()
     * command is what lets a physics-controlled player walk. We also make sure
     * here that the camera moves with player.
     */
    @Override
    public void simpleUpdate(float tpf) {
//        Vector3f camDir = cam.getUp().clone().multLocal(0.4f);
//        Vector3f camLeft = cam.getLeft().clone().multLocal(0.4f);
//        walkDirection.set(0, 0, 0);
//        int direction = Client.CMSG_MOVE_TELEPORT;
//        Vector3f dir = new Vector3f(0, 0, 0);
//        if (left) {
//            dir = camLeft;
//            walkDirection.addLocal(dir);
//            direction = Client.CMSG_MOVE_LEFT;
//            sendPlayerMovingVector(dir, direction);
//            // isMoving = true;
//        }
//        if (right) {
//            dir = camLeft.negate();
//            walkDirection.addLocal(dir);
//            direction = Client.CMSG_MOVE_RIGHT;
//            sendPlayerMovingVector(dir, direction);
//            // isMoving = true;
//        }
//        if (up) {
//            dir = camDir;
//            walkDirection.addLocal(dir);
//            direction = Client.CMSG_MOVE_FORWARD;
//            sendPlayerMovingVector(dir, direction);
//            // isMoving = true;
//        }
//        if (down) {
//            dir = camDir.negate();
//            walkDirection.addLocal(dir);
//            direction = Client.CMSG_MOVE_BACK;
//            sendPlayerMovingVector(dir, direction);
//            // isMoving = true;
//        }
//        playerControl.setWalkDirection(walkDirection);
    }

    private void sendPlayerMovingVector(Vector3f dir, int direction) {
//        camNode.setLocalTranslation(controlCube.getLocalTranslation().setY(currY));
//        GameManager.getInstance().sendPlayerMove(direction, dir.getX(), dir.getY(), dir.getZ(), 0);
    }

    public void loadPlayer() {
    }

//    public void addMapObject(MapObject entity) {
//        Box b = new Box(2, 2, 2);
//        Geometry g = new Geometry("Box", b);
//        g.setMaterial(mat1);
//        g.setLocalTranslation(entity.getX(), entity.getY(), entity.getZ());
//        rootNode.attachChild(g);
//        mapObjects.put(entity, g);
//        entities.put(entity.getGuid(), g);
//    }
//
//    public void enemyLoggedIn(final Player enemy) {
//        enqueue(new Callable<Long>() {
//            @Override
//            public Long call() throws Exception {
//                // TODO Auto-generated method stub
//                Sphere s = new Sphere(10, 10, 5);
//                Geometry g = new Geometry("Sphere", s);
//                Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//                g.setMaterial(mat);
//                g.setLocalTranslation(enemy.getX() + 10, enemy.getY() + 10, enemy.getZ() + 10);
//                rootNode.attachChild(g);
//                players.put(enemy, g);
//                entities.put(enemy.getGuid(), g);
//                CharacterControl control = addCharacterControl();
//                g.addControl(control);
//                playerControls.put(enemy.getGuid(), control);
//                return null;
//            }
//        });
//    }
//
//    public void updateEntityPosition(final Entity entity) {
//        enqueue(new Callable<Long>() {
//            @Override
//            public Long call() throws Exception {
//                CharacterControl c = playerControls.get(entity.getGuid());
//                if (c != null) {
//                    c.setPhysicsLocation(new Vector3f(entity.getX(), entity.getY(), entity.getZ()));
//                }
//                return null;
//            }
//        });
//    }
//
//    public void entityVectorMove(final Long guid, final float x, final float y, final float z, float h, final boolean stop) {
//        enqueue(new Callable<Long>() {
//            @Override
//            public Long call() throws Exception {
//                CharacterControl control = playerControls.get(guid);
//                // Logger.getLogger(getClass()).info("guid " + guid + " moving "
//                // + " x = " + x + " y = " + y + " z = " + z);
//                if (control != null) {
//                    if (!stop) {
//                        Vector3f dir = new Vector3f();
//                        dir.addLocal(new Vector3f(x, y, z));
//                        control.setWalkDirection(dir);
//                    } else {
//                        Vector3f dir = new Vector3f();
//                        dir.addLocal(new Vector3f(0, 0, 0));
//                        control.setWalkDirection(dir);
//                        control.setPhysicsLocation(new Vector3f(x, y, z));
//                    }
//                }
//                return null;
//            }
//        });
//    }

    public void serverMessage(String message) {
        if (hudText == null)
            hudText = new BitmapText(guiFont, false);
        hudText.setSize(guiFont.getCharSet().getRenderedSize()); // font size
        hudText.setColor(ColorRGBA.Blue); // font color
        hudText.setText(message); // the text
        hudText.setLocalTranslation(300, hudText.getLineHeight(), 0); // position
        guiNode.attachChild(hudText);
    }

//    public void playerSay(Player player, String message) {
//        if (hudText == null)
//            hudText = new BitmapText(guiFont, false);
//        hudText.setSize(guiFont.getCharSet().getRenderedSize()); // font size
//        hudText.setColor(ColorRGBA.Red); // font color
//        hudText.setText(player.getName() + " : " + message); // the text
//        hudText.setLocalTranslation(300, hudText.getLineHeight(), 0); // position
//        guiNode.attachChild(hudText);
//    }
}
