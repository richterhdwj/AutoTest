package tempTest;

/**
 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 */
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import support.ModelBase;
import support.tableView.TableViewColumnSet;
import support.tableView.TableViewCustom;

/**
 * A simple table with a header row.
 *
 * @see javafx.scene.control.TableCell
 * @see javafx.scene.control.TableColumn
 * @see javafx.scene.control.TablePosition
 * @see javafx.scene.control.TableRow
 * @see javafx.scene.control.TableView
 */
public class TableSample extends Application {
    private void init(Stage primaryStage) throws Exception {
        Group root = new Group();
        primaryStage.setScene(new Scene(root,400,200));
        List<Person> data = new ArrayList<Person>();
            data.add(new Person("Jacob",     "Smith",    "jacob.smith@example.com"));
            data.add(new Person("Isabella",  "Johnson",  "isabella.johnson@example.com"));
            data.add(new Person("Ethan",     "Williams", "ethan.williams@example.com"));
            data.add(new Person("Emma",      "Jones",    "emma.jones@example.com" ));
            data.add(new Person("Michael",   "Brown",    "michael.brown@example.com"));
        ModelBase.setCheckBoxs(data, null,true);
        Person personSet=(Person)ModelBase.getListOne(data);
        TableViewCustom tableView = new TableViewCustom(personSet,primaryStage,"read");
        tableView.setTableViewColumn();
        TableView getTableView=tableView.setTableViewData(data);
        VBox vbox=new VBox(1);
        vbox.setPrefSize(400,200);
        vbox.getChildren().add(getTableView);
        
        root.getChildren().add(vbox);
    }

    public static class Person extends ModelBase{
        private String name;
        private String code;
        private String email;
        
        private ComboBox comboBoxName;

        public ComboBox getComboBoxName() {
            return comboBoxName;
        }

        public void setComboBoxName(ComboBox comboBoxName) {
            this.comboBoxName = comboBoxName;
        }
        
        public Person(){
            
        }
        
        public Person(String name, String code, String email) {
            this.name = name;
            this.code = code;
            this.email = email;
        }
        
        @SuppressWarnings({"unchecked","fallthrough"})
        public TableViewColumnSet[] read(){
            comboBoxName=new ComboBox();
            comboBoxName.setPromptText(name);
            comboBoxName.setEditable(true);
            comboBoxName.setItems(FXCollections.observableArrayList(
            "Option 1", "Option 2", "Option 3",
            "Option 4", "Option 5", "Option 6",
            "Longer ComboBox item",
            "Option 7", "Option 8", "Option 9",
            "Option 10", "Option 12"));
            setTableViewColumn(new TableViewColumnSet[]{
              new TableViewColumnSet("name","姓名",null,comboBoxName,name,true,true),
              new TableViewColumnSet("code","编号",60.0,null,code,true,true),
              new TableViewColumnSet("email","邮件地址",null,null,email,true,true),
            });
            return this.getTableViewColsSet();
        }
        
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Override public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }
    public static void main(String[] args) { launch(args); }
}
