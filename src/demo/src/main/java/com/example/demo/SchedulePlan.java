package com.example.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
 * SchedulePlanInputFile name is passed through the command line as args[1]
 * Read from SchedulePlanInputFile with the format:
 * 1. One line containing a course ID
 * 2. c (int): number of courses
 * 3. c lines, each with one course ID
 * 
 * Step 3:
 * SchedulePlanOutputFile name is passed through the command line as args[2]
 * Output to SchedulePlanOutputFile with the format:
 * 1. One line containing an int c, the number of semesters required to take the
 * course
 * 2. c lines, each with space separated course ID's
 */
import java.util.*;

public class SchedulePlan {
    private Node[] al;
    private ArrayList<String> takenCourses = new ArrayList<>();
    private ArrayList<String> neededCourses = new ArrayList<>();
    private ArrayList<Integer> print = new ArrayList<>();
    // private Node[] semesterPlan;
    private ArrayList<Node> semesterPlan = new ArrayList<>();
    private String target;
    private int semesterCounter;
    private Eligible getLength;
    private int iterations = 0;
    private DFS dfs;
    private HashMap<String, Integer> numberOfPrereqs = new HashMap<>();
    private FileWriter writer;

    public SchedulePlan(String filename) throws FileNotFoundException {
        AdjList vertice = new AdjList(filename);
        vertice.createLists();
        al = vertice.getVertices();
        getLength = new Eligible(filename);
        dfs = new DFS(filename);
    }

    public void readPlanner(String secondFile) throws FileNotFoundException {
        File file = new File(secondFile);
        Scanner in = new Scanner(file);
        target = in.nextLine();
        int numberOfClassesTaken = in.nextInt();
        in.nextLine();
        int i = 0;
        while (in.hasNextLine()) {
            String takenCourse = in.nextLine();
            ArrayList<String> add = getLength.findMoreTakenCourses(takenCourse);
            takenCourses.add(takenCourse);
            for (int j = 0; j < add.size(); j++) {
                takenCourses.add(add.get(j));
            }

        }
        in.close();
    }

    public ArrayList<Integer> determineOrder() {
        dfs.clearList();
        dfs.dfs(target);
        ArrayList<Integer> prereqCounter = new ArrayList<>();
        neededCourses = dfs.printEdges();
        ArrayList<String> parse = new ArrayList<>();
        for (int i = 0; i < neededCourses.size(); i++) {
            parse.add(neededCourses.get(i));
            if (takenCourses.contains(neededCourses.get(i))) {
                parse.remove(neededCourses.get(i));
            }

        }
        dfs.clearList();

        // need to copy all elements of arraylist so that it doesnt clear
        for (int i = 0; i < parse.size(); i++) {
            ArrayList<String> toBeRemoved = new ArrayList<>();
            dfs.dfs(parse.get(i));
            toBeRemoved = dfs.printEdges();
            // need to determine total number of taken courses and check if it contains any
            // in toBeRemoved => if so, remove
            int size = toBeRemoved.size();
            for (int j = 0; j < toBeRemoved.size(); j++) {
                if (takenCourses.contains(toBeRemoved.get(j))) {
                    size--;
                }
            }
            prereqCounter.add(size);
            numberOfPrereqs.put(parse.get(i), size);
            dfs.clearList();

        }
        return prereqCounter;
    }

    public ArrayList<Integer> insertionSort() { // may need to change later
        ArrayList<Integer> sort = determineOrder();
        for (int i = 0; i < sort.size(); i++) {
            for (int j = i; j > 0; j--) {
                if (sort.get(j).compareTo(sort.get(i)) > 0) { // only swaps once
                    int temp = sort.get(j);
                    sort.set(j, sort.get(i));
                    sort.set(i, temp);
                    i = i - 1;
                }
            }
        }
        return sort;
    }

    private ArrayList<String> removeTakenCoursesFromDFS(String course) {
        dfs.clearList();
        dfs.dfs(course);
        ArrayList<String> need = dfs.printEdges();
        for (int j = 0; j < takenCourses.size(); j++) {
            if (need.contains(takenCourses.get(j))) {
                need.remove(takenCourses.get(j));
                // remove.add(need.get(j));
            }
        }
        // for(int l = 0; l < re)

        return need;
    }

    private void removeZeroPrereqs() {
        Iterator<Integer> it = print.iterator();
        while (it.hasNext()) {
            if (it.next() == 0) {
                it.remove();
            }
        }

    }

    public void setOutputFile(String output) throws IOException {
        writer = new FileWriter(output);
    }

    public void printSchedule() throws IOException {
        print = insertionSort();
        ArrayList<String> need = removeTakenCoursesFromDFS(target);
        Iterator<String> it = need.iterator();
        while (it.hasNext()) {
            String course = it.next();
            if (numberOfPrereqs.get(course) == 0) {
                writer.write(course + " ");
                takenCourses.add(course);
                numberOfPrereqs.remove(course);
                it.remove();
            }
        }
        semesterCounter++;
        writer.write("\n");
        if (need.isEmpty()) {
            writer.write(semesterCounter + " semesters");
            writer.close();
            return;
        } else {
            removeZeroPrereqs();
            updateList(target);
        }

    }

    private int findPrintIndex(String course) {
        int currentIndex = numberOfPrereqs.get(course);
        for (int j = 0; j < print.size(); j++) {
            if (print.get(j) == currentIndex) {
                return j;
            }
        }
        return 0;
    }

    // updates number of prereqs as you take classes, making them smaller
    private void updateList(String course) throws IOException {
        ArrayList<String> dfsForCourse = removeTakenCoursesFromDFS(course);
        if (dfsForCourse.isEmpty()) {
            return;
        }
        ArrayList<String> iterate = new ArrayList<>();
        iterate.addAll(dfsForCourse);
        // problem is dfsForCourse gets
        for (int i = 0; i < iterate.size(); i++) {
            String remove = iterate.get(i);
            ArrayList<String> toBeRemoved = removeTakenCoursesFromDFS(remove);
            int targetIndex = findPrintIndex(remove);
            numberOfPrereqs.replace(remove, toBeRemoved.size());
            print.set(targetIndex, toBeRemoved.size()); // problematic error, not
        }
        printSchedule();

    }

}
