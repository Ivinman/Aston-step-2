package Module1;

import java.util.HashMap;
import java.util.Map;

public class Main {
	public static void main(String[] args) {
		Map<Integer, String> map = new AnotherMap<>(10);
		map.put(10, "sss");
		System.out.println(map);
		map.put(26, "ttt");
		System.out.println(map);
		map.put(36, "ppp");
		System.out.println("get36=" + map.get(36));
		System.out.println(map.keySet());
		System.out.println(map.values());
		System.out.println(map.entrySet());
		System.out.println(map.containsValue("fff"));
		map.remove(17);
		System.out.println(map);
		Map<Integer, String> m = new HashMap<>();
		m.put(16, "kkk");
		m.put(15, "lll");
		map.putAll(m);
		System.out.println("putAll " + map);
		System.out.println("get=" + map.get(15));
	}
}
