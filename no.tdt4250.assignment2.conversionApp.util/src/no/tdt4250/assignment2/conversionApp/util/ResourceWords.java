package no.tdt4250.assignment2.conversionApp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceWords extends SortedSetWords {

	private void read(InputStream input) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input, "utf-8"));
		String line = null;
		nextLine: while ((line = reader.readLine()) != null) {
			for (int i = 0; i < line.length(); i++) {
				if (! Character.isLetter(line.charAt(i))) {
					if (i > 0) {
						addWord(line.substring(0, i).toLowerCase());
					}
					continue nextLine;
				}
			}
			addWord(line.toLowerCase());
		}
	}

	public ResourceWords(InputStream input) throws IOException {
		read(input);
	}
}
