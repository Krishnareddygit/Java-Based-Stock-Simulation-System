package Analytics;

import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import Model.Trade;

public class ReportGenerator {

    public static void generateReports(Queue<Trade> trades){

        System.out.println("\n----- Trading Reports -----");

        printTotalTrades(trades);

        printQuantityPerStock(trades);

        printMostTradedStock(trades);

        printTradesPerUser(trades);
    }

    private static void printTotalTrades(Queue<Trade> trades){

        System.out.println("Total Successful Trades: "
                + trades.size());
    }

    private static void printQuantityPerStock(Queue<Trade> trades){

        Map<String,Integer> quantityPerStock =
                trades.stream()
                        .collect(Collectors.groupingBy(
                                Trade::getStockSymbol,
                                Collectors.summingInt(Trade::getQuantity)
                        ));

        System.out.println("\nQuantity Traded Per Stock");

        quantityPerStock.forEach((stock,qty) ->
                System.out.println(stock + " -> " + qty));
    }

    private static void printMostTradedStock(Queue<Trade> trades){

        Map<String,Integer> quantityPerStock =
                trades.stream()
                        .collect(Collectors.groupingBy(
                                Trade::getStockSymbol,
                                Collectors.summingInt(Trade::getQuantity)
                        ));

        quantityPerStock.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(entry ->
                        System.out.println(
                                "\nMost Traded Stock: "
                                        + entry.getKey()));
    }

    private static void printTradesPerUser(Queue<Trade> trades){

        Map<String,Long> tradesPerUser =
                trades.stream()
                        .collect(Collectors.groupingBy(
                                Trade::getUserId,
                                Collectors.counting()
                        ));

        System.out.println("\nTrades Per User");

        tradesPerUser.forEach((user,count) ->
                System.out.println(user + " -> " + count));
    }
}
