package prereqchecker;

import java.util.*;
import java.util.ArrayList;

/**
 * 
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
 * EligibleInputFile name is passed through the command line as args[1]
 * Read from EligibleInputFile with the format:
 * 1. c (int): Number of courses
 * 2. c lines, each with 1 course ID
 * 
 * Step 3:
 * EligibleOutputFile name is passed through the command line as args[2]
 * Output to EligibleOutputFile with the format:
 * 1. Some number of lines, each with one course ID
 */
public class Eligible {
    private ArrayList<String> eligibleCourses = new ArrayList<>();
    private Node[] al;
    // private String[] takenCourses;
    private ArrayList<String> takenCourses = new ArrayList<>();
    private DFS dfs;

    public Eligible(String filename) {
        StdIn.setFile(filename);
        AdjList vertice = new AdjList(filename);
        vertice.createLists();
        al = vertice.getVertices();
        dfs = new DFS(filename);
    }

    public void determineTakenCourses(String secondFile) {
        StdIn.setFile(secondFile);
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

    public ArrayList<String> findMoreTakenCourses(String inputCourse) {
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

    public void determineEligibeCourses() {
        for (int i = 0; i < al.length; i++) {
            int counter = 0;
            if (!takenCourses.contains(al[i].courseID)) {
                // need to check if direct prereqs are in takencourses
                int prereqLength = getLengthOfChain(al[i]);
                Node parse = al[i].next;
                while (parse != null) {
                    if (takenCourses.contains(parse.courseID)) {
                        counter++;
                    }
                    parse = parse.next;
                }
                if (prereqLength == counter) {
                    eligibleCourses.add(al[i].courseID);
                }
            }
        }
    }

    public int getLengthOfChain(Node node) {
        int counter = 0;
        for (int i = 0; i < al.length; i++) {
            if (al[i].equals(node)) {
                Node temp = al[i];
                while (temp != null) {
                    counter++;
                    temp = temp.next;
                }
            }
        }
        return counter - 1;

    }

    public void print(String outputFile) {
        for (int i = 0; i < eligibleCourses.size(); i++) {
            StdOut.println(eligibleCourses.get(i));
        }
    }

    public static void main(String[] args) {

        if (args.length < 3) {
            StdOut.println(
                    "Execute: java -cp bin prereqchecker.Eligible <adjacency list INput file> <eligible INput file> <eligible OUTput file>");
            return;
        }
        String inputFile = args[0];
        Eligible run = new Eligible(inputFile);
        String secondFile = args[1];
        run.determineTakenCourses(secondFile);
        run.determineEligibeCourses();
        String outputFile = args[2];
        StdOut.setFile(outputFile);
        run.print(outputFile);

        // WRITE YOUR CODE HERE
    }
}
