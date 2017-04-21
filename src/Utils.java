import com.sun.tools.javac.util.ArrayUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;

public class Utils {
	public static byte[] readFile(String fileName) throws IOException {
		byte[] bytesContent = Files.readAllBytes(Paths.get(fileName));
		return bytesContent;
	}
	
	public static void writeToFile(byte[] content, String filePath) throws IOException {
		Files.write(Paths.get(filePath), content);
	}
	
	public static Map<Byte, Byte> convertTextKeyIntoKeyHash(String fileName) throws IOException {
        Map<Byte, Byte> map = new HashMap<Byte, Byte>();
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String line = "";
        while ((line = in.readLine()) != null) {
            String parts[] = line.split(" ");
            byte key = parts[0].getBytes("UTF-8")[0];
            byte value = parts[1].getBytes("UTF-8")[0];
            map.put(key, value);
        }
        in.close();
        return map;
	}

	public static void writeKeyToFile(Map<Byte, Byte> key, String fileName) throws IOException {
		FileOutputStream fs = new FileOutputStream(new File(fileName));
		BufferedOutputStream bs = new BufferedOutputStream(fs);
		byte[] bytesToWrite = new byte[3];

		for (Entry<Byte, Byte> entry : key.entrySet()) {
			bytesToWrite[0] = entry.getKey();
			bytesToWrite[1] = ' ';
			bytesToWrite[2] = entry.getValue();

			bs.write(bytesToWrite);
			bs.write(System.lineSeparator().getBytes());
		}

		bs.close();
	}

	
	public static Byte getKeyByValue(Map<Byte, Byte> key, Byte value) {
	    for (Entry<Byte, Byte> entry : key.entrySet()) {
	        if (Objects.equals(value, entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
	
	public static HashSet<String> createDictionaryFromFile(String dictionary){
		HashSet<String> dictionarySet = new HashSet<String>();
		String [] words = dictionary.split(System.lineSeparator());
		for (String s: words){
			dictionarySet.add(s);
		}
		return dictionarySet;
	}

	public static List<Map<Byte, Byte>> generateKeyList(String keyValues) throws UnsupportedEncodingException {
		List<Map<Byte, Byte>> list = new ArrayList<Map<Byte, Byte>>();
		Set<String> possibleKeysAsStrings = generatePerm(keyValues);
		Map<Byte, Byte> mapToAdd = null;

		for (String s : possibleKeysAsStrings) {
			mapToAdd = new HashMap<Byte, Byte>();

			mapToAdd.put((byte)('a'), s.getBytes("UTF-8")[0]);
			mapToAdd.put((byte)('b'), s.getBytes("UTF-8")[1]);
			mapToAdd.put((byte)('c'), s.getBytes("UTF-8")[2]);
			mapToAdd.put((byte)('d'), s.getBytes("UTF-8")[3]);
			mapToAdd.put((byte)('e'), s.getBytes("UTF-8")[4]);
			mapToAdd.put((byte)('f'), s.getBytes("UTF-8")[5]);
			mapToAdd.put((byte)('g'), s.getBytes("UTF-8")[6]);
			mapToAdd.put((byte)('h'), s.getBytes("UTF-8")[7]);

			list.add(mapToAdd);
		}

		return list;
	}

	public static List<Map<Byte, Byte>> generateKeyListByPartialKeysAndValues(List<Byte> keys, String keyValues, Map<Byte, Byte> partialKey) {
		List<Map<Byte, Byte>> list = new ArrayList<Map<Byte, Byte>>();

		if (keyValues.isEmpty()) {
			list.add(partialKey);
			return list;
		}

		Set<String> possibleKeysAsStrings = generatePerm(keyValues);
		Map<Byte, Byte> mapToAdd = null;
		int i;

		for (String s : possibleKeysAsStrings) {
			mapToAdd = new HashMap<Byte, Byte>();
			mapToAdd.putAll(partialKey);
			i = 0;

			for (Byte b : keys) {
				mapToAdd.put(b, s.getBytes()[i++]);
			}

			list.add(mapToAdd);
		}

		return list;
	}

	public static String convertListOfBytesToString(List<Byte> list) {
		int i = 0;
		byte[] bytesList = new byte[list.size()];

		for (Byte b : list) {
			bytesList[i++] = b;
		}

		return new String(bytesList);
	}

	private static Set<String> generatePerm(String input) {
		Set<String> set = new HashSet<String>();

		if (input == "")
			return set;

		Character a = input.charAt(0);

		if (input.length() > 1) {
			input = input.substring(1);
			Set<String> permSet = generatePerm(input);

			for (String x : permSet) {
				for (int i = 0; i <= x.length(); i++) {
					set.add(x.substring(0, i) + a + x.substring(i));
				}
			}
		}

		else {
			set.add(a + "");
		}

		return set;
	}
}