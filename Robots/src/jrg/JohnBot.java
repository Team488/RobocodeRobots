package jrg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

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
  Position predictedEnemyPosition = new Position(0, 0);
  ArrayList<JohnBullet> myFiredBullets;
  
  public void run()
  {
	  setAdjustRadarForRobotTurn(true);
	  setAdjustGunForRobotTurn(true);
	  setAdjustRadarForGunTurn(true);
	  
	  myFiredBullets = new ArrayList<JohnBullet>();
	  
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
	  double absoluteHeadingRobocodeUnits = (getHeadingRadians() + e.getBearingRadians());
	  
	    
	  absoluteHeadingToEnemyRobot = RobocodePhysicsUtil.ConvertRadians(absoluteHeadingRobocodeUnits);
	  // if negative make positive, just to make debugging a little easier
	  if (absoluteHeadingToEnemyRobot < 0)
	  {
		  absoluteHeadingToEnemyRobot += 2*Math.PI;
	  }
	  
	  Position enemyPos = determineEnemyLocation(e.getDistance());
	  
	  //TODO: this needs to take into account how far away the enemy robot is, including predicted bullet travel time.
	  predictedEnemyPosition = predictEnemyFutureLocation(enemyPos);
	  double aimGunAt = determineAbsoluteBearingToPosition(predictedEnemyPosition);
	  pointGunAt(aimGunAt);
	  
	  double necessaryRadarTurn = absoluteHeadingRobocodeUnits - getRadarHeadingRadians();
	  setTurnRadarRightRadians(1.3 * Utils.normalRelativeAngle(necessaryRadarTurn)); 
	  
	  //System.out.println("Predicting enemy will be at: " + predictedEnemyPosition.toString());
	  //System.out.println("My location is:              " + getSelfPosition().toString());
	  //System.out.println("Enemy robot found at:        " + enemyPos.toString());
	  
	  circleStrafe();
	  
	  FireAsneeded(e.getDistance());
  }
  
  private Position getSelfPosition()
  {
	  return new Position(getX(), getY());
  }
  
  private Position determineEnemyLocation(double distance)
  {
	  //System.out.println("Absolute bearing to enemy: " + this.absoluteHeadingToEnemyRobot);
	  //System.out.println("Distance to enemy: " + distance);
	  double enemyX = getX() + distance * Math.cos(this.absoluteHeadingToEnemyRobot); 
	  double enemyY = getY() + distance * Math.sin(this.absoluteHeadingToEnemyRobot);
	  
	  return new Position(enemyX, enemyY);
  }
  
  private Position predictEnemyFutureLocation(Position currentEnemyPos)
  {	  
	  double bulletSpeed = Rules.getBulletSpeed(1);
	  System.out.println("ExpectedSpeed:"+ bulletSpeed);
	  
	  Position velocity = lastEnemyPosition.diffFrom(currentEnemyPos);
	  
	  
	  double timeOfImpact = time_of_impact(currentEnemyPos.X, currentEnemyPos.Y, velocity.X, velocity.Y, bulletSpeed);
	  
	  //predict forwards to eventual position
	  velocity.ScalarMult(timeOfImpact);
	  	  
	  Position predictedPosition = currentEnemyPos.clone();
	  predictedPosition.Add(velocity);
	  
	  predictedPosition.restrict(getBattleFieldWidth(), getBattleFieldHeight());
	  //System.out.println("Predicted: "+ predictedPosition);
	  //System.out.println("Height: "+ getBattleFieldHeight());
	  lastEnemyPosition = currentEnemyPos;
	  return predictedPosition;
  }
  
  private double time_of_impact(double px, double py, double vx, double vy, double s)
  {
      double a = s * s - (vx * vx + vy * vy);
      double b = px * vx + py * vy;
      double c = px * px + py * py;

      double d = b*b + a*c;

      double t = 0;
      if (d >= 0)
      {
          t = (b + Math.sqrt(d)) / a;
          if (t < 0)
              t = 0;
      }

      return t;
  }
  
  private double determineAbsoluteBearingToPosition(Position pos)
  {
	  Position diff = getSelfPosition().diffFrom(pos);
	  
	  return Math.atan2(diff.Y, diff.X);
  }

	private void pointGunAt(double goalBearing) {
		// Figure out where the gun is currently pointing
		//System.out.println("GoalBearing:" + goalBearing);
		double currentGunHeading = RobocodePhysicsUtil.ConvertRadians(getGunHeadingRadians());
		//System.out.println("CurrentGun:" + currentGunHeading);
		// Compare that to where we want to shoot
		double diffInHeadings = JohnUtils.SmallestAngleBetween(currentGunHeading, goalBearing); 
		// Point in that direction
		setTurnGunRightRadians(diffInHeadings);
	}
  
	private void FireAsneeded(double distance) {
		double power = 1;
		
		if (distance < 30)
		{
			power = 10;
		}
		
		if (distance < 1000)
		  {
			  if (getGunHeat() == 0)
			  {
				  JohnBullet b = new JohnBullet(setFireBullet(power));
				  myFiredBullets.add(b);
			  }
		  }
	}
	
	private void circleStrafe() {
		double leftChassisTurn = 
				JohnUtils.SmallestAngleBetween(
						absoluteHeadingToEnemyRobot + Math.PI/2, RobocodePhysicsUtil.ConvertRadians(getHeadingRadians()));  
		double rightChassisTurn = 
				JohnUtils.SmallestAngleBetween(
						absoluteHeadingToEnemyRobot - Math.PI/2, RobocodePhysicsUtil.ConvertRadians(getHeadingRadians()));
		
		double chassisTurn = 0;
		if (Math.abs(leftChassisTurn) < Math.abs(rightChassisTurn))
		{
			System.out.println("Left");
			chassisTurn = leftChassisTurn;
		}
		else
		{
			System.out.println("Right");
			chassisTurn = -rightChassisTurn;
		}
		
		// There also needs to be some "repulsion" from walls.
		// super simple - if too far from center, turn towards center?
		
		Position arenaCenter = new Position(getBattleFieldWidth()/2, getBattleFieldHeight()/2);
		Position self = getSelfPosition();
		
		if (arenaCenter.getDistanceTo(self) > arenaCenter.Y*.66)
		{
			System.out.println("Turning to center");
			// use different movement, turn to center
			chassisTurn = 
					JohnUtils.SmallestAngleBetween(
							determineAbsoluteBearingToPosition(arenaCenter), RobocodePhysicsUtil.ConvertRadians(getHeadingRadians()));  
		}
				  
		  if (getVelocity() == 0)
		  {
			  direction*= -1;
		  }
		  setTurnRightRadians(chassisTurn);
		  setAhead(1000 * direction);
	}
	
	public void onPaint(Graphics2D g){
		g.setColor(java.awt.Color.RED);
		g.fillRect((int)lastEnemyPosition.X, (int)lastEnemyPosition.Y, 10, 10);
		g.setColor(java.awt.Color.YELLOW);
		g.fillRect((int)predictedEnemyPosition.X, (int)predictedEnemyPosition.Y, 10, 10);
		g.setColor(java.awt.Color.RED);
		
		double aimGunAt = determineAbsoluteBearingToPosition(predictedEnemyPosition);
		double currentGunHeading = RobocodePhysicsUtil.ConvertRadians(getGunHeadingRadians());
		double diffInHeadings = currentGunHeading - aimGunAt;
		
		g.setColor(java.awt.Color.RED);
		g.fillRect(0, 0, 20, (int)diffInHeadings*100);
		
		//paintLineFromRobot(g, absoluteHeadingToEnemyRobot, java.awt.Color.GREEN);
		paintLineFromRobotToAbsolute(g, predictedEnemyPosition, java.awt.Color.YELLOW);
		paintLineFromRobot(g, aimGunAt, java.awt.Color.RED);
		
		paintLineFromRobot(g, absoluteHeadingToEnemyRobot+ Math.PI/2, java.awt.Color.ORANGE);
		
		myFiredBullets.removeIf(b -> b.inner().isActive() == false);
		System.out.println("Size:" + myFiredBullets.size());
		for (JohnBullet b : myFiredBullets)
		{
			if (b != null)
			{
				System.out.println("Speed:" + b.inner().getVelocity());
				paintBulletTrack(g, b, Color.BLACK);
			}
		}
		
	}
	
	private void paintBulletTrack(Graphics2D g, JohnBullet b, java.awt.Color color)
	{
		g.setColor(color);
		double radians = RobocodePhysicsUtil.ConvertRadians(b.inner().getHeadingRadians());
		Position finalPos = new Position(Math.cos(radians), Math.sin(radians));
		finalPos.ScalarMult(1000);
		finalPos.Add(b.initial);
		g.drawLine((int)b.initial.X, (int)b.initial.Y, (int)finalPos.X, (int)finalPos.Y);
	}
	
	private void paintLineFromRobotToAbsolute(Graphics2D g, Position pos, java.awt.Color color)
	{
		g.setColor(color);
		Position mine = getSelfPosition();
		g.drawLine((int)mine.X, (int)mine.Y, (int)pos.X, (int)pos.Y);
	}
	
	private void paintLineFromRobot(Graphics2D g, Position pos, java.awt.Color color)
	{
		g.setColor(color);
		Position mine = getSelfPosition();
		pos.Add(mine);
		g.drawLine((int)mine.X, (int)mine.Y, (int)pos.X, (int)pos.Y);
	}
	
	private void paintLineFromRobot(Graphics2D g, double radians, java.awt.Color color)
	{
		Position pos = new Position(Math.cos(radians), Math.sin(radians));
		pos.ScalarMult(100);
		paintLineFromRobot(g, pos, color);
	}
}
