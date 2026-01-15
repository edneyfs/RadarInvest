package br.com.edneysiqueira.radarinvest.infrastructure.crawler;

import br.com.edneysiqueira.radarinvest.infrastructure.crawler.model.FiiRuleDTO;
import br.com.edneysiqueira.radarinvest.infrastructure.util.BusinessDayCalculator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class FiiHubCrawlerProvider {

    private static final String BASE_URL = "https://fiihub.netlify.app";
    private static final String CALENDAR_PATH = "/app/calendario";
    // Regex looking for JSON.parse('...') pattern containing "ticker"
    private static final Pattern JSON_PATTERN = Pattern.compile("JSON\\.parse\\('(\\[\\{.*?\"ticker\":.*?\\}\\])'\\)");

    private List<FiiRuleDTO> cachedRules = Collections.emptyList();
    private LocalDateTime lastUpdate = LocalDateTime.MIN;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Optional<LocalDate> getProventoDate(String ticker) {
        ensureCacheValid();

        return cachedRules.stream()
                .filter(rule -> rule.getTicker().equalsIgnoreCase(ticker))
                .findFirst()
                .map(rule -> calculateDate(rule.getAnnouncementDay()));
    }

    private LocalDate calculateDate(int announcementDay) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        return BusinessDayCalculator.calculateNthBusinessDay(year, month, announcementDay);
    }

    private void ensureCacheValid() {
        if (lastUpdate.isBefore(LocalDateTime.now().minusHours(24)) || cachedRules.isEmpty()) {
            refreshRules();
        }
    }

    private void refreshRules() {
        try {
            log.info("Refreshing FiiHub rules...");
            // 1. Get Main Page to find JS file
            Document doc = Jsoup.connect(BASE_URL + CALENDAR_PATH).get();
            Element scriptElement = doc.selectFirst("script[src^='/assets/index-']");

            if (scriptElement == null) {
                log.error("Could not find main JS file in FiiHub.");
                return;
            }

            String jsUrl = BASE_URL + scriptElement.attr("src");
            log.info("Downloading JS bundle: {}", jsUrl);

            // 2. Download JS Content
            String jsContent = Jsoup.connect(jsUrl).ignoreContentType(true).execute().body();

            // 3. Extract JSON
            Matcher matcher = JSON_PATTERN.matcher(jsContent);
            if (matcher.find()) {
                String jsonString = matcher.group(1);
                // The JSON inside the JS might be escaped (e.g. \' instead of '), but usually
                // JSON.parse expects clean JSON.
                // However, if it's inside single quotes in JS '...', quotes inside might be
                // escaped.
                // Let's assume standard behavior for now, but might need unescaping if it
                // fails.

                cachedRules = objectMapper.readValue(jsonString, new TypeReference<List<FiiRuleDTO>>() {
                });
                lastUpdate = LocalDateTime.now();
                log.info("Successfully loaded {} rules from FiiHub.", cachedRules.size());
            } else {
                log.error("Could not find JSON rules in JS bundle.");
            }

        } catch (IOException e) {
            log.error("Error refreshing FiiHub rules", e);
        }
    }
}
