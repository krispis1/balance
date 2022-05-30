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

    public ByteArrayInputStream exportStatements(String tsFrom, String tsTo) {
        List<Statement> statements;

        if (tsFrom.isEmpty() && tsTo.isEmpty()) {
            statements = statementRepository.findAll();
        } else if (tsFrom.isEmpty()) {
            statements = statementRepository.findAllWithOperationTimeBefore(Timestamp.valueOf(tsTo));
        } else if (tsTo.isEmpty()) {
            statements = statementRepository.findAllWithOperationTimeAfter(Timestamp.valueOf(tsFrom));
        } else {
            statements = statementRepository.findAllByOperationTimeBetween(Timestamp.valueOf(tsFrom), Timestamp.valueOf(tsTo));
        }
        return StatementCsvUtil.buildCsv(statements);
    }
}
