package tempTest;
/**
 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 */
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * An example of a menu bar. The example includes use of the system bar, if the
 * current platform supports a system bar.
 *
 * @see javafx.scene.control.MenuBar
 * @see javafx.scene.control.Menu
 * @see javafx.scene.control.MenuItem
 * @resource menuInfo.png
 */
public class MenuSample extends Application {

    private final Label sysMenuLabel = new Label("Using System Menu");

    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setScene(new Scene(root));
        final String os = System.getProperty("os.name");
        VBox vbox = new VBox(20);
        final Label outputLabel = new Label();
        final MenuBar menuBar = new MenuBar();
        
        //Sub menus for Options->Submenu 1
        MenuItem menu111 = MenuItemBuilder.create().text("blah").build();
        final MenuItem menu112 = MenuItemBuilder.create().text("foo").build();
        final CheckMenuItem menu113 = CheckMenuItemBuilder.create().text("Show \"foo\" item").selected(true).build();
        menu113.selectedProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable valueModel) {
                menu112.setVisible(menu113.isSelected());
                System.err.println("MenuItem \"foo\" is now " + (menu112.isVisible() ? "" : "not") + " visible.");
            }
        });
        // Options->Submenu 1 submenu 
        Menu menu11 = MenuBuilder.create()
                .text("Submenu 1")
                .graphic(new ImageView(new Image(MenuSample.class.getResourceAsStream("menuInfo.png"))))
                .items(menu111, menu112, menu113)
                .build();

        // Options->Submenu 2 submenu
        MenuItem menu121 = MenuItemBuilder.create().text("Item 1").build();
        MenuItem menu122 = MenuItemBuilder.create().text("Item 2").build();
        Menu menu12 = MenuBuilder.create().text("Submenu 2").items(menu121, menu122).build();

        // Options->Change Text
        final String change[] = {"Change Text", "Change Back"};
        final MenuItem menu13 = MenuItemBuilder.create().text(change[0]).accelerator(KeyCombination.keyCombination("Shortcut+C")).build();
        menu13.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                menu13.setText((menu13.getText().equals(change[0])) ? change[1] : change[0]);
                outputLabel.setText(((MenuItem) t.getTarget()).getText() + " - action called");
            }
        });        
        
        // Options menu
        Menu menu1 = MenuBuilder.create().text("Options").items(menu11, menu12, menu13).build();

        menuBar.getMenus().addAll(menu1);

        if (os != null && os.startsWith("Mac")) {
            Menu systemMenuBarMenu = new Menu("MenuBar Options");

            final CheckMenuItem useSystemMenuBarCB = new CheckMenuItem("Use System Menu Bar");
            useSystemMenuBarCB.setSelected(false);
            menuBar.useSystemMenuBarProperty().bind(useSystemMenuBarCB.selectedProperty());
            systemMenuBarMenu.getItems().add(useSystemMenuBarCB);

            menuBar.getMenus().add(systemMenuBarMenu);
        }

        vbox.getChildren().addAll(menuBar);
        if (os != null && os.startsWith("Mac")) {
            HBox hbox = HBoxBuilder.create().alignment(Pos.CENTER).build();
            sysMenuLabel.setStyle("-fx-font-size: 24");
            hbox.getChildren().add(sysMenuLabel);
            vbox.getChildren().add(hbox);
            sysMenuLabel.setVisible((menuBar.getHeight() == 0) ? true : false);
            menuBar.heightProperty().addListener(new ChangeListener<Number>() {
                public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                    sysMenuLabel.setVisible((menuBar.getHeight() == 0) ? true : false);
                }
            });
        }
        root.getChildren().add(vbox);
    }

    @Override public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }
    public static void main(String[] args) { launch(args); }
    
    public MenuSample(Stage primaryStage) throws Exception{
        start(primaryStage);
    }
}
