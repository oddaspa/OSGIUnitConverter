package no.tdt4250.assignment2.conversionApp.rest;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleSerializers;

import no.tdt4250.assignment2.conversionApp.api.DictSearchResult;

public class DictModule extends Module {

	@Override
	public String getModuleName() {
		return "DictModule";
	}

	@Override
	public Version version() {
		return Version.unknownVersion();
	}

	private final SimpleSerializers serializers = new SimpleSerializers();

	public DictModule() {
		serializers.addSerializer(DictSearchResult.class, new JsonSerializer<DictSearchResult>() {
			@Override
			public void serialize(DictSearchResult dictSearchResult, JsonGenerator jsonGen,
					SerializerProvider serializerProvider) throws IOException {
				jsonGen.writeStartObject(dictSearchResult);
				jsonGen.writeBooleanField("success", dictSearchResult.isSuccess());
				jsonGen.writeStringField("message", dictSearchResult.getMessage());
				if (dictSearchResult.getLink() != null) {
					jsonGen.writeStringField("link", dictSearchResult.getLink().toString());
				}
			}
		});
	}

	@Override
	public void setupModule(final SetupContext context) {
		context.addSerializers(serializers);
	}
}
