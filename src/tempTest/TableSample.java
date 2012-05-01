package tempTest;

/**
 * Copyright (c) 2008, 2012 Oracle and/or its affiliates. All rights reserved.
 * Use is subject to license terms.
 */
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * A sample in which a node moves from one location to another over a given
 * time.
 *
 * @related animation/transitions/FadeTransition @related
 * animation/transitions/FillTransition @related
 * animation/transitions/ParallelTransition @related
 * animation/transitions/PathTransition @related
 * animation/transitions/PauseTransition @related
 * animation/transitions/RotateTransition @related
 * animation/transitions/ScaleTransition @related
 * animation/transitions/SequentialTransition @related
 * animation/transitions/StrokeTransition
 *
 * @see javafx.animation.TranslateTransition
 * @see javafx.animation.TranslateTransitionBuilder
 * @see javafx.animation.Transition
 */
public class TableSample extends Application {

    private TranslateTransition translateTransition;
    private TranslateTransition translateTransition2;

    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setScene(new Scene(root, 400, 400));
        
        Label x = new Label("aapo998xsadsahduqwohduowquhwquohxquwohuwqobwqohqw");

        Rectangle rect = new Rectangle(0, 0, 150, 150);
        rect.setFill(null);
        rect.setStroke(Color.WHITE);
        rect.setStrokeWidth(40);

        Label y = new Label("你好！");
//        y.setLayoutX(300);
//        y.setLayoutY(50);

        Rectangle rect2 = new Rectangle(150, 0, 250, 150);
        rect2.setFill(Color.WHITE);

        root.getChildren().addAll(x, rect, rect2, y);
        translateTransition = new TranslateTransition(Duration.seconds(4), x);
        translateTransition.setFromX(-x.getText().length() * x.getFont().getSize());
        translateTransition.setFromY(50);
        translateTransition.setToX(primaryStage.getScene().getWidth() + 50);
        translateTransition.setCycleCount(Timeline.INDEFINITE);
        translateTransition.setAutoReverse(false);

        translateTransition2 = new TranslateTransition(Duration.seconds(4), y);
        translateTransition2.setFromX(-x.getText().length() * x.getFont().getSize());
        translateTransition2.setFromY(50);
        translateTransition2.setToX(primaryStage.getScene().getWidth() + 50);
        translateTransition2.setCycleCount(Timeline.INDEFINITE);
        translateTransition2.setAutoReverse(false);

//        translateTransition = TranslateTransitionBuilder.create()
//                .duration(Duration.seconds(10))
//                .node(x)
//                .fromX(-x.getText().length()*x.getFont().getSize())
//                .toX(primaryStage.getScene().getWidth()+50)
//                .cycleCount(Timeline.INDEFINITE)
//                .autoReverse(false)
//                .build();
    }

    public void play() throws InterruptedException {
        translateTransition.play();
        Thread thread = new Thread(
                                new Runnable() {

                                    @Override
                                    public void run() {
                                try {
                                    translateTransition2.play();
                                    System.out.println("yes!");
                                } catch (Exception ex) {
                                    Logger.getLogger(TableSample.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                                    }
                                });
        thread.sleep(5000);
        thread.start();
    }

    @Override
    public void stop() {
        translateTransition.stop();
    }

    public double getSampleWidth() {
        return 400;
    }

    public double getSampleHeight() {
        return 40;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
        play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
