package com.howtographql.sampl.hackernewsgraphqljava;

import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataProvider implements CommandLineRunner {
    private final LinkRepository linkRepository;

    @Override
    @Transactional
    public void run(String... strings) throws Exception {
        linkRepository.save(Link
                .builder()
                .url("http://howtographql.com")
                .description("Your favorite GraphQL page")
                .build());
        linkRepository.save(Link
                .builder()
                .url("http://graphql.org/learn/")
                .description("The official docks")
                .build());
    }
}
