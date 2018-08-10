package com.howtographql.sampl.hackernewsgraphqljava.service;

import com.howtographql.sampl.hackernewsgraphqljava.model.Link;
import com.howtographql.sampl.hackernewsgraphqljava.model.LinkFilter;
import com.howtographql.sampl.hackernewsgraphqljava.model.Links;
import com.howtographql.sampl.hackernewsgraphqljava.resolvers.Query;
import lombok.extern.java.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.howtographql.sampl.hackernewsgraphqljava.util.Logging.logInfo;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@Log
@Aspect
@Order(LOWEST_PRECEDENCE)
@Component
public class AopLoggingService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Validator/logger for {@link Query#links(LinkFilter, int, int, String)}
     *
     * @param joinPoint
     * @param links
     */
    @AfterReturning(
            pointcut = "execution(* com.howtographql.sampl.hackernewsgraphqljava.service.LinkServiceImpl.findAll(..))",
            returning = "links"
    )
    public void onAfterLinks(final JoinPoint joinPoint, final Links links) {
        logInfo("Query - links [%s]", links);
    }

    /**
     * Validator/logger for {@link Query#link(Long)}
     *
     * @param joinPoint
     * @param link
     */
    @AfterReturning(
            pointcut = "execution(* com.howtographql.sampl.hackernewsgraphqljava.service.LinkServiceImpl.findOne(..))",
            returning = "link"
    )
    public void onAfterLink(final JoinPoint joinPoint, final Link link) {
        logInfo("Query - link [%s]", link);
    }

}
