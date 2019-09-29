package no.tdt4250.assignment2.conversionApp.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.tdt4250.assignment2.conversionApp.api.DictSearchResult;

@Component
@JaxrsExtension
@Produces(MediaType.APPLICATION_JSON)
public class DictSearchResultWriter implements MessageBodyWriter<DictSearchResult> {

	@Override
	public boolean isWriteable(Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType) {
		return clazz == DictSearchResult.class;
	}

	private ObjectMapper objectMapper = new ObjectMapper();
	{
		objectMapper.registerModule(new DictModule());
	}
	
	@Override
	public void writeTo(DictSearchResult dictSearchResult, Class<?> clazz, Type type, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> values, OutputStream output) throws IOException, WebApplicationException {
		objectMapper.writeValue(output, dictSearchResult);
		output.flush();
	}
}
