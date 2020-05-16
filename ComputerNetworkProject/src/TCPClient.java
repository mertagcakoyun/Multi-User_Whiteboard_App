
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mehmetozcan
 */
public class TCPClient {

    private Socket clientSocket;
    private ObjectInputStream clientInput;
    private ObjectOutputStream clientOutput;
    private Thread clientThread;

    public ArrayList<String> allowedHosts;

    protected void start(String host, int port) throws IOException {

        clientSocket = new Socket(host, port);
        clientOutput = new ObjectOutputStream(clientSocket.getOutputStream());
        clientInput = new ObjectInputStream(clientSocket.getInputStream());

        clientThread = new ListenThread();
        clientThread.start();
    }

    protected void sendObject(DrawObject message) throws IOException {
        //TODO: arayüzde çizim yaptıktan sonra çizim yaptıgın fonksiyondan çizimin değerlerini al
        // draw objesini olustur ve bu objeyi server'a bu fonksiyon ile yolla
        clientOutput.writeObject(message);
    }

    protected void disconnect() throws IOException {
        // bütün streamleri ve soketleri kapat
        if (clientInput != null) {
            clientInput.close();
        }
        if (clientOutput != null) {
            clientOutput.close();
        }
        if (clientThread != null) {
            clientThread.interrupt();
        }
        if (clientSocket != null) {
            clientSocket.close();
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
                                //TODO: izin verilen client ise, gelen draw object datası ile ilgili class'da çizim işlemini yap
                                System.out.println(((DrawObject) mesaj).color);
                            }
                        }

                    }

                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Error - ListenThread : " + ex);
            }
        }
    }

    public static void main(String[] args) {
        
        DrawObject drawObj = new DrawObject(10, 15, 20, 25, "blue", 3,true);
        TCPClient client = new TCPClient();
        client.allowedHosts = new ArrayList<>();
        client.allowedHosts.add("/127.0.0.1");
        try {
            client.start("localhost", 8000);
            client.sendObject(drawObj);
            client.sendObject(drawObj);
        } catch (IOException ex) {
            Logger.getLogger(TCPClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
