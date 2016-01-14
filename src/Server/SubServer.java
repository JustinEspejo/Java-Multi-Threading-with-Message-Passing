package Server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

import javax.management.monitor.Monitor;


public class SubServer extends Thread{
	protected boolean timesUp = false;
	int count =0;
	private static Vector<Object> orangeStudents = new Vector<Object>();
	private static Vector<Object> blueStudents = new Vector<Object>();
	private static Vector<Object> waitingForShow = new Vector<Object>();
	private static int showCapacity;
	private boolean paradeHappening;
	private Object groupDone = new Object();
	static Object waitParade = new Object();
	Object groupFinding = new Object();
	Object startGroupParade = new Object();
	Object waitingToFinishParade = new Object();
	private BufferedReader receive;
	private static PrintWriter send;
	private int numSeat;
	

	 
	 SubServer(Socket s, int numSeat){
		this.numSeat = numSeat;
		
        try 
        {
			receive = new BufferedReader(new InputStreamReader(s.getInputStream()));
	        send = new PrintWriter(s.getOutputStream(), true);
		}
        catch (IOException e) 
        {
			System.out.println("Error: Cannot read InputStream");
			e.printStackTrace();
		}

	}
	 
	 public void run() {

	        	while (true) 
	        	{
	        		
	                try 
	                {
	                	String receivedMessage = receive.readLine();
	                	String process[] = receivedMessage.split(",", -1);
	                	int methodNumber = Integer.parseInt(process[0]);
	                	String threadType = process[1];
	                	String threadName = process[2];
	                	String id = process[3];
	                	processMessage(threadType,methodNumber,threadName,id);
					
	                } 
	                catch (IOException e) 
	                {
						System.out.println("Error");
						e.printStackTrace();
					}
	        		
	        	
	            }
	 }
	 
	 public static void processMessage(String threadType,int methodNumber,String threadName, String id){
		 
		 if(threadType.equals("STUDENT")){
			switch (methodNumber)
			{
			case 0: 
				waitForParade(id);
				break;
			case 1:
				findGroup(threadName);
				break;
			case 2:
				showLine();
				break;	
			}
		 }
		 else
		 {
			 switch(methodNumber)
			 {
				case 0: 
					paradeStart();
					break;
				case 1:
					startShow();
					break;
				case 2:
					endtime();
					break;
					
				case 3:
					endParade();
					break;		

			 }
			 
			 
		 }
	 }
	 


	 //student method 0
	protected static void waitForParade(String id){
		synchronized(waitParade){
			while(true){
				try
				{
					System.out.println("Waiting for parade");
//					s.display("Waiting for parade");
					waitParade.wait();
					break;
				}
				catch (InterruptedException e){
					continue;
				}
			}
			send.println(id);
		}
	}

	//clock method 0
	protected static void paradeStart(){
		synchronized (waitParade){
			waitParade.notifyAll();
		}
	}
	
	//student method 1
	protected static void findGroup(String threadName){
    	String StudentColor[] = threadName.split(" ", -1);
		Object groupFinding = new Object();
		boolean color;
		synchronized (groupFinding){
			//				s.display("its my turn");
			if(StudentColor[0].equals("Orange"))
			{
			color = true;
			}
			else
			{
			color = false;
			}
			if (waitGroup(groupFinding,color)){
				try {
					groupFinding.wait();
					startParade();

				} catch (InterruptedException e) {
					System.out.println("CANT BE HERE ");
				}
			}
			else{


			}
		}	

		//			System.out.println(blueStudents.size());
		//			System.out.println(orangeStudents.size());
	}


	//student 
	private static void startParade() {
//		display(" is parading");
	   System.out.println("Is parading");
		nap(500);		
	}


