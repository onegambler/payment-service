package roberto.magale.api.model;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import roberto.magale.api.exceptions.InsufficientFundingException;

import java.math.BigDecimal;

public class AccountTest {

    @Test
    public void addAmountWorksAsExpected() {
        Account account = Account.builder().balance(TEN).holderName("rob").build();
        account.addAmount(ONE);
        assertThat(account.getBalance()).isEqualTo(new BigDecimal(11));
    }

    @Test
    public void subtractAmountWorksAsExpected() {
        Account account = Account.builder().balance(TEN).holderName("rob").build();
        account.subtractAmount(ONE);
        assertThat(account.getBalance()).isEqualTo(new BigDecimal(9));
    }

    @Test
    public void whenAmountIsNotSufficientThenSubtractAmountThrowsAnException() {
        Account account = Account.builder().balance(ONE).holderName("rob").id(5L).build();
        assertThatThrownBy(() -> account.subtractAmount(TEN))
                .isInstanceOf(InsufficientFundingException.class)
                .hasMessage("HTTP 400 Bad Request");
    }
}
