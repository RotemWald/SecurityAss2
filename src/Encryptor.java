import java.util.Map;

public class Encryptor {
	private char[] plainText;
	private char[] initializationVector;
	private Map<Character, Character> key;
	
	public Encryptor(char[] plainText, char[] initializationVector, Map<Character, Character> key) {
		this.plainText = plainText;
		this.initializationVector = initializationVector;
		this.key = key;
	}
	
	public char[] encrypt(int blockSize) {
		int index = 0, i, j;
		char[] plainTextToEncrypt = padPlaintext(blockSize);
		int numOfBlocks = plainTextToEncrypt.length / blockSize;
		char[] cipherToReturn = new char[plainTextToEncrypt.length];
		
		char[] blockToEncrypt = xorTexts(initializationVector, plainTextToEncrypt, 0, 9);
		char[] blockEncrypted = encryptBlock(blockToEncrypt, blockSize);
		
		for (i = 0; i < blockSize; i++) {
			cipherToReturn[index++] = blockEncrypted[i];
		}
		
		for (i = 2; i <= numOfBlocks; i++) {
			blockToEncrypt = xorTexts(blockEncrypted, plainTextToEncrypt, index, index+9);
			blockEncrypted = encryptBlock(blockToEncrypt, blockSize);
			
			for (j = 0; j < blockSize; j++) {
				cipherToReturn[index++] = blockEncrypted[j];
			}
		}
		
		return cipherToReturn;
	}
	
	private char[] padPlaintext(int blockSize) {
		int originalPtSize = plainText.length;
		int padSize = originalPtSize % blockSize;
		
		if (padSize == 0) {
			return plainText;
		}
		
		else {
			int i;
			char[] newPlaintext = new char[originalPtSize + (10-padSize)];
			
			for (i = 0; i < originalPtSize; i++) {
				newPlaintext[i] = plainText[i];
			}
			
			for (; i < newPlaintext.length; i++) {
				newPlaintext[i] = 0;
			}
			
			return newPlaintext;
		}
	}
	
	private char[] encryptBlock(char[] block, int blockSize) {
		char[] returnCipher = new char[blockSize];
		
		for (int i = 0; i < blockSize; i++) {
			if (block[i] >= 'a' && block[i] <= 'h') {
				returnCipher[i] = key.get(block[i]);
			}
			
			else {
				returnCipher[i] = block[i];
			}
		}
		
		return returnCipher;
	}
	
	private char[] xorTexts(char[] sideA, char[] sideB, int sideBFrom, int sideBTo) {
		char[] returnXor = new char[sideBTo-sideBFrom+1];
		int j = 0;
		
		for (int i = sideBFrom; i <= sideBTo; i++) {
			returnXor[j] = (char) (sideA[j] ^ sideB[i]);
			j++;
		}
		
		return returnXor;
	}
}