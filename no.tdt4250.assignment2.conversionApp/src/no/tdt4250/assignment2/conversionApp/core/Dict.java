package no.tdt4250.assignment2.conversionApp.core;

public interface Dict {
	String getDictName();
	DictSearchResult search(String searchKey);
}
