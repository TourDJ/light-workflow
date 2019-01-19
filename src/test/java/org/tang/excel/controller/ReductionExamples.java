package org.tang.excel.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReductionExamples {

    public static void main(String... args) {
        
        // Create sample data

        List<Person> roster = Person.createRoster();
    
        System.out.println("Contents of roster:");
        
        roster
            .stream()
            .forEach(p -> p.printPerson());

        System.out.println();

        // 1. Average age of male members, average operation
        
        double average = roster
            .stream()
            .filter(p -> p.getGender() == Person.Sex.MALE)
            .mapToInt(Person::getAge)
            .average()
            .getAsDouble();
            
        System.out.println("Average age (bulk data operations): " +
            average);
        
        // 2. Sum of ages with sum operation
        
        Integer totalAge = roster
            .stream()
            .mapToInt(Person::getAge)
            .sum();
            
        System.out.println("Sum of ages (sum operation): " +
            totalAge);
        
        // 3. Sum of ages with reduce(identity, accumulator)
        
        Integer totalAgeReduce = roster
            .stream()
            .map(Person::getAge)
            .reduce(
                0,
                (a, b) -> a + b);
            
        System.out.println(
            "Sum of ages with reduce(identity, accumulator): " +
            totalAgeReduce);
         
        // 4. Average of male members with collect operation
         
        Averager averageCollect = roster.stream()
            .filter(p -> p.getGender() == Person.Sex.MALE)
            .map(Person::getAge)
            .collect(Averager::new, Averager::accept, Averager::combine);
                   
        System.out.println("Average age of male members: " +
            averageCollect.average());
         
        // 5. Names of male members with collect operation

        System.out.println("Names of male members with collect operation: ");         
        List<String> namesOfMaleMembersCollect = roster
            .stream()
            .filter(p -> p.getGender() == Person.Sex.MALE)
            .map(p -> p.getName())
            .collect(Collectors.toList());         

        namesOfMaleMembersCollect
            .stream()
            .forEach(p -> System.out.println(p));
             
        // 6. Group members by gender
         
        System.out.println("Members by gender:");
        Map<Person.Sex, List<Person>> byGender =
            roster
                .stream()
                .collect(
                    Collectors.groupingBy(Person::getGender));
                
        List<Map.Entry<Person.Sex, List<Person>>>
            byGenderList = 
            new ArrayList<>(byGender.entrySet());
            
        byGenderList
            .stream()
            .forEach(e -> {
                System.out.println("Gender: " + e.getKey());
                e.getValue()
                    .stream()
                    .map(Person::getName)
                    .forEach(f -> System.out.println(f)); });
         
        // 7. Group names by gender
         
        System.out.println("Names by gender:");
        Map<Person.Sex, List<String>> namesByGender =
            roster
                .stream()
                .collect(
                     Collectors.groupingBy(
                         Person::getGender,                      
                         Collectors.mapping(
                             Person::getName,
                             Collectors.toList())));
                     
        List<Map.Entry<Person.Sex, List<String>>>
            namesByGenderList = 
                new ArrayList<>(namesByGender.entrySet());
                     
        namesByGenderList
            .stream()
            .forEach(e -> {
                System.out.println("Gender: " + e.getKey());
                e.getValue()
                    .stream()
                    .forEach(f -> System.out.println(f)); });
         
        // 8. Total age by gender
        
        System.out.println("Total age by gender:");
        Map<Person.Sex, Integer> totalAgeByGender =
            roster
                .stream()
                .collect(
                     Collectors.groupingBy(
                         Person::getGender,                      
                         Collectors.reducing(
                             0,
                             Person::getAge,
                             Integer::sum)));
                
        List<Map.Entry<Person.Sex, Integer>>
            totalAgeByGenderList = 
            new ArrayList<>(totalAgeByGender.entrySet());
                     
        totalAgeByGenderList
            .stream()
            .forEach(e -> 
                System.out.println("Gender: " + e.getKey() +
                    ", Total Age: " + e.getValue()));
             
        // 9. Average age by gender
         
        System.out.println("Average age by gender:");
        Map<Person.Sex, Double> averageAgeByGender =
            roster
                .stream()
                .collect(
                     Collectors.groupingBy(
                         Person::getGender,                      
                         Collectors.averagingInt(Person::getAge)));
                 
        for (Map.Entry<Person.Sex, Double> e : averageAgeByGender.entrySet()) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }
    }
}
