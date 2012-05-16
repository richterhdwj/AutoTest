/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.databaseModel;

/**
 *
 * @author hdwjy
 */
public class WordLink {
    private String tableName="T_WORD_ANSWER_LINK";
    
    private String pid;
    private String sampleSeq;
    private String wordSeq;
    
    public String[][] getTableModelSet(){
        return new String[][]{
            {"tableName",tableName,"TableName"},
            {"pid","F_PID","mainKey"},
            {"sampleSeq","F_SAMPLE_SEQ","String"},
            {"wordSeq","F_WORD_SEQ","String"}
        };
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSampleSeq() {
        return sampleSeq;
    }

    public void setSampleSeq(String sampleSeq) {
        this.sampleSeq = sampleSeq;
    }

    public String getWordSeq() {
        return wordSeq;
    }

    public void setWordSeq(String wordSeq) {
        this.wordSeq = wordSeq;
    }

    public String getTableName() {
        return tableName;
    }
    
}
