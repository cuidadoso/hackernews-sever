package com.howtographql.sampl.hackernewsgraphqljava.service;

import com.howtographql.sampl.hackernewsgraphqljava.model.*;
import com.howtographql.sampl.hackernewsgraphqljava.resolvers.Query;
import lombok.extern.java.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.howtographql.sampl.hackernewsgraphqljava.util.Logging.logInfo;

@Log
@Aspect
@Order()
@Component
public class AopLoggingService {

    /**
     * Validator/logger for {@link Query#links(LinkFilter, int, int, String)}
     *
     * @param joinPoint
     * @param entities
     */
    @AfterReturning(
            pointcut = "execution(* com.howtographql.sampl.hackernewsgraphqljava.service.AbstractService.findAll(..))",
            returning = "entities"
    )
    public void onAfterLinks(final JoinPoint joinPoint, final BaseEntities entities) {
        logInfo("Find all entities of %s. Total count of entities is %d.",
                entityName(joinPoint),
                entities.getPageInfo().getTotal());
    }

    /**
     * Validator/logger for {@link Query#link(Long)}
     *
     * @param joinPoint
     * @param entity
     */
    @AfterReturning(
            pointcut = "execution(* com.howtographql.sampl.hackernewsgraphqljava.service.AbstractService.findOne(..))",
            returning = "entity"
    )
    public void onAfterLink(final JoinPoint joinPoint, final BaseEntity entity) {
        logInfo("Find one entity %s with id - link [%d]",
                entityName(joinPoint), entity.getId());
    }

    private String entityName(final JoinPoint joinPoint) {
        AbstractServiceHelper target = (AbstractServiceHelper) joinPoint.getTarget();
        return target.getClassOfEntity().getSimpleName();
    }

}
