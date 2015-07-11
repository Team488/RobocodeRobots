package tournament;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import robocode.AdvancedRobot;
import robocode.control.*;
import robocode.control.events.*;

//
// Application that demonstrates how to run two sample robots in Robocode using the
// RobocodeEngine from the robocode.control package.
//
// @author Flemming N. Larsen
//
public class RoundRobin {

    public static void main(String[] args) throws IOException {

        // Disable log messages from Robocode
        RobocodeEngine.setLogMessagesEnabled(false);

        // Create the RobocodeEngine
        //   RobocodeEngine engine = new RobocodeEngine(); // Run from current working directory
        RobocodeEngine engine = new RobocodeEngine(new java.io.File("../../RobocodeRobots/Robocode")); // Run from C:/Robocode
        
        // Add our own battle listener to the RobocodeEngine 
        engine.addBattleListener(new BattleObserver());

        // Show the Robocode battle view
        engine.setVisible(true);

        // Setup the battle specification

        int numberOfRounds = 5;
        BattlefieldSpecification battlefield = new BattlefieldSpecification(800, 600); // 800x600
        
        // So, creating the list of selected robots is a little complicated. I think it needs to look something like this:
        // 1) Crawl through the RobocodeRobots\Robots\src to find all classes that extend robot, and find
        //    their namespace & name (like "jrg.JohnBot")
        // 2) Compile the robots (I'm gonna skip this for now - for now I'll just use eclipse on my dev laptop)
        // 3) Copy the *.class files to
        
        List<Path> listOfFiles = new ArrayList<>();
                
        listOfFiles = Files.walk(Paths.get("."))
        .filter(Files::isRegularFile)
        .filter(u -> u.getFileName().toString().contains(".java"))
        .filter(u -> isPathARobot(u))
        .collect(Collectors.toList());
        // Find all robots
        //listOfFiles.get(0).
        
        // Note - I have learned that you need them to be in folders that have their correct package name.
        RobotSpecification[] selectedRobots = engine.getLocalRepository("sample.RamFire,sample.Corners");

        BattleSpecification battleSpec = new BattleSpecification(numberOfRounds, battlefield, selectedRobots);
        // Run our specified battle and let it run till it is over
        engine.runBattle(battleSpec, true); // waits till the battle finishes

        // Cleanup our RobocodeEngine
        engine.close();

        // Make sure that the Java VM is shut down properly
        System.exit(0);
    }
    
    private static boolean isPathARobot(Path robotPath)
    {
    	String code = "";
		try {
			code = new String(Files.readAllBytes(robotPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return (code.contains("extends AdvancedRobot") || code.contains("extends Robot"));
    }
}

//
// Our private battle listener for handling the battle event we are interested in.
//
class BattleObserver extends BattleAdaptor {

    // Called when the battle is completed successfully with battle results
    public void onBattleCompleted(BattleCompletedEvent e) {
        System.out.println("-- Battle has completed --");
        
        // Print out the sorted results with the robot names
        System.out.println("Battle results:");
        for (robocode.BattleResults result : e.getSortedResults()) {
            System.out.println("  " + result.getTeamLeaderName() + ": " + result.getFirsts());
        }
    }

    // Called when the game sends out an information message during the battle
    public void onBattleMessage(BattleMessageEvent e) {
        System.out.println("Msg> " + e.getMessage());
    }

    // Called when the game sends out an error message during the battle
    public void onBattleError(BattleErrorEvent e) {
        System.out.println("Err> " + e.getError());
    }
}