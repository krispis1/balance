package lt.inventi.balance.controller;

import lt.inventi.balance.service.AccountService;
import lt.inventi.balance.util.StatementCsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/balance")
    public ResponseEntity<String> getBalance(@RequestParam String accountNumber ,@RequestParam Optional<String> tsFrom, @RequestParam Optional<String> tsTo) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body("Balance: " + accountService.calculateBalance(accountNumber ,tsFrom.orElseGet(() -> ""), tsTo.orElseGet(() -> "")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Could not calculate balance.\nReason: " + e.getMessage());
        }
    }
}
