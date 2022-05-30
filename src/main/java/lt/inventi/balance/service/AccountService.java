package lt.inventi.balance.service;

import lt.inventi.balance.model.Account;
import lt.inventi.balance.util.CurrencyUtil;
import lt.inventi.balance.util.CurrencyUtil.Currency;
import lt.inventi.balance.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account getAccount(String accountNumber) {
        if (accountRepository.existsByAccountNumber(accountNumber)) {
            return accountRepository.findByAccountNumber(accountNumber);
        } else {
            throw new RuntimeException("Account not found: " + accountNumber);
        }
    }

    public Account saveAccount(String accountNumber, Currency currency) {
        if (accountNumber.isEmpty())
            throw new RuntimeException("Account number can't be empty");

        if (currency == null)
            throw new RuntimeException("Currency can't be empty");

        if (!accountRepository.existsByAccountNumber(accountNumber)) {
            Account account = new Account();
            account.setAccountNumber(accountNumber);
            account.setAmount(0D);
            account.setCurrency(currency);
            return accountRepository.save(account);
        }
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public void updateBalance(Account account, Double amount, Currency statementCurrency) {
        account.setAmount(account.getAmount() + CurrencyUtil.convertCurrency(statementCurrency, account.getCurrency(), amount));
        accountRepository.save(account);
    }

    public Double calculateFullBalance(String accountNumber) {
        try {
            return getAccount(accountNumber).getAmount();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Encountered error while calculating balance: " + e.getMessage());
        }
    }

    public Double calculateBalanceUntil(String accountNumber, String tsTo) {
        try {
            Double amount = accountRepository.getBalanceUntil(Timestamp.valueOf(tsTo), accountNumber);
            if (amount != null) {
                return amount;
            } else {
                throw new NullPointerException("Amount doesn't exist until date: " + tsTo);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Encountered error while calculating balance: " + e.getMessage());
        }
    }

    public Double calculateBalanceFrom(String accountNumber, String tsFrom) {
        try {
            Double amount = accountRepository.getBalanceFrom(Timestamp.valueOf(tsFrom), accountNumber);
            if (amount != null) {
                return amount;
            } else {
                throw new NullPointerException("Amount doesn't exist from date: " + tsFrom);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Encountered error while calculating balance: " + e.getMessage());
        }
    }

    public Double calculateBalanceBetween(String accountNumber, String tsFrom, String tsTo) {
        try {
            Double amount = accountRepository.getBalanceBetween(Timestamp.valueOf(tsFrom), Timestamp.valueOf(tsTo), accountNumber);
            if (amount != null) {
                return amount;
            } else {
                throw new NullPointerException("Amount doesn't exist for dates: " + tsFrom + " / " + tsTo);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Encountered error while calculating balance: " + e.getMessage());
        }
    }
}
