package com.paulmatthews;

import java.util.*;

public class Dijkstras {

    private static Graph graph;
    private static Set<String> edges;
    private static String sourceNode;
    private static String destNode;
    private static int maxDistance;


    public static void main(String[] args) {
        graph = new Graph();
        edges = new HashSet<>();

//Here are 4 variants of input for algorithm to be tested with

//      4 node graph example 1
//      String graphInfo = "[A,B,3] [B,C,5] [C,D,1] [D,E,1]";
//      String routeToFind = "A->E,10";

//      4 node graph example 2
//      String graphInfo = "[A,B,3] [B,C,5] [C,D,2] [A,C,7]";
//      String routeToFind = "A->D,10";

//      6 node graph example 3
//      String graphInfo = "[A,B,5] [A,C,7] [B,C,1] [C,D,3] [A,D,8] [D,E,3]";
//      String routeToFind = "A->E,10";

//      13 node graph example 4
        String graphInfo = "[E,M,4] [E,L,19] [P,M,2] [L,P,12] [P,Q,10] [Q,F,4] [Q,G,7] [F,A,3] [G,H,10] [H,A,3] [G,A,5] [F,L,7] [A,L,2]";
        String routeToFind = "H->E,25";

        if (validInputSyntax(graphInfo) && validRouteSyntax(routeToFind)) {
            if (checkLogicalInput(graphInfo)) {
                parseNodes(graphInfo);
                if (parseRoute(routeToFind)) {
                    System.out.println(shortestDistance(sourceNode, destNode, maxDistance));
                } else {
                    System.out.println("Invalid Graph/Node data entered.");
                }
            } else {
                System.out.println("Invalid Graph/Node data entered.");
            }
        } else {
            System.out.println("Input Syntax Error. Please provide edges on the form [A,B,10]\n" +
                    "A is the source node and B is the target. 10 here is the max distance.");
        }
    }


    /**
     * @param input string to test form of against regular expression
     * @return true or false that imput is correctly formed
     */
    public static boolean validInputSyntax(String input) {
        boolean valid = input.matches("(\\[[A-Z],[A-Z],\\d(\\d)*\\] )*(\\[[A-Z],[A-Z],\\d(\\d)*\\]){1}");
        return valid;
    }


    /**
     * @param input route string to test
     * @return true or false route sepcification is valid.
     */
    public static boolean validRouteSyntax(String input) {
        boolean valid = input.matches("[A-Z]->[A-Z],\\d(\\d)*");
        return valid;
    }


    /**
     * @param input String from user
     * @return true or false that the specifed nodes and edges can produce a valid
     * bi-directional, weighted and connected graph with no duplicate edges.
     */
    public static boolean checkLogicalInput(String input) {
        Set<String> existingEdges = new HashSet<>();

        // split the input string into relevant info
        String[] setOfNodeStrings = input.split("\\]");

        // strip string for edge info only
        for (String eachNodeString : setOfNodeStrings) {
            String stripped = eachNodeString.replaceAll("[\\[ ,]", "");
            if (stripped.substring(0, 1).equals(stripped.substring(1, 2))) {
                return false;
            }
            // test string form
            String inverse = stripped.substring(1, 2) + stripped.substring(0, 1);
//            System.out.println(stripped + " : inv = " + inverse );
            if (existingEdges.contains(stripped.substring(0, 2)) || existingEdges.contains(inverse)) {
                return false;
            } else {
                existingEdges.add(stripped.substring(0, 2));
                edges.add(stripped);
            }
        }
        return true;
    }


    /**
     * @param nodeData takes user input and builds weighted graph nodes and the subsequent graph
     */
    public static void parseNodes(String nodeData) {

        Set<String> nodes = new HashSet<>();

        // extract node info from data on edges - set ensures each node found once only
        for (String ed : edges) {
            nodes.add(ed.substring(0, 1));
            nodes.add(ed.substring(1, 2));
        }
        // add nodes to graph
        for (String nodeName : nodes) {
            Node newNode = new Node(nodeName);
            graph.addNode(nodeName, newNode);
        }
        // add all edges
        for (String edgeInfo : edges) {
            addEdges(edgeInfo);
        }
        System.out.println("Graph loaded: \n"+graph.showGraph());
    }


