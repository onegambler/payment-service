package roberto.magale.api.model;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.ValidationMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

@Builder(toBuilder = true)
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class Transaction {

    @JsonProperty("id")
    private Long id;

    @NotNull
    @JsonProperty("sourceAccountId")
    private Long sourceAccountId;

    @NotNull
    @JsonProperty("destinationAccountId")
    private Long destinationAccountId;
    @NotNull
    @JsonProperty("amount")
    private BigDecimal amount;


    @ValidationMethod(message="source account id must be different to destination account id")
    @JsonIgnore
    public boolean isNotSameAccount() {
        return !sourceAccountId.equals(destinationAccountId);
    }
}
