package com.howtographql.sampl.hackernewsgraphqljava.service;

import com.howtographql.sampl.hackernewsgraphqljava.model.BaseEntities;
import com.howtographql.sampl.hackernewsgraphqljava.model.BaseEntity;
import com.howtographql.sampl.hackernewsgraphqljava.model.LinkFilter;
import com.howtographql.sampl.hackernewsgraphqljava.resolvers.Query;
import lombok.extern.java.Log;
import org.apache.commons.text.StrBuilder;
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
     * @param joinPoint Join point object
     * @param entities  List od entities with page info.
     */
    @AfterReturning(
            pointcut = "execution(* com.howtographql.sampl.hackernewsgraphqljava.service.AbstractService.findAll(..))",
            returning = "entities"
    )
    public void onAfterFindAll(final JoinPoint joinPoint, final BaseEntities entities) {
        logInfo("Find all entities of %s. Total count of entities is %d.",
                entityName(joinPoint),
                entities.getPageInfo().getTotal());
    }

    /**
     * Validator/logger for {@link Query#link(Long)}
     *
     * @param joinPoint Join point object
     * @param entity    Entity.
     */
    @AfterReturning(
            pointcut = "execution(* com.howtographql.sampl.hackernewsgraphqljava.service.AbstractService.findOne(..))",
            returning = "entity"
    )
    public void onAfterFindOne(final JoinPoint joinPoint, final BaseEntity entity) {
        logInfo("Find one entity %s with id [%d]",
                entityName(joinPoint), entity.getId());
    }

    /**
     * Validator/logger for {@link Query#link(Long)}
     *
     * @param joinPoint Join point object
     * @param entity    Entity.
     */
    @AfterReturning(
            pointcut = "execution(* com.howtographql.sampl.hackernewsgraphqljava.service.AbstractService.save(..))",
            returning = "entity"
    )
    public void onAfterSave(final JoinPoint joinPoint, final BaseEntity entity) {
        logInfo("Save entity [%s] - [%s]",
                entityName(joinPoint), entity.toString());
    }

    /**
     * Validator/logger for {@link Query#link(Long)}
     *
     * @param joinPoint Join point object
     */
    @AfterReturning(
            pointcut = "execution(* com.howtographql.sampl.hackernewsgraphqljava.service.AbstractService.delete(..))"
    )
    public void onAfterDelete(final JoinPoint joinPoint) {
        logInfo("Delete entity with ID [%s]", deleteArguments(joinPoint));
    }

    private String entityName(final JoinPoint joinPoint) {
        AbstractServiceHelper target = (AbstractServiceHelper) joinPoint.getTarget();
        return target.getClassOfEntity().getSimpleName();
    }

    private String deleteArguments(final JoinPoint joinPoint) {
        Object arg = joinPoint.getArgs()[0];
        if (arg instanceof BaseEntity) {
            return ((BaseEntity) arg).getId().toString();
        }
        if (arg instanceof Iterable) {
            StrBuilder ids = new StrBuilder();
            ((Iterable) arg).forEach(a -> ids.append(a).append(", "));
            return ids.substring(0, ids.length() - 2);
        }
        return (String) arg;
    }
}
