package grails.plugins.springcache.annotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import grails.plugins.springcache.cache.CacheFacade;
import grails.plugins.springcache.cache.CacheKey;
import grails.plugins.springcache.cache.CacheProvider;
import grails.plugins.springcache.cache.DefaultCacheKey;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class CacheAspect {

	private final Logger log = LoggerFactory.getLogger(CacheAspect.class);

	private CacheProvider cacheManager;

	@Around("@annotation(cacheable)")
	public Object invokeCachedMethod(ProceedingJoinPoint pjp, Cacheable cacheable) throws Throwable {
		CacheFacade cache = cacheManager.getCache(cacheable.cacheName());
		CacheKey key = generateCacheKey(pjp);
		return getFromCacheOrInvoke(pjp, cache, key);
	}

	CacheKey generateCacheKey(ProceedingJoinPoint pjp) {
		List<Object> values = new ArrayList<Object>();
		values.add(pjp.getSignature().getName());
		values.addAll(Arrays.asList(pjp.getArgs()));
		return new DefaultCacheKey(values);
	}

	Object getFromCacheOrInvoke(ProceedingJoinPoint pjp, CacheFacade cache, CacheKey key) throws Throwable {
		Object value;
		if (cache.containsKey(key)) {
			log.debug("Cache hit for %s", key.toString());
			value = cache.get(key);
		} else {
			log.debug("Cache miss for %s", key.toString());
			value = pjp.proceed();
			cache.put(key, value);
		}
		return value;
	}

	@Autowired(required = true)
	public void setCacheManager(CacheProvider cacheManager) {
		this.cacheManager = cacheManager;
	}

}