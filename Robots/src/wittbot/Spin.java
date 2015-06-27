package wittbot;

import robocode.HitRobotEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;

public class Spin extends Robot{

	boolean robotSeen = false;
	
	public void run()
	{
		while (true)
		{
			if(!robotSeen) {
				turnLeft(10);
			} else {
				ahead(10);
				robotSeen = false;
				turnRight(4);
				turnLeft(1.5);
			}
		}
	}
	
	public void onScannedRobot (ScannedRobotEvent e) {
		fire(e.getDistance()/e.getVelocity());
		robotSeen = true;
	}
	
	public void onHitByBullet () {
		turnLeft(90);
		ahead(100);
	}
}
