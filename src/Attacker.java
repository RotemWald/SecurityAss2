import java.util.List;
import java.util.Map;
import java.util.Set;

public class Attacker {
	private Set<String> dictionary;
	private List<Map<Character, Character>> possibleKeys;
	private String cipherText;
	private String initVector;
	
	public Attacker(String dictionary, String cipherText, String initVector) {
		this.dictionary = Utils.createDictionaryFromFile(dictionary);
		this.possibleKeys = Utils.generateKeyList("abcdefgh");
		this.cipherText = cipherText;
		this.initVector = initVector;
	}

	public Map<Character, Character> attack() {
		Map<Character, Character> chosenKey = null;
		Decryptor decryptor;
		int currentKnownWords;
		int bestKnownWords = 0;

		// Average word length in English equals to 5 characters.
		// Let's check up to 2000 words in a given cipher text.
		int cipherTextAvgWords = cipherText.length() / 5;
		int cipherTextLengthOfWordsToCheck = Math.min(cipherTextAvgWords, 70);
		int cipherTextLengthToCheck = cipherTextLengthOfWordsToCheck * 5;

		for (Map<Character, Character> currentKey : possibleKeys) {
			decryptor = new Decryptor(currentKey, cipherText.substring(0, cipherTextLengthToCheck).toCharArray(), initVector.toCharArray());
			String plain = new String(decryptor.decrypt(10));

			String[] plainWords = plain.split("\\s+");
			currentKnownWords = 0;

			for (String s : plainWords) {
				if (dictionary.contains(s.toLowerCase())) {
					currentKnownWords++;
				}
			}

			if (currentKnownWords > bestKnownWords) {
				bestKnownWords = currentKnownWords;
				chosenKey = currentKey;
			}
		}

		return chosenKey;
	}
}
