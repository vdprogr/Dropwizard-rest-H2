package com.dropwizard.example.model.mapper;

import com.dropwizard.example.model.Person;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMapper implements RowMapper<Person> {

    @Override
    public Person map(ResultSet rs, StatementContext ctx) throws SQLException {

        Person person = new Person();
        person.setId(rs.getLong("id"));
        person.setFirstName(rs.getString("firstname"));
        person.setLastName(rs.getString("lastname"));

        return person;
    }

}