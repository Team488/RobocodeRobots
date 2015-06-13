package jrg;

import robocode.AdvancedRobot;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class JohnBot
  extends AdvancedRobot
{
  double absoluteHeadingToEnemyRobot;
  double plannedRadarTurn;
  double lastPlannedRadarTurn;
  double direction = 1;
  
  public void run()
  {
	  setAdjustRadarForRobotTurn(true);
	  setAdjustGunForRobotTurn(true);
	  setAdjustRadarForGunTurn(true);
	  
	  System.out.println("Starting JohnBot.");
	    while (true)
	    {
	    	System.out.println("Core loop start");
	    	turnRadarRightRadians(Double.POSITIVE_INFINITY);
	        scan();
	    }
  }
    
  public void onScannedRobot(ScannedRobotEvent e)
  {
	  System.out.println("Enemy robot found!");
	  this.absoluteHeadingToEnemyRobot = (getHeadingRadians() + e.getBearingRadians());
    
	  double necessaryRadarTurn = this.absoluteHeadingToEnemyRobot - getRadarHeadingRadians();
	  setTurnRadarRightRadians(1.3 * Utils.normalRelativeAngle(necessaryRadarTurn)); 
	  
	  pointGunAtEnemy();
	  
	  circleStrafe();
	  
	  FireAsneeded();
  }

	private void pointGunAtEnemy() {
		double gunTurn = absoluteHeadingToEnemyRobot - this.getGunHeadingRadians();
		  if (gunTurn > Math.PI/4)
		  {
			  gunTurn = Math.PI/4;
		  }
		  setTurnGunRightRadians(gunTurn);
	}
  
	private void FireAsneeded() {
		  System.out.println("Firing as needed");
		  setFire(Rules.MAX_BULLET_POWER);
	}
	
	private void circleStrafe() {
		double chassisTurn = absoluteHeadingToEnemyRobot - this.getHeadingRadians() + (Math.PI / 2)* 0.75;
		
		
		if (chassisTurn > Math.PI/4)
		  {
			chassisTurn = Math.PI/4;
		  }
		  setTurnRightRadians(chassisTurn);
		  
		  if (getVelocity() == 0)
		  {
			  direction*= -1;
		  }
		  
		  setAhead(1000 * direction);
	}
}
