
import java.awt.Color;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mehmetozcan
 */
public class DrawObject implements java.io.Serializable{
    
    public int x1 , y1, x2, y2;
    public Color color;
    public int model;
    private String fromClient;
    public boolean isFill;
    // doluluk kontrolununde gonderilmesi icin isFill eklendi. 
    public DrawObject(int x1, int x2, int y1, int y2, Color color, int model,boolean isFill){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.color = color;
        this.model = model;
        this.isFill=isFill;
    }
    
     public void setFromClient(String hostIP){
         this.fromClient = hostIP;
     }

    public String getFromClient(){
        return fromClient;
    }
    
}





