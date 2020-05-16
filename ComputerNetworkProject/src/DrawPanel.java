
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DrawPanel extends JPanel {

    private LinkedList<MyShape> myShapes;
    private LinkedList<MyShape> clearedShapes;
    private int currentShapeType; //Hem ana kullanıcı ekranında cizilecek hem de tcp ile yollanacak neyi cizecegimizi gosteren int deger
    private MyShape currentShapeObject; //su an cizdigimiz obje
    private Color currentShapeColor;
    private boolean currentShapeFilled; //determine whether shape is filled or not
    JLabel statusLabel; //mousenin koordinatlari icin tuttugumuz label
    private Socket clientSocket;
    private ObjectInputStream clientInput;
    private ObjectOutputStream clientOutput;
    private Thread clientThread;

    public ArrayList<String> allowedHosts;

    public DrawPanel(JLabel statusLabel) throws IOException {

        myShapes = new LinkedList<MyShape>();
        clearedShapes = new LinkedList<MyShape>();

        //default olarak verdigimiz degerler
        currentShapeType = 0;
        currentShapeObject = null;
        currentShapeColor = Color.BLACK;
        currentShapeFilled = false;

        this.statusLabel = statusLabel;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        add(statusLabel, BorderLayout.SOUTH);  //Kullanicinin mouse koordinatlarinin bulundugu kisim

        // mouse eventleri
        MouseHandler handler = new MouseHandler();
        addMouseListener(handler);
        addMouseMotionListener(handler);
        clientSocket = new Socket("localhost", 8000);
        clientOutput = new ObjectOutputStream(clientSocket.getOutputStream());
        clientInput = new ObjectInputStream(clientSocket.getInputStream());
        allowedHosts = new ArrayList<>();
        allowedHosts.add("/127.0.0.1");                  ////
        clientThread = new ListenThread();
        clientThread.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        ArrayList<MyShape> shapeArray = myShapes.getArray();
        for (int counter = shapeArray.size() - 1; counter >= 0; counter--) {
            shapeArray.get(counter).draw(g);
        }

        if (currentShapeObject != null) {
            currentShapeObject.draw(g);
        }
    }

    public void setCurrentShapeType(int type) {
        currentShapeType = type;
    }

    public void setCurrentShapeColor(Color color) {
        currentShapeColor = color;
    }

    public void setCurrentShapeFilled(boolean filled) {
        currentShapeFilled = filled;
    }

    public void clearLastShape() {
        if (!myShapes.isEmpty()) {
            clearedShapes.addFront(myShapes.removeFront()); //Linklistten son eklenen elemanı siliyorum
            repaint();
        }
    }

    public void redoLastShape() {
        if (!clearedShapes.isEmpty()) {
            myShapes.addFront(clearedShapes.removeFront()); //son silinene elemani geri yukluyorum
            repaint();
        }
    }

    public void clearDrawing() {
        myShapes.makeEmpty();
        clearedShapes.makeEmpty();
        repaint(); //linkedlist temizlenince tum cizilenler silinmis oluyor
    }

    private class MouseHandler extends MouseAdapter {

        public void mousePressed(MouseEvent event) {
            switch (currentShapeType) {
                case 0:
                    currentShapeObject = new MyLine(event.getX(), event.getY(),
                            event.getX(), event.getY(), currentShapeColor);
                    break;
                case 1:
                    currentShapeObject = new MyRectangle(event.getX(), event.getY(),
                            event.getX(), event.getY(), currentShapeColor, currentShapeFilled);
                    break;
                case 2:
                    currentShapeObject = new MyOval(event.getX(), event.getY(),
                            event.getX(), event.getY(), currentShapeColor, currentShapeFilled);
                    break;

            }
        }

        public void mouseReleased(MouseEvent event) {
            //mouse tiklama isi bitirilince degerlerimizi set ediyoruz
            currentShapeObject.setX2(event.getX());
            currentShapeObject.setY2(event.getY());
//            System.out.println(currentShapeObject.getY2());               Mert Kapattı
            myShapes.addFront(currentShapeObject); //yarattigimiz degeri linkedlistimize ekliyoruz
            clearedShapes.makeEmpty();
            try {
                clientOutput.writeObject(new DrawObject(currentShapeObject.getX1(), currentShapeObject.getY1(), currentShapeObject.getX2(), currentShapeObject.getY2(), currentShapeColor.toString(), currentShapeType, currentShapeFilled));
            } catch (IOException ex) {
                Logger.getLogger(DrawPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            repaint();
            currentShapeObject = null; //ekledikten sonra temp datamızı null atayip yeni cizimi bekliyoruz

        }

        public void mouseMoved(MouseEvent event) {
            statusLabel.setText(String.format("Mouse Coordinates X: %d Y: %d", event.getX(), event.getY()));
        }

        public void mouseDragged(MouseEvent event) {

            currentShapeObject.setX2(event.getX());
            currentShapeObject.setY2(event.getY());

            statusLabel.setText(String.format("Mouse Coordinates X: %d Y: %d", event.getX(), event.getY()));

            repaint();

        }

    }

    class ListenThread extends Thread {

        // server'dan gelen mesajları dinle
        @Override
        public void run() {
            try {

                Object mesaj;

                while ((mesaj = clientInput.readObject()) != null) {

                    if (mesaj instanceof DrawObject) {
                        for (String allowedHost : allowedHosts) {
                            if (((DrawObject) mesaj).getFromClient().equals(allowedHost)) {
                                //TODO: izin verilen client ise, gelen draw object datası ile ilgili çizim işlemini yap
                               
                                // currentShape objesi myShapes LinkedListine eklenirken null pointer alınıyor. mouseReleased kısmında currentshape null atanmak zorunda olduğu için.
                               
                                // Yeni bir myShape Nesnesi oluşturulamıyor. draw metodu sıkıntı çıkarıyor. 
                                // MyShape shape=new MyShape(((DrawObject) mesaj).x1, ((DrawObject) mesaj).y1, ((DrawObject) mesaj).x2, ((DrawObject) mesaj).y2, Color.BLUE);  Bu şekilde draw edilemiyor
                                
//                                DrawObject object = new DrawObject(((DrawObject) mesaj).x1, ((DrawObject) mesaj).y1, ((DrawObject) mesaj).x2, ((DrawObject) mesaj).y2,
//                                        ((DrawObject) mesaj).color, ((DrawObject) mesaj).model, ((DrawObject) mesaj).isFill);   // mesajı cast etmemek için bir Draw objesi oluşturuldu. 

                                System.out.println("x1 = " + ((DrawObject) mesaj).x1 + "    y1 =  " + ((DrawObject) mesaj).y1 + "    renk = " + ((DrawObject) mesaj).color.toString());
                                
                                
                                currentShapeObject.setX1(((DrawObject) mesaj).x1);
                                currentShapeObject.setY1(((DrawObject) mesaj).y1);
                                currentShapeObject.setX2(((DrawObject) mesaj).x2);
                                currentShapeObject.setY2(((DrawObject) mesaj).y2);
                                currentShapeColor=Color.getColor(((DrawObject) mesaj).color);
                                currentShapeType=((DrawObject) mesaj).model;
                                
                                myShapes.addFront(currentShapeObject);
//                                 
                                System.out.println("x1 = " + ((DrawObject) mesaj).x1 + "    y1 =  " + ((DrawObject) mesaj).y1 + "    renk = " + ((DrawObject) mesaj).color + "ozellikleri Basarlılı");
                            }
                        }

                    }

                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Error - ListenThread : " + ex);
            }
        }
    }

}
