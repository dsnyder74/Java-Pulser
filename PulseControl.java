/** File: MainProgramLoop.java
 * 
 *  @author David
 */

package dgs.StepCounter.v1;

/**
 * This class controls the pulser thread object, which will run in a separate
 * thread.  It is not the actual object running in the separate thread.
 * 
 * @author David
 */
public class PulseControl {

	// myIndicator is the custom dialog control which will receive a message
	// each time there is a pulse.  Has set.
	/**
	 * StepIndicator is the custom dialog control which will receive a message 
	 * each time there is a pulse.  Has set.
	 */
	private StepIndicator myIndicator = null;
	
	/**
	 * Sets the custom dialog control for this object.
	 * 
	 * @param si	The custom dialog control.
	 */
	public void setIndicator( StepIndicator si ) {
		myIndicator = si;
	}
	
	/**
	 * pulseThreadObject is the object that contains the pulser code that 
	 * will run in a separate thread.
	 */
	// 
	private PulseThread pulseThreadObject = null;
	
	/**
	 * pulseThread is the actual slave thread.
	 */
	private Thread pulseThread = null;

	/**
	 * Duration between pulses.  Has get/set.
	 */
	private int iDuration = 0;
	
	/**
	 * Returns the current duration between pulses in milliseconds.
	 * 
	 * @return	The current duration between pulses in milliseconds.
	 */
	public int getDuration() {
		return iDuration;
	}
	
	/**
	 * Sets the duration between pulses in milliseconds.
	 * 
	 * @param millsec	The new duration between pulses in milliseconds.
	 */
	public void setDuration( int millsec ) {
		iDuration = millsec;
		System.out.println("Duration in PulseControl set to "+iDuration);
		
		// Notify pulseThreadObject,if it exists, that the pulse duration
		// changed.
		if (pulseThreadObject != null) {
			synchronized (pulseThreadObject) {
				pulseThreadObject.setDuration(millsec);
				pulseThreadObject.notify();
			}
		}
		
	}
	
	/**
	 * Generic constructor.
	 */
	public PulseControl() {
		System.out.println("Created Pulse Controller");
	}
	
	/**
	 * Constructor, with a default duration between pulses.
	 * 
	 * @param duration	Duration between pulses.
	 */
	public PulseControl( int duration ) {
		setDuration( duration );
		System.out.println("Created Pulse Controller, duration is "+ iDuration);
	}
	
	/**
	 * Constructor, with a default duration between pulses and the custom 
	 * indicator object which will be pinged with each pulse.
	 * 
	 * @param duration	Duration between pulses.
	 * @param indy		Custom indicator
	 */
	public PulseControl( int duration, StepIndicator indy ) {
		setDuration( duration );
		setIndicator( indy );
		System.out.println("Created Pulse Controller, received Indicator, duration is "+ iDuration);
	}

	/**
	 * Start pulsing.  If there is no slave thread for the pulser, create a 
	 * new one.
	 */
	public void Start() {
		
		System.out.println("PulseControl.Start() called.");
		
		// if no pulse thread object exists...
		if (pulseThreadObject == null) {
			
			// Create a new pulse thread object.
			System.out.println("Creating new PulseThread thread.");
			pulseThreadObject = new PulseThread( myIndicator, iDuration );
			
			// Create a new thread for the object.
			System.out.println("Creating new thread.");
			pulseThread = new Thread( pulseThreadObject );
			
			// Start the new thread.
			System.out.println("Starting thread.");
			pulseThread.start();
			System.out.println("PulseThread thread created.");

		// if pulse thread object does exist
		} else {
			
			System.out.println("PulseThread thread exists.");
			
			synchronized (pulseThreadObject) {
				
				// Tell the pulse thread object to get ready to start
				pulseThreadObject.Start();
				
				// Use notify to wake the thread up.
				pulseThreadObject.notify();
				
			}
			
			System.out.println("PulseThread thread notified.");
			
		}  //  end if (pulseThreadObject == null)...
		
	}  //  end method Start
	
	/**
	 * Pause the pulsing thread and object.
	 */
	public void Pause() {

		System.out.println("PulseControl.Pause() called.");
		
		synchronized (pulseThreadObject) {
			
			// Tell the pulse thread object to get ready to pause
			pulseThreadObject.Pause();
			
			// Wake the thread up if it is between pulses.
			pulseThreadObject.notify();
		}
		
	}  //  end method Stop

}
