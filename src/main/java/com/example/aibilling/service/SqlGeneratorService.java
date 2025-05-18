package com.example.aibilling.service;

import com.example.aibilling.entity.QueryLog;
import com.example.aibilling.repository.QueryLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SqlGeneratorService {

    private final ChatClient chatClient;
    private final QueryLogRepository queryLogRepository;
    private final EntityManager entityManager;

    private static final int MAX_RETRIES = 3;

    @Retryable(value = RuntimeException.class, maxAttempts = MAX_RETRIES, backoff = @Backoff(delay = 2000))
    public String generateAndValidateSql(final String rule) {
        final String context = buildEntityContext();
        String prompt = context + "\n\nGenerate a valid SQL query for: " + rule;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            final String sql = chatClient.call(prompt);
            log.info("Generated SQL (attempt {}): {}", attempt, sql);

            try {
                if (isValidSql(sql)) {
                    queryLogRepository.save(QueryLog.builder().generatedSql(sql).sandbox(true).build());
                    return sql;
                }
            } catch (Exception e) {
                final String errorFeedback = e.getMessage().split("\n")[0];
                prompt += "\n\nNote: The previous SQL failed with the following error:\n" + errorFeedback;
                log.warn("Attempt {} failed with error: {}", attempt, errorFeedback);
            }
        }

        throw new RuntimeException("Failed to generate valid SQL after " + MAX_RETRIES + " attempts.");
    }

    private boolean isValidSql(final String sql) {
        try {
            final String explainQuery = "EXPLAIN " + sql;
            entityManager.createNativeQuery(explainQuery).getResultList();
            return true;
        } catch (final Exception e) {
            return false;
        }
    }

    private String buildEntityContext() {
        final Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
        final List<Map<String, Object>> jsonEntities = new ArrayList<>();

        for (final EntityType<?> entity : entities) {
            final Map<String, Object> entityMap = new HashMap<>();

            final Class<?> clazz = entity.getJavaType();

            final Table tableAnnotation = clazz.getAnnotation(Table.class);
            final String tableName = (tableAnnotation != null && !tableAnnotation.name().isBlank())
              ? tableAnnotation.name()
              : clazz.getSimpleName();

            entityMap.put("table", tableName);

            final Map<String, String> attributes = Arrays.stream(clazz.getDeclaredFields())
              .filter(f -> !Modifier.isStatic(f.getModifiers()))
              .collect(Collectors.toMap(
                field -> {
                    if (field.isAnnotationPresent(Column.class)) {
                        final Column column = field.getAnnotation(Column.class);
                        if (!column.name().isBlank()) return column.name();
                    }
                    if (field.isAnnotationPresent(JoinColumn.class)) {
                        final JoinColumn join = field.getAnnotation(JoinColumn.class);
                        if (!join.name().isBlank()) return join.name();
                    }
                    return field.getName();
                },
                field -> field.getType().getSimpleName()
              ));

            entityMap.put("columns", attributes);
            jsonEntities.add(entityMap);
        }

        final Map<String, Object> context = Map.of("entities", jsonEntities);

        try {
            return new ObjectMapper().writeValueAsString(context);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize entity context to JSON", e);
        }
    }
}
