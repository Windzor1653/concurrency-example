package ru.tinkoff.edu.parallel_work;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Slf4j
class CrawlerTest {

    private static final List<String> urls = List.of(
            "https://en.wikipedia.org/wiki/Programming",
            "https://en.wikipedia.org/wiki/Computer_programming",
            "https://en.wikipedia.org/wiki/Software_engineering",
            "https://en.wikipedia.org/wiki/Software_development_process",
            "https://en.wikipedia.org/wiki/Software_development",
            "https://en.wikipedia.org/wiki/Software_design",
            "https://en.wikipedia.org/wiki/Software_testing",
            "https://en.wikipedia.org/wiki/Software_quality",
            "https://en.wikipedia.org/wiki/Software_architecture",
            "https://en.wikipedia.org/wiki/Software_maintenance",
            "https://en.wikipedia.org/wiki/Software_configuration_management",
            "https://en.wikipedia.org/wiki/Software_repository",
            "https://en.wikipedia.org/wiki/Software_release",
            "https://en.wikipedia.org/wiki/Software_versioning",
            "https://en.wikipedia.org/wiki/Software_requirements",
            "https://en.wikipedia.org/wiki/Software_engineering_professionalism",
            "https://en.wikipedia.org/wiki/Software_lifecycle",
            "https://en.wikipedia.org/wiki/Software_prototyping",
            "https://en.wikipedia.org/wiki/Software_verification_and_validation",
            "https://en.wikipedia.org/wiki/Software_security",
            "https://en.wikipedia.org/wiki/Software_deployment",
            "https://en.wikipedia.org/wiki/Software_configuration",
            "https://en.wikipedia.org/wiki/Software_development_tools",
            "https://en.wikipedia.org/wiki/Software_engineering_methodology",
            "https://en.wikipedia.org/wiki/Software_reverse_engineering",
            "https://en.wikipedia.org/wiki/Software_error",
            "https://en.wikipedia.org/wiki/Software_bug",
            "https://en.wikipedia.org/wiki/Software_metrics",
            "https://en.wikipedia.org/wiki/Software_performance_testing",
            "https://en.wikipedia.org/wiki/Software_code_review",
            "https://en.wikipedia.org/wiki/Software_development_environment"
            );
    public static final Consumer<String> NOOP_CONSUMER = (body) -> {
    };


    @Test
    void singleThreaded() throws InterruptedException {
        // given
        var crawler = new SingleThreadedCrawler();
        // when
        var start = System.currentTimeMillis();
        crawler.crawl(urls, NOOP_CONSUMER);
        // then
        log.info("Single thread. Time passed: " + (System.currentTimeMillis() - start));
    }

    @Test
    void completableFuture() throws InterruptedException {
        // given
        var crawler = CompletableFutureCrawler.ofThreads(3);
        var latch = new CountDownLatch(urls.size());
        // when
        var start = System.currentTimeMillis();
        crawler.crawl(new CopyOnWriteArrayList<>(this.urls), (content) -> latch.countDown());
        // then
        latch.await(15, TimeUnit.SECONDS);
        log.info("CompletableFuture. Time passed: " + (System.currentTimeMillis() - start));
    }

    @Test
    void executor() throws InterruptedException {
        // given
        var crawler = ExecutorCrawler.ofThreads(3);
        var latch = new CountDownLatch(urls.size());
        // when
        var start = System.currentTimeMillis();
        crawler.crawl(new CopyOnWriteArrayList<>(this.urls), (content) -> latch.countDown());
        // then
        latch.await(15, TimeUnit.SECONDS);
        log.info("Executor. Time passed: " + (System.currentTimeMillis() - start));
    }


}