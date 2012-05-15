/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.databaseTable;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import main.databaseModel.WordRecord;
import main.databaseModel.WordTopic;
import support.ModelBase;
import support.tableView.TableViewColumnSet;

/**
 *
 * @author hdwjy
 */
public class ExampleViewTable extends ModelBase {

    private String contect;
    private String type;
    private WordTopic wordTopic;

    @SuppressWarnings({"unchecked", "fallthrough"})
    public TableViewColumnSet[] read() {
        setTableViewColumn(new TableViewColumnSet[]{
                    new TableViewColumnSet("contect", "题目", 450d, null, contect),
                    new TableViewColumnSet("type", "题目类型", 70d, null, type)});
        return this.getTableViewColsSet();
    }

    public String getContect() {
        return contect;
    }

    public void setContect(String contect) {
        this.contect = contect;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public WordTopic getWordTopic() {
        return wordTopic;
    }

    public void setWordTopic(WordTopic wordTopic) {
        this.wordTopic = wordTopic;
    }
}
