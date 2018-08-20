package roberto.magale.service;

import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import roberto.magale.api.model.Account;
import roberto.magale.db.Repository;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    private Repository repository;

    private AccountService accountService;

    @Before
    public void setUp() {
        accountService = new AccountService(repository);
    }

    @Test
    public void shouldCorrectlyReturnAnAccountWhenGetAccountIsInvoked() {
        long accountId = 1L;
        Account expectedAccount = Account.builder().balance(TEN).holderName("rob").id(accountId).build();

        when(repository.getAccount(accountId)).thenReturn(expectedAccount);
        Optional<Account> actualAccount = accountService.getAccount(accountId);

        assertThat(actualAccount).hasValue(expectedAccount);
        verify(repository).getAccount(accountId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void shouldCorrectlyReturnAnAccountWhenInsertAccountIsInvoked() {
        long accountId = 1L;
        Account expectedAccount = Account.builder().balance(TEN).holderName("rob").build();
        when(repository.insert(expectedAccount)).thenReturn(accountId);

        Account actualAccount = accountService.addAccount(expectedAccount);

        assertThat(actualAccount).isEqualToIgnoringGivenFields(expectedAccount, "id");
        verify(repository).insert(expectedAccount);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void shouldCorrectlyReturnAnAccountWhenUpdateAccountIsInvoked() {
        Account expectedAccount = Account.builder().id(1L).balance(TEN).holderName("rob").build();

        when(repository.update(expectedAccount)).thenReturn(true);
        Optional<Account> actualAccount = accountService.updateAccount(expectedAccount, 1L);

        assertThat(actualAccount).hasValue(expectedAccount);
        verify(repository).update(expectedAccount);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void shouldCorrectlyInvokeRemoveAccount() {
        long accountId = 1L;

        accountService.removeAccount(accountId);

        verify(repository).remove(accountId);
        verifyNoMoreInteractions(repository);
    }
}