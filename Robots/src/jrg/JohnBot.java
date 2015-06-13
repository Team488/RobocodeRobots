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
  Position lastEnemyPosition = new Position(0, 0);
  
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
	  this.absoluteHeadingToEnemyRobot = (getHeadingRadians() + e.getBearingRadians());
	  
	  Position enemyPos = determineEnemyLocation(e);
	  System.out.println("My location is:              " + getSelfPosition().toString());
	  System.out.println("Enemy robot found at:        " + enemyPos.toString());
	  Position predictedEnemyPos = predictEnemyFutureLocation(enemyPos);
	  System.out.println("Predicting enemy will be at: " + predictedEnemyPos.toString());
	  double aimGunAt = determineAbsoluteBearingToPosition(predictedEnemyPos);
    
	  double necessaryRadarTurn = this.absoluteHeadingToEnemyRobot - getRadarHeadingRadians();
	  setTurnRadarRightRadians(1.3 * Utils.normalRelativeAngle(necessaryRadarTurn)); 
	  
	  pointGunAt(aimGunAt);
	  
	  circleStrafe();
	  
	  FireAsneeded();
  }
  
  private Position getSelfPosition()
  {
	  return new Position(getX(), getY());
  }
  
  private Position determineEnemyLocation(ScannedRobotEvent e)
  {
	  System.out.println("Absolute bearing to enemy: " + this.absoluteHeadingToEnemyRobot);
	  System.out.println("Distance to enemy: " + e.getDistance());
	  double enemyX = getX() + e.getDistance() * Math.cos(this.absoluteHeadingToEnemyRobot); 
	  double enemyY = getY() + e.getDistance() * Math.sin(this.absoluteHeadingToEnemyRobot);
	  
	  return new Position(enemyX, enemyY);
  }
  
  private Position predictEnemyFutureLocation(Position currentEnemyPos)
  {	  
	  Position predictedPosition = currentEnemyPos.clone();
	  predictedPosition.Add(lastEnemyPosition.diffFrom(currentEnemyPos));
	  
	  lastEnemyPosition = currentEnemyPos;
	  return predictedPosition;
  }
  
  private double determineAbsoluteBearingToPosition(Position pos)
  {
	  Position currentSelfPosition = getSelfPosition();
	  Position diff = getSelfPosition().diffFrom(pos);
	  
	  return Math.atan2(diff.Y, diff.X);
  }

	private void pointGunAt(double absoluteBearing) {
		double gunTurn = absoluteBearing - this.getGunHeadingRadians();
		/*  if (gunTurn > Math.PI/4)
		  {
			  gunTurn = Math.PI/4;
		  }*/
		  setTurnGunRightRadians(absoluteBearing);
	}
  
	private void FireAsneeded() {
		  System.out.println("Firing as needed");
		  setFire(1);
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
