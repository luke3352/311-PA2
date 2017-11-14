// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add additional methods and fields)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may include java.util.ArrayList etc. here, but not junit, apache commons, google guava, etc.)

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class GraphProcessor {

    class Node {
        String val;
        Node parent;

        public Node (String val, Node parent) {
            this.val = val;
            this.parent = parent;
        }
    }

    private Hashtable<String, ArrayList<String>> adjList;
    private int vertices;

    // NOTE: graphData should be an absolute file path
    public GraphProcessor(String graphData) {
        adjList = new Hashtable<>();
        vertices = 0;
        File file = new File(graphData);

        try {
            Scanner sc = new Scanner(file);
            if (sc.hasNextLine()) {
                vertices = Integer.parseInt(sc.next());
            }

            while (sc.hasNextLine()){
                addEdge(sc.next(), sc.next());
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Adds an edge to an undirected graph
    private void addEdge(String src, String dest) {
        // Add an edge from src to dest.
        if (adjList.containsKey(src)) {
           adjList.get(src).add(dest);
        } else {
            ArrayList<String> list = new ArrayList<>();
            list.add(dest);
            adjList.put(src, list);
        }
    }

    //number of edges going out of a node
    public int outDegree(String v) {
        if (adjList.containsKey(v)) {
            return adjList.get(v).size();
        } else {
            return 0;
        }
    }

    public ArrayList<String> bfsPath(String u, String v) {
        // Mark all the vertices as not visited(By default
        // set as false)

        // Create a queue for BFS
        LinkedList<Node> queue = new LinkedList<>();
        ArrayList<String> path = new ArrayList<>();
        // Mark the current node as visited and enqueue it
        HashSet<String> visited = new HashSet<>();
        Node parent = new Node(u, null);
        visited.add(u);
        queue.add(parent);
        Node current = null;
        boolean foundPath = false;

        while (!queue.isEmpty()) {
            // Dequeue a vertex from queue and print it
            current = queue.poll();
            if (current.val.equals(v)) {
                foundPath = true;
                break;
            }
            // Get all adjacent vertices of the dequeued vertex
            // If a adjacent has not been visited, then mark it
            // visited and enqueue it
            ArrayList<String> adj = adjList.get(current.val);
            for(String edge: adj) {
                if (!visited.contains(edge)) {
                    visited.add(edge);
                    queue.add(new Node(edge, current));
                }
            }
        }

        while (foundPath && current != null) {
            path.add(current.val);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }

    public int diameter()
    {
        // implementation
        return 0;
    }

    public int centrality(String v)
    {
        // implementation
        return 0;
    }

    public static void main(String args[]) {
        GraphProcessor gp = new GraphProcessor("./GraphData.Txt");
        System.out.println(gp.bfsPath("Ames","Chicago"));
    }
}