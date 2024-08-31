package com.example.demo;

import java.io.FileNotFoundException;
import java.util.*;

public class Hashmap {
    public HashMap<String, Integer> map = new HashMap<>();
    private Node[] al;

    // take as an
    public Hashmap(String filename) throws FileNotFoundException {
        AdjList graph = new AdjList(filename);
        al = graph.getVertices();
        for (int i = 0; i < al.length; i++) {
            map.put(al[i].courseID, i);
        }
    }

    public int get(String course) {
        return map.get(course);
    }

}
