import java.io.FileNotFoundException;
import java.io.FileReader;
import com.google.gson.*;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Bag;

class LabNetworkCabling {
    private Map<Integer, String> deviceTypes;
    private EdgeWeightedGraph graph;
    private PrimMST mst;
    private int totalWeight;

    public LabNetworkCabling(Map<Integer, String> deviceTypes, List<int[]> links){
        // create a Minimum Spanning Tree
        this.deviceTypes = deviceTypes;
        this.graph = new EdgeWeightedGraph(deviceTypes.size());
        for (int[] link : links) {
            Edge edge = new Edge(link[0], link[1], link[2]);
            graph.addEdge(edge);
        }
        mst = new PrimMST(graph);
        totalWeight = mst.weight(); // returns the total weight of the MST
    }

    public int cablingCost() {
        return totalWeight;
    }


    public int serverToRouter(){
        int srDistance = 0;
        // find the path distance between the server and the router
        int serverIndex = -1;
        int routerIndex = -1;
        for (Map.Entry<Integer, String> entry : deviceTypes.entrySet()) {
            if (entry.getValue().equals("Server")) {
                serverIndex = entry.getKey();
            } else if (entry.getValue().equals("Router")) {
                routerIndex = entry.getKey();
            }
        }

        // Use function in KruskalMST to find the distance between the server and the router
        srDistance = mst.distanceInMST(serverIndex, routerIndex);

        return srDistance;
    }



    public int mostPopularPrinter(){
        // find the most popular printer and return its index
        Map<Integer, Integer> printerUsage = new HashMap<>();
        for (Map.Entry<Integer, String> entry : deviceTypes.entrySet()) {
            if (entry.getValue().equals("Printer")) {
                printerUsage.put(entry.getKey(), 0);
            }
        }

        // Use precomputed distances to find the nearest printer for each computer
        for (Map.Entry<Integer, String> entry : deviceTypes.entrySet()) {
            if (entry.getValue().equals("Computer")) {
                int nearestPrinter = -1;
                int minDistance = Integer.MAX_VALUE;
                for (Integer printer : printerUsage.keySet()) {
                    int distance = mst.distanceInMST(entry.getKey(), printer);
                    if (distance < minDistance) {
                        nearestPrinter = printer;
                        minDistance = distance;
                    }
                }
                if (nearestPrinter != -1) {
                    printerUsage.put(nearestPrinter, printerUsage.get(nearestPrinter) + 1);
                }
            }
        }

        // Find and return the most popular printer based on usage
        return printerUsage.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(-1);
    }

    public static void main(String[] args) {

        // [device index, device type]
        Map<Integer, String> deviceTypes = Map.of(
                0, "Router",
                1, "Server",
                2, "Printer",
                3, "Printer",
                4, "Computer",
                5, "Computer",
                6, "Computer"
        );

        // [device a, device b, link distance (cable length)]
        List<int[]> links = List.of(
                new int[]{0, 1, 4},
                new int[]{1, 2, 2},
                new int[]{2, 4, 1},
                new int[]{0, 3, 3},
                new int[]{1, 3, 8},
                new int[]{3, 5, 7},
                new int[]{3, 6, 9},
                new int[]{0, 6, 5}
        );

        LabNetworkCabling Network = new LabNetworkCabling(deviceTypes, links);
        System.out.println("Total Cabling Cost: " + Network.cablingCost());
        System.out.println("Distance between Server and Router: " + Network.serverToRouter());
        System.out.println("Most Popular Printer: " + Network.mostPopularPrinter());
    }
}

class PrimMST {
    private static final double FLOATING_POINT_EPSILON = 1.0E-12;

    private Edge[] edgeTo;        // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
    private int[] distTo;      // distTo[v] = weight of shortest such edge
    private boolean[] marked;     // marked[v] = true if v on tree, false otherwise
    private IndexMinPQ<Integer> pq;
    private List<Edge>[] mstAdj;  // adjacency list representation of the MST
    private int[][] allPairsDist; // distance matrix for all pairs of vertices

