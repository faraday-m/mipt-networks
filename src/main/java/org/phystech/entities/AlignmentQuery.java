package org.phystech.entities;

import java.util.Objects;

public class AlignmentQuery {
  private String seq1;
  private String seq2;
  private String matrix;
  private double gapOpen;
  private double gapExtend;
  
  public AlignmentQuery() {
    seq1 = "";
    seq2 = "";
    matrix = "blosum62";
    gapOpen = 15.0;
    gapExtend = 5.0;
  }
  
  public String getSeq1() {
    return seq1;
  }
  
  public void setSeq1(String seq1) {
    this.seq1 = seq1;
  }
  
  public String getSeq2() {
    return seq2;
  }
  
  public void setSeq2(String seq2) {
    this.seq2 = seq2;
  }
  
  public String getMatrix() {
    return matrix;
  }
  
  public void setMatrix(String matrix) {
    this.matrix = matrix;
  }
  
  public double getGapOpen() {
    return gapOpen;
  }
  
  public void setGapOpen(double gapOpen) {
    this.gapOpen = gapOpen;
  }
  
  public double getGapExtend() {
    return gapExtend;
  }
  
  public void setGapExtend(double gapExtend) {
    this.gapExtend = gapExtend;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AlignmentQuery that = (AlignmentQuery) o;
    return Double.compare(that.gapOpen, gapOpen) == 0 && Double.compare(that.gapExtend, gapExtend) == 0 && seq1.equals(that.seq1) && seq2.equals(that.seq2) && matrix.equals(that.matrix);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seq1, seq2, matrix, gapOpen, gapExtend);
  }
}
