package flappyDot;

import java.awt.Graphics;

import javax.swing.JPanel;

public class Renderer extends JPanel
{
	private static final long serialVersionUID = 8095379131479840912L;
	
	 protected void paintComponent(Graphics g) 
	 {		
		super.paintComponent(g);
		
		FlappyDot.flappyDot.repaint(g);
	 }
	 
}

	