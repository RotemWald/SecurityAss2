import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
	public static String readFile(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
	}
	
	public static void writeToFile(String content, String filePath) throws IOException {
		Files.write(Paths.get(filePath), content.getBytes());
	}
	
	public static Map<Character, Character> convertTextKeyIntoKeyHash(String fileName) throws IOException {
        Map<Character, Character> map = new HashMap<Character, Character>();
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String line = "";
        while ((line = in.readLine()) != null) {
            String parts[] = line.split(" ");
            char key = parts[0].charAt(0);
            char value = parts[1].charAt(0);
            map.put(key, value);
        }
        in.close();
        return map;
	}

	public static void writeKeyToFile(Map<Character, Character> key, String fileName) throws IOException {
		Files.write(Paths.get(fileName), () -> key.entrySet().stream()
				.<CharSequence>map(e -> e.getKey() + " " + e.getValue())
				.iterator());
	}
	
	public static Character getKeyByValue(Map <Character, Character> key,Character value) {
	    for (Entry<Character, Character> entry : key.entrySet()) {
	        if (Objects.equals(value, entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
	
	public static HashSet<String> createDictionaryFromFile(String dictionary){
		HashSet<String> dictionarySet = new HashSet<String>();
		String [] words = dictionary.split("\r\n");
		for (String s: words){
			dictionarySet.add(s);
		}
		return dictionarySet;
	}
	
	public static List<Map<Character, Character>> generateKeyList(String keyValues) {
		List<Map<Character, Character>> list = new ArrayList<Map<Character, Character>>();
		Set<String> possibleKeysAsStrings = generatePerm(keyValues);
		Map<Character, Character> mapToAdd = null;
		
		for (String s : possibleKeysAsStrings) {
			mapToAdd = new HashMap<Character, Character>();
			
			mapToAdd.put('a', s.charAt(0));
			mapToAdd.put('b', s.charAt(1));
			mapToAdd.put('c', s.charAt(2));
			mapToAdd.put('d', s.charAt(3));
			mapToAdd.put('e', s.charAt(4));
			mapToAdd.put('f', s.charAt(5));
			mapToAdd.put('g', s.charAt(6));
			mapToAdd.put('h', s.charAt(7));
			
			list.add(mapToAdd);
		}
		
		return list; 
	}

	private static Set<String> generatePerm(String input)
	{
	    Set<String> set = new HashSet<String>();
	    if (input == "")
	        return set;

	    Character a = input.charAt(0);

	    if (input.length() > 1)
	    {
	        input = input.substring(1);

	        Set<String> permSet = generatePerm(input);

	        for (String x : permSet)
	        {
	            for (int i = 0; i <= x.length(); i++)
	            {
	                set.add(x.substring(0, i) + a + x.substring(i));
	            }
	        }
	    }
	    else
	    {
	        set.add(a + "");
	    }
	    return set;
	}
}