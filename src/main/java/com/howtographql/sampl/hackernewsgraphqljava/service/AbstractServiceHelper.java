package com.howtographql.sampl.hackernewsgraphqljava.service;

import com.howtographql.sampl.hackernewsgraphqljava.model.*;
import com.howtographql.sampl.hackernewsgraphqljava.repository.BaseRepository;
import com.howtographql.sampl.hackernewsgraphqljava.util.ObjectType;
import com.querydsl.core.types.dsl.BooleanExpression;
import graphql.GraphQLException;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static com.howtographql.sampl.hackernewsgraphqljava.configurations.SpringBeanUtils.session;
import static com.howtographql.sampl.hackernewsgraphqljava.util.Collections.isBlankCollection;
import static com.howtographql.sampl.hackernewsgraphqljava.util.Collections.makeList;
import static com.howtographql.sampl.hackernewsgraphqljava.util.Constants.NOW;
import static com.howtographql.sampl.hackernewsgraphqljava.util.Constants.NULL;
import static com.howtographql.sampl.hackernewsgraphqljava.util.Logging.logError;
import static com.howtographql.sampl.hackernewsgraphqljava.util.Logging.logInfo;
import static com.howtographql.sampl.hackernewsgraphqljava.util.ObjectType.*;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Getter
public abstract class AbstractServiceHelper<Entity extends BaseEntity, Entities extends BaseEntities>
        implements AbstractService<Entity, Entities> {
    protected final Map<ObjectType, String> classes;
    protected final BaseRepository<Entity> repository;
    protected final Class entityClass;
    protected final Class pageableClass;
    protected final Class specClass;


    protected AbstractServiceHelper(Map<ObjectType, String> classes, BaseRepository<Entity> repository) {
        this.classes = classes;
        this.repository = repository;
        this.entityClass = ENTITY.getClass(classes);
        this.pageableClass = PAGEABLE.getClass(classes);
        this.specClass = SPEC.getClass(classes);
    }

    @Override
    public Entity findOne(Long id) {
        return repository.findOne(id);
    }

    public List<Entity> findAll() {
        return repository.findAll();
    }

    public List<Entity> findAll(List<Filter> filter) {
        BooleanExpression predicate = filtered(filter);
        Iterable<Entity> entities = findAll(predicate);
        return makeList(entities);
    }

    public List<Entity> findAll(BooleanExpression predicate) {
        Iterable<Entity> entities = repository.findAll(predicate);
        return makeList(entities);
    }

    public Entities findAll(List<Filter> filter, int page, int size, List<OrderBy> orderBy) {
        BooleanExpression predicate = filtered(filter);
        Sort sort = orders(orderBy);
        Pageable pageable = new PageRequest(page, size, sort);
        Page<Entity> entityPage = page(predicate, pageable);
        return entities(entityPage);
    }

    public Entities findAll(BooleanExpression predicate, int page, int size, List<OrderBy> orderBy) {
        Sort sort = orders(orderBy);
        Pageable pageable = new PageRequest(page, size, sort);
        Page<Entity> entityPage = page(predicate, pageable);
        return entities(entityPage);
    }

    @Override
    public Entity save(Entity entity) {
        return repository.save(entity);
    }

    @Override
    public List<Entity> save(List<Entity> entities) {
        return repository.save(entities);
    }

    @Override
    public boolean delete(Entity entity) {
        if (entity != null && exists(entity.getId())) {
            entity.setDeletedAt(NOW);
            entity.setUpdatedBy(session().userId());
            repository.save(entity);
            return true;
        }
        throw new GraphQLException(String.format("Entity %s [%s] not exists", entityClass.getSimpleName(),
                entity == null ? NULL : entity.toString()));
    }

    @Override
    public boolean delete(Long id) {
            return delete(repository.findOne(id));
    }

    @Override
    public boolean delete(Iterable<Entity> entities) {
        entities.forEach(this::delete);
        return true;
    }

    @Override
    public boolean exists(Long id) {
        return repository.exists(id);
    }

    private Page<Entity> page(BooleanExpression predicate, Pageable pageable) {
        return repository.findAll(predicate, pageable);
    }

    private Entities entities(Page<Entity> entityPage) {
        try {
            return (Entities) pageableClass.getDeclaredConstructor(List.class, PageInfo.class)
                    .newInstance(entityPage.getContent(), pageInfo(entityPage));
        } catch (Exception e) {
            logError("Error: %s", e.getMessage());
            throw new GraphQLException("Server error");
        }
    }

    private PageInfo pageInfo(Page<Entity> entities) {
        return PageInfo.builder()
                .hasNextPage(entities.nextPageable() != null)
                .hasPreviousPage(entities.previousPageable() != null)
                .total(entities.getTotalElements())
                .totalPages(entities.getTotalPages())
                .build();
    }

    private Sort orders(List<OrderBy> orderBy) {
        if (!isBlankCollection(orderBy) && isBaseEntity()) {
            String id = orderBy.get(0).getId();
            boolean desc = orderBy.get(0).isDesc();
            switch (id) {
                case "id":
                case "createdAt":
                    return new Sort(new Order(desc ? DESC : ASC, id));
                default:
                    try {
                        // Just to check if classOfEntity contains field with name = fieldName
                        entityClass.getDeclaredField(id);
                        return new Sort(new Order(desc ? DESC : ASC, id));
                    } catch (NoSuchFieldException e) {
                        logInfo("Entity [%s] doesn't have field [%s].", entityClass.getSimpleName(), id);
                    }
            }
        }
        return new Sort(new Order(ASC, "id"));
    }

    protected boolean isBaseEntity() {
        return BaseEntity.class.equals(entityClass.getSuperclass());
    }

    protected BooleanExpression filtered(List<Filter> filter) {
        BooleanExpression predicates = null;
        if(!isBlankCollection(filter) && isBaseEntity()) {
            for (Filter f : filter) {
                String id = f.getId();
                String value = f.getValue();
                try {
                    Method method = predicates().get(id);
                    BooleanExpression predicate = (BooleanExpression) method.invoke(specClass, value);
                    switch (id) {
                        case "id":
                        case "createdAt":
                            predicates = predicates == null ? predicate : predicates.and(predicate);
                        default:
                            // Just to check if classOfEntity contains field with name = id
                            entityClass.getDeclaredField(id);
                            predicates = predicates == null ? predicate : predicates.and(predicate);
                    }
                } catch (Exception e) {
                    logInfo("Entity [%s] doesn't have predicates.", entityClass.getSimpleName());
                }
            }
        }
        return predicates;
    }

    protected Map<String, Method> predicates() throws Exception {
            Method method = specClass.getDeclaredMethod("predicates");
            method.setAccessible(true);
            return  (Map<String, Method>) method.invoke(specClass);
    }
}
