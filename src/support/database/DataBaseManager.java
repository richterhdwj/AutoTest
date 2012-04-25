/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support.database;

import java.lang.reflect.Constructor;
import support.database.DataBaseBean;

/**
 *
 * @author Richter
 */
public class DataBaseManager extends DataBaseBean{
    public String getSqlValue(Object obj,String paramets,Object value) throws Exception{
        String ret=null;
        String[][] parametsTable=getTableAllColnum(obj);
        for(String[] paramet:parametsTable){
            if(paramet[0].equals(paramets)){
                if(paramet[2].equals("String")){
                    ret="'"+value+"'";
                    break;
                }else{
                    ret=value.toString();
                    break;
                }
            }
        }
        return ret;
    }
    
    public String getSqlName(Object obj,String paramets) throws Exception{
        String ret=null;
        String[][] parametsTable=getTableAllColnum(obj);
        for(String[] paramet:parametsTable){
            if(paramet[0].equals(paramets)){
                    ret=paramet[1];
                    break;
            }
        }
        return ret;
    }
}
