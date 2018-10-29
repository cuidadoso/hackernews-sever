package com.howtographql.sampl.hackernewsgraphqljava.service.impl;

import com.google.common.collect.ImmutableMap;
import com.howtographql.sampl.hackernewsgraphqljava.model.Vote;
import com.howtographql.sampl.hackernewsgraphqljava.model.Votes;
import com.howtographql.sampl.hackernewsgraphqljava.repository.VoteRepository;
import com.howtographql.sampl.hackernewsgraphqljava.service.AbstractServiceHelper;
import com.howtographql.sampl.hackernewsgraphqljava.service.VoteService;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import static com.howtographql.sampl.hackernewsgraphqljava.specifications.VoteSpecifications.voteByLinkAndUser;
import static com.howtographql.sampl.hackernewsgraphqljava.util.ObjectType.*;

@Service("voteService")
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class VoteServiceImpl extends AbstractServiceHelper<Vote, Votes> implements VoteService {
    public VoteServiceImpl(VoteRepository repository) {
        super(ImmutableMap.of(
                ENTITY, "model.Vote",
                PAGEABLE, "model.Votes",
                SPEC, "specifications.VoteSpecifications",
                PROJECT_PATH, "com.howtographql.sampl.hackernewsgraphqljava"
        ), repository);
    }

    @Override
    public boolean existsUniq(Long linkId, Long userId) {
        return !findAll(voteByLinkAndUser(linkId, userId)).isEmpty();
    }
}
