package Model;

import java.time.LocalDateTime;

public class Trade {
    private final String userId;
    private final String stockSymbol;
    private final int quantity;
    private final TradeType tradeType;
    private final TradeStatus status;
    private final String message;
    private final LocalDateTime timestamp;

    public Trade(String userId,
                       String stockSymbol,
                       int quantity,
                       TradeType tradeType,
                       TradeStatus status,
                       String message) {

        this.userId = userId;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.tradeType = tradeType;
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getUserId() { return userId; }

    public String getStockSymbol() { return stockSymbol; }

    public int getQuantity() { return quantity; }

    public TradeType getTradeType() { return tradeType; }

    public TradeStatus getStatus() { return status; }

    public String getMessage() { return message; }

    public LocalDateTime getTimestamp() { return timestamp; }
}