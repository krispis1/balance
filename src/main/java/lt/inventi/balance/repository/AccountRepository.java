package lt.inventi.balance.repository;

import lt.inventi.balance.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String accountNumber);
    Account findByAccountNumber(String accountNumber);

    @Query(value = "select SUM(DISTINCT statements.amount) from balance.statements where statements.account_number = :accountNumber and statements.operation_time <= :tsTo", nativeQuery = true)
    Double getBalanceUntil(@Param("tsTo") Timestamp tsTo, @Param("accountNumber") String accountNumber);

    @Query(value = "select SUM(DISTINCT statements.amount) from balance.statements where statements.account_number = :accountNumber and statements.operation_time >= :tsFrom", nativeQuery = true)
    Double getBalanceFrom(@Param("tsFrom") Timestamp tsFrom, @Param("accountNumber") String accountNumber);

    @Query(value = "select SUM(DISTINCT statements.amount) from balance.statements where statements.account_number = :accountNumber and statements.operation_time >= :tsFrom and statements.operation_time <= :tsTo", nativeQuery = true)
    Double getBalanceBetween(@Param("tsFrom") Timestamp tsFrom, @Param("tsTo") Timestamp tsTo, @Param("accountNumber") String accountNumber);
}
