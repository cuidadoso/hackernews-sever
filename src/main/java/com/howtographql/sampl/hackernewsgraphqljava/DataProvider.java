package com.howtographql.sampl.hackernewsgraphqljava;

import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import com.howtographql.sampl.hackernewsgraphqljava.repository.LinkRepository;
import com.howtographql.sampl.hackernewsgraphqljava.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.howtographql.sampl.hackernewsgraphqljava.configurations.SpringBeanUtils.session;
import static com.howtographql.sampl.hackernewsgraphqljava.util.Constants.NOW;

@Component
@RequiredArgsConstructor
public class DataProvider implements CommandLineRunner {
    private final LinkRepository linkRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... strings) throws Exception {
        session().saveUserId(1L);
        Long userId = userRepository.save(User.builder()
                .name("name")
                .email("email")
                .password("pass")
                .build())
                .getId();
        linkRepository.save(Link
                .builder()
                .url("http://howtographql.com")
                .description("Your favorite GraphQL page")
                .userId(userId)
                .build());
        linkRepository.save(Link
                .builder()
                .url("http://graphql.org/learn/")
                .description("The official docks")
                .userId(userId)
                .build());
        linkRepository.save(Link
                .builder()
                .url("http://url1.com")
                .description("descr1")
                .userId(userId)
                .build());
        linkRepository.save(Link
                .builder()
                .url("http://url2")
                .description("descr2")
                .userId(userId)
                .build());
        linkRepository.save(Link
                .builder()
                .url("http://url3.com")
                .description("descr3")
                .userId(userId)
                .build());
        linkRepository.save(Link
                .builder()
                .url("http://url4")
                .description("descr4")
                .userId(userId)
                .build());
        linkRepository.save(Link
                .builder()
                .url("http://url5.com")
                .description("descr5")
                .userId(userId)
                .build());
        linkRepository.save(Link
                .builder()
                .url("http://url6")
                .description("descr6")
                .userId(userId)
                .build());
        Link deleted = Link.builder()
                .url("http://urlDeleted")
                .description("deleted")
                .userId(userId)
                .build();
        deleted.setDeletedBy(1L);
        deleted.setDeletedAt(NOW);
        linkRepository.save(deleted);
    }
}
