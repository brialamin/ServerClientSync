/*
 * Name: Samuel Adams
 * Date: November 13, 2012
 * Class: CSCI 3306
 * Build Instruction: Compile Server.java before running Server.class.
 * 					  This must be done because of the added class
 * 					  TCPClient in order to multi-thread the server.
 */
import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;
	

public class Server
{
	@SuppressWarnings("resource")
	public static void main(String args[]) throws Exception
    {
		//creating server socket binding at port # 14500
        ServerSocket welcomeSocket = new ServerSocket(14500);
		System.out.println("Run the Client");
 
        while(true)
        {
        	//ready to accept client request
            Socket connectionSocket = welcomeSocket.accept();
            TCPClient client = new TCPClient(connectionSocket);
            if(connectionSocket != null)
            {                
                client.start();
                System.out.println("Connected with a Client (thread "+(client.getId())+"): " + connectionSocket.getInetAddress() + " at " + connectionSocket.getPort());
                while(client.isAlive()){}
                System.out.println("Client: " + connectionSocket.getInetAddress() + " disconnected (thread " + (client.getId())+")");
            }
        }
    }
}
 
class TCPClient extends Thread
{
    private Socket connectionSocket;
    private BufferedReader inFromClient;
    private BufferedWriter outToClient;
 
    public TCPClient(Socket c) throws IOException
    {
        connectionSocket = c;
    }
 
    public void run()
    {
        try
        {
        	//opening the input stream to read data from client connection
            inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            //System.out.println(inFromClient.readLine());
            
            //using output stream responding data
            outToClient = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
            
            if(inFromClient.readLine().equals("Switch"))
            {
            	 //format date to send
		        	 DateFormat dateFormat = new SimpleDateFormat("MMMMMMMMMM dd, yyyy");
		        	 DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		        	 Date date = new Date();
		        	 DateFormat outDate = new SimpleDateFormat("dd MM yyyy");
		        	 //send the day and time
		        	 outToClient.write(outDate.format(date) + " ");
		        	 outToClient.write(timeFormat.format(date) + '\n');
		        	 outToClient.flush();
		        	 //print the time and day the timestamp was sent
		        	 System.out.print("Sent timestamp on ");
		        	 System.out.print(dateFormat.format(date));
		        	 System.out.print(" at ");
		        	 System.out.print(timeFormat.format(date));
		        	 System.out.println(" to " + connectionSocket.getInetAddress());
		        	 
            } else {
            	outToClient.write("Connected" + '\n');
            	outToClient.flush();
            	
            }
			
			//closing the in & out streams
			//out.close();
			//inFromClient.close();
			
        }
        catch(Exception e)
        {
            //System.out.println("Unable to connect with the Client!" + e);
        }
    }
}