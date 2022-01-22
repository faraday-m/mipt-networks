package org.phystech.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Aligner {
  static final boolean PRINT_ALIGN_MATRIX = true;
  static final boolean PRINT_ALIGNMENT = true;
  String seq1;
  String seq2;
  int len1, len2;
  double openPenalty, extendPenalty;
  SubstitutionMatrix subMatrix;
  ArrayList<ArrayList<DirectedDouble>> alignMatrix = new ArrayList<>();
  
  ArrayList<Alignment> temp_alignments = new ArrayList<>();
  ArrayList<AlignmentEntity> alignments = new ArrayList<>();
  double max_score;
  
  public class DirectedDouble {
  
    public static final int LEFT_DIRECTION = 1;
    public static final int TOP_DIRECTION = 2;
    public static final int DIAG_DIRECTION = 4;
    public double score;
    public byte direction;
    public int[] position;
    public double horGap;
    public double vertGap;
  
    DirectedDouble(double score) {
      this.score = score;
      horGap = 0;
      vertGap = 0;
      direction = 0;
    }
  }
  
  public Aligner(String seq1, String seq2, double open, double extend, SubstitutionMatrix subMatrix)
  {
    
    this.seq1 = seq1;
    this.seq2 = seq2;
    this.subMatrix = subMatrix;
    len1 = seq1.length();
    len2 = seq2.length();
    openPenalty = open;
    extendPenalty = extend;
  }
  
  public ArrayList<AlignmentEntity> getAlignments() {
    return alignments;
  }
  
  //25 ms
  public double generateAlignMatrix() {
    double max_score = 0;
    for (int i = 0; i <= len1; i++) {
      alignMatrix.add(new ArrayList<>());
      ArrayList<DirectedDouble> column = alignMatrix.get(alignMatrix.size()-1);
      if (i == 0) {
        initFirstColumn(i, column);
      }
      else {
        for (int j = 0; j <= len2; j++) {
          double max_pt = 0;
          if (j == 0) {
            initWithZero(i, column, j);
          }
          else {
            HashMap<List<Integer>, Double> points = new HashMap<>();
            double diag = (alignMatrix.get(i-1).get(j-1).score +
                subMatrix.getValue(seq1.charAt(i-1), seq2.charAt(j-1)));
            if (diag > max_pt) {
              max_pt = setAlignmentPoint(diag, i-1, j - 1, points);
            }
            max_pt = checkGap(j, max_pt, points, alignMatrix.get(i - 1).get(j).horGap, alignMatrix.get(i - 1), i - 1);
            max_pt = checkGap(j-1, max_pt, points, column.get(j-1).vertGap, column, i);
            // < 1 mcs
            DirectedDouble ptr = new DirectedDouble(max_pt);
            if (max_pt > max_score) {
              max_score = max_pt;
            }
            ptr.position = new int[]{i, j};
            setDirections(i, j, max_pt, points, ptr);
            column.add(ptr);
          }
          if (PRINT_ALIGN_MATRIX) {
            System.out.printf("%6.1f ", column.get(j).score);
          }
          
        }
        
      }
      if (PRINT_ALIGN_MATRIX) {
        System.out.println();
      }
    }
    return max_score;
  }

  private void initWithZero(int i, ArrayList<DirectedDouble> column, int j) {
    DirectedDouble pt = new DirectedDouble(0.0);
    pt.position = new int[]{i, j};
    column.add(pt);
  }

  private void setDirections(int i, int j, double max_pt, HashMap<List<Integer>, Double> points, DirectedDouble ptr) {
    byte direction = 0;
    boolean isHorGapped = false;
    boolean isVertGapped = false;
    for (List<Integer> pos : points.keySet()) {
      if (points.get(pos) == max_pt) {
        if ((pos.get(0) == i - 1) && (pos.get(1) == j - 1)) {
          direction += DirectedDouble.DIAG_DIRECTION;
        }
        if ((pos.get(0) == i) && (pos.get(1) == j - 1)) {
          direction += DirectedDouble.TOP_DIRECTION;
          isVertGapped = true;
        }
        if ((pos.get(0) == i - 1) && (pos.get(1) == j)) {
          direction += DirectedDouble.LEFT_DIRECTION;
          isHorGapped = true;
        }
      }
    }
    ptr.direction = direction;
    if (isHorGapped) {
      ptr.horGap = alignMatrix.get(i -1).get(j).horGap + 1;
    }
    else {
      ptr.horGap = 0;
    }

    if (isVertGapped) {
      ptr.vertGap = alignMatrix.get(i).get(j -1).horGap + 1;
    }
    else {
      ptr.vertGap = 0;
    }
  }

  private double checkGap(int j, double max_pt, HashMap<List<Integer>, Double> points, double horGap, ArrayList<DirectedDouble> directedDoubles, int i) {
    double score;
    if (horGap == 0) {
      score = directedDoubles.get(j).score - openPenalty;
      if (score >= max_pt) {
        max_pt = setAlignmentPoint(score, i, j, points);
      }
    } else {
      score = directedDoubles.get(j).score - extendPenalty;
      if (score >= max_pt) {
        max_pt = setAlignmentPoint(score, i, j, points);
      }
    }
    return max_pt;
  }

  private double setAlignmentPoint(double score, int i, int j, HashMap<List<Integer>, Double> points) {
    double max_pt = score;
    List<Integer> pt = new ArrayList<>();
    pt.add(i);
    pt.add(j);
    points.put(pt, max_pt);
    return max_pt;
  }

  private void initFirstColumn(int i, ArrayList<DirectedDouble> column) {
    for (int j = 0; j <= len2; j++) {
      initWithZero(i, column, j);
      if (PRINT_ALIGN_MATRIX) {
        System.out.printf("%6.1f ", column.get(j).score);
      }
    }
  }

  void printAlignments(DirectedDouble pt, Alignment al) {
    if (pt.score == 0) {
      StringBuilder s1 = new StringBuilder(al.getSeq1());
      StringBuilder s2 = new StringBuilder(al.getSeq2());
      s1.reverse();
      s2.reverse();
      al.setSeq1(s1.toString());
      al.setSeq2(s2.toString());
  
      temp_alignments.add(al);
      if (PRINT_ALIGNMENT) {
        System.out.printf("len: %d\n", al.getSeq1().length());
        System.out.printf("%s\n", al.getSeq1());
        System.out.printf("%s\n", al.getSeq2());
      }
      
    }
    else {
      int pos1 = pt.position[0];
      int pos2 = pt.position[1];
      char i = seq1.charAt(pos1 - 1);
      char j = seq2.charAt(pos2 - 1);
      if ((pt.direction & DirectedDouble.DIAG_DIRECTION) != 0) {
        addSymbol(al, i, j, pos1 - 1, pos2 - 1);
      }
      if ((pt.direction & DirectedDouble.LEFT_DIRECTION) != 0) {
        addSymbol(al, i, '-', pos1 - 1, pos2);
      }
      if ((pt.direction & DirectedDouble.TOP_DIRECTION) != 0) {
        addSymbol(al, '-', j, pos1, pos2 - 1);
      }
    }
    
  }

  private void addSymbol(Alignment al, char i, char j, int i2, int i3) {
    Alignment new_al = new Alignment(al);
    new_al.setSeq1(new_al.getSeq1().concat(Character.toString(i)));
    new_al.setSeq2(new_al.getSeq2().concat(Character.toString(j)));
    DirectedDouble ptr = alignMatrix.get(i2).get(i3);
    printAlignments(ptr, new_al);
  }

  // <1 ms
  public List<AlignmentEntity> calculateAlignments(AlignmentResponse alignmentResponse) {
    max_score = generateAlignMatrix();
    for (int i = 0; i <= len1; i++) {
      for (int j = 0; j <= len2; j++) {
        if (alignMatrix.get(i).get(j).score == max_score) {
          System.out.printf("=== Alignment for point %d %d ===\n", i, j);
          Alignment al = new Alignment("","",max_score);
          printAlignments(alignMatrix.get(i).get(j), al);
        }
      }
    }
    
    for (Alignment al : temp_alignments) {
      AlignmentEntity entity = new AlignmentEntity(al, alignmentResponse);
      alignments.add(entity);
    }
    return alignments;
  }
}
