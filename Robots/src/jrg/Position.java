package jrg;

public class Position {
	
	public double X;
	public double Y;
	
	public Position(double x, double y)
	{
		X = x;
		Y = y;
	}
	
	public void Add(double x, double y)
	{
		X += x;
		Y += y;
	}
	
	public void Add(Position pos)
	{
		X += pos.X;
		Y += pos.Y;
	}
	
	public double getDistanceTo(Position pos)
	{
		return Math.sqrt(Math.pow((pos.X - X), 2) + Math.pow((pos.Y - Y), 2));
	}
	
	public Position clone()
	{
		return new Position(X, Y);
	}
	
	public Position diffFrom(Position pos)
	{
		return new Position(pos.X - X, pos.Y - Y);
	}
	
	@Override
	public String toString() {
		return "X:" + X + ", Y:" + Y;
	}

}
