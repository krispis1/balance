package lt.inventi.balance.service;

import lt.inventi.balance.model.Account;
import lt.inventi.balance.model.Statement;
import lt.inventi.balance.repository.StatementRepository;
import lt.inventi.balance.util.StatementCsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@Service
public class StatementService {
    @Autowired
    private StatementRepository statementRepository;
    @Autowired
    private AccountService accountService;

    public void saveStatements(MultipartFile file) {
        try {
            List<Statement> statements = StatementCsvUtil.parseCsv(file.getInputStream());
            for (Statement statement : statements) {
                Account account = accountService.saveAccount(statement.getAccountNumber(), statement.getCurrency());
                accountService.updateBalance(account, statement.getAmount(), statement.getCurrency());
                statement.setAccount(account);
            }
            statementRepository.saveAll(statements);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store CSV data: " + e.getMessage());
        }
    }

    public ByteArrayInputStream exportAllStatements() {
        List<Statement> statements = statementRepository.findAll();
        return StatementCsvUtil.buildCsv(statements);
    }

    public ByteArrayInputStream exportAllStatementsUntil(String tsTo) {
        try {
            List<Statement> statements = statementRepository.findAllWithOperationTimeBefore(Timestamp.valueOf(tsTo));
            return StatementCsvUtil.buildCsv(statements);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error encountered when exporting statements: " + e.getMessage());
        }
    }

    public ByteArrayInputStream exportAllStatementsAfter(String tsFrom) {
        try {
            List<Statement> statements = statementRepository.findAllWithOperationTimeAfter(Timestamp.valueOf(tsFrom));
            return StatementCsvUtil.buildCsv(statements);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error encountered when exporting statements: " + e.getMessage());
        }
    }

    public ByteArrayInputStream exportAllStatementsBetween(String tsFrom, String tsTo) {
        try {
            List<Statement> statements = statementRepository.findAllByOperationTimeBetween(Timestamp.valueOf(tsFrom), Timestamp.valueOf(tsTo));
            return StatementCsvUtil.buildCsv(statements);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error encountered when exporting statements: " + e.getMessage());
        }
    }
}
