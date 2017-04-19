import java.util.Map;
import java.io.*;

public class App {
	public static void main(String[] args) throws IOException {
		String algType = "";
		String mode = "";
		String inputFilePath = "";
		String keyFilePath = "";
		String ivFilePath = "";
		String outputFilePath = "";
		
		byte[] inputText;
		byte[] ivText;
		Map<Byte, Byte> key;
		
		for (int i=0; i<args.length; i++){
			switch (args[i]) {
				case "-a":
					algType = args[++i];
					break;
				case "-c":
					mode = args[++i];
					break;
				case "-t":
					inputFilePath = args[++i];
					break;
				case "-k":
					keyFilePath = args[++i];
					break;
				case "-v":
					ivFilePath = args[++i];
					break;
				case "-o":
					outputFilePath = args[++i];
					break;
			}
		}
		
		inputText = Utils.readFile(inputFilePath);
		ivText = Utils.readFile(ivFilePath);
				
		switch (algType) {
			case "sub_cbc_10":
				if (mode.equals("encryption")) {
					key = Utils.convertTextKeyIntoKeyHash(keyFilePath);
					Encryptor encryptor = new Encryptor(inputText, ivText, key);
					byte[] result = encryptor.encrypt(10);
					Utils.writeToFile(result, outputFilePath);
				}
				else if (mode.equals("decryption")) {
					key = Utils.convertTextKeyIntoKeyHash(keyFilePath);
					Decryptor decryptor = new Decryptor(key, inputText, ivText);
					byte[] result = decryptor.decrypt(10);
					Utils.writeToFile(result, outputFilePath);
				}
				else if (mode.equals("attack")) {
					String dictionary = new String(Utils.readFile("dictionary.txt"));
					Attacker attacker = new Attacker(dictionary, inputText, ivText);
					Map<Byte, Byte> chosenKey = attacker.attack();
					Utils.writeKeyToFile(chosenKey, outputFilePath);
				}

				break;

			case "sub_cbc_52":
				if (mode.equals("encryption")) {
					key = Utils.convertTextKeyIntoKeyHash(keyFilePath);
					Encryptor encryptor = new Encryptor(inputText, ivText, key);
					byte[] result = encryptor.encrypt(8128);
					Utils.writeToFile(result, outputFilePath);
				}
				else if (mode.equals("decryption")) {
					key = Utils.convertTextKeyIntoKeyHash(keyFilePath);
					Decryptor decryptor = new Decryptor(key, inputText, ivText);
					byte[] result = decryptor.decrypt(8128);
					Utils.writeToFile(result, outputFilePath);
				}

				break;
		}
	}
}