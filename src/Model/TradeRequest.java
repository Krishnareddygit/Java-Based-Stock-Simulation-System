package Model;

public class TradeRequest {

    private final String userId;
    private final String stockSymbol;
    private final int quantity;
    private final TradeType tradeType;

    public TradeRequest(String userId, String stockSymbol, int quantity, TradeType tradeType) {
        this.userId = userId;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.tradeType = tradeType;
    }

    public String getUserId() {
        return userId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public TradeType getTradeType() {
        return tradeType;
    }
}
