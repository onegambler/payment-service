package roberto.magale.db.mapper;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import roberto.magale.api.model.Transaction;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionMapper implements RowMapper<Transaction> {
    private static final String ID_COLUMN_NAME = "id";
    private static final String AMOUNT_COLUMN_NAME = "amount";
    private static final String SOURCE_COLUMN_NAME = "source";
    private static final String DESTINATION_COLUMN_NAME = "destination";

    @Override
    public Transaction map(ResultSet rs, StatementContext ctx) throws SQLException {
        return Transaction.builder()
                .id(rs.getLong(ID_COLUMN_NAME))
                .amount(rs.getBigDecimal(AMOUNT_COLUMN_NAME))
                .sourceAccountId(rs.getLong(SOURCE_COLUMN_NAME))
                .destinationAccountId(rs.getLong(DESTINATION_COLUMN_NAME))
                .build();
    }
}