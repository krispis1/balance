package lt.inventi.balance.service;

import static org.junit.Assert.*;
import lt.inventi.balance.model.Account;
import lt.inventi.balance.repository.AccountRepository;
import lt.inventi.balance.util.CurrencyUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
public class AccountServiceTest {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Test
    public void createAccountValid() {
        accountService.saveAccount("ACC1", CurrencyUtil.Currency.EUR);

        List<Account> accounts = accountRepository.findAll();

        assertEquals(accounts.size(), 1);
        assertEquals(accounts.get(0).getAccountNumber(), "ACC1");
        assertEquals(accounts.get(0).getCurrency(), CurrencyUtil.Currency.EUR);
    }

    @Test
    public void createAccountEmptyAccountNumber() {
        try {
            accountService.saveAccount("", CurrencyUtil.Currency.EUR);
        } catch (RuntimeException e) {
            assertEquals("Account number can't be empty", e.getMessage());
        }

        List<Account> accounts = accountRepository.findAll();
        assertEquals(accounts.size(), 0);
    }
}
