package com.howtographql.sampl.hackernewsgraphqljava.service;

import com.howtographql.sampl.hackernewsgraphqljava.model.Vote;
import com.howtographql.sampl.hackernewsgraphqljava.model.Votes;

public interface VoteService  extends AbstractService<Vote, Votes> {
    boolean existsUniq(Long linkId, Long userId);
}
