package prereqchecker;

import java.util.ArrayList;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * AdjListInputFile name is passed through the command line as args[0]
 * Read from AdjListInputFile with the format:
 * 1. a (int): number of courses in the graph
 * 2. a lines, each with 1 course ID
 * 3. b (int): number of edges in the graph
 * 4. b lines, each with a source ID
 * 
 * Step 2:
 * ValidPreReqInputFile name is passed through the command line as args[1]
 * Read from ValidPreReqInputFile with the format:
 * 1. 1 line containing the proposed advanced course
 * 2. 1 line containing the proposed prereq to the advanced course
 * 
 * Step 3:
 * ValidPreReqOutputFile name is passed through the command line as args[2]
 * Output to ValidPreReqOutputFile with the format:
 * 1. 1 line, containing either the word "YES" or "NO"
 */
public class ValidPrereq {
    private String firstCourse;
    private String secondCourse;
    private prereqchecker.Node[] al;
    private Hashmap map;
    private DFS findPrereqs;
    private Eligible eligible;
    // private Node vertice;

    // private class Node {
    // private Node next;
    // private String courseID;

    // private Node(Node next, String courseId) {
    // this.next = null;
    // this.courseID = courseId;
    // }
    // }

    public ValidPrereq(String filename) {
        StdIn.setFile(filename);
        AdjList vertice = new AdjList(filename);
        vertice.createLists();
        al = vertice.getVertices();
        map = new Hashmap(filename);
        eligible = new Eligible(filename);
        findPrereqs = new DFS(filename);
    }

    public void readPrereq(String secondFile) { // may need to edit to return array
        StdIn.setFile(secondFile);
        firstCourse = StdIn.readLine();
        secondCourse = StdIn.readLine();
    }

    // need to find more taken courses
    // first call hash
    // adjusted valid prereq input before
    public String checkPrereq(String firstCourse) {
        findPrereqs.clearList();
        findPrereqs.createEdge(firstCourse, secondCourse);
        findPrereqs.dfs(firstCourse);
        if (findPrereqs.returnCycle()) {
            return "NO";
        } else {
            return "YES";
        }
    }

    public void print(String lastFile) {
        StdOut.print(checkPrereq(firstCourse));
    }

    public static void main(String[] args) {

        if (args.length < 3) {
            StdOut.println(
                    "Execute: java -cp bin prereqchecker.ValidPrereq <adjacency list INput file> <valid prereq INput file> <valid prereq OUTput file>");
            return;
        }
        String inputFile = args[0];
        ValidPrereq run = new ValidPrereq(inputFile);
        String secondFile = args[1];
        run.readPrereq(secondFile);
        // run.checkPrereq();
        String outputFile = args[2];
        StdOut.setFile(outputFile);
        run.print(outputFile);

        // WRITE YOUR CODE HERE
    }
}
