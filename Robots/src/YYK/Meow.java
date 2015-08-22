package YYK;

import robocode.BulletHitEvent;
import robocode.HitRobotEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;


public class Meow extends Robot{

	public void run()
	{
		while (true)
		{
		turnLeft(20);	
		}
	}
	public void onScannedRobot(ScannedRobotEvent e) {
	        fire(3);
	        fire(3);
	        ahead(200);
    }
	
	public void onBulletHit(BulletHitEvent evnt)
	{
	    System.out.println("I hit " + evnt.getName() + "!");
	}
		public void onHitRobot(HitRobotEvent event) {
		       if (event.getBearing() > -90 && event.getBearing() <= 90) {
		           fire(4);
    	           ahead(200);
    	           fire(4);
    	           ahead(100);
    	           
		       }
		}
	}
		
