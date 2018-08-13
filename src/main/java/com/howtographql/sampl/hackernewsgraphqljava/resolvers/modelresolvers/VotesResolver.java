package com.howtographql.sampl.hackernewsgraphqljava.resolvers.modelresolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.howtographql.sampl.hackernewsgraphqljava.model.PageInfo;
import com.howtographql.sampl.hackernewsgraphqljava.model.Vote;
import com.howtographql.sampl.hackernewsgraphqljava.model.Votes;

import java.util.List;

public class VotesResolver implements GraphQLResolver<Votes> {
    public List<Vote> items(Votes votes) {
        return votes.getItems();
    }

    public PageInfo pageInfo(Votes votes) {
        return votes.getPageInfo();
    }
}
