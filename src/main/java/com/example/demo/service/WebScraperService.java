package com.example.demo.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class WebScraperService {

    public String scrapeWebsite(String url) {

        try {
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            // Remove unnecessary tags
            document.select("script, style, noscript").remove();

            // Extract readable text
            return document.body().text();

        } catch (Exception e) {
            return "Failed to scrape website content.";
        }
    }
}
