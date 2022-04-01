package MarketData;

import Instrument.EquityInstrument;
import Instrument.EquityInstrumentDataProvider;
import PortfolioManager.Portfolio;

import java.util.Iterator;
import java.util.Set;

public class EquityMarketDataProvider implements Runnable{
    private Portfolio portfolio;
    private EquityInstrumentDataProvider instrumentData;

    public EquityMarketDataProvider(Portfolio portfolio) {
        this.portfolio = portfolio;
        this.instrumentData = portfolio.getEquityInstrumentDataProvider();
    }

    @Override
    public void run() {
        long sleepIntervalMin = 500;
        long sleepIntervalMax = 2000;
        long timeToSleep = (long) (sleepIntervalMin + (sleepIntervalMax-sleepIntervalMin * Math.random()));

        while(true) {
            portfolio.handle(getNextTradeEvent());
            try {
                Thread.sleep(timeToSleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private TradeEvent getNextTradeEvent() {
        //Get a random symbol from the supported stock universe
        Set<String> instrumentSet = instrumentData.getInstrumentUniverse();
        long randomElementPosition = Math.round(Math.random()*(instrumentSet.size()-1));

        String symbol = "";
        Iterator<String> iterator = instrumentSet.iterator();
        while (iterator.hasNext() && randomElementPosition>=0) {
            symbol = iterator.next();
            randomElementPosition--;
        }

        EquityInstrument instrument = (EquityInstrument) instrumentData.getInstrument(symbol);
        long qty = Math.round(instrument.getAverageDailyVolume()*Math.random()/1000)+1; // Random qty up to 0.1% of average daily volume
        double price = instrument.getLastClosePrice()*(1+((Math.random()*0.02)-0.01)); //Random price within 1% of yesterday's close

        return new TradeEvent(symbol, qty, price);
    }
}
