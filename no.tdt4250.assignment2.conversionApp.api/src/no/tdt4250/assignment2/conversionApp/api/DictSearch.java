package no.tdt4250.assignment2.conversionApp.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class DictSearch {

	private static final String DEFAULT_MESSAGE = "Sorry, no matches";
	private static final String INFO_MESSAGE = "Conversion should be in the from [from unit][to unit][value]";

	private Collection<Dict> dictionaries = new ArrayList<Dict>();
	
	public void addDictionary(Dict dict) {
		dictionaries.add(dict);
	}

	public void removeDictionary(Dict dict) {
		dictionaries.remove(dict);
	}
	
	public DictSearch(Dict... dicts) {
		dictionaries.addAll(Arrays.asList(dicts));
	}
	
	private DictSearchResult search(String searchKey, Iterable<Dict> dictionaries) {
		StringBuilder messages = new StringBuilder();
		URI link = null;
		boolean success = false;
		for (Dict dict : dictionaries) {
			DictSearchResult result = dict.search(searchKey);
			if (result.isSuccess()) {
				messages.append(result.getMessage());
				messages.append("(" + dict.getDictName() + ")\n");
				
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
		return new DictSearchResult(success, messages.toString(), link);
	}

	public DictSearchResult search(String dictKey, String searchKey) {
		return search(searchKey, dictionaries.stream().filter(dict -> dict.getDictName().equals(dictKey)).collect(Collectors.toList()));
	}

	public DictSearchResult search(String searchKey) {
		return search(searchKey, dictionaries);
	}
}
