package sample;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import sample.controllers.DataController;
import sample.controllers.KeyController;
import sample.enums.Direction;
import sample.enums.PlayerType;
import sample.world.GameObject;
import sample.interfaces.InputSystem;
import sample.world.MoveableObject;
import sample.world.PlayerObject;
import sample.world.SwordObject;

import java.util.ArrayList;


public class GameLoop extends Thread implements Runnable {


    private double diffSeconds;
    private Canvas canvas;
    private GraphicsContext gc;

    public static ArrayList<GameObject> gameObjects;

    private PlayerObject player1;
    private PlayerObject player2;

    private SwordObject sword1;
    private SwordObject sword2;

    public GameLoop() {
        this.canvas = Main.canvas;
        initialize();
    }


    private void initialize() {
        KeyController.getInstance();
        DataController.getInstance();

        gc = canvas.getGraphicsContext2D();
        gameObjects = new ArrayList<GameObject>();

        player1 = new PlayerObject(100,100, PlayerType.PLAYER_ONE, Direction.RIGHT);
        player2 = new PlayerObject(300, 100, PlayerType.PLAYER_TWO, Direction.RIGHT);
        gameObjects.add(player1);
        gameObjects.add(player2);

        sword1 = new SwordObject(400,400, Direction.RIGHT, player1);
        sword2 = new SwordObject(400,400, Direction.RIGHT, player2);
        gameObjects.add(sword1);
        gameObjects.add(sword2);
    }

    public void run() {

        long lastTick = System.currentTimeMillis();

        while (true) {

            // Elapsed time
            long currentTick = System.currentTimeMillis();
            diffSeconds = (currentTick - lastTick) / 1000.0;
            lastTick = currentTick;


            update();
            clearScreen();
            draw();


            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    private void update() {
        // TODO NOCH UNTERSCHIEDE, vlt fehler
        for (GameObject obj : gameObjects) {
            if(obj instanceof MoveableObject){
                if(obj instanceof InputSystem){
                    ((InputSystem) obj).processInput();
                }
                ((MoveableObject) obj).update(diffSeconds);
            }
            obj.update();
        }
        KeyController.getInstance().updateKeyController();
    }


    private void draw() {
        for (GameObject obj : gameObjects) {
            obj.draw(gc);
        }
    }

    private void clearScreen()
    {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
