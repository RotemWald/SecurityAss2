import java.util.Map;

public class Decryptor {
	private Map<Character, Character> key;
	private char[] cipherText;
	private char[] initializationVector;
	
	public Decryptor(Map<Character, Character> key, char[] cipherText, char[] initializationVector) {
		this.key = key;
		this.cipherText = cipherText;
		this.initializationVector = initializationVector;
	}

	public char[] decrypt(int blockSize){
		int index = 0, i, j;
		int numOfBlocks = cipherText.length / blockSize;
		char[] plainToReturn = new char[cipherText.length];

		char[] blockDecrypted = decryptBlock(cipherText, 0, 9);
		char[] blockXored = xorTexts(initializationVector, blockDecrypted, 0, 9);

		for (i = 0; i < blockSize; i++) {
			plainToReturn[index++] = blockXored[i];
		}

		for (i = 2; i <= numOfBlocks; i++) {
			blockDecrypted = decryptBlock(cipherText, index, index+9);
			blockXored = xorTexts(blockDecrypted, cipherText, index-10, index-1 );

			for (j = 0; j < blockSize; j++) {
				plainToReturn[index++] = blockXored[j];
			}
		}

		return plainToReturn;
	}

	public char[] decryptLimit(int blockSize, int maxNumOfWords){
		int currentNumberOfWords = 0;
		int index = 0, i, j;
		int numOfBlocks = cipherText.length / blockSize;
		char[] plainToReturn = new char[cipherText.length];

		char[] blockDecrypted = decryptBlock(cipherText, 0, 9);
		char[] blockXored = xorTexts(initializationVector, blockDecrypted, 0, 9);

		for (i = 0; i < blockSize; i++) {
			plainToReturn[index++] = blockXored[i];

			if (blockXored[i] == ' ') {
				currentNumberOfWords++;
				if (currentNumberOfWords == maxNumOfWords) {
					return plainToReturn;
				}
			}
		}

		for (i = 2; i <= numOfBlocks; i++) {
			blockDecrypted = decryptBlock(cipherText, index, index+9);
			blockXored = xorTexts(blockDecrypted, cipherText, index-10, index-1 );

			for (j = 0; j < blockSize; j++) {
				plainToReturn[index++] = blockXored[j];

				if (blockXored[j] == ' ') {
					currentNumberOfWords++;
					if (currentNumberOfWords == maxNumOfWords) {
						return plainToReturn;
					}
				}
			}
		}

		return plainToReturn;
	}
	
	private char[] decryptBlock(char[] block, int from, int to){
		char[] returnPlain = new char[to-from+1];
		int j = 0;
		
		for (int i = from; i <= to; i++) {
			if (block[i] >= 'a' && block[i] <= 'h') {
				returnPlain[j++] = Utils.getKeyByValue(key, block[i]);
			}
			else {
				returnPlain[j++] = block[i];
			}
		}
		return returnPlain;
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