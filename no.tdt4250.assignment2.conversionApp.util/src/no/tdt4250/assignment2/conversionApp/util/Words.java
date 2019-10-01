package no.tdt4250.assignment2.conversionApp.util;

import java.util.ArrayList;

public interface Words extends Iterable<CharSequence> {
	public boolean hasWord(CharSequence word);
	public ArrayList<String> getWords();
}
