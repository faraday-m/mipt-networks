package org.phystech.parser;

import reactor.core.publisher.Mono;

public interface Parser {
    public String parseSeq(Mono<String> input);
}
