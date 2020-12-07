package com.paulmatthews;

import java.util.HashMap;
import java.util.Map;

public class Node {

    String name;
    Map<String, Integer> edges;

    /**
     *
     * @param name for node
     */
    public Node(String name) {
        edges = new HashMap<>();
        this.name = name;
    }

    /**
     *
     * @param nodeName an adjacent node connected to this node.
     * @param weight is the distance from this node to the adjacent node.
     */
    public void addEdge(String nodeName, int weight) {
        edges.put(nodeName, weight);
    }

    public String showEdges() {
        return edges.toString();
    }
}


