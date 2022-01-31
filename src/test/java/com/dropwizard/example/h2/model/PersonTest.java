package com.dropwizard.example.h2.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * The Person test class.
 */
public class PersonTest {

    private Person person;

    @Before
    public void setUp(){
        person = new Person();
    }

    @Test
    public void test_constructor(){
        final Long id = 10l;
        final String firstname = "Test firstname";
        final String lastname = "Test lastname";

        final Person myPerson = new Person(id, firstname, lastname);

        assertEquals(id, myPerson.getId());
        assertEquals( firstname, myPerson.getFirstName());
        assertEquals( lastname, myPerson.getLastName());

    }

    @Test
    public void test_equals(){

        final Person person1 = new Person(1L, "Test_firstname_1", "Test_lastname_1");
        final Person person2 = new Person(1L, "Test_firstname_1", "Test_lastname_1");

        assertTrue(person1.equals(person2));

    }

    @Test
    public void test_not_equals(){

        final Person person1 = new Person(1L, "Test_firstname_1", "Test_lastname_1");

        final Person personId = new Person(2L, "Test_firstname_1", "Test_lastname_1");
        final Person personFirstname = new Person(1L, "Test_firstname_2", "Test_lastname_1");
        final Person personLastName = new Person(1L, "Test_firstname_1", "Test_lastname_2");

        assertTrue(!person1.equals(personId));
        assertTrue(!person1.equals(personFirstname));
        assertTrue(!person1.equals(personLastName));

    }


}