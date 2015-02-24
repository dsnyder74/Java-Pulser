package dgs.StepCounter.v1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
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

	private JPanel contentPane;

	// The indicator control.
	private StepIndicator myIndicator;

	// The PulseControl object
	private PulseControl myPulseController = null;
	
	/**
	 * Create the frame.
	 */
	public StepGUI( PulseControl pc ) {
		
		System.out.println("StepGUI constructor called.");
		if (pc == null) {
			System.out.println("PulseControl is null.");
			return;
		}

		myPulseController = pc;
		
		// Main frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel indicatorPanel = new JPanel();
		contentPane.add(indicatorPanel, BorderLayout.CENTER);
		
		JPanel controlPanel = new JPanel();
		contentPane.add(controlPanel, BorderLayout.SOUTH);
		
		
		// Indicator panel
		StepIndicator indicator = new StepIndicator();
		myPulseController.setIndicator(indicator);
		indicatorPanel.add(indicator);
		
		JLabel lblStepMessage = new JLabel("Step Message");
		int retainHeight = lblStepMessage.getPreferredSize().height;
		lblStepMessage.setPreferredSize(new Dimension(250, retainHeight));
		indicatorPanel.add(lblStepMessage);
		
		// Control panel
		JLabel lblDuration = new JLabel("Duration:");
		controlPanel.add(lblDuration);
		
		int iDur = myPulseController.getDuration();
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(iDur,0,10000,100));
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				System.out.println("Duration change event.");
				String s = spinner.getValue().toString();
				int i = Integer.parseInt(s);
				System.out.println("Telling Pulse Controller of new duration.");
				myPulseController.setDuration(i);
			}
		});
		
		controlPanel.add(spinner);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if ((btnStart.getText() == "Start") || (btnStart.getText() == "Resume")) {
					pc.Start();
					btnStart.setText("Pause");
				} else {
					pc.Pause();
					btnStart.setText("Resume");
				}
			}
		} );
		
		controlPanel.add(btnStart);
		
	}

}
