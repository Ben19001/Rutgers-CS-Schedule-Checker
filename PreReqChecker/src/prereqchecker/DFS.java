package prereqchecker;

import java.util.*;

public class DFS {
    private boolean[] isMarked;
    private boolean[] onStack;
    private String[] edgeTo;
    private Node[] al;
    private Hashmap mapped;
    private AdjList graph;
    private ArrayList<String> path = new ArrayList<>();
    private boolean cycle;

    public DFS(String filename) {
        StdIn.setFile(filename);
        graph = new AdjList(filename);
        graph.createLists();
        al = graph.getVertices();
        mapped = new Hashmap(filename);
        isMarked = new boolean[al.length];
        edgeTo = new String[al.length];
        onStack = new boolean[al.length];
    }

    public void dfs(String classEdge) {
        int v = mapped.get(classEdge);
        onStack[v] = true;
        isMarked[v] = true;
        Node temp = al[v].next;

        while (temp != null) {
            int w = mapped.get(temp.courseID);

            if (!isMarked[w]) {
                edgeTo[w] = classEdge;
                dfs(temp.courseID);
            } else if (onStack[w]) {
                cycle = true;
                return;
            }

            temp = temp.next;
        }

        onStack[v] = false;
    }

    public ArrayList<String> printEdges() {
        for (int i = 0; i < edgeTo.length; i++) {
            if (edgeTo[i] != null) {
                // StdOut.println(al[i].courseID);
                path.add(al[i].courseID);
            }
        }
        return path;
    }

    public void createEdge(String firstCourse, String secondCourse) {
        // need to add course to edge
        int insertHere = mapped.get(firstCourse);
        Node here = al[insertHere];
        while (here.next != null) {
            here = here.next;
        }
        here.next = new Node(null, secondCourse);
    }

    public void deleteEdge(String course) {
        for (int i = 0; i < al.length; i++) {
            Node current = al[i];
            while (current.next != null) {
                if (current.courseID.equals(course)) {
                    current = current.next;
                }
                current = current.next;
            }
        }
        int removeHere = mapped.get(course);
        al[removeHere] = null;
    }

    public boolean returnCycle() {
        return cycle;
    }

    public int getSizeOfPath() {
        int counter = 0;
        for (int i = 0; i < isMarked.length; i++) {
            if (isMarked[i]) {
                counter++;
            }
        }
        return counter;
    }

    // public void printEdges(String outputFile) {
    // for (int i = 0; i < edgeTo.length; i++) {
    // if (edgeTo[i] != null) {
    // StdOut.println(al[i].courseID);
    // }
    // }
    // }

    public void clearList() {
        path.clear();
        cycle = false;
        edgeTo = new String[al.length];
        isMarked = new boolean[al.length];
        onStack = new boolean[al.length];
    }

    // public boolean hasPathTo(String classEdge) {
    // int v = mapped.get(classEdge);
    // return isMarked[v];
    // }

    // // how do we know what start is
    // public String pathTo(String destination, String begin) {
    // int v = mapped.get(destination);
    // int start = mapped.get(begin);
    // String result = "";
    // for (int x = v; x != start; x = mapped.get(edgeTo[x])) {
    // result = x + " " + result;
    // if (!hasPathTo(destination)) {
    // return " ";
    // }
    // }
    // result = start + " " + result;
    // return result;
    // }

    // reverse dfs?

    // keep main method for testing purposes only
    // public static void main(String[] args) {
    // if (args.length < 2) {
    // StdOut.println(
    // "Execute: java -cp bin prereqchecker.SchedulePlan <adjacency list INput file>
    // <schedule plan INput file> <schedule plan OUTput file>");
    // return;
    // }
    // String filename = args[0];
    // AdjList graph = new AdjList(filename);
    // DFS run = new DFS(filename);
    // run.dfs("cs112");
    // String outputFile = args[1];
    // StdOut.setFile(outputFile);
    // run.printEdges(outputFile);
    // }
}
