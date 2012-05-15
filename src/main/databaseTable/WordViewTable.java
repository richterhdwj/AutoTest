/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.databaseTable;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import main.databaseModel.WordRecord;
import support.ModelBase;
import support.tableView.TableViewColumnSet;

/**
 *
 * @author hdwjy
 */
public class WordViewTable extends ModelBase {

    private String type;
    private String contect;
    private String trans;
    private String example;
    private WordRecord wordRecord;

    @SuppressWarnings({"unchecked", "fallthrough"})
    public TableViewColumnSet[] read() {
        setTableViewColumn(new TableViewColumnSet[]{
                    new TableViewColumnSet("type", "类型", 70d, null, type),
                    new TableViewColumnSet("contect", "词条", 450d, null, contect),
                    new TableViewColumnSet("trans", "解释", 150d, null, trans),
                    new TableViewColumnSet("example", "例句", 174d, null, example)});
        return this.getTableViewColsSet();
    }

    public String getContect() {
        return contect;
    }

    public void setContect(String contect) {
        this.contect = contect;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public WordRecord getWordRecord() {
        return wordRecord;
    }

    public void setWordRecord(WordRecord wordRecord) {
        this.wordRecord = wordRecord;
    }
}
