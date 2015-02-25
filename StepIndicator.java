/**
 * File: StepIndicator
 * 
 * @author David
 *
 */

package dgs.StepCounter.v1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * StepIndicator is a customized spring control, extended off of a JPanel.
 * It's specifications are:
 *    - Draw as a small rectangle
 *    - Fill the rectangle with the off color
 *    - When told so by a public method, fill the rectangle with the 'on' 
 *      color, wait for a small period of time, then fill the rectangle 
 *      with the 'off' color.  This results in the rectangle appearing to 
 *      'flash' or 'pulse'.
 *      
 * @author David
 *
 */
public class StepIndicator extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1132505568395138440L;

	/**
	 * The period of time in milliseconds that the control should be filled
	 * with the 'on' color.
	 */
	private static final int BLINK_DURATION = 300;
	
	/**
	 * The indicator's 'on' color.
	 */
	private static final Color BLINKER_ON_COLOR = Color.GREEN;
	
	/**
	 * The indicator's 'off' color
	 */
	private static final Color BLINKER_OFF_COLOR = Color.DARK_GRAY;
	
	/**
	 * The indicator's default width.
	 */
	private static final int DEFAULT_BLINKER_WIDTH = 250;
	
	/**
	 * The indicator's default height.
	 */
	private static final int DEFAULT_BLINKER_HEIGHT = 20;
	
	/**
	 * The indicator's current color
	 */
	private Color currentColor = BLINKER_OFF_COLOR;
	/**
	 * 
	 * @param c	Set the current color, and repaint so it actually is the new
	 * color
	 */
	public void setIndicatorColor(Color c) {
		currentColor = c;
		repaint();
	}
	
	/**
	 * The indicator's current dimensions.
	 */
	private Dimension indicatorSize = new Dimension( DEFAULT_BLINKER_WIDTH, DEFAULT_BLINKER_HEIGHT);
	/**
	 * 
	 * @param d The new size of the indicator passed as a Dimension.
	 */
	public void setIndicatorDimension (Dimension d) {
		indicatorSize = d;
	}
	/**
	 * 
	 * @param h The new height of the indicator, without changing the width.
	 */
	public void setIndicatorHeight (int h) {
		indicatorSize.height = h;
	}
	/**
	 * 
	 * @param w The new width of the indicatorm without changing the height.
	 */
	public void setIndicatorWidth (int w) {
		indicatorSize.width = w;
	}

	/**
	 * Generic empty constructor
	 */
	public StepIndicator() {
	}
	
	/**
	 * paintComponent override.  Used to draw the indicator as a rectangle 
	 * with the specified current color.
	 */
	@Override protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Rectangle r = this.getBounds();
		g.setColor(currentColor);
		g.fillRect(0, 0, r.width, r.height);
	}
	
	/**
	 * getPreferredSize override. Not the best way to get the indicator
	 * to the correct size. But if the Panel in which the indicator is
	 * embbeded is using a FlowLayout, this works ok.
	 * A better solution may be to switch the parent Panel's layout.
	 */
	@Override public Dimension getPreferredSize() {
	    return new Dimension(250, 20); // appropriate size
	}
	

	/**
	 * This function causes the indicator to blink.
	 */
	public void NextStep() {
		
		// Color blinker on
		StepIndicator si = this;
		this.setIndicatorColor(BLINKER_ON_COLOR);
		
		// Wait blinker duration, then color it back to off.
		Timer timer = new Timer(BLINK_DURATION, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((Timer) e.getSource()).stop();
				si.setIndicatorColor(BLINKER_OFF_COLOR);
			}
		} );
		timer.start();
		
	} // NextStep() end 
	
} // Class StepIndicator end
