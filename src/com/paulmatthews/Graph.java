package com.paulmatthews;

import java.util.*;

public class Graph {


        Map<String, Node> nodes;


        public Graph() {
            nodes = new HashMap<>();
        }

        /**
         *
         * @param name of the node
         * @param node object
         */
        public void addNode(String name, Node node) {
            nodes.put(name, node);
        }

        /**
         * Useful for debugging and extensibility
         * @return a string representation of graph
         */
        public String showGraph() {
            String out = "";
            Iterator maperator = nodes.entrySet().iterator();
            while (maperator.hasNext()) {
                Map.Entry entry = (Map.Entry) maperator.next();
                Node node = (Node) entry.getValue();
                out += entry.getKey().toString() + ": " + node.showEdges() + "\n";
            }
            return out;
        }

        /**
         *
         * @param name of node
         * @return node object
         */
        public Node getNode(String name) {
            return nodes.get(name);
        }

        /**
         *
         * @param nodeName
         * @return true that a specified node exists in the graph
         */
        public boolean validNode(String nodeName) {
            if (nodes.keySet().contains(nodeName)) {
                return true;
            } else {
                return false;
            }
        }

}
