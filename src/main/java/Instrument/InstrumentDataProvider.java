package Instrument;

import java.util.Set;

public interface InstrumentDataProvider {

    public Set<String> getInstrumentUniverse();

    public Instrument getInstrument(String symbol);
}
