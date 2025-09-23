package id.web.fitrarizki.healthcare.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CacheServiceImplTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @InjectMocks
    private CacheServiceImpl cacheServiceImpl;

    @Test
    void get_OnAvailableCache_ReturnValue() {

    }

}