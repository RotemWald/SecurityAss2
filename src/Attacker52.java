import java.util.HashMap;
import java.util.Map;

/**
 * Created by rotemwald on 21/04/17.
 */
public class Attacker52 {
    private byte[] initVector;
    private byte[] cipherText;
    private byte[] knownPlaintext;
    private byte[] knownCiphertext;

    public Attacker52(byte[] initVector, byte[] cipherText, byte[] knownPlaintext, byte[] knownCiphertext) {
        this.initVector = initVector;
        this.cipherText = cipherText;
        this.knownPlaintext = knownPlaintext;
        this.knownCiphertext = knownCiphertext;
    }

    public Map<Byte, Byte> attack() {
        Map<Byte, Byte> chosenKey = new HashMap<Byte, Byte>();

        byte[] knownPlaintextPadded = padKnownPlaintext();
        byte[] knownPlaintextXoredWithIV = xorTexts(knownPlaintextPadded, initVector);

        for (int i = 0; i < knownCiphertext.length; i++) {
            byte currentXoredByte = knownPlaintextXoredWithIV[i];
            byte currentCipherByte = knownCiphertext[i];

            if ((currentXoredByte >= 'a' && currentXoredByte <= 'z') || (currentXoredByte >= 'A' && currentXoredByte <= 'Z')) {
                if (!chosenKey.containsKey(currentXoredByte)) {
                    chosenKey.put(currentXoredByte, currentCipherByte);
                }
            }
        }

        return chosenKey;
    }

    private byte[] padKnownPlaintext() {
        int originalPtSize = knownPlaintext.length;
        int padSize = originalPtSize % 8128;

        if (padSize == 0) {
            return knownPlaintext;
        }

        else {
            int i;
            byte[] newPlaintext = new byte[originalPtSize + (8128-padSize)];

            for (i = 0; i < originalPtSize; i++) {
                newPlaintext[i] = knownPlaintext[i];
            }

            for (; i < newPlaintext.length; i++) {
                newPlaintext[i] = 0;
            }

            return newPlaintext;
        }
    }

    private byte[] xorTexts(byte[] sideA, byte[] sideB) {
        byte[] returnXor = new byte[8128];
        int j = 0;

        for (int i = 0; i < 8128; i++) {
            returnXor[j] = (byte)(sideA[j] ^ sideB[i]);
            j++;
        }

        return returnXor;
    }
}