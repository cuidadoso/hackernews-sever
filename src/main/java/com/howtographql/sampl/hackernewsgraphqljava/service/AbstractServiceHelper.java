package com.howtographql.sampl.hackernewsgraphqljava.service;

import com.howtographql.sampl.hackernewsgraphqljava.model.BaseEntities;
import com.howtographql.sampl.hackernewsgraphqljava.model.BaseEntity;
import com.howtographql.sampl.hackernewsgraphqljava.model.PageInfo;
import com.howtographql.sampl.hackernewsgraphqljava.repository.BaseRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import graphql.GraphQLException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.List;

import static com.howtographql.sampl.hackernewsgraphqljava.configurations.SpringBeanUtils.session;
import static com.howtographql.sampl.hackernewsgraphqljava.util.Collections.makeList;
import static com.howtographql.sampl.hackernewsgraphqljava.util.Constants.NOW;
import static com.howtographql.sampl.hackernewsgraphqljava.util.Constants.NULL;
import static com.howtographql.sampl.hackernewsgraphqljava.util.Logging.logError;
import static com.howtographql.sampl.hackernewsgraphqljava.util.Logging.logInfo;
import static org.springframework.data.domain.Sort.Direction.ASC;

@Getter
@RequiredArgsConstructor
public abstract class AbstractServiceHelper<Entity extends BaseEntity, Entities extends BaseEntities>
        implements AbstractService<Entity, Entities> {
    private final Class<Entity> classOfEntity;
    private final Class<Entities> classOfEntities;
    protected final BaseRepository<Entity> repository;

    @Override
    public Entity findOne(Long id) {
        return repository.findOne(id);
    }

    public List<Entity> findAll() {
        return repository.findAll();
    }

    public List<Entity> findAll(BooleanExpression predicate) {
        Iterable<Entity> entities = repository.findAll(predicate);
        return makeList(entities);
    }

    public Entities findAll(BooleanExpression predicate, int page, int size, String orderBy) {
        Page<Entity> entityPage = page(predicate, page, size, orderBy);
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
        throw new GraphQLException(String.format("Entity %s [%s] not exists", classOfEntity.getSimpleName(),
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

    private Page<Entity> page(BooleanExpression predicate, int page, int size, String orderBy) {
        Pageable pageable = new PageRequest(page, size, orders(orderBy));
        return repository.findAll(predicate, pageable);
    }

    private Entities entities(Page<Entity> entityPage) {
        try {
            return classOfEntities.getDeclaredConstructor(List.class, PageInfo.class)
                    .newInstance(entityPage.getContent(), pageInfo(entityPage));
        } catch (Exception e) {
            logError("Error: %s", e.getMessage());
            throw new GraphQLException("Server error");
        }
    }

    private PageInfo pageInfo(Page<Entity> entities) {
        int totalPages = entities.getTotalPages();
        int pageNumber = entities.getNumber() + 1;
        long total = entities.getTotalElements();
        return PageInfo.builder()
                .hasNextPage(pageNumber < totalPages)
                .hasPreviousPage(pageNumber > 1)
                .total(total)
                .build();
    }

    private Sort orders(String orderBy) {
        if (!StringUtils.isBlank(orderBy) && isBaseEntity()) {
            String[] orderParams = orderBy.split("_");
            String fieldName = orderParams[0];
            Direction direction = Direction.fromStringOrNull(orderParams[1]);
            switch (fieldName) {
                case "id":
                case "createdAt":
                    return new Sort(new Order(direction == null ? ASC : direction, fieldName));
                default:
                    try {
                        // Just to check if classOfEntity contains field with name = fieldName
                        classOfEntity.getDeclaredField(fieldName);
                        return new Sort(new Order(direction == null ? ASC : direction, fieldName));
                    } catch (NoSuchFieldException e) {
                        logInfo("Entity [%s] doesn't have field [%s].", classOfEntity.getSimpleName(), fieldName);
                    }
            }
        }
        return new Sort(new Order(ASC, "id"));
    }

    private boolean isBaseEntity() {
        return BaseEntity.class.equals(classOfEntity.getSuperclass());
    }
}
