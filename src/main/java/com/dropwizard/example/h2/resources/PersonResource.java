package com.dropwizard.example.h2.resources;

import com.codahale.metrics.annotation.Timed;
import com.dropwizard.example.h2.DAO.PersonDAO;
import com.dropwizard.example.h2.model.Person;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonResource.class);

    private final PersonDAO personDAO;

    public PersonResource(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GET
    @Timed
    public Response getAllPersons() {
        List<Person> persons = personDAO.getAll();
        LOGGER.info("Fetched persons count - " + persons.size());

        return Response.ok(persons).build();
    }

    @GET
    @Timed
    @Path("{id}")
    public Response getPersonById(@PathParam("id") Long id) {

        LOGGER.info("Fetching person by id {} ", id);
        Optional<Person> personFromDb = personDAO.getById(id);
        if (personFromDb.isPresent()) {
            return Response.ok(personFromDb).build();
        }
        else {

            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Person with ID = "+id+" not found !")
                    .build();
        }
    }

    @POST
    @Timed
    public Response addPerson(@Valid Person person) {
        LOGGER.info("Adding person {}", person.getFirstName() + " " + person.getLastName());

        Long id = personDAO.addPerson(person);

        Optional<Person> personFromDb = personDAO.getById(id);
        return Response.ok(personFromDb).build();
    }

    @PUT
    @Timed
    @Path("{id}")
    public Response update(@PathParam("id") Long id, @Valid Person person) {
        LOGGER.info("Updating person with id {}", id);

        Person updatePerson = new Person(id, person.getFirstName(), person.getLastName());
        personDAO.update(updatePerson);
        Optional<Person> personFromDb = personDAO.getById(id);
        if (personFromDb.isPresent()) {
            return Response.ok(personFromDb).build();
        }
        else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Person with ID = "+id+" not found for update !")
                    .build();
        }
    }

    @DELETE
    @Timed
    @Path("{id}")
    public Response deletePerson(@PathParam("id") Long id) {
        LOGGER.info("deleting person with id {}", id);
        personDAO.deleteById(id);

        return Response.ok().build();
    }
}
