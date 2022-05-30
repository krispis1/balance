package lt.inventi.balance.controller;

import lt.inventi.balance.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<String> getBalance(@RequestParam String accountNumber, @RequestParam Optional<String> tsFrom, @RequestParam Optional<String> tsTo) {
        try {
            if (!tsFrom.isPresent() && !tsTo.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body("Balance: " + accountService.calculateFullBalance(accountNumber) + " " + accountService.getAccount(accountNumber).getCurrency());
            } else if (!tsFrom.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body("Balance: " + accountService.calculateBalanceUntil(accountNumber, tsTo.orElseGet(() -> "")) + " " + accountService.getAccount(accountNumber).getCurrency());
            } else if (!tsTo.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body("Balance: " + accountService.calculateBalanceFrom(accountNumber, tsFrom.orElseGet(() -> "")) + " " + accountService.getAccount(accountNumber).getCurrency());
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("Balance: " + accountService.calculateBalanceBetween(accountNumber ,tsFrom.orElseGet(() -> ""), tsTo.orElseGet(() -> "")) + " " + accountService.getAccount(accountNumber).getCurrency());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Could not calculate balance.\nReason: " + e.getMessage());
        }
    }
}
