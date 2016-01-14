//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.Socket;
//
//public class ServerHandler 
//{
//	BufferedReader receive; 
//	boolean processedMessage = false;
//	String messageReceived;
//	
//	public void startServerHandler(Socket s)
//	{
//		
//		try {
//			receive = new BufferedReader(new InputStreamReader(s.getInputStream()));
//		} catch (IOException e) {
//			System.out.println("");
//			e.printStackTrace();
//		}
//		
//	}
//	
////	
////	public ServerHandler(){
////		
////		
////	}
//
//	public String getMessage(){
//		
//		if(processedMessage) return readMessage();
//		
//
//		
//	}
//	
//	public String readMessage(){
//		
//		try {
//			messageReceived = receive.readLine();
//			
//		} catch (IOException e) {
//			System.out.println("Error");
//			e.printStackTrace();
//		}
//		return messageReceived;
//
//	}
//		
//	
//}
