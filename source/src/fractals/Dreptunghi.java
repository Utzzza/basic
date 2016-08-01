package  fractals;

/**
 *  
 * Aceasta clasa e tratata ca o structura C.  Aici sunt tinute dimensiunile dreptunghiului.
 */
public class Dreptunghi {
	double minX, minY, maxX, maxY;
	
	Dreptunghi(double minXX, double minYY, double maxXX, double maxYY) {
		minX = minXX;
		minY = minYY;
		maxX = maxXX;
		maxY = maxYY;
	}
	
	Dreptunghi() {
		minX = 0.0;
		minY = 0.0;
		maxX = 0.0;
		maxY = 0.0;
	}
}