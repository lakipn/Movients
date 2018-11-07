package com.lazarmilosavljevic.movients.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.lazarmilosavljevic.movients.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.lazarmilosavljevic.movients.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.lazarmilosavljevic.movients.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.lazarmilosavljevic.movients.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.lazarmilosavljevic.movients.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.lazarmilosavljevic.movients.domain.Movie.class.getName(), jcacheConfiguration);
            cm.createCache(com.lazarmilosavljevic.movients.domain.Movie.class.getName() + ".casts", jcacheConfiguration);
            cm.createCache(com.lazarmilosavljevic.movients.domain.Movie.class.getName() + ".images", jcacheConfiguration);
            cm.createCache(com.lazarmilosavljevic.movients.domain.Movie.class.getName() + ".torrents", jcacheConfiguration);
            cm.createCache(com.lazarmilosavljevic.movients.domain.Movie.class.getName() + ".genres", jcacheConfiguration);
            cm.createCache(com.lazarmilosavljevic.movients.domain.Cast.class.getName(), jcacheConfiguration);
            cm.createCache(com.lazarmilosavljevic.movients.domain.Genre.class.getName(), jcacheConfiguration);
            cm.createCache(com.lazarmilosavljevic.movients.domain.Genre.class.getName() + ".movies", jcacheConfiguration);
            cm.createCache(com.lazarmilosavljevic.movients.domain.Image.class.getName(), jcacheConfiguration);
            cm.createCache(com.lazarmilosavljevic.movients.domain.Torrent.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
