package lt.inventi.balance.util;

import lt.inventi.balance.model.Statement;
import org.apache.commons.csv.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class StatementCsvUtil {
    public enum header {
        ACCOUNT_NUMBER, OPERATION_TIME, BENEFICIARY, COMMENT, AMOUNT, CURRENCY
    }
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
                statement.setAccountNumber(csvRecord.get(header.ACCOUNT_NUMBER));
                statement.setOperationTime(Timestamp.valueOf(csvRecord.get(header.OPERATION_TIME)));
                statement.setBeneficiary(csvRecord.get(header.BENEFICIARY));
                statement.setComment(csvRecord.get(header.COMMENT));
                statement.setAmount(Double.parseDouble(csvRecord.get(header.AMOUNT)));
                statement.setCurrency(Statement.Currency.valueOf(csvRecord.get(header.CURRENCY)));
                statements.add(statement);
            }
            return statements;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream buildCsv(List<Statement> statements) {
        final CSVFormat format = CSVFormat.DEFAULT.withHeader(header.class).withQuoteMode(QuoteMode.MINIMAL);
        try (
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)
        ) {
            for (Statement statement : statements) {
                csvPrinter.printRecord(
                        statement.getAccountNumber(),
                        statement.getOperationTime(),
                        statement.getBeneficiary(),
                        statement.getComment(),
                        statement.getAmount(),
                        statement.getCurrency()
                );
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to build CSV file: " + e.getMessage());
        }
    }
}
