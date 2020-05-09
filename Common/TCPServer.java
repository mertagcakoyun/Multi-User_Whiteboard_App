
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mehmetozcan
 */
public class TCPServer {

    private ServerSocket serverSocket;
    private Thread serverThread;
    private HashSet<ObjectOutputStream> allClients = new HashSet<>();

    protected void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server başlatıldı");
        serverThread = new Thread(() -> {
            while (!serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Yeni bir client bağlandı : " + clientSocket.getInetAddress());
                    new ListenThread(clientSocket).start();
                } catch (IOException ex) {
                    System.out.println("Hata - new Thread() : " + ex);
                    break;
                }
            }
        });
        serverThread.start();
    }

    protected void sendBroadcast(String message) throws IOException {
        for (ObjectOutputStream output : allClients) {
            output.writeObject("Server : " + message);
        }
    }

    class ListenThread extends Thread {
        private final Socket clientSocket;
        private ObjectInputStream clientInput;
        private ObjectOutputStream clientOutput;

        private DrawObject object;
        private String hostIP;
        private ListenThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {

            try {
                clientInput = new ObjectInputStream(clientSocket.getInputStream());
                clientOutput = new ObjectOutputStream(clientSocket.getOutputStream());
                allClients.add(clientOutput);
                Object obj;
                
                while ((obj = clientInput.readObject()) != null) {
                    object = (DrawObject) obj;
                    hostIP = clientSocket.getInetAddress().toString();
                    object.setFromClient(hostIP);
                    
                    for (ObjectOutputStream out : allClients) {
                        if (out != clientSocket.getOutputStream()) {
                            out.writeObject(object);
                        }

                    }
                }
                

            } catch (IOException | ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            } finally {
                try {
                    allClients.remove(clientOutput);

                    if (clientInput != null) {
                        clientInput.close();
                    }
                    if (clientOutput != null) {
                        clientOutput.close();
                    }
                    if (clientSocket != null) {
                        clientSocket.close();
                    }

                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {

        try {
            TCPServer server = new TCPServer();
            server.start(8000);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

}
