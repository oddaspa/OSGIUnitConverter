package no.tdt4250.assignment2.conversionApp.api;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface Dict {
	String getDictName();
	DictSearchResult search(String searchKey);
}
