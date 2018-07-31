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
        linkRepository.save(new Link("http://howtographql.com", "Your favorite GraphQL page"));
        linkRepository.save(new Link("http://graphql.org/learn/", "The official docks"));
    }
}
