package org.phystech.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GetSequenceService {
    @Autowired
    WebClient webClient;

    public Mono<String>  getSequenceById(String id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("uniprot/{id}.fasta")
                        .build(id))
                .retrieve()
                .bodyToMono(String.class);
    }
}
