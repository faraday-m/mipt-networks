package org.phystech.services;

import org.phystech.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlignmentService {
  Map<String, SubstitutionMatrix> matrices;

  @Autowired
  CacheManager cacheManager;

  public AlignmentService() {
    this.matrices = new HashMap<>();
    matrices.put("blosum62", new FileSubstitutionMatrix("blosum62.txt"));
  }

  @Cacheable(cacheNames = "AlignmentCache")
  public AlignmentResponse doAlignments(AlignmentQuery query) {
    SubstitutionMatrix alignMatrix = matrices.get(query.getMatrix());
    String seq1 = query.getSeq1();
    String seq2 = query.getSeq2();
    AlignmentResponse alignmentResponse = new AlignmentResponse();
    Aligner aligner = new Aligner(seq1, seq2, query.getGapOpen(), query.getGapExtend(), alignMatrix);
    List<AlignmentEntity> alignments = aligner.calculateAlignments(alignmentResponse);
    alignmentResponse.setAlignments(alignments);
    if (alignments.isEmpty()) {
      alignmentResponse.setScore(0);
    }
    else {
      alignmentResponse.setScore(alignments.get(0).getScore());
    }
    alignmentResponse.setQueryId(query);
    return alignmentResponse;
  }
  
}
