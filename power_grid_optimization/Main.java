import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Main class
 */
// FREE CODE HERE
public class Main {
    public static void main(String[] args) throws IOException {

        /** MISSION POWER GRID OPTIMIZATION BELOW **/

        System.out.println("##MISSION POWER GRID OPTIMIZATION##");
        // TODO: Your code goes here
        // You are expected to read the file given as the first command-line argument to read 
        // the energy demands arriving per hour. Then, use this data to instantiate a 
        // PowerGridOptimization object. You need to call getOptimalPowerGridSolutionDP() method
        // of your PowerGridOptimization object to get the solution, and finally print it to STDOUT.

        String fileName = args[0];
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        String[] demandsString = lines.get(0).split(" ");
        ArrayList<Integer> demandSchedule = new ArrayList<>();
        for (String demand : demandsString) {
            demandSchedule.add(Integer.parseInt(demand));
        }

        PowerGridOptimization powerGridOptimization = new PowerGridOptimization(demandSchedule);

        OptimalPowerGridSolution optimalSolution = powerGridOptimization.getOptimalPowerGridSolutionDP();

        System.out.println("The total number of demanded gigawatts: " + Arrays.stream(demandsString).mapToInt(Integer::parseInt).sum());
        System.out.println("Maximum number of satisfied gigawatts: " + optimalSolution.getmaxNumberOfSatisfiedDemands());
        ArrayList<Integer> hours = optimalSolution.getHoursToDischargeBatteriesForMaxEfficiency();
        String formattedHours = hours.toString().substring(1, hours.toString().length() - 1);
        System.out.println("Hours at which the battery bank should be discharged: " + formattedHours);
        System.out.println("The number of unsatisfied gigawatts: " + (Arrays.stream(demandsString).mapToInt(Integer::parseInt).sum() - optimalSolution.getmaxNumberOfSatisfiedDemands()));

        System.out.println("##MISSION POWER GRID OPTIMIZATION COMPLETED##");

        /** MISSION ECO-MAINTENANCE BELOW **/

        System.out.println("##MISSION ECO-MAINTENANCE##");
        // TODO: Your code goes here
        // You are expected to read the file given as the second command-line argument to read
        // the number of available ESVs, the capacity of each available ESV, and the energy requirements 
        // of the maintenance tasks. Then, use this data to instantiate an OptimalESVDeploymentGP object.
        // You need to call getMinNumESVsToDeploy(int maxNumberOfAvailableESVs, int maxESVCapacity) method
        // of your OptimalESVDeploymentGP object to get the solution, and finally print it to STDOUT.

        fileName = args[1];
        lines = Files.readAllLines(Paths.get(fileName));
        String[] esvData = lines.get(0).split(" ");
        int maxNumberOfAvailableESVs = Integer.parseInt(esvData[0]);
        int maxESVCapacity = Integer.parseInt(esvData[1]);
        String[] taskDemandsString = lines.get(1).split(" ");
        ArrayList<Integer> taskDemands = new ArrayList<>();
        for (String demand : taskDemandsString) {
            taskDemands.add(Integer.parseInt(demand));
        }

        OptimalESVDeploymentGP esvDeployment = new OptimalESVDeploymentGP(taskDemands);

        int minESVs = esvDeployment.getMinNumESVsToDeploy(maxNumberOfAvailableESVs, maxESVCapacity);

        if (minESVs != -1) {
            System.out.println("The minimum number of ESVs to deploy: " + minESVs);
            ArrayList<ArrayList<Integer>> tasksAssigned = esvDeployment.getMaintenanceTasksAssignedToESVs();
            for (int i = 0; i < tasksAssigned.size(); i++) {
                System.out.println("ESV " + (i + 1) + " tasks: " + tasksAssigned.get(i));
            }
        } else {
            System.out.println("Warning: Mission Eco-Maintenance Failed.");
        }
        System.out.println("##MISSION ECO-MAINTENANCE COMPLETED##");
    }
}
