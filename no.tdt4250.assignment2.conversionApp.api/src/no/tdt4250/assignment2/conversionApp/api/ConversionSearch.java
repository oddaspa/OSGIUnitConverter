package no.tdt4250.assignment2.conversionApp.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class ConversionSearch {

	private static final String DEFAULT_MESSAGE = "Sorry, no matches";
	private static final String INFO_MESSAGE = "Conversion should be in the from [from unit][to unit][value]";

	private Collection<Conversion> conversions = new ArrayList<Conversion>();
	
	public void addConversion(Conversion conversion) {
		conversions.add(conversion);
	}

	public void removeConversion(Conversion conversion) {
		conversions.remove(conversion);
	}
	
	public ConversionSearch(Conversion... convs) {
		conversions.addAll(Arrays.asList(convs));
	}
	
	private ConversionSearchResult search(String searchKey, Iterable<Conversion> conversions) {
		StringBuilder messages = new StringBuilder();
		URI link = null;
		boolean success = false;
		for (Conversion conversion : conversions) {
			ConversionSearchResult result = conversion.search(searchKey);
			if (result.isSuccess()) {
				messages.append(result.getMessage());
				messages.append("(" + conversion.getConversionName() + ")\n");
				messages.append(searchKey);
				success = true;
				if (link == null) {
					link = result.getLink();
				}
			}
		}
		if (messages.length() == 0) {
			messages.append(DEFAULT_MESSAGE);
			messages.append(INFO_MESSAGE);
		}
		return new ConversionSearchResult(success, messages.toString(), link);
	}

	public ConversionSearchResult search(String convKey, String searchKey) {
		return search(searchKey, conversions.stream().filter(conv -> conv.getConversionName().equals(convKey)).collect(Collectors.toList()));
	}

	public ConversionSearchResult search(String searchKey) {
		return search(searchKey, conversions);
	}
}
