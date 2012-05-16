/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.innerView;

/**
 *
 * @author hdwjy
 */
public class ViewParamets {
    private static int screenWidth = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
    private static int screenHeight = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }
    
}
