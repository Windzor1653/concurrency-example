package ru.tinkoff.edu.parallel_work;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class CompletableFutureCrawler {

    private ExecutorService executor;

    public CompletableFutureCrawler(ExecutorService executor) {
        this.executor = executor;
    }

    public static CompletableFutureCrawler ofThreads(int threadCount) {
        return new CompletableFutureCrawler(Executors.newFixedThreadPool(threadCount));
    }

    public void crawl(List<String> urls, Consumer<String> sink) {
        for (String url : urls) {
            CompletableFuture.supplyAsync(() -> url, executor)
                    .thenApply(this::fetchPageBody)
                    .thenAccept(sink);
        }
    }

    private String fetchPageBody(String url) {
        Connection.Response response = null;
        try {
            response = Jsoup.connect(url).execute();
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch page " + url, e);
        }
        return response.body();
    }
}
