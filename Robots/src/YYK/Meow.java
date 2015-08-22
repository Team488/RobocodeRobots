package YYK;

import robocode.AdvancedRobot;
import robocode.BulletHitEvent;
import robocode.HitRobotEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;


public class Meow extends AdvancedRobot{

	public void run()
	{
		while (true)
		{
		setTurnLeft(50);
		setAhead(15);
		execute();
		}
	}
	public void onScannedRobot(ScannedRobotEvent e) {
	    setTurnRight(20);   
		setFire(4);
	        //ahead(100);
		execute();
   
	}
		public void onHitRobot(HitRobotEvent event) {
		       if (event.getBearing() > -90 && event.getBearing() <= 90) {
		         back(100);
		         
		      // Set the gun to turn 180 degrees to the left
		         setTurnGunLeft(180);

		         // Set the gun to turn 90 degrees to the right instead of left
		         // (overrides the previous order)
		         setTurnGunLeft(-90);

		         
		         // Executes the last setTurnGunLeft()
		         execute();
		       
		       }
		
		
		
		       
		}
	}
		
