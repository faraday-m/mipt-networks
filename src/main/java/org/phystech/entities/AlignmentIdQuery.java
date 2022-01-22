package org.phystech.entities;

public class AlignmentIdQuery {
    private String id1;
    private String id2;
    private String matrix;
    private double gapOpen;
    private double gapExtend;

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
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

    public AlignmentQuery createAlignmentQuery(String seq1, String seq2) {
        AlignmentQuery query = new AlignmentQuery();
        query.setSeq1(seq1);
        query.setSeq2(seq2);
        query.setGapExtend(getGapExtend());
        query.setGapOpen(getGapOpen());
        query.setMatrix(getMatrix());
        return query;
    }
}
