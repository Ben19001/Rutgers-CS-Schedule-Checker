package com.example.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

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
    private Scanner in;

    // edit this => need to populate each element with a linked list
    // need to add first 29 lines here
    public AdjList(String filename) throws FileNotFoundException {
        File file = new File(filename);
        in = new Scanner(file);
        vertices = in.nextInt();
        in.nextLine();
        al = new Node[vertices];
        for (int i = 0; i < al.length; i++) {
            String courseId = in.nextLine();
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
        int counter = in.nextInt();
        int i = 0;
        while (i < counter) {
            in.nextLine();
            String index = in.next();
            String nextNode = in.next();
            for (int j = 0; j < al.length; j++) {
                if (al[j].courseID.equals(index)) {
                    Node current = al[j];
                    while (current.next != null) {
                        current = current.next;
                    } 
                    current.next = new Node(null, nextNode);
                }
            }
            i++;         
        }
        in.close();
    }

    //strictly for testing purposes
    public void print(String output) {
        File two = new File(output);
        try(FileWriter writer = new FileWriter(two)) {
            for (int i = 0; i < al.length; i++) {
                Node temp = al[i];
                while (temp != null) {
                    writer.write(temp.courseID + " ");
                    temp = temp.next;
                }
                writer.write('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    
    }

}
