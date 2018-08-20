package com.howtographql.sampl.hackernewsgraphqljava.loader;

import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.howtographql.sampl.hackernewsgraphqljava.util.Constants.USER_ID;

@Component
@Order(1)
@RequiredArgsConstructor
public class DataLoader0001Link implements ApplicationRunner {
    private final static List<Link> LINK_LIST = new ArrayList<>();
    @Qualifier("linkService")
    private final LinkService linkService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        createLink("http://howtographql.com", "Your favorite GraphQL page");
        createLink("http://graphql.org/learn/", "The official docks");
        createLink("http://url1.com", "descr1");
        createLink("http://url2", "descr2");
        createLink("http://url3", "descr3");
        createLink("http://url4", "descr4");
        createLink("http://url5", "descr5");
        createLink("http://url6", "descr6");
        createLink("http://url7", "descr7");

        linkService.save(LINK_LIST);
    }

    private void createLink(String url, String description) {
        if (linkService.existsUniq(url)) {
            return;
        }
        LINK_LIST.add(Link.builder()
                .url(url)
                .description(description)
                .userId(USER_ID)
                .build());
    }
}
