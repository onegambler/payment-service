package roberto.magale.db.mapper;

import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import roberto.magale.api.model.Account;

import java.sql.ResultSet;
import java.sql.SQLException;

@RunWith(MockitoJUnitRunner.class)
public class AccountMapperTest {

    @Mock
    private ResultSet resultSet;

    private AccountMapper accountMapper = new AccountMapper();

    @Test
    public void shouldProperlyMapAnAccountFromResultSet() throws SQLException {
        when(resultSet.getLong("id")).thenReturn(5L);
        when(resultSet.getBigDecimal("balance")).thenReturn(TEN);
        when(resultSet.getString("holder_name")).thenReturn("rob");

        Account mappedAccount = accountMapper.map(resultSet, null);

        Account expectedAccount = Account.builder().balance(TEN).holderName("rob").id(5L).build();
        assertThat(mappedAccount).isEqualTo(expectedAccount);
    }
}
