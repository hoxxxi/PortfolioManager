package PortfolioManager;

public class AssetPosition {
    private final String symbol;
    private final long quantity;
    private double price;

    public AssetPosition(String symbol, long quantity, double price) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public double getNotionalValue() {
        return price * quantity;
    }
}
