package Instrument;

public interface OptionsInstrument extends Instrument {

    String getUnderlyingStock();

    public int getStrike();

    public double getMaturity();

    public long getAverageDailyVolume();

    public double getInterestRate();
}
