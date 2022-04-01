package Instrument;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class EquityInstrumentDataProvider implements InstrumentDataProvider {
    private Map<String, Instrument> instrumentData;

    public EquityInstrumentDataProvider(File instrumentFile) {
        instrumentData = new HashMap<>();
        loadEquityInstrumentData(instrumentFile);
    }

    @Override
    public Instrument getInstrument(String symbol) {
        return instrumentData.get(symbol);
    }

    @Override
    public Set<String> getInstrumentUniverse() {
        return instrumentData.keySet();
    }

    private boolean validEquityHeader(String line) {
        if(line.equals("symbol,securityType,lastClosePrice,averageDailyVolume,volatility"))
            return true;
        return false;
    }

    private void loadEquityInstrumentData(File instrumentFile) {
        try {
            Scanner scanner = new Scanner(instrumentFile);
            boolean headerRead = false;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(!headerRead) {
                    if(!validEquityHeader(line)) {
                        System.err.println("ERROR processing equity instrument data header for "+instrumentFile.getPath());
                        break;
                    }
                    headerRead = true;
                    continue;
                }
                try {
                    String[] details = line.split(",");
                    String symbol = details[0];
                    SecurityType securityType = SecurityType.valueOf(details[1]);
                    double lastClosePrice = Double.parseDouble(details[2]);
                    long averageDailyVolume = Long.parseLong(details[3]);
                    double volatility = Double.parseDouble(details[4]);
                    Instrument instrument = new EquityInstrumentImpl(symbol, securityType, lastClosePrice, averageDailyVolume, volatility);
                    instrumentData.put(symbol, instrument);
                } catch (Exception e) {
                    System.err.println("ERROR processing instrument data line: "+line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
