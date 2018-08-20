package roberto.magale;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import roberto.magale.api.model.Account;
import roberto.magale.api.model.Transaction;

import java.math.BigDecimal;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;


public class PaymentServiceAppIntegrationTest {

    @ClassRule
    public static final DropwizardAppRule<PaymentServiceConfiguration> RULE =
            new DropwizardAppRule<>(PaymentServiceApplication.class,
                    ResourceHelpers.resourceFilePath("test-config.yml"));

    private static Client CLIENT;

    private Account firstAccount;
    private Account secondAccount;


    @BeforeClass
    public static void setupClass() {
        CLIENT = new JerseyClientBuilder(RULE.getEnvironment()).build("test client");
        Flyway flyway = new Flyway();
        DataSourceFactory f = RULE.getConfiguration().getDatabase();
        flyway.setDataSource(f.getUrl(), f.getUser(), f.getPassword());
        flyway.migrate();
    }

    @Before
    public void setUp() {
        firstAccount = createAccount(Account.builder().balance(TEN).holderName("rob").build());
        secondAccount = createAccount(Account.builder().balance(TEN).holderName("erto").build());
    }

    @Test
    public void shouldCorrectlyTransferMoneyBetweenAccounts() {
        Transaction transaction = Transaction.builder()
                .amount(ONE)
                .sourceAccountId(firstAccount.getId())
                .destinationAccountId(secondAccount.getId())
                .build();
        Response response = CLIENT.target(
                String.format("http://localhost:%d/transaction", RULE.getLocalPort()))
                .request()
                .post(Entity.json(transaction));

        assertThat(response.getStatus()).isEqualTo(201);

        Account actualFirstAccount = getAccount(firstAccount.getId());
        Account actualSecondAccount = getAccount(secondAccount.getId());

        assertThat(actualFirstAccount.getBalance()).isEqualTo(new BigDecimal(9));
        assertThat(actualSecondAccount.getBalance()).isEqualTo(new BigDecimal(11));
    }

    @Test
    public void shouldReturnBadRequestIfSourceAndDestinationAccountIdsAreSame() {
        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal(200))
                .sourceAccountId(firstAccount.getId())
                .destinationAccountId(firstAccount.getId())
                .build();
        Response response = CLIENT.target(
                String.format("http://localhost:%d/transaction", RULE.getLocalPort()))
                .request()
                .post(Entity.json(transaction));

        assertThat(response.getStatus()).isEqualTo(422);
        assertThat(response.readEntity(String.class)).isEqualTo("{\"errors\":[\"source account id must be different to destination account id\"]}");
    }

    @Test
    public void shouldReturnBadRequestIfAmountIsNotSufficientForTransfer() {
        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal(200))
                .sourceAccountId(firstAccount.getId())
                .destinationAccountId(secondAccount.getId())
                .build();
        Response response = CLIENT.target(
                String.format("http://localhost:%d/transaction", RULE.getLocalPort()))
                .request()
                .post(Entity.json(transaction));

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.readEntity(String.class)).isEqualTo("{\"message\":\"Account 3 has insufficient funding. Request Rejected\"}");
    }

    private Account createAccount(Account account) {
        Response response = CLIENT.target(
                String.format("http://localhost:%d/account", RULE.getLocalPort()))
                .request()
                .post(Entity.json(account));
        return response.readEntity(Account.class);
    }

    private Account getAccount(Long id) {
        Response response = CLIENT.target(
                String.format("http://localhost:%d/account/%s", RULE.getLocalPort(), id))
                .request()
                .get();
        return response.readEntity(Account.class);
    }
}
