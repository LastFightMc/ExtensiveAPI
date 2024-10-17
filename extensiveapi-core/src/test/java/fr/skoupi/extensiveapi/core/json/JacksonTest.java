package fr.skoupi.extensiveapi.core.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

import java.io.File;

@SuppressWarnings("all")
public class JacksonTest extends IDataSerialisable<Employee> {

    static Employee employee;
    static File employeeFile;
    static ObjectMapper mapper;

    @BeforeAll
    @Order(1)
    static void beforeAll() {
        employeeFile = new File("test-output", "employee.json");
        employee = new Employee("Jimmy", "SKAH", 20, new String[]{"Java", "C++", "C"});
        mapper = new ObjectMapper();
    }

    @DisplayName("Test JSON Serialization")
    @Test
    @Order(2)
    void testSerialization() {
        if (employeeFile.exists()) employeeFile.delete();
        Assertions.assertDoesNotThrow(() -> save(employeeFile, employee, mapper));
        Assertions.assertTrue(employeeFile.exists(), "The file was not created");
    }


    @DisplayName("Test JSON Deserialization")
    @Test
    @Order(3)
     void testDeserialization() {
        employee = null;
        Assertions.assertDoesNotThrow(() -> employee = load(employeeFile, Employee.class, mapper));

        Assertions.assertEquals(employee.getFirstName(), "Jimmy");
        Assertions.assertEquals(employee.getLastName(), "SKAH");
        Assertions.assertEquals(employee.getAge(), 20);
        Assertions.assertEquals(employee.getSkills().length, 3);
        Assertions.assertArrayEquals(employee.getSkills(), new String[]{"Java", "C++", "C"});

    }


}
