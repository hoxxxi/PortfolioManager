package BlackScholesModel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BlackScholesModelTest {

    @Test
    public void getCallValueTest() {
        double spot = 110.0; //spot
        double strike = 100.0; //strike
        double rate = 0.02; // interest rate for 1 year
        double stockVolatility = 0.35; //stock volatility
        double time = 0.5; // expiry in 6 months

        assertEquals(16.70649582448866d, BlackScholesModel.getCallValue(spot, strike, rate, stockVolatility, time), 0.0000000001d);
    }

    @Test
    public void getPutValueTest() {
        double spot = 110.0; //spot
        double strike = 100.0; //strike
        double rate = 0.02; // interest rate for 1 year
        double stockVolatility = 0.35; //stock volatility
        double time = 0.5; // expiry in 6 months

        assertEquals(5.71147919940546d, BlackScholesModel.getPutValue(spot, strike, rate, stockVolatility, time), 0.0000000001d);
    }

    @Test
    public void cumulativeNormalDistributionTest() {
        assertEquals(0.01074503614231d, BlackScholesModel.cumulativeNormalDistribution(-2.3), 0.0000000001d);
        assertEquals(0.04452959052997d, BlackScholesModel.cumulativeNormalDistribution(-1.7), 0.0000000001d);
        assertEquals(0.30854682705034d, BlackScholesModel.cumulativeNormalDistribution(-0.5), 0.0000000001d);
        assertEquals(0.50000000000000d, BlackScholesModel.cumulativeNormalDistribution(0.0), 0.0000000001d);
        assertEquals(0.69145317294965d, BlackScholesModel.cumulativeNormalDistribution(0.5), 0.0000000001d);
        assertEquals(0.95547040947002d, BlackScholesModel.cumulativeNormalDistribution(1.7), 0.0000000001d);
        assertEquals(0.98925496385768d, BlackScholesModel.cumulativeNormalDistribution(2.3), 0.0000000001d);
    }

    @Test
    public void presentValueTest() {
        assertEquals(95.1229424500714d, BlackScholesModel.presentValue(100, 0.1, 0.5), 0.0000000001d);
    }
}
