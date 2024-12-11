// change project_4 to netid
package project_4;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;
import project_4.Graph.Edge;
import project_4.Graph.Factory;
import project_4.Graph.GraphAlgorithm;
import project_4.Graph.Vertex;

/**
 * PERT algorithm implementation for finding critical paths in a directed acyclic graph (DAG).
 * Computes earliest and latest start/completion times, slack, and critical vertices.
 *
 * @author Giridhar Nair
 */
public class PERT extends GraphAlgorithm<PERT.PERTVertex> {

    private final LinkedList<Vertex> finishList = new LinkedList<>();
    private final Graph graph;
    private final int[] state; // Cycle detection state array: 0 = unvisited, 1 = visiting, 2 = visited
    private static final int UNVISITED = 0;
    private static final int VISITING = 1;
    private static final int VISITED = 2;

    /**
     * Represents attributes of a vertex used in PERT calculations.
     */
    public static class PERTVertex implements Factory {

        // Attributes of a vertex
        int duration;
        int es;
        int ec;
        int ls;
        int lc;

        /**
         * Constructor for PERTVertex.
         *
         * @param u The vertex represented by this object.
         */
        public PERTVertex(Vertex u) {
            duration = 0;
            es = 0;
            ec = 0;
            ls = Integer.MAX_VALUE;
            lc = Integer.MAX_VALUE;
        }

        /**
         * Factory method to create a PERTVertex for a given graph vertex.
         *
         * @param u The vertex for which a PERTVertex is created.
         * @return A new PERTVertex instance.
         */
        public PERTVertex make(Vertex u) {
            return new PERTVertex(u);
        }
    }

    /**
     * Private constructor for PERT.
     *
     * @param g The input graph.
     */
    private PERT(Graph g) {
        super(g, new PERTVertex(null));
        this.graph = g;
        this.state = new int[graph.size()];
    }

    /**
     * Sets the task duration for a given vertex.
     *
     * @param u The vertex.
     * @param d The duration of the task represented by the vertex.
     */
    public void setDuration(Vertex u, int d) {
        get(u).duration = d;
    }

    /**
     * Executes the PERT algorithm.
     *
     * @return true if the graph is a DAG, false otherwise.
     */
    public boolean pert() {
        if (!isDAG()) {
            return false;
        }

        topologicalOrder();
        int projectLength = 0;

        // Calculate earliest start and earliest complete times
        for (Vertex u : finishList) {
            PERTVertex pu = get(u);
            pu.ec = pu.es + pu.duration;
            projectLength = Math.max(projectLength, pu.ec);

            for (Edge e : graph.incident(u)) {
                Vertex v = e.otherEnd(u);
                PERTVertex pv = get(v);
                pv.es = Math.max(pv.es, pu.ec);
            }
        }

        // Calculate latest start and latest complete times
        for (Vertex u : finishList) {
            PERTVertex pu = get(u);
            pu.ls = projectLength - pu.duration;
            pu.lc = projectLength;
        }

        // Reverse topological order to update latest start times
        for (int i = finishList.size() - 1; i >= 0; i--) {
            Vertex u = finishList.get(i);

            for (Edge e : graph.incident(u)) {
                Vertex v = e.otherEnd(u);
                PERTVertex pu = get(u);
                PERTVertex pv = get(v);
                pu.lc = Math.min(pu.lc, pv.ls);
                pu.ls = pu.lc - pu.duration;
            }
        }

        return true;
    }

    /**
     * Finds a topological order of the graph using Depth-First Search (DFS).
     * This method assumes the graph is a Directed Acyclic Graph (DAG).
     *
     * @return A LinkedList of vertices in topological order, with the first vertex being the starting point.
     */
    LinkedList<Vertex> topologicalOrder() {
        resetState();

        for (Vertex v : graph) {
            if (state[v.getIndex()] == UNVISITED) {
                dfs(v);
            }
        }

        return finishList;
    }

    /**
     * Depth-first search to find topological order.
     *
     * @param v The vertex to start DFS from.
     */
    private void dfs(Vertex v) {
        state[v.getIndex()] = VISITING;

        for (Edge e : graph.incident(v)) {
            Vertex u = e.otherEnd(v);
            if (state[u.getIndex()] == UNVISITED) {
                dfs(u);
            }
        }

        state[v.getIndex()] = VISITED;
        finishList.addFirst(v);
    }

