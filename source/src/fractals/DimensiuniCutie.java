package fractals;

/**  
 * Clasa ce mentine datele acumulate in legatura cu estimarea  
 */
public class DimensiuniCutie {

	private int maxDim;
	private int curDim;
	private double[] boxDim;
	private int[] boxesFilled;
	private Pozitionare[] placement;
	private double[] boxSize;
	private double fracDim;

	DimensiuniCutie(int maxD)
	{
		maxDim = maxD;
		curDim = -1;
		boxDim = new double[maxD];
		boxesFilled = new int[maxD];
		placement = new Pozitionare[maxD];
		boxSize = new double[maxD];
		fracDim = 1.0;
	}
	
	public void add(double bD, int bF, Pozitionare pl, double bS) {
		if (curDim < maxDim - 1) {
			curDim++;
			boxDim[curDim] = bD;
			boxesFilled[curDim] = bF;
			placement[curDim] = pl.copy();
			boxSize[curDim] = bS;
			fracDim = logRegression(boxDim, boxesFilled, curDim + 1);
			fracDim = Math.round(fracDim * 100.0) / 100.0;
		}
	}
	
	public int getNumDims() {
		return curDim;
	}
	
	public double getFracDim() {
		return fracDim;
	}

	public Pozitionare getPlacement(int i) {
		if (i < 0 || i > curDim)
			return null;
		else
			return placement[i];
	}
	
	public double getBoxDim(int i) {
		if (i < 0 || i > curDim)
			return -1.0;
		else
			return boxDim[i];
	}
	
	public double getBoxSize(int i) {
		if (i < 0 || i > curDim)
			return -1.0;
		else
			return boxSize[i];
	}
	
	public int getBoxesFilled(int i) {
		if (i < 0 || i > curDim)
			return -1;
		else
			return boxesFilled[i];
	}

	 
	private double logRegression(double[] X, int[] Y, int N)
	{
		int i;
		double mX, mY, vX, vXY;  
		double dX, dY;  
		
		 
		if (N < 2) return 0.0;
		else if (N < 3) return 1.0;
		 
		mX = mY = 0.0;
		for (i = 0; i < N; i++)
		{
			mX += Math.log(X[i]);
			mY += Math.log((double)Y[i]);
		}
		mX /= (double)N;
		mY /= (double)N;
		 
		vX = vXY = 0.0;
		for (i = 0; i < N; i++)
		{
			dX = Math.log(X[i]) - mX;
			vX += dX * dX;
			dY = Math.log(Y[i]) - mY;
			vXY += dX * dY;
		}
		 
		return vXY / vX;
	}

}