/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tempTest;

import java.sql.*;

/**
 *
 * @author hdwjy
 */
public class DateBaseLinkTest {
    
    public static void main(String[] args) throws Exception {
        String databasePath=System.getProperty("user.dir")+System.getProperty("file.separator")+"database"+System.getProperty("file.separator")+"database";
        Class.forName("org.sqlite.JDBC");
        Connection conn =
                DriverManager.getConnection("jdbc:sqlite:"+databasePath,null,null);
        Statement stat = conn.createStatement();
//        stat.executeUpdate("drop table if exists people;");
//        stat.executeUpdate("create table people (name, occupation);");
//        PreparedStatement prep = conn.prepareStatement(
//                "insert into people values (?, ?);");
//
//        prep.setString(1, "Gandhi");
//        prep.setString(2, "politics");
//        prep.addBatch();
//        prep.setString(1, "Turing");
//        prep.setString(2, "computers");
//        prep.addBatch();
//        prep.setString(1, "Wittgenstein");
//        prep.setString(2, "smartypants");
//        prep.addBatch();
//
//        conn.setAutoCommit(false);
//        prep.executeBatch();
//        conn.setAutoCommit(true);

        ResultSet rs = stat.executeQuery("select * from t_user_info;");
        while (rs.next()) {
            System.out.println("name = " + rs.getString("f_user_name"));
            System.out.println("job = " + rs.getString("f_user_password"));
        }
        rs.close();
        conn.close();
    }
}
