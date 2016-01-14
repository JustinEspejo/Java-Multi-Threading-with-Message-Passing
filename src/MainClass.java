import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import Server.SubServer;


/*
 * Justin Espejo
 * CS344
 * Project 1 - Monitors
 */


public class MainClass {
	
	public static BufferedReader receive;
	public static String message = "";
	
	public static void main(String[] args){
		//Initial Values (later ask the user)
		int theater_capacity = 7;
		int blueStudents = 8;
		int orangeStudents = 16;
		Monitor h = new Monitor(theater_capacity);
	
		
        try {
    		String server = (args.length == 0) ? "localhost" : args[1];
			Socket s = new Socket(server, 6789);
			//create orange student thread
			receive = new BufferedReader(new InputStreamReader(s.getInputStream()));
			message = receive.readLine();
			
			Clock c = new Clock(h,s);

			BlueStudent o[] = new BlueStudent[orangeStudents];
			for(int i = 0; i < orangeStudents; i++){
				o[i] = new BlueStudent(h, i, true,c,s);
			}
			
			//create blue student thread
			BlueStudent b[] = new BlueStudent[blueStudents];
			for(int i = 0; i < blueStudents; i++){
				b[i] = new BlueStudent(h, i, false,c,s);
			}
			
		} catch (UnknownHostException e) {
			System.out.println("Error: Unknown host exception :" + e);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error: :" + e);
			e.printStackTrace();
		}

	}
	

}
