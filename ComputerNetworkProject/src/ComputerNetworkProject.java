
import java.io.IOException;
import java.net.Socket;


public class ComputerNetworkProject
{
    public static void main( String args[] ) throws Exception
    {
        DrawFrame paintGui = new DrawFrame(); 
    } 
    private void start(String host, int port) throws IOException{
        Socket socket = new Socket(host,port);
        
    }
}