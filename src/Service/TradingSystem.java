package Service;

import Model.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.*;

import java.util.concurrent.*;

public class TradingSystem {

    private final ConcurrentHashMap<String, Stock> stocks =
            new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String,
            ConcurrentHashMap<String,Integer>> userPortfolios =
            new ConcurrentHashMap<>();

    private final ConcurrentLinkedQueue<Trade> tradeResults =
            new ConcurrentLinkedQueue<>();

    private final ExecutorService executor;

    public TradingSystem(int threadCount){
        executor = Executors.newFixedThreadPool(threadCount);
    }

    public void addStock(Stock stock){
        stocks.put(stock.getStockSymbol(),stock);
    }

    public void submitTrade(TradeRequest request){
        executor.submit(() -> processTrade(request));
    }

    private void processTrade(TradeRequest request){

        Stock stock = stocks.get(request.getStockSymbol());

        if(stock == null){
            tradeResults.add(
                    new Trade(
                            request.getUserId(),
                            request.getStockSymbol(),
                            request.getQuantity(),
                            request.getTradeType(),
                            TradeStatus.FAILED,
                            "Stock not found"
                    )
            );
            return;
        }

        userPortfolios.putIfAbsent(
                request.getUserId(),
                new ConcurrentHashMap<>());

        ConcurrentHashMap<String,Integer> portfolio =
                userPortfolios.get(request.getUserId());

        boolean success = false;
        String message = "";

        if(request.getTradeType() == TradeType.BUY){

            success = stock.buyStocks(request.getQuantity());

            if(success){

                portfolio.merge(
                        request.getStockSymbol(),
                        request.getQuantity(),
                        Integer::sum);

                message = "Buy successful";
            }
            else{
                message = "Insufficient market stock";
            }

        }
        else{

            int owned =
                    portfolio.getOrDefault(
                            request.getStockSymbol(),0);

            if(owned >= request.getQuantity()){

                portfolio.put(
                        request.getStockSymbol(),
                        owned - request.getQuantity());

                stock.sellStocks(request.getQuantity());

                success = true;
                message = "Sell successful";
            }
            else{
                message = "Insufficient user holdings";
            }
        }

        Trade result =
                new Trade(
                        request.getUserId(),
                        request.getStockSymbol(),
                        request.getQuantity(),
                        request.getTradeType(),
                        success ? TradeStatus.SUCCESS : TradeStatus.FAILED,
                        message
                );

        tradeResults.add(result);
    }

    public void shutdownAndAwaitTermination(){

        executor.shutdown();

        try{
            executor.awaitTermination(1,TimeUnit.MINUTES);
        }
        catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    public ConcurrentLinkedQueue<Trade> getTradeResults(){
        return tradeResults;
    }
}