package com.howtographql.sampl.hackernewsgraphqljava.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.model.User;
import com.howtographql.sampl.hackernewsgraphqljava.repository.LinkRepository;
import com.howtographql.sampl.hackernewsgraphqljava.repository.LinkSpecifications;
import com.howtographql.sampl.hackernewsgraphqljava.repository.UserRepository;
import com.howtographql.sampl.hackernewsgraphqljava.repository.UserSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.howtographql.sampl.hackernewsgraphqljava.util.Collections.makeList;

@Component
@RequiredArgsConstructor
public class Query implements GraphQLQueryResolver {
    private final LinkRepository linkRepository;
    private final UserRepository userRepository;

    // Link query resolvers
    public List<Link> allLinks() {
        return linkRepository.findAll();
    }

    public Link link(Long id) {
        return linkRepository.findOne(id);
    }

    public List<Link> linksByUrl(String url) {
        return makeList(linkRepository.findAll(LinkSpecifications.linkByUrl(url)));
    }

    // User query resolvers
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public User user(Long id) {
        return userRepository.findOne(id);
    }

    public List<User> usersByEmail(String email) {
        return makeList(userRepository.findAll(UserSpecifications.userByEmail(email)));
    }
}
