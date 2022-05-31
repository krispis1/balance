package lt.inventi.balance.util;

import static org.junit.Assert.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
public class CurrencyUtilTest {
    @Test
    public void convertEurToGbp() {
        Double converted = CurrencyUtil.convertCurrency(CurrencyUtil.Currency.EUR, CurrencyUtil.Currency.GBP, 1D);

        assertEquals(converted, 0.85, 0);
    }

    @Test
    public void convertEurToUsd() {
        Double converted = CurrencyUtil.convertCurrency(CurrencyUtil.Currency.EUR, CurrencyUtil.Currency.USD, 1D);

        assertEquals(converted, 1.08, 0);
    }

    @Test
    public void convertUsdToEur() {
        Double converted = CurrencyUtil.convertCurrency(CurrencyUtil.Currency.USD, CurrencyUtil.Currency.EUR, 1D);

        assertEquals(converted, 0.93, 0);
    }

    @Test
    public void convertUsdToGbp() {
        Double converted = CurrencyUtil.convertCurrency(CurrencyUtil.Currency.USD, CurrencyUtil.Currency.GBP, 1D);

        assertEquals(converted, 0.79, 0);
    }

    @Test
    public void convertGbpToEur() {
        Double converted = CurrencyUtil.convertCurrency(CurrencyUtil.Currency.GBP, CurrencyUtil.Currency.EUR, 1D);

        assertEquals(converted, 1.17, 0);
    }

    @Test
    public void convertGbpToUsd() {
        Double converted = CurrencyUtil.convertCurrency(CurrencyUtil.Currency.GBP, CurrencyUtil.Currency.USD, 1D);

        assertEquals(converted, 1.26, 0);
    }
}
