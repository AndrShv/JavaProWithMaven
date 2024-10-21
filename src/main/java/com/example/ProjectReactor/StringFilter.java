package com.example.ProjectReactor;

import reactor.core.publisher.Flux;

public class StringFilter {
    public static Flux<String> filterStringsByLength(Flux<String> strings) {
        return strings.filter(str -> str.length() > 5);
    }

    public static void main(String[] args) {
        Flux<String> stringFlux = Flux.just("apple", "banana", "pear", "watermelon", "kiwi");
        filterStringsByLength(stringFlux)
                .subscribe(System.out::println);  // Output: banana, watermelon
    }
}
