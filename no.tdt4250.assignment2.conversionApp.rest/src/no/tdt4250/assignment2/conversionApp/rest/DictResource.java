package no.tdt4250.assignment2.conversionApp.rest;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

//import javax.json.JsonObject;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsResource;

import com.fasterxml.jackson.core.JsonProcessingException;

import no.tdt4250.assignment2.conversionApp.api.Dict;
import no.tdt4250.assignment2.conversionApp.api.DictSearch;
import no.tdt4250.assignment2.conversionApp.api.DictSearchResult;

@Component(service=DictResource.class)
@JaxrsResource
@Path("dict")
public class DictResource {

	@Reference(
			policy = ReferencePolicy.DYNAMIC
			)
	private volatile Collection<Dict> dictionaries;
	
	public DictSearch getDictSearch() {
		return new DictSearch(dictionaries.toArray(new Dict[dictionaries.size()]));
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public DictSearchResult search(@QueryParam("q") String q) throws JsonProcessingException {
		System.out.println(q);
		return getDictSearch().search(q);
	}
	
	@GET
	@Path("/{lang}")
	@Produces(MediaType.APPLICATION_JSON)
	public DictSearchResult search(@PathParam("lang") String lang, @QueryParam("q") String q) throws JsonProcessingException {
		return getDictSearch().search(lang, q);
	}
}
