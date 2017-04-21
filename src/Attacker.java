import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Attacker {
	private Set<String> dictionary;
	private List<Map<Byte, Byte>> possibleKeys;
	private byte[] cipherText;
	private byte[] initVector;
	
	public Attacker(List<Map<Byte, Byte>> possibleKeys, String dictionary, byte[] cipherText, byte[] initVector) throws UnsupportedEncodingException {
		this.dictionary = Utils.createDictionaryFromFile(dictionary);
		this.possibleKeys = possibleKeys;
		this.cipherText = cipherText;
		this.initVector = initVector;
	}

	public Map<Byte, Byte> attack(int blockSize) {
		Map<Byte, Byte> chosenKey = null;
		Decryptor decryptor;
		int currentKnownWords;
		int bestKnownWords = 0;

		// Average word length in English equals to 5 characters.
		// Let's check up to x words in a given cipher text.
		int cipherTextAvgWords = cipherText.length / 5;
		int cipherTextLengthOfWordsToCheck = Math.min(cipherTextAvgWords, 70);
		int cipherTextLengthToCheck = cipherTextLengthOfWordsToCheck * 5;

		for (Map<Byte, Byte> currentKey : possibleKeys) {
			decryptor = new Decryptor(currentKey, Arrays.copyOfRange(cipherText, 0, cipherTextLengthToCheck), initVector);
			String plain = new String(decryptor.decrypt(blockSize));

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
