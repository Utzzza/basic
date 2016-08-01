package  fractals;

 
public class Pozitionare {
	public double angle;
	public Punct anchor;
	
	Pozitionare() {
		anchor = new Punct();
	}
	
	public Pozitionare copy() {
		Pozitionare p = new Pozitionare();
		p.angle = angle;
		p.anchor.x = anchor.x;
		p.anchor.y = anchor.y;
		return p;
	}
}
