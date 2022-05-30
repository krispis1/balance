package lt.inventi.balance.model;

import lombok.Data;
import lt.inventi.balance.util.CurrencyUtil.Currency;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "statements")
public class Statement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "statement_id")
    private Integer statementId;

    @Column(name = "account_number")
    @NotEmpty
    private String accountNumber;

    @Column(name = "operation_time")
    @NotNull
    private Timestamp operationTime;

    @Column(name = "beneficiary")
    @NotEmpty
    private String beneficiary;

    @Column(name = "comment")
    private String comment;

    @Column(name = "amount")
    @NotNull
    private Double amount;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Currency currency;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="account_id", nullable=false)
    private Account account;
}
