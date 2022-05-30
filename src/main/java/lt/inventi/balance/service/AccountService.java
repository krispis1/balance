package lt.inventi.balance.service;

import lt.inventi.balance.model.Account;
import lt.inventi.balance.util.CurrencyUtil;
import lt.inventi.balance.util.CurrencyUtil.Currency;
import lt.inventi.balance.repository.AccountRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account saveAccount(String accountNumber, Currency currency) {
        if (!accountRepository.existsByAccountNumber(accountNumber)) {
            Account account = new Account();
            account.setAccountNumber(accountNumber);
            account.setAmount(0D);
            account.setCurrency(currency);
            return accountRepository.saveAndFlush(account);
        }
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public void updateBalance(Account account, Double amount, Currency statementCurrency) {
        account.setAmount(account.getAmount() + CurrencyUtil.convertCurrency(statementCurrency, account.getCurrency(), amount));
        accountRepository.save(account);
    }

    public Double calculateBalance(String accountNumber, String tsFrom, String tsTo) {
        if (tsFrom.isEmpty() && tsTo.isEmpty()) {
            try {
                return accountRepository.findByAccountNumber(accountNumber).getAmount();
            } catch (Exception e) {
                throw new NullPointerException("Account not found: " + accountNumber);
            }
        } else if (tsFrom.isEmpty()) {
            try {
                return accountRepository.getBalanceUntil(Timestamp.valueOf(tsTo), accountNumber);
            } catch (Exception e) {
                throw new NullPointerException("Account not found: " + accountNumber);
            }
        } else if (tsTo.isEmpty()) {
            try {
                return accountRepository.getBalanceFrom(Timestamp.valueOf(tsFrom), accountNumber);
            } catch (Exception e) {
                throw new NullPointerException("Account not found: " + accountNumber);
            }
        } else {
            try {
                return accountRepository.getBalanceBetween(Timestamp.valueOf(tsFrom), Timestamp.valueOf(tsTo), accountNumber);
            } catch (Exception e) {
                throw new NullPointerException("Account not found: " + accountNumber);
            }
        }
    }
}
