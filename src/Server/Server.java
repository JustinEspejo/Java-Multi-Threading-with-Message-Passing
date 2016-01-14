package Server;

import java.io.IOException;
import java.net.ServerSocket;


public class Server {
	int numSeat=7;
	private static ServerSocket socket;

	
	public Server ()
	{
//		while(true){
		try {
				System.out.println("Server Ready!");
				socket = new ServerSocket(6789);
				SubServer subServer = new SubServer(socket.accept(), numSeat);
				
			//spawn a helper,in here is named SubServerThread 	
				
		}//try
		catch (IOException e)
		{
			System.out.println("Unable to listen to port.");
			e.printStackTrace();
		}//catch
	}//constructor
//	}
	
	public static void main (String [] args)
	{
		new Server();
	}//main
	
	
}//class
