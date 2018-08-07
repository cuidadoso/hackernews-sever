package com.howtographql.sampl.hackernewsgraphqljava;

import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZonedDateTime;

import static java.time.ZoneOffset.UTC;

@Component
@RequiredArgsConstructor
public class DataProvider implements CommandLineRunner {
    private final LinkRepository linkRepository;

    private static final ZonedDateTime NOW = Instant.now().atZone(UTC);

    @Override
    @Transactional
    public void run(String... strings) throws Exception {
        linkRepository.save(Link
                .builder()
                .createdAt(NOW)
                .url("http://howtographql.com")
                .description("Your favorite GraphQL page")
                .build());
        linkRepository.save(Link
                .builder()
                .createdAt(NOW)
                .url("http://graphql.org/learn/")
                .description("The official docks")
                .build());
        linkRepository.save(Link
                .builder()
                .createdAt(NOW)
                .url("http://url1.com")
                .description("descr1")
                .build());
        linkRepository.save(Link
                .builder()
                .createdAt(NOW)
                .url("http://url2")
                .description("descr2")
                .build());
        linkRepository.save(Link
                .builder()
                .createdAt(NOW)
                .url("http://url3.com")
                .description("descr3")
                .build());
        linkRepository.save(Link
                .builder()
                .createdAt(NOW)
                .url("http://url4")
                .description("descr4")
                .build());
        linkRepository.save(Link
                .builder()
                .createdAt(NOW)
                .url("http://url5.com")
                .description("descr5")
                .build());
        linkRepository.save(Link
                .builder()
                .createdAt(NOW)
                .url("http://url6")
                .description("descr6")
                .build());
    }
}
