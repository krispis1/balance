package lt.inventi.balance.util;

import lt.inventi.balance.model.Statement;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {
    static final String TYPE = "text/csv";

    public static boolean isCsv(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<Statement> parseCsv(InputStream is) {
        try (
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())
        ) {
            List<Statement> statements = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                Statement statement = new Statement();
                statement.setAccountNumber(csvRecord.get("Account number"));
                statement.setOperationTime(Timestamp.valueOf(csvRecord.get("Operation time")));
                statement.setBeneficiary(csvRecord.get("Beneficiary"));
                statement.setComment(csvRecord.get("Comment"));
                statement.setAmount(Double.parseDouble(csvRecord.get("Amount")));
                statement.setCurrency(Statement.Currency.valueOf(csvRecord.get("Currency")));
                statements.add(statement);
            }
            return statements;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
    }
}
