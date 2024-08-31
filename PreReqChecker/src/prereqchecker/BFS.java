package prereqchecker;

import java.util.*;

public class BFS {
    private boolean[] isMarked;
    private String[] edgeTo;
    private int[] disTo;
    private Node[] al;
    private Hashmap mapped;

    public BFS(String filename) {
        StdIn.setFile(filename);
        AdjList graph = new AdjList(filename);
        graph.createLists();
        al = graph.getVertices();
        mapped = new Hashmap(filename);
        isMarked = new boolean[al.length];
        edgeTo = new String[al.length];
    }

    // bfs doesn't use recursion
    // isMarked isn't being updated properly in this implementation
    public void bfs(String courseEdge) {
        Queue<String> q = new LinkedList<>();
        q.add(courseEdge);
        int v = mapped.get(courseEdge);
        isMarked[v] = true;
        while (!q.isEmpty()) {
            // need to change courseEdge somehow
            String dq = q.remove(); // need to pop first one off queue
            int x = mapped.get(dq);
            Node temp = al[x].next;
            while (temp != null) {
                q.add(temp.courseID);
                int w = mapped.get(temp.courseID);
                edgeTo[w] = dq;
                disTo[w] = disTo[x] + 1;
                temp = temp.next;
            }
        }
    }

    // public static void main(String[] args) {

    // }
}
