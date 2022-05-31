package lt.inventi.balance.service;

import static org.junit.Assert.*;

import lt.inventi.balance.model.Account;
import lt.inventi.balance.model.Statement;
import lt.inventi.balance.repository.AccountRepository;
import lt.inventi.balance.repository.StatementRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
public class StatementServiceTest {
    @Autowired
    StatementService statementService;

    @Autowired
    StatementRepository statementRepository;
    @Autowired
    AccountRepository accountRepository;

    @Test
    public void saveStatements() {
        MockMultipartFile file = new MockMultipartFile("statement", "statement.csv", "text/csv", "ACCOUNT_NUMBER,OPERATION_TIME,BENEFICIARY,COMMENT,AMOUNT,CURRENCY\nACC1,2022-05-29 21:45:27,Mantas,,100,EUR".getBytes(StandardCharsets.UTF_8));
        statementService.saveStatements(file);

        List<Statement> statements = statementRepository.findAll();

        assertEquals(statements.size(), 1);
        assertEquals(statements.get(0).getAccountNumber(), "ACC1");

        List<Account> accounts = accountRepository.findAll();

        assertEquals(accounts.size(), 1);
        assertEquals(accounts.get(0).getAccountNumber(), "ACC1");
    }
}
