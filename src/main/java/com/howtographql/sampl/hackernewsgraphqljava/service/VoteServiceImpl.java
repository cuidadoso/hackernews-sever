package com.howtographql.sampl.hackernewsgraphqljava.service;

import com.howtographql.sampl.hackernewsgraphqljava.model.Vote;
import com.howtographql.sampl.hackernewsgraphqljava.model.Votes;
import com.howtographql.sampl.hackernewsgraphqljava.repository.VoteRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Service("voteService")
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class VoteServiceImpl extends AbstractServiceHelper<Vote, Votes> {
    public VoteServiceImpl(VoteRepository repository) {
        super(Vote.class, Votes.class, repository);
    }
}
