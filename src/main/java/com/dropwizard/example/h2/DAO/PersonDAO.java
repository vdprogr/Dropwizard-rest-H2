package com.dropwizard.example.h2.DAO;

import com.dropwizard.example.h2.mapper.PersonMapper;
import com.dropwizard.example.h2.model.Person;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

public interface PersonDAO {

    @SqlQuery("select * from person where id = :id")
    @RegisterRowMapper(PersonMapper.class)
    Optional<Person> getById(@Bind("id") Long id);

    @SqlQuery("select id, firstname, lastname from person")
    @RegisterRowMapper(PersonMapper.class)
    List<Person> getAll();

    @SqlUpdate("insert into person (firstname, lastname) values (:firstName, :lastName)")
    @GetGeneratedKeys
    Long addPerson(@BindBean Person person);

    @SqlUpdate("update person set firstname = :firstName, lastname = :lastName where id = :id")
    void update(@BindBean Person person);

    @SqlUpdate("delete from person where id = :id")
    void deleteById(@Bind("id") Long id);

}
