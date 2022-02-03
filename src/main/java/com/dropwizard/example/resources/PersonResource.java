package com.dropwizard.example.resources;

import com.codahale.metrics.annotation.Timed;
import com.dropwizard.example.h2.dao.PersonDao;
import com.dropwizard.example.model.Person;
import com.dropwizard.example.service.PersonService;
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

    private final PersonService personService;

    public PersonResource(PersonService personService) {
        this.personService = personService;
    }

    @GET
    @Timed
    public Response getAllPersons() {
        LOGGER.info("Getting all persons ");
        return Response.ok(personService.getAll()).build();
    }

    @GET
    @Timed
    @Path("{id}")
    public Response getPersonById(@PathParam("id") Long id) {
        LOGGER.info("Getting person with id {}", id);
        return Response.ok(personService.getPerson(id)).build();
    }

    @POST
    @Timed
    public Response addPerson(@Valid Person person) {
        LOGGER.info("Adding person {}", person.getFirstName() + " " + person.getLastName());
        return Response.ok(personService.createPerson(person)).build();
    }

    @PUT
    @Timed
    @Path("{id}")
    public Response updatePerson(@PathParam("id") Long id, @Valid Person person) {
        LOGGER.info("Updating person with id {}", id);

        person.setId(id);
        return  Response.ok(personService.updatePerson(person)).build();
    }

    @DELETE
    @Timed
    @Path("{id}")
    public Response deletePerson(@PathParam("id") Long id) {
        LOGGER.info("Deleting person with id {}", id);
        personService.deletePerson(id);
        return Response.ok().build();
    }
}
