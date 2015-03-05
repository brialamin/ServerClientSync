/*
 * Name: Samuel Adams
 * Date: November 13, 2012
 * Class: CSCI 3306
 * Build Instructions: None
 */
import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

public class Client {
	public static void main(String[] args) throws Exception{
		Socket clientSocket = null;
		BufferedWriter OutToServer = null;
		BufferedReader InFromServer = null;
		try{
		//check the arguments
		if(args.length > 0){
			//check for the ip address first
			for(int ip = 0; ip < args.length; ip++){
				//set InetAddress to null to cycle through arguments first
				InetAddress node = null;
				//if the -c is activated, skip for now until the ip address is checked
				if(args[ip].equals("-c")){}
				//if there is no -c, then ignore the previous lines
				else{
					//if the InetAddres is not valid, connect to the local host
					try {
						node = InetAddress.getByName(args[ip]);
					}
					catch (UnknownHostException ex) {
						System.out.println("Cannot find host " + args[ip] + "\n" + "Connecting to local host instead");
						clientSocket = new Socket("localhost", 14500);
					}
					//if the socket is still set to null, check these
					if(clientSocket == null){
						if (isHostname(args[ip])) {
							clientSocket = new Socket(node.getHostAddress(), 14500);
						}
						else {  
							clientSocket = new Socket(node.getHostName(), 14500);
						}
					}
				}
			}
			//after cycling through the args to see if a valid ip address is there, 
			//cycle back through to see if the switch has been input
			for(int s = 0; s < args.length; s++){
				if(args[s].equals("-c")){
					//if the ip address was not given, set it to the local host
					if(clientSocket == null){
						clientSocket = new Socket("localhost", 14500);
						OutToServer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
						while(true){
							//tell the server the switch is sent
							OutToServer.write("Switch\n");		
							OutToServer.flush();
							InFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
							//read the time sent from the server
							String date = InFromServer.readLine();
							DateFormat Format = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
							//parse the data from the server
							Date serverDate = Format.parse(date);
							//format the server for the Runtime commands
							DateFormat newFormatDate = new SimpleDateFormat("dd-MM-yy");
							DateFormat newFormatTime = new SimpleDateFormat("HH:mm:ss");
							Runtime.getRuntime().exec("cmd /C date " + newFormatDate.format(serverDate));//in dd-MM-yy
							Runtime.getRuntime().exec("cmd /C time " + newFormatTime.format(serverDate));//in HH:mm:ss
							System.out.print("Received timestamp on ");
				        	System.out.print(newFormatDate.format(serverDate));
				        	System.out.print(" at ");
				        	System.out.println(newFormatTime.format(serverDate));
							System.out.println("Press Enter to terminate.");
							DataInputStream disconnect = new DataInputStream(System.in);
							while(disconnect.available() == 0){}
							clientSocket.close();
							OutToServer.close();
							InFromServer.close();
							System.exit(0);
							
						}
					}else{
					//tell the server the switch has been input
					OutToServer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
					while(true){
						//commented above, same code
						OutToServer.write("Switch\n");		
						OutToServer.flush();
						InFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
						String date = InFromServer.readLine();
						DateFormat Format = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
						Date serverDate = Format.parse(date);
						DateFormat newFormatDate = new SimpleDateFormat("dd-MM-yy");
						DateFormat newFormatTime = new SimpleDateFormat("HH:mm:ss");
						Runtime.getRuntime().exec("cmd /C date " + newFormatDate.format(serverDate));
						Runtime.getRuntime().exec("cmd /C time " + newFormatTime.format(serverDate));
						System.out.println("Press Enter to terminate.");
						DataInputStream disconnect = new DataInputStream(System.in);
						while(disconnect.available() == 0){}
						clientSocket.close();
						OutToServer.close();
						InFromServer.close();
						System.exit(0);
					}
					}
				}
			}
			//if there are no arguments, set the client to connect to the local host
			}else{
				clientSocket = new Socket("localhost", 14500);
				OutToServer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
				InFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				while(true){
					OutToServer.write("Communicating with Server" + '\n');
					OutToServer.flush();
					System.out.println("Press Enter to terminate.");
					DataInputStream disconnect = new DataInputStream(System.in);
					while(disconnect.available() == 0){}
					clientSocket.close();
					OutToServer.close();
					InFromServer.close();
					System.exit(0);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	private static boolean isHostname(String host) {
		
    if (host.indexOf(':') != -1) return false;
      
    char[] c = host.toCharArray();
	for (int i = 0; i < c.length; i++) {
		if (!Character.isDigit(c[i])) {
			if (c[i] != '.') return true;
		}
	}
    return false;

   }
}


