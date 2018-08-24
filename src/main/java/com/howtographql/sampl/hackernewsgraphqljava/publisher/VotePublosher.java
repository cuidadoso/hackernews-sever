package com.howtographql.sampl.hackernewsgraphqljava.publisher;

import com.howtographql.sampl.hackernewsgraphqljava.model.Vote;
import com.howtographql.sampl.hackernewsgraphqljava.model.Votes;
import com.howtographql.sampl.hackernewsgraphqljava.resolvers.exceptions.CustomException;
import com.howtographql.sampl.hackernewsgraphqljava.service.AbstractService;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static io.reactivex.BackpressureStrategy.BUFFER;

@Getter
@Component
public class VotePublosher {
    private final Flowable<Vote> publisher;
    @Qualifier("voteService")
    private final AbstractService<Vote, Votes> voteService;

    public VotePublosher(AbstractService<Vote, Votes> voteService) {
        Observable<Vote> voteObservable = Observable.create(emitter -> {
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            executorService.scheduleAtFixedRate(newVoteTick(emitter), 0, 2, TimeUnit.SECONDS);

        });
        ConnectableObservable<Vote> connectableObservable = voteObservable.share().publish();
        connectableObservable.connect();
        this.publisher = connectableObservable.toFlowable(BUFFER);
        this.voteService = voteService;
    }

    private Runnable newVoteTick(ObservableEmitter<Vote> emitter) {
        return () -> {
            List<Vote> votes = voteService.findAll();
            if (votes != null) {
                emitVotes(emitter, votes);
            }
        };
    }

    private void emitVotes(ObservableEmitter<Vote> emitter, List<Vote> votes) {
        votes.forEach(vote -> {
            try {
                emitter.onNext(vote);
            } catch (RuntimeException e) {
                throw new CustomException(String.format("Cannot send link %n %s", e.getLocalizedMessage()));
            }
        });
    }
}
