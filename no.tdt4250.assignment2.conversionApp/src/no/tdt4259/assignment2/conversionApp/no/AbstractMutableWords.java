package no.tdt4259.assignment2.conversionApp.no;

public abstract class AbstractMutableWords implements MutableWords {

	public boolean isLegalWord(CharSequence word) {
		int count = word.length(), lowerCount = 0, upperCount = 0;
		for (int i = 0; i < count; i++) {
			char c = word.charAt(i);
			if (! Character.isLetter(c)) {
				return false;
			}
			if (Character.isLowerCase(c)) {
				lowerCount++;
			} else if (Character.isUpperCase(c)) {
				upperCount++;
			}
		}
		return isLegalWord(count, lowerCount, upperCount);
	}

	private boolean isLegalWord(int count, int lowerCount, int upperCount) {
		return lowerCount == count;
	}
	
	public static void addAll(MutableWords mutableWords, Words words) {
		for (CharSequence word : words) {
			mutableWords.addWord(word);
		}
	}
}
