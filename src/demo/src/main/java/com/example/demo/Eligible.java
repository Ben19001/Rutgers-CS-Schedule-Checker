package com.example.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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

    public Eligible(String filename) throws FileNotFoundException {
        AdjList vertice = new AdjList(filename);
        vertice.createLists();
        al = vertice.getVertices();
    }

    public void determineTakenCourses(String secondFile) throws FileNotFoundException {
        File file = new File(secondFile);
        Scanner in = new Scanner(file);
        in.nextLine();
        while (in.hasNextLine()) {
            String inputCourse = in.nextLine();
            takenCourses.add(inputCourse);
            findMoreTakenCourses(inputCourse);
        }
        in.close();
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
        File output = new File(outputFile);
        try( FileWriter writer = new FileWriter(output)) {
            for (int i = 0; i < eligibleCourses.size(); i++) {
                writer.write(eligibleCourses.get(i) + '\n');
            }
        } catch (IOException e){
            e.printStackTrace();
        }
       
    }

}
