package Instrument;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class OptionsInstrumentImpl implements OptionsInstrument{
    private final String symbol;
    private final SecurityType securityType;
    private final String underlyingStock;
    private final long averageDailyVolume;
    private final int strike;
    private final double maturity;
    private final double interestRate;

    public OptionsInstrumentImpl(String symbol, SecurityType securityType, String underlyingStock, int strike, String maturity, long averageDailyVolume, double interestRate) throws ParseException {
        this.symbol = symbol;
        this.securityType = securityType;
        this.underlyingStock = underlyingStock;
        this.averageDailyVolume = averageDailyVolume;
        this.strike = strike;
        this.interestRate = interestRate;
        this.maturity = getNumericMaturity(maturity);
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public SecurityType getSecurityType() {
        return securityType;
    }

    @Override
    public String getUnderlyingStock() {
        return underlyingStock;
    }

    @Override
    public long getAverageDailyVolume() {
        return averageDailyVolume;
    }

    @Override
    public double getInterestRate() {
        return interestRate;
    }

    @Override
    public int getStrike() {
        return strike;
    }

    @Override
    public double getMaturity() {
        return maturity;
    }

    private double getNumericMaturity(String maturity) throws ParseException {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final Date date = format.parse(maturity);
        final long millis = date.getTime() - System.currentTimeMillis();
        return TimeUnit.MILLISECONDS.toDays(millis) / 365d;
    }
}
