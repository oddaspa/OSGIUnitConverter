package no.tdt4259.assignment2.conversionApp.no;

import no.tdt4250.assignment2.conversionApp.core.Dict;
import no.tdt4250.assignment2.conversionApp.core.DictSearchResult;

public class NbDict implements Dict {

	private NSFWords words = NSFWords.create("nb");
	
	@Override
	public String getDictName() {
		return "nb";
	}

	@Override
	public DictSearchResult search(String searchKey) {
		if (words.hasWord(searchKey)) {
			return new DictSearchResult(true, "Yes, " + searchKey + " was found!", null);
		} else {
			return new DictSearchResult(false, "No, " + searchKey + " wasn't found...", null);
		}
	}
}
