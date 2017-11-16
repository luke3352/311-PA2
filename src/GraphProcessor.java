// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add additional methods and fields)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may include java.util.ArrayList etc. here, but not junit, apache commons, google guava, etc.)

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
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
    private ArrayList<String> vertices;
    private int numOfVertices;
    private int diameter;
    private int maxShortestPath;
    private double[][] dist;

    // NOTE: graphData should be an absolute file path
    public GraphProcessor(String graphData) {
        adjList = new Hashtable<>();
        vertices = new ArrayList<>();
        numOfVertices = 0;
        diameter = 0;
        maxShortestPath = 0;
        File file = new File(graphData);

        try {
            Scanner sc = new Scanner(file);
            if (sc.hasNextLine()) {
                numOfVertices = Integer.parseInt(sc.next());
            }

            while (sc.hasNextLine()){
                addEdge(sc.next(), sc.next());
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // initialize distance array since numOfVertices has been now set
        dist = new double[numOfVertices][numOfVertices];

        // Use flyod warshall algorithm to calculate the distances
        // initialize the distance matrix
        for (int i = 0; i < numOfVertices; i++) {
            for (int j = 0; j < numOfVertices; j++) {
                if (i == j) {
                    dist[i][j] = 0;
                } else {
                    dist[i][j] = adjList.get(vertices.get(i)).contains(vertices.get(j)) ? 1 : Double.POSITIVE_INFINITY;
                }
            }
        }

        for (int k = 0; k < numOfVertices; k++) {
            // Pick all vertices as source one by one
            for (int i = 0; i < numOfVertices; i++) {
                // Pick all vertices as destination for the
                // above picked source
                for (int j = 0; j < numOfVertices; j++) {
                    // If vertex k is on the shortest path from
                    // i to j, then update the value of dist[i][j]
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                    if (dist[i][j] > maxShortestPath && dist[i][j] != Double.POSITIVE_INFINITY) {
                        maxShortestPath = (int) dist[i][j];
                    }
                }
            }
        }
        for (int i=0; i<numOfVertices; i++) {
            for (int j=0; j<numOfVertices; j++)
            {
                System.out.print(dist[i][j]+"   ");
            }
            System.out.println();
        }
    }

    // Adds an edge to an undirected graph
    private void addEdge(String src, String dest) {
        // Add an edge from src to dest.
        if (!vertices.contains(src)) {
            vertices.add(src);
        }

        if (!vertices.contains(dest)) {
            vertices.add(dest);
        }

        if (!adjList.containsKey(dest)) {
            ArrayList<String> list = new ArrayList<>();
            adjList.put(dest, list);
        }

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

    private void dfsHelper(String v, HashSet<String> hs, Hashtable<String, ArrayList<String>> graph) {
        if (v == null) {
            return;
        }
        for (String u: graph.get(v)) {
            if (!hs.contains(u)) {
                hs.add(u);
                dfsHelper(u, hs, graph);
            }
        }
    }

    private Hashtable<String, ArrayList<String>> reverseGraph (Hashtable<String, ArrayList<String>> ht) {
        Hashtable<String, ArrayList<String>> reverse = new Hashtable<>();
        Set<String> keys = ht.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String u = iterator.next();
            for (String v: ht.get(u)) {
                if (!reverse.containsKey(v)) {
                    reverse.put(v, new ArrayList<>());
                }
                reverse.get(v).add(u);
            }
        }
        return reverse;
    }

    private boolean checkIfStronglyConnected () {
        // Kosarajus algorithm to check if graph is strongly connected
        HashSet<String> visited = new HashSet<>();

        // Step 2: Do DFS traversal starting from first vertex.
        Set<String> s = adjList.keySet();
        Iterator<String> c = s.iterator();
        String random = c.next();
        dfsHelper(random, visited, adjList);

        // If DFS traversal doesn't visit all vertices, then
        // return false.
        if (visited.size() != numOfVertices) {
            return false;
        }

        // Step 3: Create a reversed graph
        Hashtable<String, ArrayList<String>> reverseGraph = reverseGraph(adjList);

        // Step 4: Mark all the vertices as not visited (For
        // second DFS)
        visited = new HashSet<>();

        // Step 5: Do DFS for reversed graph starting from
        // first vertex. Staring Vertex must be same starting
        // point of first DFS
        s = reverseGraph.keySet();
        c = s.iterator();
        random = c.next();
        dfsHelper(random, visited, reverseGraph);

        // If all vertices are not visited in second DFS, then
        // return false
        if (visited.size() != numOfVertices) {
            return false;
        }

        return true;
    }

    public int diameter() {
        if (checkIfStronglyConnected()) {
            diameter = maxShortestPath;
        } else {
            // if graph is not strongly connected
            // return 2n
            diameter = 2*numOfVertices;
        }
        return diameter;
    }

    public int centrality(String v) {
        int centrality = 0;
        if (adjList.containsKey(v)) {
            for (int i=0; i < numOfVertices; i++) {
                for (int j=0; j < numOfVertices; j++) {
                    int index = vertices.indexOf(v);
                    if (i != j && dist[i][j] == dist[i][index] + dist[index][j]) {
                        centrality++;
                    }
                }
            }
        }

        return centrality;
    }

    public static void main(String args[]) {
        GraphProcessor gp = new GraphProcessor("./File.Txt");
        System.out.println(gp.bfsPath("/wiki/Category_theory","/wiki/Herman_Hollerith"));
        System.out.println(gp.diameter());
        System.out.println(gp.centrality("/wiki/Computer_Science"));
    }
}