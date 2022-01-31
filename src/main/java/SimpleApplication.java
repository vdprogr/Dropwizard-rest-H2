import com.dropwizard.example.h2.DAO.PersonDAO;
import com.dropwizard.example.h2.resources.PersonResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.db.ManagedPooledDataSource;
import io.dropwizard.flyway.FlywayFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleApplication extends Application<SimpleConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleApplication.class);

    public static void main( String[] args )
    {
        try {
            new SimpleApplication().run(args);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(SimpleConfiguration configuration, Environment environment) throws Exception {

        /* Get handle to datasource */
        DataSourceFactory dataSourceFactory = configuration.getDataSourceFactory();
        final ManagedDataSource dataSource = dataSourceFactory.build(environment.metrics(), "h2");
        if (dataSource instanceof ManagedPooledDataSource) {
            LOGGER.info("database URL inUse : {}", ((ManagedPooledDataSource) dataSource).getUrl());
            LOGGER.info("database ConnectionPool inUse : {}", ((ManagedPooledDataSource) dataSource).getPoolProperties());
        }

        /* JDBI3 is being used as ORM */
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, dataSourceFactory, dataSource, "h2");

        /* Flyway is being used to automatically apply changes to database */
        FlywayFactory flywayFactory = configuration.getFlywayFactory();
        flywayFactory.build(dataSource).migrate();

        /* Register the resources to be used in the project */
        final PersonDAO personDAO = jdbi.onDemand(PersonDAO.class);
        final PersonResource personResource = new PersonResource(personDAO);

        environment.jersey().register(personResource);
    }
}