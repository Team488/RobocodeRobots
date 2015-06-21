package jrg;

import robocode.Bullet;

public class JohnBullet {
	
	Bullet myBullet;
	Position initial;
	
	public JohnBullet(Bullet b)
	{
		myBullet = b;
		initial = new Position(b.getX(), b.getY());
	}
	
	public Bullet inner()
	{
		return myBullet;
	}

}
