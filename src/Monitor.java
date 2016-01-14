/*
 * Justin Espejo
 * CS344
 * Project 1 - Monitors
 */

import java.util.Vector;

public class Monitor {
	protected boolean timesUp = false;
	int count =0;
	private Vector<Object> orangeStudents = new Vector<Object>();
	private Vector<Object> blueStudents = new Vector<Object>();
	private Vector<Object> waitingForShow = new Vector<Object>();
	private int showCapacity;
	private boolean paradeHappening;
	private Object groupDone = new Object();
	Object waitParade = new Object();
	Object groupFinding = new Object();
	Object startGroupParade = new Object();
	Object waitingToFinishParade = new Object();

	public Monitor (int showCapacity) {
		this.showCapacity = showCapacity;

	}


	protected void waitForParade(BlueStudent s){
		synchronized(waitParade){
			while(true){
				try
				{
					s.display("Waiting for parade");
					waitParade.wait();
					break;
				}
				catch (InterruptedException e){
					continue;
				}
			}
		}
	}

	protected void paradeStart(){
		synchronized (waitParade){
			waitParade.notifyAll();
		}
	}

	protected void findGroup(BlueStudent s){
		Object groupFinding = new Object();
		synchronized (groupFinding){
			//				s.display("its my turn");
			boolean color = s.isOrange;
			if (waitGroup(groupFinding,color,s)){
				try {
					groupFinding.wait();
					startParade(s);

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



	private void startParade(BlueStudent s) {
		s.display(" is parading");
		nap(500);		
	}



	protected synchronized boolean waitGroup(Object groupFinding, boolean color,BlueStudent s){
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
				startParade(s);

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
				startParade(s);

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

	public void showLine(BlueStudent s) {

		Object convey = new Object();
		synchronized (convey) {
			if (cannotEnterNow(convey,s))
				while (true) // wait to be notified, not interrupted
					try { convey.wait();
					showTime(s);
					break; }
			// notify() after interrupt() race condition ignored
			catch (InterruptedException e) { continue; }
		}
	}

	private void showTime(BlueStudent s) {

		s.display(" is Watching the show");
		nap(3000);		

	}


	private synchronized boolean cannotEnterNow(Object convey, BlueStudent s) {
		waitingForShow.addElement(convey);
		//			s.display("this is the size" + waitingForShow.size());
		return true;
	}

	public synchronized void startShow()
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



	private void nap(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public void endtime() {
		int length = waitingForShow.size();
		for(int i=0; i<length; i++){
			synchronized(waitingForShow.elementAt(0)){
				waitingForShow.elementAt(0).notify();
				waitingForShow.removeElementAt(0);
			}
		}	


	}


	public void endParade() {
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



