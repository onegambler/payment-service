package roberto.magale.db.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import roberto.magale.api.model.Account;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountMapper implements RowMapper<Account> {

    private static final String BALANCE_COLUMN_NAME = "balance";
    private static final String ID_COLUMN_NAME = "id";
    private static final String HOLDER_NAME_COLUMN_NAME = "holder_name";

    @Override
    public Account map(ResultSet rs, StatementContext ctx) throws SQLException {
        return Account.builder()
                .balance(rs.getBigDecimal(BALANCE_COLUMN_NAME))
                .id(rs.getLong(ID_COLUMN_NAME))
                .holderName(rs.getString(HOLDER_NAME_COLUMN_NAME))
                .build();
    }
}