    public PrimMST(EdgeWeightedGraph G) {
        int V = G.V();
        edgeTo = new Edge[V];
        distTo = new int[V];
        marked = new boolean[V];
        pq = new IndexMinPQ<Integer>(V);
        mstAdj = (List<Edge>[]) new List[V];
        for (int v = 0; v < V; v++) {
            mstAdj[v] = new ArrayList<>();
            distTo[v] = Integer.MAX_VALUE;
        }

        for (int v = 0; v < V; v++)      // run from each vertex to find
            if (!marked[v]) prim(G, v);  // minimum spanning forest

        // Build the MST adjacency list
        for (int v = 0; v < V; v++) {
            Edge e = edgeTo[v];
            if (e != null) {
                int w = e.other(v);
                mstAdj[v].add(e);
                mstAdj[w].add(e);
            }
        }

        // Initialize allPairsDist matrix with infinity
        allPairsDist = new int[V][V];
        for (int i = 0; i < V; i++) {
            Arrays.fill(allPairsDist[i], Integer.MAX_VALUE);
            allPairsDist[i][i] = 0;
        }

        // Populate allPairsDist using BFS from each vertex
        for (int i = 0; i < V; i++) {
//            bfs(i, V);
            dfs(i, V);
        }
    }

    // run Prim's algorithm in graph G, starting from vertex s
    private void prim(EdgeWeightedGraph G, int s) {
        distTo[s] = 0;
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            scan(G, v);
        }
    }

    // scan vertex v
    private void scan(EdgeWeightedGraph G, int v) {
        marked[v] = true;
        for (Edge e : G.adj(v)) {
            int w = e.other(v);
            if (marked[w]) continue;         // v-w is obsolete edge
            if (e.weight() < distTo[w]) {
                distTo[w] = (int) e.weight();
                edgeTo[w] = e;
                if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
                else                pq.insert(w, distTo[w]);
            }
        }
    }

//    private void bfs(int start, int V) {
//        boolean[] visited = new boolean[V];
//        Queue<Integer> queue = new Queue<>();
//        queue.enqueue(start);
//        visited[start] = true;
//        allPairsDist[start][start] = 0.0;
//
//        while (!queue.isEmpty()) {
//            int v = queue.dequeue();
//            for (Edge e : mstAdj[v]) {
//                int w = e.other(v);
//                if (!visited[w]) {
//                    allPairsDist[start][w] = allPairsDist[start][v] + e.weight();
//                    visited[w] = true;
//                    queue.enqueue(w);
//                }
//            }
//        }
//    }

    // dfs iterative version
//    private void dfs(int start, int V) {
//        boolean[] visited = new boolean[V];
//        Stack<Integer> stack = new Stack<>();
//        stack.push(start);
//        visited[start] = true;
//        allPairsDist[start][start] = 0;
//
//        while (!stack.isEmpty()) {
//            int v = stack.pop();
//            for (Edge e : mstAdj[v]) {
//                int w = e.other(v);
//                if (!visited[w]) {
//                    allPairsDist[start][w] = allPairsDist[start][v] + e.weight();
//                    visited[w] = true;
//                    stack.push(w);
//                }
//            }
//        }
//    }

    // dfs recursive version
    private void dfs(int start, int V) {
        boolean[] visited = new boolean[V];
        dfs(start, start, visited);
    }

    private void dfs(int start, int current, boolean[] visited) {
        visited[current] = true;
        for (Edge e : mstAdj[current]) {
            int neighbor = e.other(current);
            if (!visited[neighbor]) {
                allPairsDist[start][neighbor] = allPairsDist[start][current] + e.weight();
                dfs(start, neighbor, visited);
            }
        }
    }

    public Iterable<Edge> edges() {
        Queue<Edge> mst = new Queue<Edge>();
        for (int v = 0; v < edgeTo.length; v++) {
            Edge e = edgeTo[v];
            if (e != null) {
                mst.enqueue(e);
            }
        }
        return mst;
    }

    public int weight() {
        int weight = 0;
        for (Edge e : edges())
            weight += e.weight();
        return weight;
    }

    // Function to find the distance between two vertices in the MST
    public int distanceInMST(int u, int v) {
        return allPairsDist[u][v];
    }
}

