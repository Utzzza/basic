package fractals;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

 
public class FracDim extends JApplet implements ActionListener {
	private static final long serialVersionUID = 7720616729741130658L;
	private static final int PREFERRED_IMAGE_WIDTH = 400;
	private static final int PREFERRED_IMAGE_HEIGHT = 400;

	JButton openButton;
	ImagePanel displayPanel;
	int imageWidth, imageHeight;
	JLabel statusLabel;
	SetarePunct pSet;
	BufferedImage fractalImage;
	DimensiuniCutie bStats = new DimensiuniCutie(0);
	DimensiuneFractalaComp fDimComputer;

	 
	public class ImagePanel extends JPanel {

		private static final long serialVersionUID = 514583368310394426L;

		public ImagePanel() {
			setLayout(new BorderLayout());
			setBackground(Color.GRAY);
		}

                /**
             *
             * @param g
             */
            @Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			if (fractalImage != null)
			{
				int x = (getWidth() - fractalImage.getWidth()) / 2;
				int y = (getHeight() - fractalImage.getHeight()) / 2;
				g.drawImage(fractalImage, x, y, null);
			}
		}
	}

	 
	public void init() {
		fractalImage = null;
		fDimComputer = null;
		setSize(PREFERRED_IMAGE_WIDTH, PREFERRED_IMAGE_HEIGHT + 50);
		setBackground(Color.LIGHT_GRAY);
		setLayout(new BorderLayout());
		displayPanel = new ImagePanel();
		displayPanel.setSize(this.getWidth(), this.getHeight() - 50);
		getContentPane().add(displayPanel, BorderLayout.CENTER);
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new BorderLayout());
		statusPanel.setBackground(Color.LIGHT_GRAY);
		getContentPane().add(statusPanel, BorderLayout.SOUTH);
		openButton = new JButton("Alege imaginea");
		openButton.addActionListener(this);
		statusPanel.add(openButton, BorderLayout.WEST);
		statusLabel = new JLabel();
		statusPanel.add(statusLabel, BorderLayout.CENTER);
		statusLabel.setText("Dimensiunea fractala =                    ");
		imageWidth = displayPanel.getWidth();
		imageHeight = displayPanel.getHeight();
	}


	 
        @Override
	public void actionPerformed(ActionEvent evt) {
		JFrame parent = new JFrame();
		FileDialog fd = new FileDialog(parent, "Alege o imagine:", FileDialog.LOAD);
		fd.setVisible(true);
		String selectedItem = fd.getFile();
		if(selectedItem != null) {
			statusLabel.setText("Citire fisier . . .");
			statusLabel.paintImmediately(0, 0, statusLabel.getWidth(), statusLabel.getHeight());
			String filename = fd.getFile();
			statusLabel.setText(fd.getDirectory() + File.separator + filename);
			String fCap = filename.toUpperCase();
			if (fCap.endsWith("CSV"))
			{
				try
				{
					BufferedReader reader = new BufferedReader(new FileReader(fd.getDirectory() + File.separator + fd.getFile()));
					int i = 0;
					String text = null;
					pSet = new SetarePunct();
					while (i < SetarePunct.MAX_POINTS + 1 && (text = reader.readLine()) != null) {
						String s[] = text.split("[,\t ]");
						pSet.add(Double.valueOf(s[0]).doubleValue(), Double.valueOf(s[1]).doubleValue());
						i++;
					}
					if (i > SetarePunct.MAX_POINTS) {
						System.err.println("Sorry.  I was unable to read all the points in your image.");
						System.err.println("The number of points exceeded the maximum points in the program.");
						System.exit(-1);
					}
				}
				catch (IOException e)
				{
					statusLabel.setText("Error Reading File");
					System.err.println("There was a problem reading your file.  Sorry.");
					return;
				}
				 
				SetarePunct scaledPSet = pSet.getScaled(imageWidth - 1, imageHeight - 1);
				fractalImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
				int i;
				Punct[] points = scaledPSet.getPoints();
				for (i = 0; i < scaledPSet.getNumPoints(); i++)
					fractalImage.setRGB((int)points[i].x, (int)points[i].y, 0xFFFFFF);
			}
			else  
			{
				ImageIcon imgIcon = new ImageIcon(fd.getDirectory() + File.separator + fd.getFile());
				Image img = imgIcon.getImage();
				double w = img.getWidth(null);
				double h = img.getHeight(null);
				if (w < 0) {  
					statusLabel.setText("Error Reading Image File");
					System.err.println("There was a problem reading your image file.  Sorry.");
					return;
				}
				BufferedImage bImage = ProcesareImagine.imageToBuffered(img);
				ProcesareImagine ip = new ProcesareImagine();
				pSet = ip.extractPoints(bImage);
				 
				double scaleW = (double)imageWidth / w;
				double scaleH = (double)imageHeight / h;
				double scale;
				if (scaleW < scaleH)
					scale = scaleW;
				else
					scale = scaleH;
				int newW = (int)(w * scale);
				int newH = (int)(h * scale);
				img = bImage.getScaledInstance(newW, newH, BufferedImage.SCALE_SMOOTH);
				fractalImage = ProcesareImagine.imageToBuffered(img);
			}
			statusLabel.setText("Calculeaza . . .");
			statusLabel.paintImmediately(0, 0, statusLabel.getWidth(), statusLabel.getHeight());
			fDimComputer = new DimensiuneFractalaComp();
			bStats = fDimComputer.estimateFractalDimension(pSet, fractalImage, displayPanel, statusLabel);
			statusLabel.setText("Dimensiunea fractala = " + bStats.getFracDim());
			repaint();
		}
	}
}
