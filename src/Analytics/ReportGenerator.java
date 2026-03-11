package Analytics;

import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import Model.Trade;
import Model.TradeStatus;

import java.util.*;
import java.util.stream.Collectors;

import java.util.*;
import java.util.stream.Collectors;

public class ReportGenerator {

    public static void generateReports(Queue<Trade> trades){

        printTradeHistory(trades);

        List<Trade> successfulTrades =
                trades.stream()
                        .filter(t -> t.getStatus() == TradeStatus.SUCCESS)
                        .collect(Collectors.toList());

        System.out.println("\n----- Trading Reports -----");

        System.out.println("Total Successful Trades: "
                + successfulTrades.size());

        Map<String,Integer> quantityPerStock =
                successfulTrades.stream()
                        .collect(Collectors.groupingBy(
                                Trade::getStockSymbol,
                                Collectors.summingInt(
                                        Trade::getQuantity)));

        System.out.println("\nQuantity Traded Per Stock");

        quantityPerStock.forEach((stock,qty) ->
                System.out.println(stock + " -> " + qty));

        quantityPerStock.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(e ->
                        System.out.println(
                                "\nMost Traded Stock: "
                                        + e.getKey()));

        Map<String,Long> tradesPerUser =
                successfulTrades.stream()
                        .collect(Collectors.groupingBy(
                                Trade::getUserId,
                                Collectors.counting()));

        System.out.println("\nTrades Per User");

        tradesPerUser.forEach((user,count) ->
                System.out.println(user + " -> " + count));
    }

    private static void printTradeHistory(Queue<Trade> trades){

        System.out.println("\n----- Trade History -----");

        trades.forEach(t ->
                System.out.println(
                        t.getUserId() + " "
                                + t.getTradeType() + " "
                                + t.getQuantity() + " "
                                + t.getStockSymbol()
                                + " -> " + t.getStatus()
                                + " (" + t.getMessage() + ")"
                )
        );
    }
}