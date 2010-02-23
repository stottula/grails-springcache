package musicstore.modules

import musicstore.Album
import grails.plugin.springcache.annotations.Cacheable
import org.codehaus.groovy.grails.web.util.WebUtils

class LatestController {

	@Cacheable(modelId = "LatestController")
	def albums = {
		println "controller=$controllerName, INCLUDE_REQUEST_URI_ATTRIBUTE= ${request.getAttribute(WebUtils.INCLUDE_REQUEST_URI_ATTRIBUTE)}"
		def albums = Album.list(sort: "dateCreated", order: "desc", max: 10)
		return [albumInstanceList: albums]
	}

}