package grails.plugin.springcache.web

import javax.servlet.http.HttpServletRequest
import grails.plugin.springcache.key.CacheKeyBuilder

class DefaultKeyGenerator extends AbstractKeyGenerator {

	protected void generateKeyInternal(CacheKeyBuilder builder, CachingFilterContext context, HttpServletRequest request) {
		builder << context.controllerName
		builder << context.actionName
		context.params?.sort { it.key }?.each { entry ->
			if (!(entry.key in ["controller", "action"])) {
				builder << entry
			}
		}
	}
	
}
