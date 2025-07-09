package com.fraga.projectManager.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fraga.projectManager.data.model.Member;
import com.fraga.projectManager.data.model.Project;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.fraga.projectManager.constants.CacheConstants.PREFIX_KEY_MEMBER;
import static com.fraga.projectManager.constants.CacheConstants.PREFIX_KEY_PROJECT;
import static java.time.Duration.ofSeconds;

@Configuration
@EnableCaching
public class CacheConfig {
    @Value("${cache.ttl.member:3600}") // Default TTL of 1 hour
    private long memberCacheTtl;
    @Value("${cache.ttl.project:3600}")
    private long projectCacheTtl;
    private final ObjectMapper objectMapper;
    private final RedisConnectionFactory redisConnectionFactory;
    private Map<String, RedisCacheConfiguration> initialConfig;

    public CacheConfig(ObjectMapper objectMapper, RedisConnectionFactory redisConnectionFactory) {
        this.objectMapper = objectMapper;
        this.redisConnectionFactory = redisConnectionFactory;
        this.initialConfig = new HashMap<>();
    }

    @PostConstruct
    public void init()
    {
        initialConfig.put(PREFIX_KEY_MEMBER, redisCacheConfig(ofSeconds(memberCacheTtl), Member.class));
        initialConfig.put(PREFIX_KEY_PROJECT, redisCacheConfig(ofSeconds(projectCacheTtl), Project.class));
    }

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        template.setKeySerializer(new StringRedisSerializer());

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        template.setValueSerializer(serializer);

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    @Primary
    public CacheManager cacheManager(){
        return RedisCacheManager.builder(redisConnectionFactory)
                .withInitialCacheConfigurations(initialConfig)
                .build();
    }

    private <T> RedisCacheConfiguration redisCacheConfig(Duration ttl, Class<T> clazz) {
        JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
        Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>(javaType);

        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .entryTtl(ttl);
    }

}
