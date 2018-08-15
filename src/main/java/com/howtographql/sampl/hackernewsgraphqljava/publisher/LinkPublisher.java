package com.howtographql.sampl.hackernewsgraphqljava.publisher;

import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.model.Links;
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

import static com.howtographql.sampl.hackernewsgraphqljava.util.Logging.logError;
import static io.reactivex.BackpressureStrategy.BUFFER;

@Getter
@Component
public class LinkPublisher {
    private final Flowable<Link> publisher;
    @Qualifier("linkService")
    private final AbstractService<Link, Links> linkService;

    public LinkPublisher(AbstractService<Link, Links> linkService) {
        Observable<Link> linkObservable = Observable.create(emitter -> {

            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            executorService.scheduleAtFixedRate(newLinkTick(emitter), 0, 2, TimeUnit.SECONDS);

        });

        ConnectableObservable<Link> connectableObservable = linkObservable.share().publish();
        connectableObservable.connect();

        this.publisher = connectableObservable.toFlowable(BUFFER);
        this.linkService = linkService;
    }

    private Runnable newLinkTick(ObservableEmitter<Link> emitter) {
        return () -> {
            List<Link> links = linkService.findAll();
            if (links != null) {
                emitLinks(emitter, links);
            }
        };
    }

    private void emitLinks(ObservableEmitter<Link> emitter, List<Link> links) {
        links.forEach(link -> {
            try {
                emitter.onNext(link);
            } catch (RuntimeException e) {
                logError("Cannot send link", e);
            }
        });
    }
}
