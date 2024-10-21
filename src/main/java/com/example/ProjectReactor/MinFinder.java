package com.example.ProjectReactor;

import reactor.core.publisher.Flux;

public class MinFinder {
    public static Flux<Integer> findMin(Flux<Integer> numbers) {
        return numbers.reduce(Integer::min)
                .flux();  // Преоб�уем Mono в Flux для единооб�ия
    }
    public static void main(String[] args1){
        Flux<Integer> numbers = Flux.just(10, 35, 23, 89, 9);
        findMin(numbers).subscribe(System.out::println);
    }
}
