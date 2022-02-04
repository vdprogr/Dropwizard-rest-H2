package com.dropwizard.example.integration;

import com.dropwizard.example.SimpleApplication;
import com.dropwizard.example.model.Person;
import config.configuration.SimpleConfiguration;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


import static org.junit.Assert.assertEquals;

@ExtendWith(DropwizardExtensionsSupport.class)
public class IntegrationTest {

    private static Client client;
    private static String CONFIG_PATH = "src/test/resources/config.yml" ;

    @ClassRule
    private static DropwizardAppExtension<SimpleConfiguration> EXT = new DropwizardAppExtension<>(
            SimpleApplication.class, CONFIG_PATH);

    @BeforeAll
    public static void migrateDb() throws IOException {
        client = EXT.client();

    }

    @AfterAll
    public static void tearDown() throws IOException {
        client.close();
    }

    @Test
    void testGetPersonSuccess() {
        // try to get person with ID=1
        final Person person = new Person(1L, "Dan", "Johnson");

        final Person newPerson = client.target("http://localhost:" + EXT.getLocalPort() + "/persons/1")
                .request()
                .get().readEntity(Person.class);

        assertEquals(newPerson.getId(), person.getId());
        assertEquals(newPerson.getFirstName(), person.getFirstName());
        assertEquals(newPerson.getLastName(), person.getLastName());
    }

    @Test
    void testGetPersonNotSuccess() {
        // try to get person with ID=0
        final Response response = client.target("http://localhost:" + EXT.getLocalPort() + "/persons/0")
                .request()
                .get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatusInfo().getStatusCode());
    }

    @Test
    void testGetAllPerson() {

        final Response response =
                client.target("http://localhost:" + EXT.getLocalPort() + "/persons")
                .request().get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusInfo().getStatusCode());
    }

    @Test
    void testPostPerson() {
        final Person person = new Person(4L, "Ivan", "Ivanov");

        final Person newPerson =
                client.target("http://localhost:" + EXT.getLocalPort() + "/persons")
                .request()
                .post(Entity.entity(person, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(Person.class);

        assertEquals(person.getId(), newPerson.getId());
        assertEquals(person.getFirstName(), newPerson.getFirstName());
        assertEquals(person.getLastName(), newPerson.getLastName());
    }

    @Test
    void testUpdatePersonSuccess() {
        // update person with ID=3
        final Person person = new Person(3l, "Ivan", "Ivanov");

        final Person newPerson =
                client.target("http://localhost:" + EXT.getLocalPort() + "/persons/3")
                .request()
                .put(Entity.entity(person, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(Person.class);

        assertEquals(person.getId(), newPerson.getId());
        assertEquals(person.getFirstName(), newPerson.getFirstName());
        assertEquals(person.getLastName(), newPerson.getLastName());
    }

    @Test
    void testUpdatePersonNotSuccess() {
        // try to update person with ID=0
        final Person person = new Person(0l, "Ivan", "Ivanov");

        final Response response =
                client.target("http://localhost:" + EXT.getLocalPort() + "/persons/0")
                .request()
                .put(Entity.entity(person, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatusInfo().getStatusCode());
    }


    @Test
    void testDeletePersonSuccess() {
        // try to delete person with ID=2
        final Response response =
                client.target("http://localhost:" + EXT.getLocalPort() + "/persons/2")
                .request()
                .delete();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusInfo().getStatusCode());
    }

    @Test
    void testDeletePersonNotSuccess() {
        // try to delete person with ID=0
        final Response response =
                client.target("http://localhost:" + EXT.getLocalPort() + "/persons/0")
                .request()
                .delete();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatusInfo().getStatusCode());
    }
}