    /**
     * Adds edge date to each node
     *
     * @param edgeInfo
     */
    public static void addEdges(String edgeInfo) {
        String firstNode = edgeInfo.substring(0, 1);
        String secondNode = edgeInfo.substring(1, 2);
        int weight = Integer.parseInt(edgeInfo.substring(2));
        Node n1 = graph.getNode(firstNode);
        Node n2 = graph.getNode(secondNode);
        n1.addEdge(secondNode, weight);
        n2.addEdge(firstNode, weight);
    }

    /**
     * Extracts from string and places source, destination and max distance into Solution class fields.
     *
     * @param routeData
     * @return true that the route input is valid
     */
    public static boolean parseRoute(String routeData) {
        if (graph.validNode(routeData.substring(0, 1)) && graph.validNode(routeData.substring(3, 4))) {
            sourceNode = routeData.substring(0, 1);
            destNode = routeData.substring(3, 4);
            maxDistance = Integer.parseInt(routeData.substring(5));
            System.out.println("Source: " + sourceNode + " Destination: " + destNode + " Max Distance: "+maxDistance);
            return true;
        } else {
            return false;
        }
    }


    /**
     * This method implements Dijkstras shortest path algorithm returning a string of the route.
     * Can be extended to return values too.
     *
     * @param fromNode
     * @param toNode
     * @param maxDistance route cannot exceed this
     * @return a string outlining route from source to destination in the form source->node(n)->destination
     */
    public static String shortestDistance(String fromNode, String toNode, int maxDistance) {
        System.out.println("\n*****************************************\n\nRunning Dijkstra's shortest path algorithm..\n");
        String result = "";

        Stack<String> checking = new Stack<>();
        Set<String> unchecked = new HashSet<>();
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> routes = new HashMap<>();

        // node to be examined
        Node currentNode;
        // add all nodes to unchecked set
        unchecked.addAll(graph.nodes.keySet());
        //assign source node to current node and remove from unchecked set
        currentNode = graph.getNode(fromNode);
        unchecked.remove(fromNode);

        // Now iterate thru all other nodes

        while (unchecked.size() > 0) {
            System.out.println("Checking connected nodes of Node "+ currentNode.name);
            System.out.println(currentNode.edges.keySet());
            // iterate thru nodes connected to current node

            for (String currentChildNode : currentNode.edges.keySet()) {
                // conditional to avoid looking at an edge that has been viewed already
                if (unchecked.contains(currentChildNode)) {
                    // place node in stack
                    checking.push(currentChildNode);
                    // now sum distances

                    // if another node has already reported a connection to this child node
                    if (distances.containsKey(currentChildNode)) {
                        // current weight to source
                        int currentDistanceToSourceNode = distances.get(currentChildNode);
                        // new weight to source
                        int distanceNodeToChild = currentNode.edges.get(currentChildNode);
                        int newVal = distances.get(currentNode.name) + distanceNodeToChild;

                        // have to determine that new path is shorter
                        if (newVal < currentDistanceToSourceNode) {
                            distances.put(currentChildNode, newVal);
                            routes.put(currentChildNode, currentNode.name);
                        }
                    } else {
                        // no distances for this child node exist in table
                        if (distances.size() > 0) {
                            int existingValue = 0;
                            if (distances.containsKey(currentNode.name)) {
                                existingValue = distances.get(currentNode.name);
                            }
                            distances.put(currentChildNode, currentNode.edges.get(currentChildNode) + existingValue);
                            routes.put(currentChildNode, currentNode.name);
                        } else {
                            // if the table is empty
                            distances.put(currentChildNode, currentNode.edges.get(currentChildNode));
                            routes.put(currentChildNode, currentNode.name);
                        }
                    }
                }
            }

            // pop node just inspected from set
            currentNode = graph.getNode(checking.pop());
            unchecked.remove(currentNode.name);
        }

        System.out.println("\n*************************************\n");

        if (distances.get(toNode) > maxDistance) {
            result = "Maximum Distance Exceeded.";
        } else {
            result = "->" + toNode;
            String parent = routes.get(toNode);

            while (!parent.equals(fromNode)) {
                result = "->" + parent + result;
                parent = routes.get(parent);
            }
            result = fromNode + result;
        }

        return "Shortest Route: "+ result + "\nDistance: " + distances.get(destNode);
    }
}


