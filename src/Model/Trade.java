package Model;

import java.time.LocalDateTime;

public class Trade {

    private final String userId;
    private final String stockSymbol;
    private final int quantity;
    private final TradeType tradeType;
    private final LocalDateTime timestamp;

    public Trade(String userId, String stockSymbol, int quantity, TradeType tradeType) {
        this.userId = userId;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.tradeType = tradeType;
        this.timestamp = LocalDateTime.now();
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
