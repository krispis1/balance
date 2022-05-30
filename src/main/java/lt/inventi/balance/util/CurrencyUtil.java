package lt.inventi.balance.util;

public class CurrencyUtil {
    public enum Currency {
        EUR, GBP, USD
    }

    public static Double convertCurrency(Currency statementCurrency, Currency accountCurrency, Double amount) {
        switch (statementCurrency) {
            case EUR:
                return convertEur(accountCurrency, amount);
            case USD:
                return convertUsd(accountCurrency, amount);
            case GBP:
                return convertGbp(accountCurrency, amount);
            default:
                throw new IllegalArgumentException("Currency not implemented");
        }
    }

    private static Double convertEur(Currency currency, Double amount) {
        switch (currency) {
            case GBP:
                return amount * 0.85;
            case USD:
                return amount * 1.08;
            default:
                return amount;
        }
    }

    private static Double convertUsd(Currency currency, Double amount) {
        switch (currency) {
            case GBP:
                return amount * 0.79;
            case EUR:
                return amount * 0.93;
            default:
                return amount;
        }
    }

    private static Double convertGbp(Currency currency, Double amount) {
        switch (currency) {
            case USD:
                return amount * 1.26;
            case EUR:
                return amount * 1.17;
            default:
                return amount;
        }
    }
}
