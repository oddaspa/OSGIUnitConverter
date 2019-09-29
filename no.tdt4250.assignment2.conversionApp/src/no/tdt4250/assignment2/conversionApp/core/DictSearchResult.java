package no.tdt4250.assignment2.conversionApp.core;

import java.net.URI;

public class DictSearchResult {
	private final boolean success;
	private final String message;
	private final URI link;
	
	public DictSearchResult(boolean success, String message, URI link) {
		super();
		this.success = success;
		this.message = message;
		this.link = link;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public String getMessage() {
		return message;
	}
	
	public URI getLink() {
		return link;
	}
}
