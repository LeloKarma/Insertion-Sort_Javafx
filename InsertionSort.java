package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


import java.util.Random;

//extend application class
public class InsertionSort extends Application {
   //declare instance variables
	private InsertionPanel panCenter;
    private Button btnStart, btnClose;
    private MyThread t;
    private int[] nos;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
       
    	//array to hold numbers to be sorted
    	nos = new int[10];

        btnStart = new Button("Start");
        btnClose = new Button("Close");

        panCenter = new InsertionPanel(nos);

        GridPane panSouth = new GridPane();
        panSouth.setAlignment(Pos.CENTER);
        panSouth.setHgap(10);
        panSouth.add(btnStart, 0, 0);
        panSouth.add(btnClose, 1, 0);

        BorderPane root = new BorderPane();
        root.setCenter(panCenter);
        root.setBottom(panSouth);

        Scene scene = new Scene(root, 450, 200);
        primaryStage.setTitle("Insertion Sort");
        primaryStage.setScene(scene);
        primaryStage.show();

        //event handlers
        btnStart.setOnAction(e -> {
            generateNos();
            panCenter.setFlag(true);
            t = new MyThread();
            t.start();
        });

        btnClose.setOnAction(e -> primaryStage.close());
    }

    //generating random numbers
    public void generateNos() {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            nos[i] = rand.nextInt(100) + 1;
        }
    }

    //graphical panel for reprensting sorting process
    class InsertionPanel extends Canvas {
        private int[] nos;
        private int pos1, pos2, pass;
        private boolean flag, flag1, flag2;

        public InsertionPanel(int[] x) {
            nos = x;
            setWidth(400);
            setHeight(100);
        }

        //T redraw the panel whenever its neccessary
        public void repaint() {
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());
            if (flag) {
                double w = getWidth();
                double cw = w / 10 - 4;
                if (flag1) {
                    gc.setFill(javafx.scene.paint.Color.PINK);
                    gc.fillRect(pos1 * cw + 4, 20, cw, cw);
                }
                if (flag2) {
                    gc.setFill(javafx.scene.paint.Color.CYAN);
                    gc.fillRect(pos2 * cw + 4, 20, cw, cw);
                }
                gc.setStroke(javafx.scene.paint.Color.BLACK);
                for (int i = 0; i < 10; i++) {
                    gc.strokeRect(i * cw + 4, 20, cw, cw);
                    gc.strokeText(Integer.toString(nos[i]), i * cw + 10, 20 + cw / 2);
                }
                gc.strokeText("Pass: " + pass, 4, 10);
            }
        }

        public void setFlag(boolean b) {
            flag = b;
        }

        public void setFlag1(boolean b) {
            flag1 = b;
        }

        public void setFlag2(boolean b) {
            flag2 = b;
        }

        public void setPos1(int i) {
            pos1 = i;
        }

        public void setPos2(int i) {
            pos2 = i;
        }

        public void setPass(int i) {
            pass = i;
        }
    }

    //MyThread class overrides the run method
    class MyThread extends Thread {
    	
    	//Iterates through the array and does the swapping
        public void run() {
            int n = 10, i, j, y;
            for (i = 1; i < n; i++) {
                panCenter.setPass(i + 1);
                y = nos[i];
                panCenter.setPos1(i);
                panCenter.setFlag1(true);
                panCenter.repaint();
                try {
                    Thread.sleep(4000);
                } catch (Exception e) {
                }
                for (j = i - 1; j >= 0; j--) {
                    if (y < nos[j])
                        nos[j + 1] = nos[j];
                    else
                        break;
                }
                nos[j + 1] = y;
                panCenter.setPos2(j + 1);
                panCenter.setFlag2(true);
                panCenter.repaint();
                try {
                    Thread.sleep(4000);
                } catch (Exception e) {
                }
                panCenter.setFlag1(false);
                panCenter.setFlag2(false);
            }
            javafx.application.Platform.runLater(() -> {
                panCenter.setPos1(-1);
                panCenter.setPos2(-1);
                panCenter.repaint();
            });
        }
    }

    @Override
    public void stop() {
        if (t != null && t.isAlive()) {
            t.interrupt();
        }
    }
}
