package roberto.magale.db;


import static org.jdbi.v3.core.transaction.TransactionIsolationLevel.REPEATABLE_READ;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import roberto.magale.api.exceptions.AccountNotFoundException;
import roberto.magale.api.exceptions.SameAccountTransferException;
import roberto.magale.api.model.Account;
import roberto.magale.api.model.Transaction;
import roberto.magale.db.mapper.AccountMapper;
import roberto.magale.db.mapper.TransactionMapper;

public interface Repository {

    @SqlQuery("select * from ACCOUNTS where id=:id")
    @RegisterRowMapper(AccountMapper.class)
    Account getAccount(@Bind("id") Long id);

    @GetGeneratedKeys("id")
    @SqlUpdate("insert into ACCOUNTS (holder_name, balance) values (:holderName, :balance)")
    Long insert(@BindBean Account account);

    @SqlUpdate("insert into ACCOUNTS (balance, version) values (:version, :balance)")
    boolean remove(Long accountId);

    @SqlUpdate("update ACCOUNTS set balance = :balance where id = :id")
    boolean update(@BindBean Account account);

    @org.jdbi.v3.sqlobject.transaction.Transaction(REPEATABLE_READ)
    default Long updateAccounts(Transaction transaction) {
        Long sourceAccountId = transaction.getSourceAccountId();
        Long destinationAccountId = transaction.getDestinationAccountId();


        // Ordering the db select to retrieve accounts based on the account id.
        // All locks will be requested in the same order, thus avoiding deadlocks.
        Account source;
        Account destination;
        if (transaction.getSourceAccountId() < transaction.getDestinationAccountId()) {
            source = getAccount(sourceAccountId);
            destination = getAccount(destinationAccountId);
        } else if (transaction.getSourceAccountId() > transaction.getDestinationAccountId()) {
            destination = getAccount(destinationAccountId);
            source = getAccount(sourceAccountId);
        } else {
            throw new SameAccountTransferException();
        }

        if (source == null) {
            throw new AccountNotFoundException(sourceAccountId);
        }

        if (destination == null) {
            throw new AccountNotFoundException(destinationAccountId);
        }

        source.subtractAmount(transaction.getAmount());
        destination.addAmount(transaction.getAmount());
        update(source);
        update(destination);
        return insert(transaction);
    }

    @SqlQuery("select * from TRANSACTIONS where id=:id")
    @RegisterRowMapper(TransactionMapper.class)
    Transaction getTransaction(@Bind("id") Long id);

    @GetGeneratedKeys("id")
    @SqlUpdate("insert into TRANSACTIONS (source, destination, amount) values (:sourceAccountId, :destinationAccountId, :amount)")
    Long insert(@BindBean Transaction transaction);

}
