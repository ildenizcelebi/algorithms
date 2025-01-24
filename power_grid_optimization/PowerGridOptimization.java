import java.util.ArrayList;

/**
 * This class accomplishes Mission POWER GRID OPTIMIZATION
 */
public class PowerGridOptimization {
    private ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour;

    public PowerGridOptimization(ArrayList<Integer> amountOfEnergyDemandsArrivingPerHour){
        this.amountOfEnergyDemandsArrivingPerHour = amountOfEnergyDemandsArrivingPerHour;
    }

    public ArrayList<Integer> getAmountOfEnergyDemandsArrivingPerHour() {
        return amountOfEnergyDemandsArrivingPerHour;
    }
    /**
     *     Function to implement the given dynamic programming algorithm
     *     SOL(0) <- 0
     *     HOURS(0) <- [ ]
     *     For{j <- 1...N}
     *         SOL(j) <- max_{0<=i<j} [ (SOL(i) + min[ E(j), P(j − i) ] ]
     *         HOURS(j) <- [HOURS(i), j]
     *     EndFor
     *
     * @return OptimalPowerGridSolution
     */
    public OptimalPowerGridSolution getOptimalPowerGridSolutionDP(){
        int N = amountOfEnergyDemandsArrivingPerHour.size();
        int[] SOL = new int[N + 1];
        ArrayList<ArrayList<Integer>> HOURS = new ArrayList<>();

        SOL[0] = 0;
        HOURS.add(new ArrayList<>());

        calculateOptimalSolution(N, SOL, HOURS);

        return findOptimalSolution(N, SOL, HOURS);
    }

    private void calculateOptimalSolution(int N, int[] SOL, ArrayList<ArrayList<Integer>> HOURS) {
        for (int j = 1; j <= N; j++) {
            int maxSatisfied = 0;
            int bestI = 0;
            for (int i = 0; i < j; i++) {
                int satisfied = SOL[i] + Math.min(amountOfEnergyDemandsArrivingPerHour.get(j - 1), getBatteryEfficiency(j - i));
                if (satisfied > maxSatisfied) {
                    maxSatisfied = satisfied;
                    bestI = i;
                }
            }
            SOL[j] = maxSatisfied;
            ArrayList<Integer> hours = new ArrayList<>(HOURS.get(bestI));
            hours.add(j);
            HOURS.add(hours);
        }
    }

    private OptimalPowerGridSolution findOptimalSolution(int N, int[] SOL, ArrayList<ArrayList<Integer>> HOURS) {
        int maxSatisfiedDemands = 0;
        ArrayList<Integer> optimalHours = new ArrayList<>();
        for (int j = 1; j <= N; j++) {
            if (SOL[j] > maxSatisfiedDemands) {
                maxSatisfiedDemands = SOL[j];
                optimalHours = new ArrayList<>(HOURS.get(j));
            }
        }
        return new OptimalPowerGridSolution(maxSatisfiedDemands, optimalHours);
    }

    private int getBatteryEfficiency(int hours) {
        return hours * hours;
    }
}