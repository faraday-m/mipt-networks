package org.phystech.entities;

public class AlignmentEntity {
  private String seq1;
  private String seq2;
  private double score;
  
  public double getScore() {
    return score;
  }
  
  public void setScore(double score) {
    this.score = score;
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
  
  
  public AlignmentEntity(Alignment al, AlignmentResponse resp) {
    this.seq1 = al.getSeq1();
    this.seq2 = al.getSeq2();
    this.score = al.getScore();
  }
  
  public AlignmentEntity() {}
  
  
}
