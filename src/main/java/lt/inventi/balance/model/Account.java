package lt.inventi.balance.model;

import lombok.Data;
import lt.inventi.balance.util.CurrencyUtil.Currency;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id")
    private Integer accountId;
    @Column(name = "account_number", unique = true)
    @NotEmpty
    private String accountNumber;
    @Column(name = "amount")
    @NotNull
    private Double amount;
    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Currency currency;
    @OneToMany(mappedBy="account")
    private Set<Statement> statements;
}
