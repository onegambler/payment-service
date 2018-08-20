package roberto.magale.db.mapper;

import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import roberto.magale.api.model.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;

@RunWith(MockitoJUnitRunner.class)
public class TransactionMapperTest {
    @Mock
    private ResultSet resultSet;

    private TransactionMapper transactionMapper = new TransactionMapper();

    @Test
    public void shouldProperlyMapAnAccountFromResultSet() throws SQLException {
        when(resultSet.getLong("id")).thenReturn(5L);
        when(resultSet.getBigDecimal("amount")).thenReturn(TEN);
        when(resultSet.getLong("source")).thenReturn(1L);
        when(resultSet.getLong("destination")).thenReturn(2L);

        Transaction mappedTransacton = transactionMapper.map(resultSet, null);

        Transaction expectedTransaction = Transaction.builder()
                .sourceAccountId(1L)
                .destinationAccountId(2L)
                .amount(TEN)
                .id(5L).build();
        assertThat(mappedTransacton).isEqualTo(expectedTransaction);
    }

}