package lt.inventi.balance.repository;

import lt.inventi.balance.model.Statement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface StatementRepository extends JpaRepository<Statement, Long> {
    List<Statement> findAllByOperationTimeBetween(Timestamp tsFrom, Timestamp tsTo);

    @Query(value = "select * from balance.statements where operation_time <= :operationTime", nativeQuery = true)
    List<Statement> findAllWithOperationTimeBefore(@Param("operationTime") Timestamp operationTime);

    @Query(value = "select * from balance.statements where operation_time >= :operationTime", nativeQuery = true)
    List<Statement> findAllWithOperationTimeAfter(@Param("operationTime") Timestamp operationTime);
}
