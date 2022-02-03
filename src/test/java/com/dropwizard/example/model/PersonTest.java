package com.dropwizard.example.model;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The Person test class.
 */
public class PersonTest {

    private Person person;

    @BeforeEach
    public void setUp(){
        person = new Person();
        person.setId(1L);
        person.setFirstName("firstname1");
        person.setLastName("lastname1");
    }

    @AfterEach
    public void tearDown() {
        person = null;
    }

    @Test
    public void testConstructor(){
        final Long id = 10l;
        final String firstname = "Test firstname";
        final String lastname = "Test lastname";

        final Person myPerson = new Person(id, firstname, lastname);

        assertEquals(id, myPerson.getId());
        assertEquals( firstname, myPerson.getFirstName());
        assertEquals( lastname, myPerson.getLastName());

    }

    @Test
    public void testEquals(){

        final Person person1 = new Person(1L, "firstname1", "lastname1");

        assertTrue(person.equals(person1));

    }

    @Test
    public void testNotEquals(){

        final Person personId = new Person(2L, "Test_firstname_1", "Test_lastname_1");
        final Person personFirstname = new Person(1L, "Test_firstname_2", "Test_lastname_1");
        final Person personLastName = new Person(1L, "Test_firstname_1", "Test_lastname_2");

        assertTrue(!person.equals(personId));
        assertTrue(!person.equals(personFirstname));
        assertTrue(!person.equals(personLastName));

    }


}