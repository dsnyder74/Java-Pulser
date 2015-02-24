/** File: MainProgramLoop.java
 *  @author David Snyder
 */

package dgs.StepCounter.v1;


public class MainProgramLoop {


	/* The program displays a dialog allowing the user to start and pause
	 * a pulsing thread.  User can change duration between pulses.
	 * 
	 *  Messages are sent to stdout documenting what the program is doing.
	 */
	

	public static void main(String[] args) {
		
        System.out.println("Starting Main Thread...");
        
        // Create new Pulse Controller object, and set the duration between
        // pulses to 800 ms.
        PulseControl myPulseController = new PulseControl(800);
        
        // Create the main app window.  Pass the controller object so the main
        // app window can controll the pulses.
        StepGUI mainWindow = new StepGUI( myPulseController );
        
        // Visible please.
        mainWindow.setVisible(true);
        
        System.out.println("End of Main Thread...");
	}

}
