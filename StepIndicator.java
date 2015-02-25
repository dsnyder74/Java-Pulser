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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

/**
 * StepIndicator is a customized spring control, extended off of a JPanel.
 * It's specifications are:
 *    - Load a queue of text messages
 *    - Draw as a small rectangle in the upper half on the JPanel
 *    - Fill that rectangle with the off color
 *    - When told so by a public method, fill the top rectangle with the 'on' 
 *      color, set the bottom label to the next text message in the queue,
 *      wait for a small period of time, then fill the top rectangle 
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
	private static final int DEFAULT_BLINKER_HEIGHT = 60;
	
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
	 * @param w The new width of the indicator without changing the height.
	 */
	public void setIndicatorWidth (int w) {
		indicatorSize.width = w;
	}
	
	/**
	 * The queue of text messages to display under the blinker
	 */
	private Queue<String> Steps = new LinkedList<String>();
	
	/**
	 * Reads a text file, and queues each line into Steps
	 */
	public void resetSteps() {
		try {
			Steps.clear();
			for (String line : Files.readAllLines(Paths.get("Steps.txt"))) {
			    Steps.add(line);
			}
		} catch (IOException e) {
			System.out.println("Exception: "+e.getMessage());
		}
	}
	
	/**
	 * JLabel to display current text message at front of Steps
	 */
	private JLabel current_step;
	
	/**
	 * Generic empty constructor
	 */
	public StepIndicator() {
		setLayout(null);
		
		current_step = new JLabel("");
		current_step.setFont(new Font("Arial", Font.BOLD, 15));
		current_step.setHorizontalAlignment(SwingConstants.CENTER);
		current_step.setBounds(10, 37, 220, 23);
		add(current_step);
		
		resetSteps();
	}
	
	/**
	 * paintComponent override.  Used to draw the indicator as a rectangle 
	 * with the specified current color.
	 */
	@Override protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Rectangle r = this.getBounds();
		g.setColor(currentColor);
		g.fillRect(0, 0, r.width, r.height/2);
	}
	
	/**
	 * getPreferredSize override. Not the best way to get the indicator
	 * to the correct size. But if the Panel in which the indicator is
	 * embbeded is using a FlowLayout, this works ok.
	 * A better solution may be to switch the parent Panel's layout.
	 */
	@Override public Dimension getPreferredSize() {
	    return indicatorSize; // appropriate size
	}
	

	/**
	 * This function causes the indicator to blink.
	 */
	public void NextStep() {
		
		// Update Step text
		String step = Steps.remove();
		current_step.setText(step);
		Steps.add(step);
		
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
