package roberto.magale.service;

import static java.util.Objects.requireNonNull;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import roberto.magale.api.model.Transaction;
import roberto.magale.db.Repository;

import java.util.Optional;

@Singleton
public class TransactionService {

    private Repository repository;

    @Inject
    public TransactionService(Repository repository) {
        this.repository = repository;
    }

    public Transaction transferMoney(Transaction transaction) {
        requireNonNull(transaction, "transaction cannot be null");
        Long transactionId = this.repository.updateAccounts(transaction);
        return transaction.toBuilder()
            .id(transactionId)
            .build();
    }

    public Optional<Transaction> getTransaction(Long transactionId) {
        return Optional.ofNullable(repository.getTransaction(transactionId));
    }
}
