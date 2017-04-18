import java.util.Map;

public class Encryptor {
	private byte[] plainText;
	private byte[] initializationVector;
	private Map<Byte, Byte> key;
	
	public Encryptor(byte[] plainText, byte[] initializationVector, Map<Byte, Byte> key) {
		this.plainText = plainText;
		this.initializationVector = initializationVector;
		this.key = key;
	}
	
	public byte[] encrypt(int blockSize) {
		int index = 0, i, j;
		byte[] plainTextToEncrypt = padPlaintext(blockSize);
		int numOfBlocks = plainTextToEncrypt.length / blockSize;
		byte[] cipherToReturn = new byte[plainTextToEncrypt.length];
		
		byte[] blockToEncrypt = xorTexts(initializationVector, plainTextToEncrypt, 0, 9);
		byte[] blockEncrypted = encryptBlock(blockToEncrypt, blockSize);
		
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
	
	private byte[] padPlaintext(int blockSize) {
		int originalPtSize = plainText.length;
		int padSize = originalPtSize % blockSize;
		
		if (padSize == 0) {
			return plainText;
		}
		
		else {
			int i;
			byte[] newPlaintext = new byte[originalPtSize + (10-padSize)];
			
			for (i = 0; i < originalPtSize; i++) {
				newPlaintext[i] = plainText[i];
			}
			
			for (; i < newPlaintext.length; i++) {
				newPlaintext[i] = 0;
			}
			
			return newPlaintext;
		}
	}
	
	private byte[] encryptBlock(byte[] block, int blockSize) {
		byte[] returnCipher = new byte[blockSize];
		
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