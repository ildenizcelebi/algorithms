import java.io.Serializable;
import java.util.*;

class UrbanTransportationApp implements Serializable {
    static final long serialVersionUID = 99L;
    
    public HyperloopTrainNetwork readHyperloopTrainNetwork(String filename) {
        HyperloopTrainNetwork hyperloopTrainNetwork = new HyperloopTrainNetwork();
        hyperloopTrainNetwork.readInput(filename);
        return hyperloopTrainNetwork;
    }

    /**
     * Function calculate the fastest route from the user's desired starting point to 
     * the desired destination point, taking into consideration the hyperloop train
     * network. 
     * @return List of RouteDirection instances
     */

    public List<RouteDirection> getFastestRouteDirections(HyperloopTrainNetwork network) {
        Map<String, StationNode> stationGraph = buildStationGraph(network);
        return findShortestPath(network, stationGraph);
    }

    private Map<String, StationNode> buildStationGraph(HyperloopTrainNetwork network) {
        Map<String, StationNode> stationGraph = new HashMap<>();

        for (TrainLine line : network.lines) {
            for (Station station : line.trainLineStations) {
                stationGraph.putIfAbsent(station.description, new StationNode(station));
            }
        }
        stationGraph.putIfAbsent(network.startPoint.description, new StationNode(network.startPoint));
        stationGraph.putIfAbsent(network.destinationPoint.description, new StationNode(network.destinationPoint));

        for (TrainLine line : network.lines) {
            for (int i = 0; i < line.trainLineStations.size() - 1; i++) {
                Station fromStation = line.trainLineStations.get(i);
                Station toStation = line.trainLineStations.get(i + 1);
                double travelTime = calculateDistance(fromStation, toStation) / network.averageTrainSpeed;

                stationGraph.get(fromStation.description).addNeighbor(stationGraph.get(toStation.description), travelTime, true);
                stationGraph.get(toStation.description).addNeighbor(stationGraph.get(fromStation.description), travelTime, true);
            }
        }

        for (StationNode nodeA : stationGraph.values()) {
            for (StationNode nodeB : stationGraph.values()) {
                if (!nodeA.equals(nodeB)) {
                    double walkTime = calculateDistance(nodeA.station, nodeB.station) / network.averageWalkingSpeed;
                    nodeA.addNeighbor(nodeB, walkTime, false);
                }
            }
        }

        return stationGraph;
    }

    private List<RouteDirection> findShortestPath(HyperloopTrainNetwork network, Map<String, StationNode> stationGraph) {
        StationNode startNode = stationGraph.get(network.startPoint.description);
        StationNode destinationNode = stationGraph.get(network.destinationPoint.description);

        PriorityQueue<StationNode> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(node -> node.distance));
        startNode.distance = 0;
        priorityQueue.add(startNode);

        while (!priorityQueue.isEmpty()) {
            StationNode currentNode = priorityQueue.poll();
            if (currentNode == destinationNode) break;

            for (Neighbor neighbor : currentNode.neighbors) {
                double newDistance = currentNode.distance + neighbor.travelTime;
                if (newDistance < neighbor.node.distance) {
                    neighbor.node.distance = newDistance;
                    neighbor.node.previousNode = currentNode;
                    neighbor.node.isTrainRide = neighbor.isTrainRide;
                    priorityQueue.add(neighbor.node);
                }
            }
        }

        List<RouteDirection> routeDirections = new ArrayList<>();
        for (StationNode node = destinationNode; node.previousNode != null; node = node.previousNode) {
            double time = calculateDistance(node.station, node.previousNode.station) / (node.isTrainRide ? network.averageTrainSpeed : network.averageWalkingSpeed);
            routeDirections.add(new RouteDirection(node.previousNode.station.description, node.station.description, time, node.isTrainRide));
        }
        Collections.reverse(routeDirections);
        return routeDirections;
    }

    private double calculateDistance(Station stationA, Station stationB) {
        return Math.sqrt(Math.pow(stationA.coordinates.x - stationB.coordinates.x, 2) + Math.pow(stationA.coordinates.y - stationB.coordinates.y, 2));
    }

    /**
     * Function to print the route directions to STDOUT
     */
    public void printRouteDirections(List<RouteDirection> directions) {
        
        // TODO: Your code goes here

        System.out.printf("The fastest route takes %.0f minute(s).\n", directions.stream().mapToDouble(d -> d.duration).sum());
        System.out.println("Directions\n----------");
        int step = 1;
        for (RouteDirection direction : directions) {
            String action = direction.trainRide ? "Get on the train from" : "Walk from";
            System.out.printf("%d. %s \"%s\" to \"%s\" for %.2f minutes.\n", step++, action, direction.startStationName, direction.endStationName, direction.duration);
        }
    }
}
class StationNode {
    Station station;
    List<Neighbor> neighbors;
    double distance;
    StationNode previousNode;
    boolean isTrainRide;

    public StationNode(Station station) {
        this.station = station;
        this.neighbors = new ArrayList<>();
        this.distance = Double.MAX_VALUE;
        this.previousNode = null;
    }

    public void addNeighbor(StationNode node, double travelTime, boolean isTrainRide) {
        this.neighbors.add(new Neighbor(node, travelTime, isTrainRide));
    }
}

class Neighbor {
    StationNode node;
    double travelTime;
    boolean isTrainRide;

    public Neighbor(StationNode node, double travelTime, boolean isTrainRide) {
        this.node = node;
        this.travelTime = travelTime;
        this.isTrainRide = isTrainRide;
    }
}
