package prereqchecker;

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
 * AdjListOutputFile name is passed through the command line as args[1]
 * Output to AdjListOutputFile with the format:
 * 1. c lines, each starting with a different course ID, then
 * listing all of that course's prerequisites (space separated)
 */
public class AdjList {
    private Node[] al;
    private int vertices;

    // edit later
    // private class Node {
    // private Node next;
    // private String courseID;            

    // private Node(Node next, String courseId) {
    // this.next = null;
    // this.courseID = courseId;
    // }
    // }

    // edit this => need to populate each element with a linked list
    // need to add first 29 lines here
    public AdjList(String filename) {
        StdIn.setFile(filename);
        vertices = StdIn.readInt();
        StdIn.readLine();
        al = new Node[vertices];
        for (int i = 0; i < al.length; i++) {
            String courseId = StdIn.readLine();
            al[i] = new Node(al[i], courseId);
        }

    }

    public void setVertices(Node[] al) {
        this.al = al;
    }

    public Node[] getVertices() {
        return al;
    }

    // need to create linked lists now
    public void createLists() {
        StdIn.readInt();
        while (!StdIn.isEmpty()) {
            String index = StdIn.readString();
            String nextNode = StdIn.readString();
            for (int i = 0; i < al.length; i++) {
                if (al[i].courseID.equals(index)) {
                    Node current = al[i];
                    while (current.next != null) {
                        current = current.next;
                    } //parses to end of LL to insert new node
                    current.next = new Node(null, nextNode);
                }
            }                                                
            StdIn.readLine();
            // StdIn.readLine();
        }
    }

    public void print(String file) {
        // createLists();
        for (int i = 0; i < al.length; i++) {
            Node temp = al[i];
            while (temp != null) {
                StdOut.print(temp.courseID + " ");
                temp = temp.next;
            }
            StdOut.println();

        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            StdOut.println(
                    "Execute: java -cp bin prereqchecker.AdjList <adjacency list INput file> <adjacency list OUTput file>");
            return;
        }
        String inputFile = args[0];
        AdjList run = new AdjList(inputFile);
        run.createLists();
        String outputFile = args[1];
        StdOut.setFile(outputFile);
        run.print(outputFile);
        // WRITE YOUR CODE HERE
    }
}
