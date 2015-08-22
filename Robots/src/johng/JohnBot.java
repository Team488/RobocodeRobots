package johng;

import robocode.Robot;
import robocode.ScannedRobotEvent;

public class JohnBot extends Robot{

	public void run()
	{
		while (true)
		{
			turnRadarLeft(10);
		}
	}
	
	@Override
	public void onScannedRobot(ScannedRobotEvent event) {
		System.out.println("I saw a robot!");
		System.out.println("Bearing:" + event.getBearing());
		System.out.println("Distance:" + event.getDistance());
	}
}
