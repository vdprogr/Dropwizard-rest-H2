package com.dropwizard.example.h2.resources;

import com.dropwizard.example.h2.DAO.PersonDAO;
import com.dropwizard.example.h2.model.Person;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class PersonResourceTest {

    private static final PersonDAO personDAO = mock(PersonDAO.class);

    public static final ResourceExtension RULE = ResourceExtension.builder()
            .addResource(new PersonResource(personDAO))
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
        reset(personDAO);
    }

    @Test
    public void getAllWithTwoElements() {
        List<Person> persons = List.of(person1, person2);
        when(personDAO.getAll()).thenReturn(persons);

        List<Person> result = RULE.target("/persons").request().get(new GenericType<List<Person>>() {});

        assertEquals(2, result.size());
        assertEquals("firstname1", result.get(0).getFirstName());
        assertEquals("firstname2", result.get(1).getFirstName());

    }

    @Test
    public void getAllWithoutElements() {
        List<Person> persons = List.of();
        when(personDAO.getAll()).thenReturn(persons);

        List<Person> result = RULE.target("/persons").request().get(new GenericType<List<Person>>() {});
        assertEquals(0, result.size());
        verify(personDAO, times(1)).getAll();
    }


    @Test
    void getPersonSuccess() {
        when(personDAO.getById(1L)).thenReturn(Optional.of(person1));

        Person found = RULE.target("/persons/1").request().get(Person.class);

        assertEquals(found.getId(), person1.getId());
        verify(personDAO, times(1)).getById(1L);
    }

    @Test
    void getPersonNotFound() {
        when(personDAO.getById(3L)).thenReturn(Optional.empty());
        final Response response = RULE.target("/persons/3").request().get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatusInfo().getStatusCode());
        verify(personDAO, times(1)).getById(3L);
    }

    @Test
    void createPerson() {
        Person addPerson = new Person();
        addPerson.setFirstName("firstname");
        addPerson.setLastName("lastname");

        when(personDAO.addPerson(addPerson)).thenReturn(1L);
        addPerson.setId(1L);
        when(personDAO.getById(1L)).thenReturn(Optional.of(addPerson));

        final Response response = RULE.target("/persons")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(addPerson, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusInfo().getStatusCode());
        verify(personDAO, times(1)).addPerson(any(Person.class));
        verify(personDAO, times(1)).getById(anyLong());
    }

    @Test
    void updatePersonIfExists() {

        when(personDAO.getById(1L)).thenReturn(Optional.of(person1));

        final Response response = RULE.target("/persons/1")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(person1, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusInfo().getStatusCode());
        verify(personDAO, times(1)).update(any(Person.class));
        verify(personDAO, times(1)).getById(anyLong());
    }

    @Test
    void updatePersonIfNotExists() {
        ;
        when(personDAO.getById(1L)).thenReturn(Optional.ofNullable(null));

        final Response response = RULE.target("/persons/1")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(person1, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatusInfo().getStatusCode());
        verify(personDAO, times(1)).update(any(Person.class));
        verify(personDAO, times(1)).getById(anyLong());
    }


    @Test()
    public void delete() {

        RULE.target("/persons/1").request().delete();
        verify(personDAO, times(1)).deleteById(1L);
    }
}