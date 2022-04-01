package Instrument;

public class EquityInstrumentImpl implements EquityInstrument{
    private final String symbol;
    private final SecurityType securityType;
    private final double lastClosePrice;
    private final long averageDailyVolume;
    private final double volatility;

    public EquityInstrumentImpl(String symbol, SecurityType securityType, double lastClosePrice, long averageDailyVolume, double volatility) {
        this.symbol = symbol;
        this.securityType = securityType;
        this.lastClosePrice = lastClosePrice;
        this.averageDailyVolume = averageDailyVolume;
        this.volatility = volatility;
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
    public double getLastClosePrice() {
        return lastClosePrice;
    }

    @Override
    public long getAverageDailyVolume() {
        return averageDailyVolume;
    }

    @Override
    public double getVolatility() {
        return volatility;
    }
}
