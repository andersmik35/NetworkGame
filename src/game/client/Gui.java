package game.client;

import game.server.GameLogic;
import game.server.model.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;

import java.util.List;

public class Gui extends Application {
    private static PlayerUpdate[] Players = new PlayerUpdate[0];

    public static final int SIZE = 30;
    public static final int SCENE_HEIGHT = SIZE * 20 + 50;
    public static final int SCENE_WIDTH = SIZE * 20 + 200;

    public static Image ImageFloor;
    public static Image ImageWall;
    public static Image HeroRight, HeroLeft, HeroUp, HeroDown;


    private static Label[][] Fields;
    private static TextArea ScoreList;

    // -------------------------------------------
    // | Maze: (0,0)              | Score: (1,0) |
    // |-----------------------------------------|
    // | boardGrid (0,1)          | scorelist    |
    // |                          | (1,1)        |
    // -------------------------------------------

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        Text mazeLabel = new Text("Maze:");
        mazeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Text scoreLabel = new Text("Score:");
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        ScoreList = new TextArea();

        GridPane boardGrid = new GridPane();

        ImageWall = new Image(getClass().getResourceAsStream("Image/wall4.png"), SIZE, SIZE, false, false);
        ImageFloor = new Image(getClass().getResourceAsStream("Image/floor1.png"), SIZE, SIZE, false, false);

        HeroRight = new Image(getClass().getResourceAsStream("Image/heroRight.png"), SIZE, SIZE, false, false);
        HeroLeft = new Image(getClass().getResourceAsStream("Image/heroLeft.png"), SIZE, SIZE, false, false);
        HeroUp = new Image(getClass().getResourceAsStream("Image/heroUp.png"), SIZE, SIZE, false, false);
        HeroDown = new Image(getClass().getResourceAsStream("Image/heroDown.png"), SIZE, SIZE, false, false);

        Fields = new Label[20][20];
        for (int j = 0; j < 20; j++) {
            for (int i = 0; i < 20; i++) {
                switch (Generel.board[j].charAt(i)) {
                    case 'w':
                        Fields[i][j] = new Label("", new ImageView(ImageWall));
                        break;
                    case ' ':
                        Fields[i][j] = new Label("", new ImageView(ImageFloor));
                        break;
                    default:
                        throw new Exception("Illegal field value: " + Generel.board[j].charAt(i));
                }
                boardGrid.add(Fields[i][j], i, j);
            }
        }
        System.out.println("Board created");
        ScoreList.setEditable(false);

        grid.add(mazeLabel, 0, 0);
        grid.add(scoreLabel, 1, 0);
        grid.add(boardGrid, 0, 1);
        grid.add(ScoreList, 1, 1);

        Scene scene = new Scene(grid, SCENE_WIDTH, SCENE_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case UP:
                    playerMoved(0, -1, "up");
                    break;
                case DOWN:
                    playerMoved(0, +1, "down");
                    break;
                case LEFT:
                    playerMoved(-1, 0, "left");
                    break;
                case RIGHT:
                    playerMoved(+1, 0, "right");
                    break;
                case ESCAPE:
                    System.exit(0);
                default:
                    break;
            }
        });

        List<Player> players = GameLogic.getInstance().getPlayers();
        // Putting default players on screen
        for (Player player : players) {
            Fields[player.getXpos()][player.getYpos()].setGraphic(new ImageView(HeroUp));
        }
        ScoreList.setText(getScoreList());
    }

    public static void removePlayerOnScreen(Pair oldpos) {
        Platform.runLater(() -> {
            Fields[oldpos.getX()][oldpos.getY()].setGraphic(new ImageView(ImageFloor));
        });
    }

    public static void placePlayerOnScreen(Pair newpos, String direction) {
        Platform.runLater(() -> {
            int newx = newpos.getX();
            int newy = newpos.getY();
            if (direction.equals("right")) {
                Fields[newx][newy].setGraphic(new ImageView(HeroRight));
            }

            if (direction.equals("left")) {
                Fields[newx][newy].setGraphic(new ImageView(HeroLeft));
            }
            if (direction.equals("up")) {
                Fields[newx][newy].setGraphic(new ImageView(HeroUp));
            }
            if (direction.equals("down")) {
                Fields[newx][newy].setGraphic(new ImageView(HeroDown));
            }
        });
    }

    public static void updateScoreTable() {
        Platform.runLater(() -> {
            ScoreList.setText(getScoreList());
        });
    }

    public void playerMoved(int deltaX, int deltaY, String direction) {
        // Send besked til serveren om at spilleren er flyttet
        TCPClient.getInstance().send("move:" + deltaX + ":" + deltaY + ":" + direction);
    }

    private static String getScoreList() {
        StringBuffer b = new StringBuffer(100);
        for (PlayerUpdate p : Players) {
            b.append(p + "\r\n");
        }
        return b.toString();
    }

    public static void updateGui(PlayerUpdate[] players) {
        for (PlayerUpdate oldP : Players) {
            removePlayerOnScreen(oldP.getPos());
        }

        for (PlayerUpdate p : players) {
            placePlayerOnScreen(p.getPos(), p.getDirection());
        }

        Players = players;
        updateScoreTable();
    }
}

