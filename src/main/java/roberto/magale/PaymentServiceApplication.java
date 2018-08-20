package roberto.magale;

import io.dropwizard.Application;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.flyway.FlywayBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import roberto.magale.health.AppHealthCheck;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class PaymentServiceApplication extends Application<PaymentServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new PaymentServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "PaymentService";
    }

    @Override
    public void initialize(final Bootstrap<PaymentServiceConfiguration> bootstrap) {
        bootstrap.addBundle(
                GuiceBundle.builder()
                        .modules(new PaymentServiceModule())
                        .enableAutoConfig(getClass().getPackage().getName())
                        .build());

        bootstrap.addBundle(new FlywayBundle<PaymentServiceConfiguration>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(PaymentServiceConfiguration configuration) {
                return configuration.getDatabase();
            }
        });

        bootstrap.addBundle(new SwaggerBundle<PaymentServiceConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(PaymentServiceConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });
    }

    @Override
    public void run(final PaymentServiceConfiguration configuration,
                    final Environment environment) {
        environment.healthChecks().register("app", new AppHealthCheck());
    }

}
