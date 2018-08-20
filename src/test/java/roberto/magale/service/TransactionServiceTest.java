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
import roberto.magale.api.model.Transaction;
import roberto.magale.db.Repository;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    @Mock
    private Repository repository;

    private TransactionService transactionService;

    @Before
    public void setUp() {
        transactionService = new TransactionService(repository);
    }

    @Test
    public void shouldCorrectlyProcessTheTransaction() {
        Transaction requestTransaction = Transaction.builder()
                .sourceAccountId(1L)
                .destinationAccountId(2L)
                .amount(TEN)
                .build();

        when(repository.updateAccounts(requestTransaction)).thenReturn(1L);
        Transaction actualTransaction = transactionService.transferMoney(requestTransaction);

        assertThat(actualTransaction).isEqualToIgnoringGivenFields(requestTransaction, "id");
        verify(repository).updateAccounts(requestTransaction);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void shouldCorrectlyReturnATransactionWhenGetTransactionIsInvoked() {
        long transactionId = 1L;
        Transaction requestTransaction = Transaction.builder()
                .sourceAccountId(3L)
                .destinationAccountId(2L)
                .amount(TEN)
                .id(transactionId)
                .build();

        when(repository.getTransaction(transactionId)).thenReturn(requestTransaction);
        Optional<Transaction> actualTransaction = transactionService.getTransaction(transactionId);

        assertThat(actualTransaction).hasValue(requestTransaction);
        verify(repository).getTransaction(transactionId);
        verifyNoMoreInteractions(repository);
    }

}