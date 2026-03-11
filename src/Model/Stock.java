package Model;

public class Stock {
    private final String stockSymbol;
    private long availableQuantity;

    public Stock(String stockSymbol, long availableQuantity) {
        this.stockSymbol = stockSymbol;
        this.availableQuantity = availableQuantity;
    }


    public synchronized boolean buyStocks(int quantity){
        if(quantity <= 0) return false;
        if(quantity <= availableQuantity){
            availableQuantity -= quantity;
            return true;
        }
        return false;
    }


    public synchronized boolean sellStocks(int quantity){
        if(quantity <= 0) return false;
        availableQuantity += quantity;
        return true;
    }


    public String getStockSymbol() {
        return stockSymbol;
    }

    public synchronized long getAvailableQuantity() {
        return availableQuantity;
    }
}
