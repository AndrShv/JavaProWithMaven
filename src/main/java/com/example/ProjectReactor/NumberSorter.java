package com.example.ProjectReactor;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.persistence.criteria.CriteriaBuilder;
import java.lang.management.MonitorInfo;
import java.util.Arrays;
import java.util.List;

import reactor.core.publisher.Flux;

public class NumberSorter {
    public static Flux<Integer> sortNumbers(Flux<Integer> numbers) {
        return numbers.sort();
    }

    public static void main(String[] args) {
        Flux<Integer> numbers = Flux.just(5, 3, 8, 1, 2);
        sortNumbers(numbers)
                .subscribe(System.out::println);  // Output: 1, 2, 3, 5, 8
    }
}
