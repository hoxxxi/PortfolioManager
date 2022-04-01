import Instrument.EquityInstrumentDataProvider;
import Instrument.OptionsInstrumentDataProvider;
import MarketData.EquityMarketDataProvider;
import PortfolioManager.Portfolio;

import java.io.File;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        File equityInstrumentFile = new File(Objects.requireNonNull(Main.class.getResource("productdata/equityInstrumentData.csv")).getPath());
        File optionsInstrumentFile = new File(Objects.requireNonNull(Main.class.getResource("productdata/optionsInstrumentData.csv")).getPath());
        File positionsFile = new File(Objects.requireNonNull(Main.class.getResource("initialPosition.csv")).getPath());
        Portfolio portfolio = new Portfolio(positionsFile, equityInstrumentFile, optionsInstrumentFile);
        Thread marketDataProvider = new Thread(new EquityMarketDataProvider(portfolio));
        marketDataProvider.start();
    }
}
