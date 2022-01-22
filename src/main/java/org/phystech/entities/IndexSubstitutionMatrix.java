package org.phystech.entities;

import java.util.HashMap;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class IndexSubstitutionMatrix implements SubstitutionMatrix {
  
  private HashMap<Character,HashMap<Character,Double>> matrix;
  private HashMap<Character,Double> indexSystem;
  private double offset;
  private double std = 0;
  private double avg = 0;
  
  private IndexSubstitutionMatrix() {}
  
  public IndexSubstitutionMatrix(HashMap<String,Double> indexSystem, double offset) {
    this.indexSystem = new HashMap<>();
    for (String s : indexSystem.keySet()) {
      this.indexSystem.put(s.charAt(0), indexSystem.get(s));
    }
    this.offset = offset;
    generateMatrix();
    setOffset();
    //printSubMatrix();
  }
  
  private void generateMatrix() {
    matrix = new HashMap<>();
    for (Character i : indexSystem.keySet()) {
      matrix.put(i, new HashMap<>());
      for (Character j : indexSystem.keySet()) {
        double value = abs(indexSystem.get(i) - indexSystem.get(j));
        matrix.get(i).put(j, value);
        avg += value;
      }
    }
    double n = Math.pow(indexSystem.keySet().size(), 2);
    
    avg = avg / n;
    
    for (Character i : indexSystem.keySet()) {
      for (Character j : indexSystem.keySet()) {
        double value = avg - matrix.get(i).get(j);
        std += pow(value,2) / (n - 1);
        matrix.get(i).replace(j, value);
      }
    }
    for (Character i : indexSystem.keySet()) {
      for (Character j : indexSystem.keySet()) {
        double value = matrix.get(i).get(j);
        matrix.get(i).replace(j, value / std);
      }
    }
  }
  
  private void setOffset() {
    for (Character i : matrix.keySet()) {
      for (Character j : matrix.keySet()) {
        double value = matrix.get(i).get(j);
        matrix.get(i).replace(j, value - std * offset);
      }
    }
  }
  
  public Double getValue(Character i, Character j) {
    return matrix.get(i).get(j);
  }
  
  public Double getValue(String i, String j) {
    return matrix.get(i.charAt(0)).get(j.charAt(0));
  }
  
  
  public void printSubMatrix() {
    System.out.printf("*   ");
    for (Character i : matrix.keySet()) {
      System.out.printf("%-5s", i);
    }
    System.out.printf("\n");
    for (Character i : matrix. keySet()) {
      System.out.printf("%s ", i);
      for (Character j : matrix.keySet()) {
        System.out.printf("%4.1f ", this.getValue(i,j));
      }
      System.out.printf("\n");
    }
  }
  
  
}
