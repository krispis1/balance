package lt.inventi.balance.service;

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

    public void saveStatements(MultipartFile file) {
        try {
            List<Statement> statements = StatementCsvUtil.parseCsv(file.getInputStream());
            statementRepository.saveAll(statements);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store CSV data: " + e.getMessage());
        }
    }

    public ByteArrayInputStream exportStatements(Timestamp tsFrom, Timestamp tsTo) {
        List<Statement> statements = statementRepository.findAll();
        return StatementCsvUtil.buildCsv(statements);
    }

    public List<Statement> getAllStatements() {
        return statementRepository.findAll();
    }
}
