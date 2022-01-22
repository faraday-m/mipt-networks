package org.phystech.entities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FileSubstitutionMatrix implements SubstitutionMatrix {
    private HashMap<Character, HashMap<Character,Double>> matrix;

    public FileSubstitutionMatrix(String filename) {
        try {
            matrix = new HashMap<>();
            File file = new File(filename);
            Scanner sc = new Scanner(new FileInputStream(file));
            List<Character> aminos = Arrays.stream(sc.nextLine().strip().split(" .")).map(s -> s.charAt(0)).collect(Collectors.toList());
            for (Character s1: aminos) {
                for (Character s2: aminos) {
                    if (!matrix.containsKey(s1)) {
                        matrix.put(s1, new HashMap<>());
                    }
                        matrix.get(s1).put(s2, (double) sc.nextInt());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Double getValue(Character i, Character j) {
        return matrix.get(i).get(j);
    }
}
