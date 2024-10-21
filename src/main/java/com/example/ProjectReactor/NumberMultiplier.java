package com.example.ProjectReactor;

import reactor.core.publisher.Flux;

import javax.persistence.criteria.CriteriaBuilder;

public class NumberMultiplier {
    public static Flux<Integer> numberMyltiplys(Flux<Integer> numbers){
        return numbers.map(num ->num*2 );
    }
    public static void main(String[] args){
        Flux<Integer> num = Flux.just(51,12,36,74,15);
        numberMyltiplys(num).subscribe(System.out::println);
    }
}
