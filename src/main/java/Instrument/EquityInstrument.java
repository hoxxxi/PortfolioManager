package Instrument;

public interface EquityInstrument extends Instrument {

    public double getLastClosePrice();

    public long getAverageDailyVolume();

    public double getVolatility();
}
