import java.util.Map;

public class Decryptor {
	private Map<Byte, Byte> key;
	private byte[] cipherText;
	private byte[] initializationVector;
	
	public Decryptor(Map<Byte, Byte> key, byte[] cipherText, byte[] initializationVector) {
		this.key = key;
		this.cipherText = cipherText;
		this.initializationVector = initializationVector;
	}

	public byte[] decrypt(int blockSize){
		int index = 0, i, j;
		int numOfBlocks = cipherText.length / blockSize;
		byte[] plainToReturn = new byte[cipherText.length];

		byte[] blockDecrypted = decryptBlock(cipherText, 0, blockSize-1);
		byte[] blockXored = xorTexts(initializationVector, blockDecrypted, 0, blockSize-1);

		for (i = 0; i < blockSize; i++) {
			plainToReturn[index++] = blockXored[i];
		}

		for (i = 2; i <= numOfBlocks; i++) {
			blockDecrypted = decryptBlock(cipherText, index, index+blockSize-1);
			blockXored = xorTexts(blockDecrypted, cipherText, index-blockSize, index-1 );

			for (j = 0; j < blockSize; j++) {
				plainToReturn[index++] = blockXored[j];
			}
		}

		return plainToReturn;
	}

	/*
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
	*/
	
	private byte[] decryptBlock(byte[] block, int from, int to){
		int blockSize = to-from+1;
		byte[] returnPlain = new byte[blockSize];
		int j = 0;
		
		for (int i = from; i <= to; i++) {
			if ((blockSize == 10) && (block[i] >= 'a' && block[i] <= 'h'))  {
				returnPlain[j++] = Utils.getKeyByValue(key, block[i]);
			}
			else if ((blockSize == 8128) && ((block[i] >= 'a' && block[i] <= 'z') || (block[i] >= 'A' && block[i] <= 'Z'))) {
				returnPlain[j++] = Utils.getKeyByValue(key, block[i]);
			}
			else {
				returnPlain[j++] = block[i];
			}
		}
		return returnPlain;
	}
	
	private byte[] xorTexts(byte[] sideA, byte[] sideB, int sideBFrom, int sideBTo) {
		byte[] returnXor = new byte[sideBTo-sideBFrom+1];
		int j = 0;
		
		for (int i = sideBFrom; i <= sideBTo; i++) {
			returnXor[j] = (byte)(sideA[j] ^ sideB[i]);
			j++;
		}
		
		return returnXor;
	}
}