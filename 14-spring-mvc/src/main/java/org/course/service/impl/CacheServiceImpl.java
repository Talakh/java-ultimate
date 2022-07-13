package org.course.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.course.service.CacheService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CacheServiceImpl implements CacheService {

    @CacheEvict(cacheNames = "largestPicture", allEntries = true)
    @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.SECONDS)
    @Override
    public void clearCache() {
        log.info("Cleaning cache!");
    }
}
