package prereqchecker;

import java.util.*;

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
 * NeedToTakeInputFile name is passed through the command line as args[1]
 * Read from NeedToTakeInputFile with the format:
 * 1. One line, containing a course ID
 * 2. c (int): Number of courses
 * 3. c lines, each with one course ID
 * 
 * Step 3:
 * NeedToTakeOutputFile name is passed through the command line as args[2]
 * Output to NeedToTakeOutputFile with the fformat:
 * 1. Some number of lines, each with one course ID
 */
public class NeedToTake {
    private ArrayList<String> neededClasses = new ArrayList<>();
    private ArrayList<String> takenCourses = new ArrayList<>();
    private String target;
    private Node[] al;
    private DFS dfs;

    public NeedToTake(String filename) {
        StdIn.setFile(filename);
        AdjList vertice = new AdjList(filename);
        vertice.createLists();
        al = vertice.getVertices();
        dfs = new DFS(filename);
    }

    public void determineTakenCourses(String secondFile) {
        StdIn.setFile(secondFile);
        target = StdIn.readLine();
        int numOfTakenCourses = StdIn.readInt();
        StdIn.readLine();
        int i = 0;
        while (i < numOfTakenCourses) {
            while (!StdIn.isEmpty()) {
                String inputCourse = StdIn.readLine();
                takenCourses.add(inputCourse);
                findMoreTakenCourses(inputCourse);
                i++;
            }
        }
    }

    private ArrayList<String> findMoreTakenCourses(String inputCourse) {
        for (int i = 0; i < al.length; i++) {
            Node temp = al[i];
            if (al[i].courseID.equals(inputCourse)) {
                while (temp.next != null) {
                    temp = temp.next;
                    if (!takenCourses.contains(temp.courseID)) {
                        takenCourses.add(temp.courseID);
                    }
                    findMoreTakenCourses(temp.courseID);
                }

            }
        }
        return takenCourses;
    }

    public ArrayList<String> findNeededClasses() {
        return findNeededClasses(target);
    }

    public int findNumberOfNeededClasses(String course) {
        ArrayList<String> temp = findNeededClasses(course);
        return temp.size();
    }

    public ArrayList<String> findNeededClasses(String target) {
        dfs.clearList();
        neededClasses.clear();
        dfs.dfs(target);
        ArrayList<String> path = dfs.printEdges();
        for (int i = 0; i < path.size(); i++) {
            if (!takenCourses.contains(path.get(i))) {
                neededClasses.add(path.get(i));
            }
        }

        return neededClasses;
    }

    public void print(String outputfile) {
        for (int i = 0; i < neededClasses.size(); i++) {
            StdOut.println(neededClasses.get(i));
        }
    }

    public static void main(String[] args) {

        if (args.length < 3) {
            StdOut.println(
                    "Execute: java NeedToTake <adjacency list INput file> <need to take INput file> <need to take OUTput file>");
            return;
        }
        String inputFile = args[0];
        NeedToTake run = new NeedToTake(inputFile);
        String secondFile = args[1];
        run.determineTakenCourses(secondFile);
        run.findNeededClasses();
        String outputFile = args[2];
        StdOut.setFile(outputFile);
        run.print(outputFile);

        // WRITE YOUR CODE HERE
    }
}
