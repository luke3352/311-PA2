// LEAVE THIS FILE IN THE DEFAULT PACKAGE
//  (i.e., DO NOT add 'package cs311.pa1;' or similar)

// DO NOT MODIFY THE EXISTING METHOD SIGNATURES
//  (you may, however, add additional methods and fields)

// DO NOT INCLUDE LIBRARIES OUTSIDE OF THE JAVA STANDARD LIBRARY
//  (i.e., you may include java.util.ArrayList etc. here, but not junit, apache commons, google guava, etc.)

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class GraphProcessor
{

    static class Graph
    {
        int V;
        Hashtable<String, ArrayList<String>> adjList = new Hashtable<>();

        // constructor
        Graph(int V)
        {
            this.V = V;
        }
    }

    // Adds an edge to an undirected graph
    static void addEdge(Graph graph, String src, String dest)
    {
        // Add an edge from src to dest.
        if(graph.adjList.containsKey(src)) {
            graph.adjList.get(src).add(dest);
        }
        else{ ArrayList<String> list = new ArrayList<>();
            list.add(dest);
            graph.adjList.put(src,list);
        }

    }

    // other member fields and methods
    int verticies;
    Graph graph;
    // NOTE: graphData should be an absolute file path
    public GraphProcessor(String graphData)
    {
        File file = new File(graphData);

        try {
            Scanner sc = new Scanner(file);
            boolean firstLine = true;
            while(sc.hasNextLine()){
                if(firstLine ==true){
                    verticies = Integer.parseInt(sc.next());
                    System.out.println(verticies);
                    firstLine = false;
                    graph = new Graph(verticies);
                }
                else addEdge(graph,sc.next(), sc.next());
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
    //number of edges going out of a node
    public int outDegree(String v)
    {
        return graph.adjList.get(v).size();
    }

    public ArrayList<String> bfsPath(String u, String v)
    {
        // Mark all the vertices as not visited(By default
        // set as false)

        // Create a queue for BFS
        LinkedList<String> queue = new LinkedList<>();
        ArrayList<String> path = new ArrayList<>();
        // Mark the current node as visited and enqueue it
        Hashtable<String,Integer> visited = new Hashtable<>();
        //seeding the visited list
        Set<String> set = graph.adjList.keySet();
        Iterator<String> i = set.iterator();
        while(i.hasNext()){
            visited.put(i.next(),0);
        }
        visited.put(u,1);
        queue.add(u);


        while (queue.size() != 0)
        {
            // Dequeue a vertex from queue and print it
            u = queue.poll();
            path.add(u);
            if(u.equals(v)){return path;}
            // Get all adjacent vertices of the dequeued vertex
            // If a adjacent has not been visited, then mark it
            // visited and enqueue it
            ArrayList<String> adj = graph.adjList.get(u);


            for(String edge:adj)
            {
                if (visited.get(edge)==0)
                {
                    visited.put(u,1);
                    queue.add(edge);
                }
            }
        }//if path is not found
        ArrayList<String> list = new ArrayList<>();
        return list;
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

}