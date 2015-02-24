/**
 * 
 */
package dgs.StepCounter.v1;

/**
 * @author David
 *
 */
public class MainProgramLoop {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
        System.out.println("Starting Main Thread...");
        PulseControl myPulseController = new PulseControl(800);
        StepGUI mainWindow = new StepGUI( myPulseController );
        mainWindow.setVisible(true);
        System.out.println("End of Main Thread...");
	}

}
