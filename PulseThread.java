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
	 * 
	 * @return	The current duration between pulses in milliseconds.
	 */
	public int getDuration() {
		return iDuration;
	}
	/**
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
	 * 
	 * @return	The number of times the pulser thread has pulsed.
	 */
	public int getCounter() {
		return iCounter;
	}
	
	/**
	 * Flag to indicate when the pulser thread is running.
	 */
	private Boolean bRunning = new Boolean(false);
	/**
	 * 
	 * @return	True if pulser is running, false if not.
	 */
	public Boolean isRunning() {
		return bRunning;
	}
	
	/**
	 * Flag to indicate if the pulser thread is waiting for a notification.
	 */
	private Boolean bWaiting = new Boolean(false);
	/**
	 * 
	 * @return	True if the pulser thread is waiting for a notify()
	 */
	public Boolean isWaiting() {
		return bWaiting;
	}
	
	/**
	 * Internal flag to indicate that the pulser thread needs to terminate.
	 */
	private Boolean bStopNow = new Boolean(false);
	
	/**
	 * Start / resume the pulse thread. Does not wake up the sleeping thread.
	 */
	public void Start() {
		bRunning = true;
		System.out.println("PulseThread.Start() called.");
	}
	
	/**
	 * Pause the pulse thread. Does not wake up the sleeping thread.
	 */
	public void Pause() {
		bRunning = false;
		System.out.println("PulseThread.Pause() called.");
	}

	// 
	/**
	 * Stop the pulser.  When starting, start at beginning. Does not wake up 
	 * the sleeping thread.
	 */
	public void Stop() {
		bRunning = false;
		bStopNow = true;
		System.out.println("PulseThread.Stop() called.");
	}
	
	/**
	 * Creates an empty PulseThread object.  This does not create an actual
	 * thread.
	 */
	public PulseThread() {
		System.out.println("Created PulseThread");
	}
	
	/**
	 * Creates a PulseThread object with a default duration.  This does not 
	 * create an actual thread.
	 * 
	 * @param duration	The default duration for the new PulseThread object.
	 */
	public PulseThread(int duration) {
		setDuration(duration);
		System.out.println("Created PulseThread, duration is "+iDuration);
	}
	
	/**
	 * Creates a PulseThread object with a default duration and the specified
	 * dialog control to be used as an indicator.  This does not create an 
	 * actual thread.
	 * 
	 * @param i			The dialog control to be used as an indicator.
	 * @param duration	The default duration for the new PulseThread object.
	 */
	public PulseThread(StepIndicator i, int duration) {
		setIndicator(i);
		setDuration(duration);
		System.out.println("Created PulseThread, received indicator, duration is "+iDuration);
	}
	
	/**
	 * This is what is executed when the new pulse thread is actually created.
	 */
	@Override
	public void run() {
		
		System.out.println("New thread run.");
		
		// Validate Indicator before going.
		if (myIndicator==null) {
			System.out.println("Indicator not initialized.");
			return;
		}

		/* Make sure internal flags are initialized, for running the pulse
		 * thread, and making sure StopNow is off, 
		 */
		bRunning = true;
		bStopNow = false;
		
		/* The pulse thread's main loop.  Keep running until told to
		 * terminate using Stop().
		 */
		while (bStopNow == false) {
			System.out.println("Starting new PulseThread loop.");

			/* If the thread execution is here, the pulser is running.  It 
			 * needs to fire off a notice to it's UI indicator, and then
			 * wait for the specified duration.  Once it wakes up, see if
			 * it was interrupted due to a state change (pause or stop) by
			 * checking the running flag. Loop if it needs to conitnue
			 * pulsing.
			 */
			while (bRunning == true) {
				
				// Let the indicator know that a pulse is occuring.
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

			/* If the pulser was not interrupted and woke up because the
			 * duration expired, then the running flag will still set, so 
			 * keep looping.
			 */
			} // while (bRunning ...	
			
			/* Normally, if the thread execution is here, it is because
			 * the thread state changed (running state to paused state,
			 * or any state to stop state). If the state change is to
			 * paused, and not stop, wait here until unpaused or told
			 * to terminate.
			 */
			if (bStopNow == false) {
				System.out.println("Waiting for pause/stop.");
				synchronized(this) {
					try {
						bWaiting=true;
						wait();
						bWaiting=false;
					} catch (InterruptedException e) {
						System.out.println("Exception: "+e.getMessage());
					}
				}
			}
			System.out.println("Done waiting.");
			
			/* The thread has woken up due to a state change.  If the
			 * requested change is to terminate the thread, turn off
			 * the running flag so the thread can terminate. 
			 */
			if (bStopNow == true) {
				bRunning = false;
				System.out.println("Terminate pulse thread message received.");
			}
			
		/* If the pulser was not told to stop, loop back up to the beginning and
		 * start running the pulser.
		 * If the pulser was told to terminate, break out of the entire loop.
		 */
		} // while (bStopNow ...
	
		System.out.println("PulseThread terminating");
	} // run() end

} // Class PulseThread end
