package fr.skoupi.extensiveapi.core.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

@SuppressWarnings("all")
public class JacksonTest extends IDataSerialisable<Employee> {

    static Employee employee;
    static File employeeFile;
    static ObjectMapper mapper;

    @BeforeAll
    static void beforeAll() {
        employeeFile = new File(JacksonTest.class.getResource("").getPath(), "employee.json");
        employee = new Employee("Jimmy", "SKAH", 19, new String[]{"Java", "C++", "C#"});
        mapper = new ObjectMapper();
    }

 /*   @DisplayName("Test JSON Serialization")
    @Test
    void testSerialization() {
        if (employeeFile.exists()) employeeFile.delete();
        Assertions.assertDoesNotThrow(() -> save(employeeFile, employee, mapper));
    }

  */

  /*  @DisplayName("Test JSON Deserialization")
    @Test
    void testDeserialization() {
        employee = null;
        Assertions.assertDoesNotThrow(() -> employee = load(employeeFile, Employee.class, mapper));

        Assertions.assertEquals(employee.getFirstName(), "Jimmy");
        Assertions.assertEquals(employee.getLastName(), "SKAH");
        Assertions.assertEquals(employee.getAge(), 19);
        Assertions.assertEquals(employee.getSkills().length, 3);
        Assertions.assertArrayEquals(employee.getSkills(), new String[]{"Java", "C++", "C#"});

    }

   */


}
