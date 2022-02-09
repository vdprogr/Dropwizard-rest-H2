package com.dropwizard.example.dao.h2;

import com.dropwizard.example.model.Person;
import com.dropwizard.example.model.mapper.PersonMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterRowMapper(PersonMapper.class)
public interface PersonDao{

    @SqlQuery("select * from person")
    List<Person> getAll();

    @SqlQuery("select * from person where id = :id")
    Optional<Person> getById(@Bind("id") Long id);

    @SqlUpdate("insert into person (firstname, lastname) values (:firstName, :lastName)")
    @GetGeneratedKeys
    Long createPerson(@BindBean Person person);

    @SqlUpdate("update person set firstname = :firstName, lastname = :lastName where id = :id")
    void updatePerson(@BindBean Person person);

    @SqlUpdate("delete from person where id = :id")
    int deleteById(@Bind("id") Long id);
}

