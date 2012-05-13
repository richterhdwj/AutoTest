/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotest;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import main.MainViewPlant;
import main.TaksObject;
import tempTest.MenuSample;
/**
 *
 * @author hdwjy
 */
public class AutoTest extends Application {

    private ProgressBar bar;
    private Label label;
    private Label schlabel;
    private Group root;
    private Stage primaryStage;
    private Rectangle rect;
    
    private TaksObject taksObject;
    Double nowLine = 0.0;

    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public static void main(String[] args) {
                launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        MenuSample a=new MenuSample(primaryStage);
//        a.start(primaryStage);
        taksObject=new TaksObject();
        this.primaryStage = primaryStage;
        init(primaryStage);
        primaryStage.show();
    }

    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void init(Stage primaryStage) {
        root = new Group();
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("突击测验 AutoTest！");
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent arg0) {
                System.exit(0);
            }
        });

        primaryStage.setResizable(false); //拒绝改变窗口大小
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);

        Rectangle rectangle = new Rectangle(600, 240, Color.BISQUE);
        rectangle.setStroke(Color.DARKRED);

        label = new Label("请稍等⋯⋯");
        label.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 36));
        final DropShadow dropShadow = new DropShadow();
        label.setEffect(dropShadow);
        dropShadow.setColor(Color.DARKBLUE);

        bar = new ProgressBar();
        bar.setPrefWidth(label.getText().length() * 50);

        schlabel = new Label("0%");
        schlabel.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.LIGHT, 12));
        StackPane stackPaneBar = new StackPane();
        stackPaneBar.setAlignment(Pos.CENTER);
        stackPaneBar.getChildren().addAll(bar, schlabel);

        VBox vbox = new VBox(2);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(label, stackPaneBar);
        
        stackPane.getChildren().addAll(rectangle, vbox);

        rect = rectangle;

        root.getChildren().add(stackPane);

        primaryStage.setScene(new Scene(root, 1024, 512));

        stackPane.setLayoutX(primaryStage.getScene().getWidth() / 2 - rectangle.getWidth() / 2);
        stackPane.setLayoutY(primaryStage.getScene().getHeight() / 2 - rectangle.getHeight() / 2);
        new Time();
    }

    public class Time extends Thread {

        public Time() {
            Thread thread = new Thread(
                    new Runnable() {

                        boolean getInObject = true;

                        @Override
                        public void run() {
                            while (getInObject) {
                                System.out.println(nowLine);
                                nowLine = nowLine + 0.5;
                                try {
                                    sleep(1);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(AutoTest.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                if (nowLine >= 1) {
                                    getInObject = false;
                                }
                            }
                        }
                    });
            thread.start();

            Thread threadUpDate = new Thread(
                    new Runnable() {

                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    sleep(25);
                                } catch (Exception e) {
                                }
                                if (nowLine >= 1) {
                                    break;
                                }
                                Platform.runLater(new Runnable() { //这是线程冲突时使用的方法

                                    @Override
                                    public void run() {
                                        bar.setProgress(nowLine);
                                        Integer useNowLine = (int) (nowLine * 100);
                                        schlabel.setText(useNowLine.toString() + "%");
                                    }
                                });
                            }

                            bar.setProgress(1);
                            Platform.runLater(new Runnable() { //这是线程冲突时使用的方法

                                @Override
                                public void run() {
                                    schlabel.setText("100%");
                                    label.setText("载入完成啦～～");
                                }
                            });

                            final FadeTransition fadeTransition = FadeTransitionBuilder.create().duration(Duration.seconds(3)).node(rect).fromValue(1).toValue(0.01).cycleCount(0).autoReverse(false).build();
                            fadeTransition.setNode(root.getChildren().get(0));
                            fadeTransition.setRate(2);
                            fadeTransition.play();
                            fadeTransition.setOnFinished(new EventHandler<ActionEvent>() {

                                @Override
                                public void handle(ActionEvent arg0) {
                                    fadeTransition.stop();
                                    taksObject.setPrimaryStage(primaryStage);
                                    taksObject.setRoot(root);
                                    
                                    root.getChildren().clear();
                                    
                                    
                                    new MainViewPlant(taksObject);
                                }
                            });
                        }
                    });
            threadUpDate.start();
        }
    }
}
