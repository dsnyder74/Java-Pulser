/**
 * 
 */
package dgs.StepCounter.v1;


/**
 * @author David
 *
 */
public class PulseControl {

	private StepIndicator myIndicator = null;
	public void setIndicator( StepIndicator si ) {
		myIndicator = si;
	}
	
	private Thread pulseThread = null;
	private PulseThread pulseThreadObject = null;
	
	// Duration between pulser.  Has get/set.
	private int iDuration = 0;
	public int getDuration() {
		return iDuration;
	}
	public void setDuration( int millsec ) {
		iDuration = millsec;
		System.out.println("Duration in PulseControl set to "+iDuration);
		// Notify pulseThreadObject (if it exists)
		if (pulseThreadObject != null) {
			synchronized (pulseThreadObject) {
				pulseThreadObject.setDuration(millsec);
				pulseThreadObject.notify();
			}
		}
		
	}
	
	// Constructor
	public PulseControl() {
		System.out.println("Created Pulse Controller");
	}
	public PulseControl( int duration ) {
		setDuration( duration );
		System.out.println("Created Pulse Controller, duration is "+ iDuration);
	}
	public PulseControl( int duration, StepIndicator indy ) {
		setDuration( duration );
		setIndicator( indy );
		System.out.println("Created Pulse Controller, received Indicator, duration is "+ iDuration);
	}

	public void Start() {
		
		if (pulseThreadObject == null) {
			System.out.println("Creating new PulseThread thread.");
			pulseThreadObject = new PulseThread( myIndicator, iDuration );
			System.out.println("Creating new thread.");
			pulseThread = new Thread( pulseThreadObject );
			System.out.println("Calling thread.run.");
			pulseThread.start();
			System.out.println("PulseThread thread created.");
			
			// Wait for thread to tell us it's ready.
			/*synchronized (pulseThreadObject) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			
			System.out.println("PulseThread thread reported ready.");
		} else {
			System.out.println("PulseThread thread exists.");
			synchronized (pulseThreadObject) {
				pulseThreadObject.Start();
				pulseThreadObject.notify();
			}
			System.out.println("PulseThread thread notified.");
		}
		
	}
	
	public void Pause() {

		synchronized (pulseThreadObject) {
			pulseThreadObject.Pause();
			pulseThreadObject.notify();
		}
		
	}

}
