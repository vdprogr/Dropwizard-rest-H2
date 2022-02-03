package com.dropwizard.example.resources;

import com.dropwizard.example.model.Person;
import com.dropwizard.example.service.PersonService;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class PersonResourceTest {

    private static final PersonService personService = mock(PersonService.class);

    public static final ResourceExtension RULE = ResourceExtension.builder()
            .addResource(new PersonResource(personService))
            .build();

    private Person person1;
    private Person person2;

    @BeforeEach
    void setup() {
        person1 = new Person();
        person1.setId(1L);
        person1.setFirstName("firstname1");
        person1.setLastName("lastname1");

        person2 = new Person();
        person2.setId(2L);
        person2.setFirstName("firstname2");
        person2.setLastName("lastname2");

    }

    @AfterEach
    void tearDown() {
        reset(personService);
    }

    @Test
    public void testGetAllPerson() {
        List<Person> persons = List.of(person1, person2);
        when(personService.getAll()).thenReturn(persons);

        List<Person> result = RULE.target("/persons").request().get(new GenericType<List<Person>>() {});

        assertEquals(2, result.size());
        assertEquals(person1.getFirstName(), result.get(0).getFirstName());
        assertEquals(person2.getFirstName(), result.get(1).getFirstName());
    }

    @Test
    public void testGetAllPersonsIfEmpty() {
        List<Person> persons = List.of();
        when(personService.getAll()).thenReturn(persons);

        List<Person> result = RULE.target("/persons").request().get(new GenericType<List<Person>>() {});
        assertTrue(result.isEmpty());
        verify(personService, times(1)).getAll();
    }


    @Test
    void testGetPerson() {
        when(personService.getPerson(1L)).thenReturn(person1);

        Person found = RULE.target("/persons/1").request().get(Person.class);

        assertEquals(found.getId(), person1.getId());
        verify(personService, times(1)).getPerson(1L);
    }

    @Test
    void testGetPersonExpectException() throws WebApplicationException {

        when(personService.getPerson(0L)).thenThrow(
                new WebApplicationException("", Response.Status.NOT_FOUND));

        final Response response = RULE.target("/persons/0")
                .request(MediaType.APPLICATION_JSON_TYPE).get();

        assertEquals(response.getStatusInfo().getStatusCode(), Response.Status.NOT_FOUND.getStatusCode());
        verify(personService, times(1)).getPerson(0L);
    }

    @Test
    void testCreatePerson() {

        when(personService.createPerson(person1)).thenReturn(person1);

        final Response response = RULE.target("/persons")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(person1, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusInfo().getStatusCode());
        verify(personService, times(1)).createPerson((any(Person.class)));
    }

    @Test
    void testUpdatePerson() {

        when(personService.updatePerson(person1)).thenReturn(person1);

        final Response response = RULE.target("/persons/1")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(person1, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusInfo().getStatusCode());
        verify(personService, times(1)).updatePerson(any(Person.class));
    }

    @Test
    void testUpdatePersonExpectException ()  throws WebApplicationException {
        Person person = new Person(0L, "firstname", "lastname");

        when(personService.updatePerson(person)).thenThrow(
                new WebApplicationException("", Response.Status.NOT_FOUND));

        final Response response = RULE.target("/persons/0")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(person, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(response.getStatusInfo().getStatusCode(), Response.Status.NOT_FOUND.getStatusCode());
        verify(personService, times(1)).updatePerson(any(Person.class));
    }

    @Test()
    public void testDeleteById() {

        RULE.target("/persons/1").request().delete();
        verify(personService, times(1)).deletePerson(1L);
    }
}