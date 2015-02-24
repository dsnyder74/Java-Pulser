package dgs.StepCounter.v1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class StepIndicator extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1132505568395138440L;

	private static final int BLINK_DURATION = 300;
	private static final Color BLINKER_ON_COLOR = Color.GREEN;
	private static final Color BLINKER_OFF_COLOR = Color.DARK_GRAY;
	
	// Indicator's blinker color
	private Color currentColor = BLINKER_OFF_COLOR;
	public void setColor(Color c) {
		currentColor = c;
		repaint();
	}


	// Constructor
	public StepIndicator() {

	}

	
	// Overrides
	@Override protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Rectangle r = this.getBounds();
		//System.out.println("r is "+r.x+" "+r.y+" "+r.width+" "+r.height);
		g.setColor(currentColor);
		g.fillRect(0, 0, r.width, r.height);
	}
	
	@Override public Dimension getPreferredSize() {
	    return new Dimension(250, 20); // appropriate size
	}
	
	
	// Other methods
	// NextStep called once per pulse
	public void NextStep() {
		
		// Color blinker on
		StepIndicator si = this;
		this.setColor(Color.GREEN);
		
		// Wait blinker duration, then color it back to off.
		Timer timer = new Timer(BLINK_DURATION, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((Timer) e.getSource()).stop();
				si.setColor(BLINKER_OFF_COLOR);
			}
		} );
		timer.start();
		
	} // NextStep() end 
	
} // Class StepIndicator end
