package org.phystech.controllers;

import org.phystech.entities.AlignmentIdQuery;
import org.phystech.parser.Parser;
import org.phystech.services.AlignmentService;
import org.phystech.entities.AlignmentQuery;
import org.phystech.entities.AlignmentResponse;
import org.phystech.services.GetSequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AlignController {
  @Autowired
  CacheManager cacheManager;
  @Autowired
  AlignmentService alignmentService;
  @Autowired
  GetSequenceService getSequenceService;
  @Autowired
  Parser parser;
  
  @GetMapping(value = "/align", consumes = "application/json")
  public ResponseEntity<AlignmentResponse> alignSequences(@RequestBody AlignmentQuery jsonRequest) {
    try {
      AlignmentResponse resp = alignmentService.doAlignments(jsonRequest);
      return new ResponseEntity<>(resp, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(new AlignmentResponse(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(value = "/alignIds", consumes = "application/json")
  public ResponseEntity<AlignmentResponse> alignSequencesByIds(@RequestBody AlignmentIdQuery jsonRequest) {
    try {
      List<String> seqs = new ArrayList<>();
      Flux.just(jsonRequest.getId1(), jsonRequest.getId2())
                  .map(getSequenceService::getSequenceById)
                  .map(parser::parseSeq)
                  .subscribe(seqs::add);
      AlignmentQuery query = jsonRequest.createAlignmentQuery(seqs.get(0), seqs.get(1));
      AlignmentResponse resp = alignmentService.doAlignments(query);
      return new ResponseEntity<>(resp, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(new AlignmentResponse(), HttpStatus.BAD_REQUEST);
    }
  }
}
