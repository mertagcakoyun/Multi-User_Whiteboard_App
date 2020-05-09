import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;



public class DrawPanel extends JPanel
{
    private LinkedList<MyShape> myShapes; 
    private LinkedList<MyShape> clearedShapes; 
    private int currentShapeType; //Hem ana kullanıcı ekranında cizilecek hem de tcp ile yollanacak neyi cizecegimizi gosteren int deger
    private MyShape currentShapeObject; //su an cizdigimiz obje
    private Color currentShapeColor; 
    private boolean currentShapeFilled; //determine whether shape is filled or not
    JLabel statusLabel; //mousenin koordinatlari icin tuttugumuz label
    
    
    public DrawPanel(JLabel statusLabel){
        
        myShapes = new LinkedList<MyShape>(); 
        clearedShapes = new LinkedList<MyShape>(); 
        
        //default olarak verdigimiz degerler
        currentShapeType=0;
        currentShapeObject=null;
        currentShapeColor=Color.BLACK;
        currentShapeFilled=false;
        
        this.statusLabel = statusLabel; 
        
        setLayout(new BorderLayout());
        setBackground( Color.WHITE ); 
        add( statusLabel, BorderLayout.SOUTH );  //Kullanicinin mouse koordinatlarinin bulundugu kisim
        
        // mouse eventleri
        MouseHandler handler = new MouseHandler();                                    
        addMouseListener( handler );
        addMouseMotionListener( handler ); 
    }
    
    
    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );
        
        
        ArrayList<MyShape> shapeArray=myShapes.getArray();
        for ( int counter=shapeArray.size()-1; counter>=0; counter-- )
           shapeArray.get(counter).draw(g);
        
        
        if (currentShapeObject!=null)
            currentShapeObject.draw(g);
    }
    
    
    public void setCurrentShapeType(int type)
    {
        currentShapeType=type;
    }
    
    
    public void setCurrentShapeColor(Color color)
    {
        currentShapeColor=color;
    }
    
   
    public void setCurrentShapeFilled(boolean filled)
    {
        currentShapeFilled=filled;
    }
    
    
   
    public void clearLastShape()
    {
        if (! myShapes.isEmpty())
        {
            clearedShapes.addFront(myShapes.removeFront()); //Linklistten son eklenen elemanı siliyorum
            repaint();
        }
    }
    
   
    public void redoLastShape()
    {
        if (! clearedShapes.isEmpty())
        {
            myShapes.addFront(clearedShapes.removeFront()); //son silinene elemani geri yukluyorum
            repaint();
        }
    }
    
    
    public void clearDrawing()
    {
        myShapes.makeEmpty();
        clearedShapes.makeEmpty();
        repaint(); //linkedlist temizlenince tum cizilenler silinmis oluyor
    }
    
    
    private class MouseHandler extends MouseAdapter 
    {
        
        public void mousePressed( MouseEvent event )
        {
            switch (currentShapeType)
            {
                case 0:
                    currentShapeObject= new MyLine( event.getX(), event.getY(), 
                                                   event.getX(), event.getY(), currentShapeColor);
                    break;
                case 1:
                    currentShapeObject= new MyRectangle( event.getX(), event.getY(), 
                                                        event.getX(), event.getY(), currentShapeColor, currentShapeFilled);
                    break;
                case 2:
                    currentShapeObject= new MyOval( event.getX(), event.getY(), 
                                                   event.getX(), event.getY(), currentShapeColor, currentShapeFilled);
                    break;
                    
            }
        } 
        
        
        public void mouseReleased( MouseEvent event )
        {
            //mouse tiklama isi bitirilince degerlerimizi set ediyoruz
            currentShapeObject.setX2(event.getX());
            currentShapeObject.setY2(event.getY());
            
            myShapes.addFront(currentShapeObject); //yarattigimiz degeri linkedlistimize ekliyoruz
            
            currentShapeObject=null; //ekledikten sonra temp datamızı null atayip yeni cizimi bekliyoruz
            clearedShapes.makeEmpty(); 
            repaint();
            
        } 
        
      
        public void mouseMoved( MouseEvent event )
        {
            statusLabel.setText(String.format("Mouse Coordinates X: %d Y: %d",event.getX(),event.getY()));
            //Mouse koordinatlarini kullaniciya gosteren label
        } 
        
        
        public void mouseDragged( MouseEvent event )
        {
            
            currentShapeObject.setX2(event.getX());
            currentShapeObject.setY2(event.getY());
            
            statusLabel.setText(String.format("Mouse Coordinates X: %d Y: %d",event.getX(),event.getY()));
            
            repaint();
            
        } 
        
    }
    
}