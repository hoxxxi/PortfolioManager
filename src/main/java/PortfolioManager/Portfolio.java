package PortfolioManager;

import BlackScholesModel.BlackScholesModel;
import Instrument.EquityInstrumentDataProvider;
import Instrument.Instrument;
import Instrument.EquityInstrument;
import Instrument.OptionsInstrumentImpl;
import Instrument.OptionsInstrumentDataProvider;
import Instrument.SecurityType;
import MarketData.TradeEvent;
import MarketData.MarketDataHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Portfolio implements MarketDataHandler {
    private Map<String, AssetPosition> positionsMap;
    private EquityInstrumentDataProvider equityInstrumentDataProvider;
    private OptionsInstrumentDataProvider optionsInstrumentDataProvider;

    public Portfolio(File positionsFile, File equityInstrumentData, File optionsInstrumentData) {
        positionsMap = new LinkedHashMap<>();
        loadInitialPositions(positionsFile);
        equityInstrumentDataProvider = new EquityInstrumentDataProvider(equityInstrumentData);
        optionsInstrumentDataProvider = new OptionsInstrumentDataProvider(optionsInstrumentData);
        setupInitialAssetPrices();
    }

    private boolean validPositionHeader(String line) {
        if(line.equals("symbol,positionSize"))
            return true;
        return false;
    }

    // Basic validation for structure of the line in a position file
    private boolean validPosition(String line) {
        return line.chars().filter(ch -> ch == ',').count() == 1;
    }

    private void loadInitialPositions(File positionsFile) {
        try {
            Scanner scanner = new Scanner(positionsFile);
            boolean headerRead = false;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(!headerRead) {
                    if(!validPositionHeader(line)) {
                        System.err.println("ERROR processing equity instrument data header for "+positionsFile.getPath());
                        break;
                    }
                    headerRead = true;
                    continue;
                }
                if (validPosition(line)) {
                    String[] details = line.split(",");
                    String symbol = details[0];
                    long quantity = Long.parseLong(details[1]);
                    AssetPosition position = new AssetPosition(symbol, quantity, 0);
                    positionsMap.put(symbol, position);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setupInitialAssetPrices() {
        for(String instrumentSymbol : equityInstrumentDataProvider.getInstrumentUniverse()) {
            EquityInstrument instrument = (EquityInstrument) equityInstrumentDataProvider.getInstrument(instrumentSymbol);
            positionsMap.get(instrumentSymbol).setPrice(instrument.getLastClosePrice());
            updateOptionPrices(new TradeEvent(instrumentSymbol, instrument.getAverageDailyVolume() / 100, instrument.getLastClosePrice()));
        }
    }

    private void updateOptionPrices(TradeEvent tradeEvent) {
        for(String instrumentSymbol : optionsInstrumentDataProvider.getInstrumentUniverse()) {
            OptionsInstrumentImpl optionInstrumentData = (OptionsInstrumentImpl) optionsInstrumentDataProvider.getInstrument(instrumentSymbol);
            EquityInstrument stockInterestData = (EquityInstrument) equityInstrumentDataProvider.getInstrument(optionInstrumentData.getUnderlyingStock());
            if(!tradeEvent.getSymbol().equals(optionInstrumentData.getUnderlyingStock())) {
                continue;
            }

            double spot = tradeEvent.getPrice();
            double strike = optionInstrumentData.getStrike();
            double discountRate = optionInstrumentData.getInterestRate();
            double stockVolatility = stockInterestData.getVolatility();
            double timeToMaturity = optionInstrumentData.getMaturity();
            switch (optionInstrumentData.getSecurityType()) {
                case CALL: positionsMap.get(instrumentSymbol).setPrice(BlackScholesModel.getCallValue(spot, strike, discountRate, stockVolatility, timeToMaturity)); break;
                case PUT: positionsMap.get(instrumentSymbol).setPrice(BlackScholesModel.getPutValue(spot, strike, discountRate, stockVolatility, timeToMaturity)); break;
            }
        }
    }


    public void printPortfolioPosition() {
        System.out.println("## Portfolio");
        System.out.printf("%20s %15s %10s %15s%n", "symbol", "price", "qty", "value");

        for (String symbol : positionsMap.keySet()) {
            double price = positionsMap.get(symbol).getPrice();
            long qty = positionsMap.get(symbol).getQuantity();
            double notionalValue = positionsMap.get(symbol).getNotionalValue();
            System.out.printf("%20s %15f %10d %15f%n", symbol, price, qty, notionalValue);
        }
        System.out.printf("Total portfolio notional USD: %33f%n", getPortfolioNotionalValue());
        System.out.println();
    }

    public double getPortfolioNotionalValue() {
        double notionalValue = 0;
        for(String symbol : positionsMap.keySet()) {
            notionalValue+= positionsMap.get(symbol).getNotionalValue();
        }
        return notionalValue;
    }

    public EquityInstrumentDataProvider getEquityInstrumentDataProvider() {
        return equityInstrumentDataProvider;
    }

    @Override
    public synchronized void handle(TradeEvent event) {
        positionsMap.get(event.getSymbol()).setPrice(event.getPrice());
        Instrument tradeInstrument = equityInstrumentDataProvider.getInstrument(event.getSymbol());
        if(tradeInstrument != null && tradeInstrument.getSecurityType() == SecurityType.STOCK) {
            updateOptionPrices(event);
        }

        event.printTradeDetails();
        printPortfolioPosition();
    }
}
