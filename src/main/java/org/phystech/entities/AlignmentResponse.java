package org.phystech.entities;

import java.util.List;

public class AlignmentResponse {
  private AlignmentQuery queryId;
  private List<AlignmentEntity> alignments;
  private double score;
  
  public List<AlignmentEntity> getAlignments() {
    return alignments;
  }
  
  public void setAlignments(List<AlignmentEntity> alignments) {
    this.alignments = alignments;
  }
  
  public double getScore() {
    return score;
  }
  
  public void setScore(double score) {
    this.score = score;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{ \n").append("\"alignments\": [\n");
    for (AlignmentEntity al : alignments) {
      sb.append("{ \n\"seq1\": ").append(al.getSeq1()).append("\n");
      sb.append(" \"seq2\": ").append(al.getSeq2()).append("\n }");
      if (!al.equals(alignments.get(alignments.size()-1))) {
        sb.append(",\n");
      }
    }
    sb.append("], \n \"score\" : ").append(score).append("\n}");
    return sb.toString();
  }
  
  public AlignmentQuery getQueryId() {
    return queryId;
  }
  
  public void setQueryId(AlignmentQuery queryId) {
    this.queryId = queryId;
  }
}
