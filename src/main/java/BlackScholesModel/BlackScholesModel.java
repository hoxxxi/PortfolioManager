package BlackScholesModel;

public class BlackScholesModel {
    /**
     * @param price reference price in the future
     * @param discountRate annualised discount rate
     * @param time number of years until price
     * @return present value of input price
     */
    static double presentValue(double price, double discountRate, double time) {
        return price * Math.exp(-1 * discountRate * time);
    }

    /**
     * Computes normal integral by using a rational polynomial approximation
     * This approximation is from Example 9.7.3 of Fike, C.T. (1968),
     * Computer Evaluation of Mathematical Functions, Englewood Cliffs, N.J., Prentice Hall
     *
     * Let P(x) be the integral of the normal density from 0 to x.
     * The best minimax approximation R(x) to P(x) in the range [0,infinity)
     * among the class of rational functions V5,5[0,infinity) satisfying
     * R(0) = P(0) = 0, and lim x tends to infinity R(x) = lim x tends to infinity P(x) = 0.5
     * is the function:
     *       a1 + a2*x + a3*x^2 + a4*x^3  + a5*x^4  + a6*x^5
     *     ----------------------------------------------------
     *       b1 + b2*x + b3*x^2 + b4*x^3  + b5*x^4  + b6*x^5
     * where the constants a1, a2, ..., a6 and b1, b2, ..., b6
     * are as defined below.
     *
     * @param stdDeviation from mean for which need cumulative normal distribution
     * @return cumulative normal distribution in the range 0.0 to 0.(9)
     */
    static double cumulativeNormalDistribution(double stdDeviation) {
        final double a1 = 0;
        final double a2 = 9.050508;
        final double a3 = 0.767742;
        final double a4 = 1.666902;
        final double a5 = -0.624298;
        final double a6 = 0.5;
        final double b1 = 22.601228;
        final double b2 = 2.776898;
        final double b3 = 5.148169;
        final double b4 = 2.995582;
        final double b5 = -1.238661;
        final double b6 = 1;

        double absoluteDeviation = Math.abs(stdDeviation);
        double numerator = ((((a6 * absoluteDeviation + a5) * absoluteDeviation + a4) * absoluteDeviation + a3) * absoluteDeviation + a2) * absoluteDeviation + a1;
        double denominator = ((((b6 * absoluteDeviation + b5) * absoluteDeviation + b4) * absoluteDeviation + b3) * absoluteDeviation + b2) * absoluteDeviation + b1;
        double offset = numerator / denominator;

        return stdDeviation > 0 ? 0.5 + offset : 0.5 - offset;
    }

    /**
     * @param spot current market price
     * @param strike price at which we can execute contract
     * @param discountRate annualised interest rate
     * @param stockVolatility stock annualised volatility
     * @param timeToMaturity number of years until contract maturity
     * @return the value of the call option contract
     */
    public static double getCallValue(double spot, double strike, double discountRate, double stockVolatility, double timeToMaturity) {
        double d1 = (Math.log(spot/strike) + ((discountRate +(Math.pow(stockVolatility,2)/2)) * timeToMaturity)) / (stockVolatility * Math.sqrt(timeToMaturity));
        double d2 = d1 - (stockVolatility * Math.sqrt(timeToMaturity));
        return spot * cumulativeNormalDistribution(d1) - presentValue(strike, discountRate, timeToMaturity) * cumulativeNormalDistribution(d2);
    }

    /**
     * @param spot current market price
     * @param strike price at which we can execute contract
     * @param discountRate annualised interest rate
     * @param stockVolatility stock annualised volatility
     * @param timeToMaturity number of years until contract maturity
     * @return the value of the put option contract
     */
    public static double getPutValue(double spot, double strike, double discountRate, double stockVolatility, double timeToMaturity) {
        double d1 = (Math.log(spot/strike) + ((discountRate +(Math.pow(stockVolatility,2)/2)) * timeToMaturity)) / (stockVolatility * Math.sqrt(timeToMaturity));
        double d2 = d1 - (stockVolatility * Math.sqrt(timeToMaturity));
        return presentValue(strike, discountRate, timeToMaturity) * cumulativeNormalDistribution(-1 * d2) - spot * cumulativeNormalDistribution(-1 * d1);
    }
}
