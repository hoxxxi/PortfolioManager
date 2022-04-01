package MarketData;

public class TradeEvent {
    private final String symbol;
    private final long quantity;
    private final double price;

    public TradeEvent(String symbol, long quantity, double price) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
    }

    public void printTradeDetails() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Trade for ");
        stringBuilder.append("symbol: ");
        stringBuilder.append(symbol);
        stringBuilder.append("\tquantity: ");
        stringBuilder.append(quantity);
        stringBuilder.append("\tprice: ");
        stringBuilder.append(price);
        stringBuilder.append("\n");
        String output =  stringBuilder.toString();
        System.out.println(output);
    }

    public String getSymbol() {
        return symbol;
    }

    public long getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
