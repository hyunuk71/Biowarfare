package gamePlay;

import javax.swing.*;

public class Timer extends JPanel implements Runnable {
	Thread thread;
	JLabel timeLimit;

	public Timer(){
		JLabel timeLimit = new JLabel();
		timeLimit.setBounds(50, 50, 100, 30);
		if(thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void timerCount() {
		int time = 10;
		timeLimit.setText("" + time);



	}


	@Override
	public void run() {
		try{
			while(true){
				timerCount();
					Thread.sleep(100);
			}
		} catch (Exception e){}
	}
}
