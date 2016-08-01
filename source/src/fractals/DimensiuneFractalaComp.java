package  fractals;

import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import  fractals.FracDim.ImagePanel;

public class DimensiuneFractalaComp {

	static final int ANGLE_TRIALS = 100;
	static final int PLACEMENT_TRIALS = 100;
	static final int MAX_BOX_DIM = 10;
	static final double STARTING_DIM = 8.0;
	static final double PI = 3.1415926537;
	static final double SMALL_FLOAT = 0.00001;
	
	private SetarePunct pSet;
	private int boxesFilled;
	
	/**
	 * Avand la dispozitie punctul ancora si dimensiunea cutiei, calculeaza numarul cutiilor 
         * ce acopera intregul set de puncte. 
	 * @param pSet punctul setat
	 * @param anchor punctul ancora
	 * @param boxDim dimensiunea cutiei testate
	 * @param boxSize marimea cutiei
	 * @intoarce numarul cutiilor umplute
	 */
	int computeBoxesFilled(SetarePunct pSet, Punct anchor, double boxDim, double boxSize)
	{
		int pointsInBoxDim = (int)boxDim + 1;
		int[][] pointsInBox = new int[pointsInBoxDim][pointsInBoxDim];

		//
		// Initializeaza evidenta variabilelor cu 0.
		//
		for (int r = 0; r < pointsInBoxDim; r++)
			for (int c = 0; c < pointsInBoxDim; c++)
				pointsInBox[r][c] = 0;
		boxesFilled = 0;
		//
		// Pentru fiecare punct, verifica evidenta cutiilor ocupate; creste
		// elementul corespunzator punctelor din cutie (vector) si totalul
		// cutiilor umplute (suma).
		//
		Punct[] points = pSet.getPoints();
		for (int p = 0; p < pSet.getNumPoints(); p++)
		{
			int r = (int)((points[p].y - anchor.y) / boxSize);
			int c = (int)((points[p].x - anchor.x) / boxSize);
			if (pointsInBox[r][c] == 0)
				boxesFilled = boxesFilled + 1;
			pointsInBox[r][c] = pointsInBox[r][c] + 1;
		}
		return boxesFilled;
	}


	//
	// Obtine un unghi intre 0 si 90 grade. 
	//
	double getRandomAngle()
	{
		return Math.random() * Math.PI / 2.0;
	}


	//
	// Obtine un punct ancora pentru grila. Acesta trebuie sa fie sub, dar si in stanga
	// fiecarui punct din gama.   
	//
	Punct getRandomAnchorPoint(Dreptunghi bounds, double boxSize)
	{
		Punct anchor = new Punct();
		anchor.x = bounds.minX - Math.random() * boxSize;
		anchor.y = bounds.minY - Math.random() * boxSize;
		return anchor;
	}


	//
	// Aceasta functie returneaza marimea unei cutii care va acoperi intregul set de date
	// indiferent de modul in care este eventual rotit.
	//
	double getInitialBoxSize()
	{
		double boxSize;
		Dreptunghi bounds = new Dreptunghi();

		bounds = pSet.determineBounds();
		boxSize = bounds.maxX - bounds.minX;
		if (boxSize < bounds.maxY - bounds.minY)
			boxSize = bounds.maxY - bounds.minY;
		boxSize *= 1.414213562373096; // enlarge box size to account for point-set rotations
		return boxSize;
	}

	//
	// Foloseste o aproximatie Monte Carlo pentru a estima numarul minim de cutii umplute de punctul setat.
  
	int estimateMinBoxesFilled(double boxDim, double boxSize, Pozitionare placement)
	{
		int i, j; // varibile locale
		double theta; // unghiul de rotatie pentru punctul setat ce respecta grila
		Punct anchor = new Punct(); // punctul ancora
		int boxesFilled; // ceea ce se incearca sa se estimeze
		int minBoxesFilled;
	
		minBoxesFilled = pSet.getNumPoints() + 1;
		for (i = 0; i < ANGLE_TRIALS; i++)
		{
			theta = getRandomAngle();
			SetarePunct pRot = pSet.rotatePointSet(theta);
			Dreptunghi bounds = pRot.determineBounds();
			for (j = 0; j < PLACEMENT_TRIALS; j++)
			{
				anchor = getRandomAnchorPoint(bounds, boxSize);
				boxesFilled = computeBoxesFilled(pRot, anchor, boxDim, boxSize);
				if (boxesFilled < minBoxesFilled)
				{
					minBoxesFilled = boxesFilled;
					placement.angle = theta;
					placement.anchor.x = anchor.x;
					placement.anchor.y = anchor.y;
				}
			}
		}
		return minBoxesFilled;
	}


	/**
	 * Sleep for a number of seconds.
	 * @param seconds
	 */
	private void sleep(int seconds)
	{
		try
		{
			Thread.currentThread();
			Thread.sleep(1000 * seconds);
		}
		catch(InterruptedException ie)
		{
			;
		}
	}


	 
	
	DimensiuniCutie estimateFractalDimension(SetarePunct pointSet, BufferedImage bImage, ImagePanel iPanel,
			JLabel statusLabel)
	{
		Pozitionare placement = new Pozitionare();
		pSet = pointSet;
		DimensiuniCutie bStats = new DimensiuniCutie(MAX_BOX_DIM);		
		if (pSet.getNumPoints() == 0)
			return bStats;
		double boxDim = STARTING_DIM;
		double boxSize = getInitialBoxSize() / boxDim;
		double maxDim = STARTING_DIM * Math.pow(2.0, Math.log10(pSet.getNumPoints())) / 2;
		DesenareCutie bDrawer = new DesenareCutie(pSet, bImage, iPanel);
		System.out.printf("\t\tCutie\t\t\t\t\tCutie\n");
		System.out.printf("Cutii\t\tDim\t\t(X0, Y0)\t\tSize\t\tTheta\n");
		do //  
		{
			boxesFilled = estimateMinBoxesFilled(boxDim, boxSize, placement);
			System.out.printf("%d\t\t1/%5.2f\t\t(%6.2f,%6.2f)\t%9.2f\t%9.2f\n", boxesFilled,
					boxDim, placement.anchor.x, placement.anchor.y, boxSize, 180.0 / PI * placement.angle);
			bStats.add(boxDim, boxesFilled, placement, boxSize);
			bDrawer.drawPoints();
			bDrawer.drawBoxes(placement, boxDim, boxSize);
			statusLabel.setText(boxesFilled + " cutii -- Calculeaza . . .");
			statusLabel.paintImmediately(0, 0, statusLabel.getWidth(), statusLabel.getHeight());
			boxDim = boxDim * 2.0;
			boxSize = boxSize / 2.0;
		}
		while (boxDim <= maxDim + SMALL_FLOAT);
		System.out.printf("Dimensiunea Fractala estimata = %f\n", bStats.getFracDim());
		sleep(5);
		bDrawer.drawPoints();
		return bStats;
	}
}