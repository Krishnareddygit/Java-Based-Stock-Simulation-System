package Service;

import Model.Stock;
import Model.Trade;
import Model.TradeRequest;
import Model.TradeType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.*;

public class TradingSystem {

    private final ConcurrentHashMap<String, Stock> stocks = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, ConcurrentHashMap<String,Integer>> userportfolio = new ConcurrentHashMap<>();

    private final ConcurrentLinkedQueue<Trade> successfulTrades =  new ConcurrentLinkedQueue<>();

    private final ExecutorService executor;

    public TradingSystem(int threadcount) {
        executor = Executors.newFixedThreadPool(threadcount);
    }

    public void addStock(Stock stock){
        stocks.put(stock.getStockSymbol(),stock);
    }

    public void submitTrade(TradeRequest request){
        executor.submit(() -> processTrade(request));
    }

    public void processTrade(TradeRequest request){
        Stock stock = stocks.get(request.getStockSymbol());
        if(stock == null) return;

        userportfolio.putIfAbsent(request.getUserId(),
                new ConcurrentHashMap<>());

        ConcurrentHashMap<String,Integer> portfolio =
                userportfolio.get(request.getUserId());

        boolean success = false;

        if(request.getTradeType() == TradeType.BUY){

            success = stock.buyStocks(request.getQuantity());

            if(success){
                portfolio.merge(
                        request.getStockSymbol(),
                        request.getQuantity(),
                        Integer::sum
                );
            }

        } else {

            int owned =
                    portfolio.getOrDefault(
                            request.getStockSymbol(),0);

            if(owned >= request.getQuantity()){

                portfolio.put(
                        request.getStockSymbol(),
                        owned - request.getQuantity());

                stock.sellStocks(request.getQuantity());

                success = true;
            }
        }

        if(success){

            Trade trade = new Trade(
                    request.getUserId(),
                    request.getStockSymbol(),
                    request.getQuantity(),
                    request.getTradeType()
            );

            successfulTrades.add(trade);
        }
    }

    public void shutdownAndAwaitTermination(){

        executor.shutdown();

        try{
            executor.awaitTermination(1,TimeUnit.MINUTES);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    public ConcurrentLinkedQueue<Trade> getSuccessfulTrades(){
        return successfulTrades;
    }
}