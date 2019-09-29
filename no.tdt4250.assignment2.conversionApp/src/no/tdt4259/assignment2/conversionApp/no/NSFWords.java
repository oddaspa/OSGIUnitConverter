package no.tdt4259.assignment2.conversionApp.no;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NSFWords extends SortedSetWords {

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

	protected NSFWords(InputStream input) {
		try {
			read(input);
		} catch (IOException e) {
		}
	}
	
	public static NSFWords create(String lang) {
		return new NSFWords(NSFWords.class.getResourceAsStream(String.format("%s.txt", lang)));
	}
}

