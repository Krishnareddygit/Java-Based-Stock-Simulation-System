package Simulator;

import java.util.Random;

import Service.TradingSystem;
import Constants.TradingConstants;
import Analytics.ReportGenerator;
import Model.*;

public class TradingSimulationRunner {

    private static final TradingSystem system =
            new TradingSystem(TradingConstants.THREAD_POOL_SIZE);

    private static final Random random = new Random();

    public static void runSimulation(){

        long start = System.currentTimeMillis();

        initializeStocks();

        generateTrades();

        system.shutdownAndAwaitTermination();

        ReportGenerator.generateReports(
                system.getTradeResults());

        long end = System.currentTimeMillis();

        System.out.println(
                "\nExecution Time: " + (end-start) + " ms");
    }

    private static void initializeStocks(){

        system.addStock(new Stock("AAPL",100));
        system.addStock(new Stock("GOOG",80));
        system.addStock(new Stock("TSLA",50));
    }

    private static void generateTrades(){

        for(int i=0;i<TradingConstants.TOTAL_TRADES;i++){

            String user =
                    TradingConstants.USERS[
                            random.nextInt(
                                    TradingConstants.USERS.length)];

            String stock =
                    TradingConstants.STOCKS[
                            random.nextInt(
                                    TradingConstants.STOCKS.length)];

            int quantity =
                    random.nextInt(
                            TradingConstants.MAX_QUANTITY)+1;

            TradeType type =
                    random.nextBoolean()
                            ? TradeType.BUY
                            : TradeType.SELL;

            TradeRequest request =
                    new TradeRequest(user,stock,quantity,type);

            system.submitTrade(request);
        }
    }
}
