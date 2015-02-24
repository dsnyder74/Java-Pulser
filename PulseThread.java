/**
 * File: PulseThread
 * 
 * @author David
 *
 */

package dgs.StepCounter.v1;


/**
 * PulseThread contains the code that actually runs in the slave thread.
 * 
 * @author David
 */
public class PulseThread implements Runnable {

	// 
	/**
	 * Custom dialog control to update at each pulse.  Has set.
	 */
	private StepIndicator myIndicator = null;
	
	/**
	 * Sets the pulsing thread to talk to a new indicator.
	 * 
	 * @param i		The new indicator.
	 */
	public void setIndicator( StepIndicator i ) {
		myIndicator = i;
	}
	
	/**
	 * Duration between pulser.  Has get/set.
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
	 * Sets a new duration in milliseconds between pulses.
	 * 
	 * @param millsec	The new duration in milliseconds between pulses.
	 */
	public void setDuration( int millsec ) {
		iDuration = millsec;
		System.out.println("Duration set to "+iDuration);
	}
	
	/**
	 * An internal pulse counter.  Has get.
	 */
	private int iCounter=0;
	/**
	 * Returns the current pulse count.
	 * 
	 * @return
	 */
	public int getCounter() {
		return iCounter;
	}
	
	// Is pulse thread running.  Has get.
	private Boolean bRunning = new Boolean(false);
	public Boolean isRunning() {
		return bRunning;
	}
	
	// Is pulse thread waiting.  Has get.
	private Boolean bWaiting = new Boolean(false);
	public Boolean isWaiting() {
		return bWaiting;
	}
	
	private Boolean bFirstRun = new Boolean(true);
	
	// Start / resume the pulse thread.
	public void Start() {
		bRunning = true;
		System.out.println("PulseThread.Start() called.");
// IMPLEMENT
	}
	
	// Pause the pulse thread.
	public void Pause() {
		bRunning = false;
		System.out.println("PulseThread.Pause() called.");
//IMPLEMENT
	}

	// Stop the pulser.  When starting, start at beginning.
	private Boolean bStopNow = new Boolean(false);
	public void Stop() {
		bRunning = false;
		bStopNow = true;
		System.out.println("PulseThread.Stop() called.");
// IMPLEMENT
	}
	
	

	// Constructors
	public PulseThread() {
		System.out.println("Created PulseThread");
	}
	public PulseThread(int duration) {
		setDuration(duration);
		System.out.println("Created PulseThread, duration is "+iDuration);
	} 
	public PulseThread(StepIndicator i, int duration) {
		setIndicator(i);
		setDuration(duration);
		System.out.println("Created PulseThread, received indicator, duration is "+iDuration);
	}
	
	@Override
	public void run() {
		
		System.out.println("New thread run.");
		
		// Validate Indicator before going.
		if (myIndicator==null) {
			System.out.println("Indicator not initialized.");
			return;
		}

		while (bStopNow == false) {
			System.out.println("Starting new PulseThread loop.");
			
			// If first run, skip this wait and just start
			if (bFirstRun==false) {
	
				// Wait for start/pause/stop
				System.out.println("Waiting for start/pause/stop.");
				synchronized(this) {
					try {
						bWaiting=true;
						wait();
						bWaiting=false;
					} catch (InterruptedException e) {
						System.out.println("Exception: "+e.getMessage());
					} // try
				} // synchronized(this)
				
				System.out.println("Done waiting.");
				
				if (bStopNow == true) {
					bRunning = false;
					System.out.println("Terminate pulse thread message received.");
				}
			} else {
				bFirstRun=false;
				bRunning=true;
			}
			
			// Do while running...
			while (bRunning == true) {
				
				// Tell Indicator to do it's thing.
				myIndicator.NextStep();
				
				iCounter++;  // Increment internal counter
				System.out.println("Pulser Thread Counter: "+iCounter);
				
				// Wait for next pulse or interruption
				synchronized(this) {
					try {
						bWaiting=true;
						System.out.println("Waiting for next pulse or interruption.");
						wait(iDuration);
						bWaiting=false;
						System.out.println("Done waiting.");
					} catch (InterruptedException e) {
						System.out.println("Exception: "+e.getMessage());
					} //try
				} // synchronized(this)

			} // while (bRunning ...	
			
		} // while (bStopNow ...
	
		System.out.println("PulseThread terminating");
	} // run() end

} // Class PulseThread end
