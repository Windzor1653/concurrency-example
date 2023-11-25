package ru.tinkoff.edu.parallel_work;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class SingleThreadedCrawler {

    public void crawl(List<String> urls, Consumer<String> sink) {
        for (String url : urls) {
            Connection.Response response = null;
            try {
                response = Jsoup.connect(url).execute();
            } catch (IOException e) {
                throw new RuntimeException("Failed to fetch page " + url, e);
            }
            sink.accept(response.body());
        }
    }
}
