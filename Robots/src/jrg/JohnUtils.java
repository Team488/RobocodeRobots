package jrg;

public class JohnUtils {

	public static double SmallestAngleBetween(double radianA, double radianB)
	{
		double a = radianA - radianB;
		double b = radianA + 2*Math.PI -radianB;
		double c = radianA - 2*Math.PI - radianB;
		
		if (isAbsoluteSmallest(a, b, c))
		{
			return a;
		}
		if (isAbsoluteSmallest(b, a, c))
		{
			return b;
		}
		return c;
	}
	
	private static boolean isAbsoluteSmallest(double candidate, double option1, double option2)
	{
		if (Math.abs(candidate) < Math.abs(option1) && (Math.abs(candidate) < Math.abs(option2)))
		{
			return true;
		}
		return false;
	}
}