    /**
     * Checks if the graph is a directed acyclic graph (DAG).
     *
     * @return true if the graph is a DAG, false otherwise.
     */
    private boolean isDAG() {
        resetState();

        for (Vertex v : graph) {
            if (state[v.getIndex()] == UNVISITED && hasCycle(v)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the graph has a cycle starting from a given vertex.
     *
     * @param v The vertex to start cycle detection from.
     * @return true if a cycle is detected, false otherwise.
     */
    private boolean hasCycle(Vertex v) {
        state[v.getIndex()] = VISITING;

        for (Edge e : graph.incident(v)) {
            Vertex u = e.otherEnd(v);
            int uIndex = u.getIndex();

            if (state[uIndex] == VISITING || (state[uIndex] == UNVISITED && hasCycle(u))) {
                return true;
            }
        }

        state[v.getIndex()] = VISITED;
        return false;
    }

    /**
     * Reset all values in state to 0 (UNVISITED)
     */
    private void resetState() {
        Arrays.fill(state, UNVISITED);
    }

    // The following methods are called after calling pert().

    /**
     * Earliest time at which task u can be completed.
     *
     * @param u The vertex representing the task.
     * @return The earliest completion time of the task.
     */
    public int ec(Vertex u) {
        return get(u).ec;
    }

    /**
     * Latest completion time of u.
     *
     * @param u The vertex representing the task.
     * @return The latest completion time of the task.
     */
    public int lc(Vertex u) {
        return get(u).lc;
    }

    /**
     * Slack of u.
     *
     * @param u The vertex representing the task.
     * @return The slack of the task.
     */
    public int slack(Vertex u) {
        return get(u).ls - get(u).es;
    }

    /**
     * Critical path length in the graph.
     *
     * @return The length of the critical path.
     */
    public int criticalPath() {
        int maxPath = 0;
        for (Vertex u : graph) {
            maxPath = Math.max(maxPath, get(u).ec);
        }
        return maxPath;
    }

    /**
     * If u is a critical vertex.
     *
     * @param u The vertex representing the task.
     * @return true if the task is critical, false otherwise.
     */
    public boolean critical(Vertex u) {
        return slack(u) == 0;
    }

    /**
     * Number of critical vertices in the graph.
     *
     * @return The number of critical vertices.
     */
    public int numCritical() {
        int count = 0;
        for (Vertex u : graph) {
            if (critical(u)) {
                count++;
            }
        }
        return count;
    }

    /* Create a PERT instance on g, runs the algorithm.
     * Returns PERT instance if successful. Returns null if G is not a DAG.
     */
    public static PERT pert(Graph g, int[] duration) {
        PERT p = new PERT(g);
        for (Vertex u : g) {
            p.setDuration(u, duration[u.getIndex()]);
        }
        // Run PERT algorithm.  Returns false if g is not a DAG
        if (p.pert()) {
            return p;
        } else {
            return null;
        }
    }

    /**
     * Main method for testing the PERT algorithm.
     *
     * @param args Command line arguments.
     * @throws Exception If an exception occurs.
     */
    public static void main(String[] args) throws Exception {
        String graph =
            "10 13   1 2 1   2 4 1   2 5 1   3 5 1   3 6 1   4 7 1   5 7 1   5 8 1   6 8 1   6 9 1   7 10 1   8 10 1   9 10 1      0 3 2 3 2 1 3 2 4 1";
        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(graph);
        Graph g = Graph.readDirectedGraph(in);
        g.printGraph(false);

        int[] duration = new int[g.size()];
        for (int i = 0; i < g.size(); i++) {
            duration[i] = in.nextInt();
        }
        PERT p = pert(g, duration);
        if (p == null) {
            System.out.println("Invalid graph: not a DAG");
        } else {
            System.out.println("Number of critical vertices: " + p.numCritical());
            System.out.println("u\tEC\tLC\tSlack\tCritical");
            for (Vertex u : g) {
                System.out.println(
                    u + "\t" + p.ec(u) + "\t" + p.lc(u) + "\t" + p.slack(u) + "\t" + p.critical(u)
                );
            }
        }
    }
}
