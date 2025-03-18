package com.example.ProjectReactor;

import reactor.core.publisher.Flux;

public class MaxFinder {
    public static Flux<Integer> findMax(Flux<Integer> numbers) {
        return numbers.reduce(Integer::max)
                .flux();  // Преобразуем Mono в Flux для единообразия
    }

    public static void main(String[] args) {
        Flux<Integer> numbers = Flux.just(10, 35, 23, 89, 4);

        findMax(numbers)
                .subscribe(System.out::println);  // Output: 89
    }
}
