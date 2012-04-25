/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 *
 * @author hdwjy
 */
public class DataBaseSet {
    private String className="org.sqlite.JDBC";
    private String dataBasePath=System.getProperty("user.dir")+System.getProperty("file.separator")+"database"+System.getProperty("file.separator")+"database";
    private String connectSet="sqlite:jdbc:"+dataBasePath;
    private String user=null;
    private String password=null;
    
    private Statement stat;
    private Connection conn;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getConnectSet() {
        return connectSet;
    }

    public void setConnectSet(String connectSet) {
        this.connectSet = connectSet;
    }

    public String getDataBasePath() {
        return dataBasePath;
    }

    public void setDataBasePath(String dataBasePath) {
        this.dataBasePath = dataBasePath;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Statement getStat() {
        return stat;
    }

    public void setStat(Statement stat) {
        this.stat = stat;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    
    /**
     * 每次都通过此方法链接数据库
     * @throws Exception 
     */
    public void connect() throws Exception{
        
        String databasePath=System.getProperty("user.dir")+System.getProperty("file.separator")+"database"+System.getProperty("file.separator")+"database";
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:"+databasePath,null,null);
        stat = conn.createStatement();
    }
    
    /**
     * 数据用完之后都通过此方法关闭数据库链接
     * @throws Exception 
     */
    public void close() throws Exception{
        conn.close();
        stat.close();
    }
}
