package no.tdt4250.assignment2.conversionApp.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import no.tdt4250.assignment2.conversionApp.util.internal.AbstractMutableWords;

public class SortedSetWords extends AbstractMutableWords {

	private SortedSet<CharSequence> wordSet = new TreeSet<CharSequence>();
	
	@Override
	public boolean hasWord(CharSequence word) {
		int hits = 
		wordSet.stream()
		.filter(str -> str.toString().startsWith(word.toString()))
		.collect(Collectors.toList()).size();
		if(hits>0) {
			return true;
		}
		return false;
	}
	
	public void addWord(CharSequence word) {
		// Neglect for now.
		//if (isLegalWord(word)) {
		//	wordSet.add(word);
		//}
		wordSet.add(word);
	}
	
	@Override
	public Iterator<CharSequence> iterator() {
		return wordSet.iterator();
	}
	public ArrayList<String> getWords() {
		ArrayList<String> arr = new ArrayList<String>();
		Iterator<CharSequence> i = wordSet.iterator();
		while(i.hasNext()) {
			arr.add((String) i.next());
		}
		return arr;
	}
	
	public String getWord(String searchKeyP) {
		Iterator<CharSequence> i = wordSet.iterator();
		while(i.hasNext()) {
			String candidate = i.next().toString();
			if(candidate.substring(0, 2).equals(searchKeyP)) {
				return candidate;
			}
		}
		return "Baboska";
	}
	
	public String toString() {
		String out = "";
		for(CharSequence c : wordSet) {
			out += c;
		}
		return out;
	}
}
