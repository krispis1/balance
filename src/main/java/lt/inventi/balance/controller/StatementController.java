package lt.inventi.balance.controller;

import lt.inventi.balance.service.StatementService;
import lt.inventi.balance.util.StatementCsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/statement")
public class StatementController {
    @Autowired
    private StatementService statementService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (StatementCsvUtil.isCsv(file)) {
            try {
                statementService.saveStatements(file);
                return ResponseEntity.status(HttpStatus.OK).body("Uploaded the file successfully: " + file.getOriginalFilename());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Could not upload the file: " + file.getOriginalFilename() + "\nReason: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload a csv file!");
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> exportFile(@RequestParam(required = false, defaultValue = "") String tsFrom, @RequestParam(required = false, defaultValue = "") String tsTo) {
        InputStreamResource file = new InputStreamResource(statementService.exportStatements(tsFrom, tsTo));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"statements.csv\"")
                .contentType(MediaType.parseMediaType(StatementCsvUtil.TYPE))
                .body(file);
    }
}
