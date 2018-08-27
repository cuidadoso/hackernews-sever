package com.howtographql.sampl.hackernewsgraphqljava.service;

import com.howtographql.sampl.hackernewsgraphqljava.model.*;
import com.howtographql.sampl.hackernewsgraphqljava.repository.BaseRepository;
import com.howtographql.sampl.hackernewsgraphqljava.resolvers.exceptions.CustomException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.howtographql.sampl.hackernewsgraphqljava.configurations.SpringBeanUtils.session;
import static com.howtographql.sampl.hackernewsgraphqljava.util.Collections.isBlankCollection;
import static com.howtographql.sampl.hackernewsgraphqljava.util.Collections.makeList;
import static com.howtographql.sampl.hackernewsgraphqljava.util.Constants.NOW;
import static com.howtographql.sampl.hackernewsgraphqljava.util.Constants.NULL;
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
            throw new CustomException(String.format("Error: %s", e.getMessage()));
        }
    }

    private PageInfo pageInfo(Page<Entity> entities) {
        return PageInfo.builder()
                .hasNextPage(entities.nextPageable() != null)
                .hasPreviousPage(entities.previousPageable() != null)
                .total(entities.getTotalElements())
                .totalPages(entities.getTotalPages())
                .pageNumber(entities.getNumber())
                .build();
    }

    private Sort orders(List<OrderBy> orderBy) {
        if (!isBlankCollection(orderBy) && isBaseEntity()) {
            List<Order> orders = new ArrayList<>();
            orderBy.forEach(o -> orders.add(createOrder(o.getId(), o.isDesc())));
            return new Sort(orders);
        }
        return null;
    }

    private Order createOrder(String id, boolean desc) {
        Order preOrder = orderByService(id, desc);
        if (preOrder != null) {
            return preOrder;
        }
        switch (id) {
            case "id":
            case "createdAt":
                return new Order(desc ? DESC : ASC, id);
            default:
                try {
                    // Just to check if classOfEntity contains field with name = fieldName
                    entityClass.getDeclaredField(id);
                    return new Order(desc ? DESC : ASC, id);
                } catch (NoSuchFieldException e) {
                    throw new CustomException(String.format("Entity [%s] doesn't have field [%s].", entityClass.getSimpleName(), id));
                }
        }
    }

    private boolean isBaseEntity() {
        return BaseEntity.class.equals(entityClass.getSuperclass());
    }

    private BooleanExpression filtered(List<Filter> filter) {
        BooleanExpression predicates = null;
        if(!isBlankCollection(filter) && isBaseEntity()) {
            for (Filter f : filter) {
                String id = f.getId();
                String value = f.getValue();
                BooleanExpression predicate = createPredicate(id, value);
                predicates = predicates == null ? predicate : predicates.and(predicate);
            }
        }
        return predicates;
    }

    private BooleanExpression createPredicate(String id, String value) {
        BooleanExpression predicate = predicateByService(id, value);
        if (predicate != null) {
            return predicate;
        }
        try {
            Method method = predicates().get(id);
            switch (id) {
                case "id":
                case "createdAt":
                    return (BooleanExpression) method.invoke(specClass, value);
                default:
                    // Just to check if classOfEntity contains field with name = id
                    entityClass.getDeclaredField(id);
                    return (BooleanExpression) method.invoke(specClass, value);
            }
        } catch (Exception e) {
            throw new CustomException(String.format("Entity [%s] doesn't have predicates for [%s].", entityClass.getSimpleName(), id));
        }
    }

    private Map<String, Method> predicates() throws Exception {
        Method method = specClass.getDeclaredMethod("predicates");
        method.setAccessible(true);
        return  (Map<String, Method>) method.invoke(specClass);
    }

    /**
     * Customize order per service
     * */
    protected Order orderByService(String id, boolean desc) {
        return null;
    }

    /**
     * Customize predicate per service
     * */
    protected BooleanExpression predicateByService(String id, String value) {
        return null;
    }
}
