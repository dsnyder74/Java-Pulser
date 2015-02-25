/**
 * File: StepGUI
 * 
 * @author David
 *
 */

package dgs.StepCounter.v1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JButton;

public class StepGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1603861279045041294L;

	/**
	 * The main content JPanel for the dialog app.
	 */
	private JPanel contentPane;

	/**
	 * The pulse control to notify (flash).
	 */
	private PulseControl myPulseController = null;
	
	/**
	 * Create the main app frame.  The PulseControl object is required so that
	 * the application can notify the Pulse Controller when to Start or Pause.
	 * 
	 * @param pc	the PulseControl object that controls the pulser.
	 */
	public StepGUI( PulseControl pc ) {
		
		System.out.println("StepGUI constructor called.");
		
		/* Validate the Pulse Controller.
		 * TODO: Throw an exception if the PulseControl is not valid.
		 */
		if (pc == null) {
			System.out.println("PulseControl is null.");
			return;
		}

		// Store the pulse controller object
		myPulseController = pc;
		
		// Main frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// Create the indicator panel
		JPanel indicatorPanel = new JPanel();
		contentPane.add(indicatorPanel, BorderLayout.CENTER);
		
		// Create the control panel
		JPanel controlPanel = new JPanel();
		contentPane.add(controlPanel, BorderLayout.SOUTH);
		
		// Set up the Indicator panel
		StepIndicator indicator = new StepIndicator();
		myPulseController.setIndicator(indicator);
		indicatorPanel.add(indicator);
		
		JLabel lblStepMessage = new JLabel("Step Message");
		int retainHeight = lblStepMessage.getPreferredSize().height;
		lblStepMessage.setPreferredSize(new Dimension(250, retainHeight));
		indicatorPanel.add(lblStepMessage);
		
		// Set up the Control panel
		JLabel lblDuration = new JLabel("Duration:");
		controlPanel.add(lblDuration);
		
		/* Set up the duration input as a JSpinner
		 * The JSpinner has a min of 0, a max of 10000 (10 sec), and a step 
		 * of 100 ms.
		 */
		int iDur = myPulseController.getDuration();
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(iDur,0,10000,100));
		// Listen for the duration input to change.
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				System.out.println("Duration change event.");
				// Get the new duration value, and tell the pulse controller.
				String s = spinner.getValue().toString();
				int i = Integer.parseInt(s);
				System.out.println("Telling Pulse Controller of new duration.");
				myPulseController.setDuration(i);
			}
		});
		controlPanel.add(spinner);
		
		JButton btnStart = new JButton("Start");
		// Listen for this button to be clicked.
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// If the button says start, tell the pulse controller to 
				// start and make the button a pause button.
				if ((btnStart.getText() == "Start") || (btnStart.getText() == "Resume")) {
					myPulseController.Start();
					btnStart.setText("Pause");
				} else {
					myPulseController.Pause();
					btnStart.setText("Resume");
				}
			}
		} );	
		controlPanel.add(btnStart);
		
	}

}
