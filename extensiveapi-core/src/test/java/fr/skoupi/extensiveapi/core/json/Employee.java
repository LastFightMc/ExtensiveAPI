package fr.skoupi.extensiveapi.core.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Employee {

    private String firstName;
    private String lastName;

    private int age;

    private String[] skills;



}
