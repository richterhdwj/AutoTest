package support;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import support.tableView.TableViewColumnSet;

public class ModelBase {

    private TableViewColumnSet[] tableViewColsSet;                              //返回用的设定结构
    private TableViewColumnSet[] tableViewColsSetBase;                          //这是新建时用的结构
    private Node checkBox;                                                      //checkBox的设定，暂不考虑多头列
    private boolean checkBoxInWrite=false;
    private boolean hasBox = false;                                             //设定是否已经存在checkbox这个东东
    private Node[] addAnyItems;                                                 //后方追加的按钮列，其实也可以是任何东西

    public TableViewColumnSet[] getTableViewColsSet() {
        return tableViewColsSet;
    }

    public void setTableViewColumn(TableViewColumnSet[] tableViewColsSet) {
        this.tableViewColsSetBase = tableViewColsSet.clone();
        tableViewColsSet=null;
        retTableViewColumnSetsbulid();
    }

    /**
     * 说是checkbox但是可以往列表的第一列塞任何一种组件
     *
     * @param checkBox
     */
    public void setCheckBox(Node checkBox) {
            if (checkBox == null) {
                CheckBox newCheckBox = new CheckBox();
                newCheckBox.setSelected(false);
                this.checkBox = newCheckBox;
            } else {
                this.checkBox = checkBox;
            }
            hasBox = true;
        retTableViewColumnSetsbulid();
    }
    
    public void checkBoxInWrite(Boolean flag){
        if(flag==null)
            checkBoxInWrite=true;
        checkBoxInWrite=flag;
    }
    
    public static void setCheckBoxs(List list,Node checkBox,Boolean flag) throws Exception{
        for(Object obj:list){
            try{
                obj.getClass().getSuperclass().getMethod("setCheckBox",Node.class).invoke(obj,checkBox);
                if(flag!=null)
                    obj.getClass().getSuperclass().getMethod("checkBoxInWrite",Boolean.class).invoke(obj,flag);
            }catch(Exception e){
                System.out.println("设定checkBox失败了");
                e.printStackTrace();
            }
        }
    }
    
    public static Object getListOne(List list){
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }
    
    /**
     * 如果不想要了就去掉
     */
    public void delCheckBox(){
        this.setCheckBox(null);
        hasBox=false;
    }

    public Node getCheckBox() {
        return checkBox;
    }

    private Object[] getAddAnyItems() {
        return addAnyItems;
    }

    private void setAddAnyItems(Node[] addAnyItems) {
        this.addAnyItems = addAnyItems;
    }

    public void addAnyItemsClear() {
        this.addAnyItems = null;
    }

    /**
     * 为列表后面追加按钮或者其他东东用
     *
     * @param item
     */
    public void addItems(Node item) {
        if (getAddAnyItems() == null) {
            addAnyItems = new Node[]{
                item
            };
        } else {
            Node[] addAnyItemsTemp = addAnyItems.clone();
            addAnyItems = new Node[addAnyItemsTemp.length + 1];
            int i = 0;
            for (i = 0; i < addAnyItemsTemp.length; i++) {
                addAnyItems[i] = addAnyItemsTemp[i];
            }
            addAnyItems[i] = item;
        }
        retTableViewColumnSetsbulid();
    }
    
    /**
     * 万一有某个不想要的组件了可以这样干掉它……
     * @param item 
     */
    public void delItems(int i){
        if(addAnyItems.length<i){
            System.out.println("太长啦~~~根本没那么多东西可以删！");
        }else{
            Node[] addAnyItemsTemp = addAnyItems.clone();
            addAnyItems = new Node[addAnyItemsTemp.length - 1];
            for(int x=0;x<addAnyItemsTemp.length;x++){
                if(x!=i-1)
                    addAnyItems[i] = addAnyItemsTemp[i];
            }
        }
        retTableViewColumnSetsbulid();
    }
    
    /**
     * 通过这个方法将已经设定好的数据组合起来，以达到无论前后何时设定都可以达到期望目标的预期
     */
    public void retTableViewColumnSetsbulid(){
        tableViewColsSet=null;
        int addAnyNum=0;
        if(addAnyItems!=null)
            addAnyNum=addAnyItems.length;
        int tableViewSetsNum=0;
        if(tableViewColsSetBase!=null)
            tableViewSetsNum=tableViewColsSetBase.length;
        int box=0;
        
        if(this.hasBox)
            box++;
        this.tableViewColsSet=new TableViewColumnSet[addAnyNum+tableViewSetsNum+box];
        
        int nowCol=0;
        if(hasBox){
            tableViewColsSet[nowCol]=new TableViewColumnSet(TableViewColumnSet.checkbox," ",20.0,this.getCheckBox(),this.getCheckBox(),null,checkBoxInWrite);
            nowCol++;
        }
        
        if(tableViewColsSetBase!=null){
            for(TableViewColumnSet nowSet:tableViewColsSetBase){
                tableViewColsSet[nowCol]=nowSet;
                nowCol++;
            }
        }
        
        if(addAnyItems!=null){
            for(Node nowNode:addAnyItems){
                tableViewColsSet[nowCol]=new TableViewColumnSet("Node",nowNode.getId(),null,nowNode,null);
                nowCol++;
            }
        }
    }
}