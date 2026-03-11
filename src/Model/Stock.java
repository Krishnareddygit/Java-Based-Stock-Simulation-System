package Model;

import java.util.concurrent.atomic.AtomicLong;

public class Stock {

    private final String stockSymbol;
    private final AtomicLong availableQuantity;

    public Stock(String stockSymbol, long quantity) {
        this.stockSymbol = stockSymbol;
        this.availableQuantity = new AtomicLong(quantity);
    }

    public boolean buyStocks(int quantity) {

        while (true) {

            long current = availableQuantity.get();

            if (quantity > current)
                return false;

            long updated = current - quantity;

            if (availableQuantity.compareAndSet(current, updated))
                return true;
        }
    }

    public void sellStocks(int quantity) {

        availableQuantity.addAndGet(quantity);
    }

    public long getAvailableQuantity() {
        return availableQuantity.get();
    }

    public String getStockSymbol() {
        return stockSymbol;
    }
}