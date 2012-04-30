/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.scene.Group;
import javafx.stage.Stage;

/**
 *
 * @author hdwjy
 */
public class TaksObject {
    private Group root;
    private Stage primaryStage;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Group getRoot() {
        return root;
    }

    public void setRoot(Group root) {
        this.root = root;
    }
    
    
}