class Edge implements Comparable<Edge> {

    private final int v;
    private final int w;
    private final int weight;

    public Edge(int v, int w, int weight) {
        if (v < 0) throw new IllegalArgumentException("vertex index must be a non-negative integer");
        if (w < 0) throw new IllegalArgumentException("vertex index must be a non-negative integer");
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public int weight() {
        return weight;
    }

    public int either() {
        return v;
    }

    public int other(int vertex) {
        if (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new IllegalArgumentException("Illegal endpoint");
    }

    @Override
    public int compareTo(Edge that) {
        return Integer.compare(this.weight, that.weight);
    }

    @Override
    public String toString() {
        return String.format("%d-%d %d", v, w, weight);
    }
}

class EdgeWeightedGraph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;
    private int E;
    private Bag<Edge>[] adj;

    public EdgeWeightedGraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be non-negative");
        this.V = V;
        this.E = 0;
        adj = (Bag<Edge>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Edge>();
        }
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    public void addEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }

    public Iterable<Edge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }
    public int degree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    public Iterable<Edge> edges() {
        Bag<Edge> list = new Bag<Edge>();
        for (int v = 0; v < V; v++) {
            int selfLoops = 0;
            for (Edge e : adj(v)) {
                if (e.other(v) > v) {
                    list.add(e);
                }
                // add only one copy of each self loop (self loops will be consecutive)
                else if (e.other(v) == v) {
                    if (selfLoops % 2 == 0) list.add(e);
                    selfLoops++;
                }
            }
        }
        return list;
    }
}

class OutputFormat{
    LabNetworkCabling l;
    Map<Integer, String> deviceTypes;
    List<int[]> links;

    int cablingCost;
    int serverToRouter;
    int mostPopularPrinter;
}

class TestCase {
    int Case;
    int score;
    ArrayList<OutputFormat> data;
}

class test_LabNetworkCabling{
    public static void main(String[] args)
    {
        Gson gson = new Gson();
        int num_ac = 0;

        try {
            TestCase[] testCases = gson.fromJson(new FileReader(args[0]), TestCase[].class);

            for(int i = 0; i<testCases.length;++i)
            {
                for (OutputFormat data : testCases[i].data) {

                    LabNetworkCabling LNC = new LabNetworkCabling(data.deviceTypes, data.links);
                    int ans_cc = data.cablingCost;
                    int ans_sr = data.serverToRouter;
                    int ans_mpp = data.mostPopularPrinter;

                    int user_cc = LNC.cablingCost();
                    int user_sr = LNC.serverToRouter();
                    int user_mpp = LNC.mostPopularPrinter();

                    if(user_cc == ans_cc && user_sr == ans_sr && user_mpp==ans_mpp)
                    {
                        System.out.println("AC");
                        num_ac++;
                    }
                    else
                    {
                        System.out.println("WA");
                        System.out.println("Input deviceTypes:\n" + data.deviceTypes);
                        System.out.println("Input links: ");
                        for (int[] link : data.links) {
                            System.out.print(Arrays.toString(link));
                        }

                        System.out.println("\nAns cablingCost: " + ans_cc );
                        System.out.println("Your cablingCost:  " + user_cc);
                        System.out.println("Ans serverToRouter:  " + ans_sr);
                        System.out.println("Your serverToRouter:  " + user_sr);
                        System.out.println("Ans mostPopularPrinter:  " + ans_mpp);
                        System.out.println("Your mostPopularPrinter:  " + user_mpp);
                        System.out.println("");
                    }
                }
            }
            System.out.println("Score: "+num_ac+"/10");
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (JsonIOException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}