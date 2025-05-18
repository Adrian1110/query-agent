package com.example.aibilling;

import com.example.aibilling.entity.QueryLog;
import com.example.aibilling.repository.QueryLogRepository;
import com.example.aibilling.service.SqlGeneratorService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ai.chat.ChatClient;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class SqlGeneratorServiceTest {

    private SqlGeneratorService sqlGeneratorService;

    @BeforeEach
    public void setup() {
        final ChatClient mockClient = Mockito.mock(ChatClient.class);
        final QueryLogRepository mockRepo = Mockito.mock(QueryLogRepository.class);
        final EntityManager mockEm = Mockito.mock(EntityManager.class);
        final Query mockQuery = Mockito.mock(Query.class);

        Mockito.when(mockClient.call(Mockito.anyString())).thenReturn("SELECT * FROM user");
        Mockito.when(mockEm.getMetamodel()).thenReturn(Mockito.mock(jakarta.persistence.metamodel.Metamodel.class));
        Mockito.when(mockEm.createNativeQuery(Mockito.anyString())).thenReturn(mockQuery);
        Mockito.when(mockQuery.setMaxResults(1)).thenReturn(mockQuery);
        Mockito.when(mockQuery.getResultList()).thenReturn(Collections.emptyList());

        sqlGeneratorService = new SqlGeneratorService(mockClient, mockRepo, mockEm);
    }

    @Test
    public void testGenerateSqlReturnsValidQuery() {
        final String sql = sqlGeneratorService.generateAndValidateSql("list all users");
        assertNotNull(sql);
        assertTrue(sql.toLowerCase().startsWith("select"));
    }

    @Test
    public void testInvalidSqlThrowsAndRetries() {
        final ChatClient mockClient = Mockito.mock(ChatClient.class);
        final QueryLogRepository mockRepo = Mockito.mock(QueryLogRepository.class);
        final EntityManager mockEm = Mockito.mock(EntityManager.class);

        Mockito.when(mockClient.call(Mockito.anyString())).thenReturn("INVALID SQL");

        final SqlGeneratorService failingService = new SqlGeneratorService(mockClient, mockRepo, mockEm);
        assertThrows(RuntimeException.class, () -> failingService.generateAndValidateSql("bad query"));
    }
}
