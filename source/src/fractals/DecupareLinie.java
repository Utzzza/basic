package  fractals;

/**
 * Clasa contine coordonatele liniei.  Constructorul se ocupa de tot:
 * ia coordonatele liniei plus cele ale zonei in care se afla pentru
 * a nu se extinde in afara sa.
 */
public class DecupareLinie extends Dreptunghi
{
	double x1, y1, x2, y2;
	boolean clippable;

	public DecupareLinie(double xx1, double yy1, double xx2, double yy2, double minXX, double minYY,
			double maxXX, double maxYY)
	{
		super(minXX, minYY, maxXX, maxYY);
		if (xx1 < xx2)
		{
			x1 = xx1;
			y1 = yy1;
			x2 = xx2;
			y2 = yy2;
		}
		else
		{
			x1 = xx2;
			y1 = yy2;
			x2 = xx1;
			y2 = yy1;			
		}
		clippable = clip();
	}
	
	
	private boolean clip()
	{
		double rangeX = x2 - x1;
		double rangeY = y2 - y1;
		if (x1 > maxX || x2 < minX)
			return false;
		if (x1 < minX)
		{
			if (rangeX == 0.0)
				return false;
			double m = rangeY / rangeX;
			double b = y1 - m * x1;
			x1 = minX;
			y1 = m * x1 + b;
		}
		if (x2 > maxX)
		{
			if (rangeX == 0.0)
				return false;
			double m = rangeY / rangeX;
			double b = y1 - m * x1;
			x2 = maxX;
			y2 = m * x2 + b;				
		}
		if (y1 < y2)
		{
			if (y1 > maxY || y2 < minY)
				return false;
			if (y1 < minY)
			{
				if (rangeY == 0.0)
					return false;
				double w = rangeX / rangeY;
				double d = x1 - w * y1;
				y1 = minY;
				x1 = w * y1 + d;
			}
			if (y2 > maxY)
			{
				if (rangeY == 0.0)
					return false;
				double w = rangeX / rangeY;
				double d = x2 - w * y2;
				y2 = maxY;
				x2 = w * y2 + d;
			}
		}
		else
		{
			if (y2 > maxY || y1 < minY)
				return false;
			if (y2 < minY)
			{
				if (rangeY == 0.0)
					return false;
				double w = rangeX / rangeY;
				double d = x2 - w * y2;
				y2 = minY;
				x2 = w * y2 + d;
			}
			if (y1 > maxY)
			{
				if (rangeY == 0.0)
					return false;
				double w = rangeX / rangeY;
				double d = x1 - w * y1;
				y1 = maxY;
				x1 = w * y1 + d;
			}
		}
		return true;
	}
	
	public boolean isClippable()
	{
		return clippable;
	}
}