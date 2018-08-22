package roberto.magale.api.model;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import roberto.magale.api.exceptions.InsufficientFundingException;

import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Builder(toBuilder = true)
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class Account {

    @JsonProperty("id")
    private Long id;
    @NotNull
    @Min(0)
    @JsonProperty("balance")
    private BigDecimal balance;
    @JsonProperty("holderName")
    @NotEmpty
    private String holderName;

    @JsonIgnore
    public void addAmount(BigDecimal amount) {
        this.balance = balance.add(amount);
    }

    @JsonIgnore
    public void subtractAmount(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundingException(id);
        }
        this.balance = this.balance.subtract(amount);
    }
}