	//student 3
	protected synchronized static boolean waitGroup(Object groupFinding, boolean color){
		boolean status;

		if(color){

			if(blueStudents.size()==1 && orangeStudents.size()>=1)
			{
				//				System.out.println("found");
				synchronized (blueStudents.elementAt(0)) {
					blueStudents.elementAt(0).notify();
					blueStudents.removeElementAt(0);
				}         

				synchronized (orangeStudents.elementAt(0)) {
					orangeStudents.elementAt(0).notify();
					orangeStudents.removeElementAt(0);
				} 

				System.out.println("group formed");
				status = false;
				//				s.display(" has triggered group");
				startParade();

			}else if(blueStudents.size()>=0 && orangeStudents.size()>=0){
				//				System.out.println("blue");
				blueStudents.addElement(groupFinding);
				status = true;

			}
			//			else if(blueStudents.size()==0 && orangeStudents.size()==0){
			//				blueStudents.addElement(groupFinding);
			//				status = true;

			//			}
			else{	
				System.out.println("BLUE " + blueStudents.size() + " " + orangeStudents.size());
				status = true;
				System.out.println("Error: Cannot be here");
			}
		}
		else 
		{
			if(blueStudents.size()>=2 && orangeStudents.size()==0)
			{
				for(int i=0; i<=1; i++){
					synchronized (blueStudents.elementAt(0)) {
						blueStudents.elementAt(0).notify();
						blueStudents.removeElementAt(0);
					}         
				}


				System.out.println("group formed");
				//				s.display(" has triggered group");
				status = false;
				startParade();

			}else if(blueStudents.size()<2){
				//				System.out.println("orange");
				orangeStudents.addElement(groupFinding);
				status = true;
			}
			//			
			else{
				System.out.println("Error: Cannot be here");
				System.out.println("orange");
				System.out.println("ORANGE " + blueStudents.size() + " " + orangeStudents.size());
				status = true;

			}
		}

		return status;
	}

	//student 4
	public static void showLine() {

		Object convey = new Object();
		synchronized (convey) {
			if (cannotEnterNow(convey))
				while (true) // wait to be notified, not interrupted
					try { convey.wait();
					showTime();
					break; }
			// notify() after interrupt() race condition ignored
			catch (InterruptedException e) { continue; }
		}
	}

	//student 5
	private static void showTime() {
		System.out.println("is Watching the show");
//		s.display(" is Watching the show");
		nap(3000);		

	}

	private synchronized static boolean cannotEnterNow(Object convey) {
		waitingForShow.addElement(convey);
		//			s.display("this is the size" + waitingForShow.size());
		return true;
	}
	
	//clock 1
	public synchronized static void startShow()
	{
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> SHOWTIME <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		if(waitingForShow.size() < showCapacity){
			int length = waitingForShow.size();
			for(int i=0; i<length; i++){
				synchronized(waitingForShow.elementAt(0)){
					waitingForShow.elementAt(0).notify();
					waitingForShow.removeElementAt(0);
					System.out.println("not enough");
				}
			}
		}else
		{

			for(int i=0; i<showCapacity; i++){
				synchronized(waitingForShow.elementAt(0))
				{
					waitingForShow.elementAt(0).notify();
					waitingForShow.removeElementAt(0);
					//					System.out.println("enough");
				}
			}

		}

	}



	private static void nap(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//clock 2
	public static void endtime() {
		int length = waitingForShow.size();
		for(int i=0; i<length; i++){
			synchronized(waitingForShow.elementAt(0)){
				waitingForShow.elementAt(0).notify();
				waitingForShow.removeElementAt(0);
			}
		}	


	}

	//clock 3
	public static void endParade() {
		synchronized (waitParade){
			waitParade.notifyAll();
		}		

		for(int i=0; i<blueStudents.size(); i++){
			synchronized (blueStudents.elementAt(0)) {
				blueStudents.elementAt(0).notify();
				blueStudents.removeElementAt(0);
			} 
		}

		for(int i=0; i<orangeStudents.size(); i++){
			synchronized (orangeStudents.elementAt(0)) {
				orangeStudents.elementAt(0).notify();
				orangeStudents.removeElementAt(0);
			} 
		}
	}




}





	 
	 
	 
	 
