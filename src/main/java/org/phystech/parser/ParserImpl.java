package org.phystech.parser;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;


@Component
public class ParserImpl implements Parser {
    @Override
    public String parseSeq(Mono<String> input) {
        String inputString = input.block();
        if (inputString == null) return "";
        StringBuilder result = new StringBuilder();
        String[] splitInput = inputString.split("\n");
        for (int i = 1; i < splitInput.length; i++) {
            result.append(splitInput[i]);
        }
        return result.toString();
    }
}
