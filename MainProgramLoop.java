/** File: MainProgramLoop.java
 * 
 *  @author David Snyder
 */

package dgs.StepCounter.v1;

/**
 * MainProgramLoop is the holder class for the program's main().
 * 
 * @author David
 */
public class MainProgramLoop {

	/**
	 * The default duration between pulses in milliseconds.
	 */
	private static int DEFAULT_DURATION = 800;
	
	/**
	 * The program displays a dialog allowing the user to start and pause
	 * a pulsing thread.  User can change duration between pulses.
	 * 
	 * @param args		Command line arguments.
	 */
	public static void main(String[] args) {
		
        System.out.println("Starting Main Thread...");
        
        /* Create new Pulse Controller object, and set the duration between
         * pulses to the default duration.
         */
        PulseControl myPulseController = new PulseControl( MainProgramLoop.DEFAULT_DURATION );
        
        /* Create the main app window.  Pass the controller object so the main
         * app window can control the pulses.
         */
        StepGUI mainWindow = new StepGUI( myPulseController );
        
        // Visible please.
        mainWindow.setVisible(true);
        
        // The main app window is modal. So the main thread here is done.
        System.out.println("End of Main Thread...");
	}

}  //  end class MainProgramLoop
