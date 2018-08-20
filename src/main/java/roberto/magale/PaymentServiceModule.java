package roberto.magale;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;
import roberto.magale.db.Repository;

public class PaymentServiceModule extends AbstractModule {

    private Jdbi database;

    @Override
    protected void configure() {
    }

    @Provides
    public Jdbi prepareDatabase(Environment environment,
                                PaymentServiceConfiguration configuration) {
        // setup database, implementing a singleton pattern here but avoiding Guice to initialize DB connection too early
        if (database == null) {
            final JdbiFactory factory = new JdbiFactory();
            database = factory.build(environment, configuration.getDatabase(), "h2");
        }
        return database;
    }

    @Provides
    public Repository accountRepository(Jdbi database) {
        return database.onDemand(Repository.class);
    }
}
