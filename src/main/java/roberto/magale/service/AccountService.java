package roberto.magale.service;

import static java.util.Objects.requireNonNull;

import com.google.inject.Inject;
import roberto.magale.api.model.Account;
import roberto.magale.db.Repository;

import java.util.Optional;


public class AccountService {

    private final Repository repository;

    @Inject
    public AccountService(Repository repository) {
        this.repository = repository;
    }

    public Account addAccount(Account account) {
        requireNonNull(account, "Account cannot be null");
        Long id = this.repository.insert(account);
        return account.toBuilder().id(id).build();
    }

    public boolean removeAccount(Long accountId) {
        requireNonNull(accountId, "AccountId cannot be null");
        return this.repository.remove(accountId);
    }

    public Optional<Account> updateAccount(Account account, Long accountId) {
        requireNonNull(account, "Account cannot be null");
        Account newAccount = account.toBuilder().id(accountId).build();
        boolean update = this.repository.update(newAccount);
        return update ? Optional.of(newAccount) : Optional.empty();
    }

    public Optional<Account> getAccount(Long accountId) {
        requireNonNull(accountId, "AccountId cannot be null");
        return Optional.ofNullable(this.repository.getAccount(accountId));
    }
}
