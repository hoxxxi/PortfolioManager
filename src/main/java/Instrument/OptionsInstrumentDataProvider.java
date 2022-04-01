package Instrument;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class OptionsInstrumentDataProvider implements InstrumentDataProvider{
    private Map<String, Instrument> instrumentData;

    public OptionsInstrumentDataProvider(File instrumentFile) {
        instrumentData = new HashMap<>();
        loadOptionsInstrumentData(instrumentFile);
    }

    @Override
    public Instrument getInstrument(String symbol) {
        return instrumentData.get(symbol);
    }

    @Override
    public Set<String> getInstrumentUniverse() {
        return instrumentData.keySet();
    }

    private boolean validOptionsHeader(String line) {
        if(line.equals("symbol,securityType,underlyingStock,strike,maturity,averageDailyVolume,interestRate"))
            return true;
        return false;
    }

    public void loadOptionsInstrumentData(File instrumentFile) {
        try {
            Scanner scanner = new Scanner(instrumentFile);
            boolean headerRead = false;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(!headerRead) {
                    if(!validOptionsHeader(line)) {
                        System.err.println("ERROR processing options instrument data header for "+instrumentFile.getPath());
                        break;
                    }
                    headerRead = true;
                    continue;
                }
                try {
                    String[] details = line.split(",");
                    String symbol = details[0];
                    SecurityType securityType = SecurityType.valueOf(details[1]);
                    String underlyingStock = details[2];
                    int strike = Integer.parseInt(details[3]);
                    String maturity = details[4];
                    long averageDailyVolume = Long.parseLong(details[5]);
                    double interestRate = Double.parseDouble(details[6]);
                    Instrument instrument = new OptionsInstrumentImpl(symbol, securityType, underlyingStock, strike, maturity, averageDailyVolume, interestRate);
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
