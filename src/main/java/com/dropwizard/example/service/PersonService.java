package com.dropwizard.example.service;

import com.dropwizard.example.h2.dao.PersonDao;
import com.dropwizard.example.model.Person;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class PersonService {

    private PersonDao personDao;

    public PersonService(PersonDao personDao) {
        this.personDao = personDao;
    }

    public List<Person> getAll() {
        return personDao.getAll();
    }

    public Person getPerson(Long id) {
        return personDao.getById(id).orElseThrow(() -> new WebApplicationException(
                String.format("Person id %s not found.", id), Response.Status.NOT_FOUND));
    }

    public Person createPerson(Person person) {
        long id = personDao.createPerson(person);
        return personDao.getById(id).get();

    }

    public Person updatePerson(Person person) {
        personDao.updatePerson(person);
        return personDao.getById(person.getId()).orElseThrow(() -> new WebApplicationException(
                String.format("Person id %s not found.", person.getId()), Response.Status.NOT_FOUND));
    }

    public void deletePerson(Long id) {
        int res = personDao.deleteById(id);
        if (res == 0 )
            throw new WebApplicationException(String.format("Person id %s not found.", id), Response.Status.NOT_FOUND);
    }
}
