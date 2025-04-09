package Module1;

import java.util.HashMap;
import java.util.Map;

public class Main {
	public static void main(String[] args) {
		Map<Integer, String> map = new AnotherMap<>(1);
		map.put(10, "sss");
		System.out.println(map);
		map.put(10, "ttt");
		System.out.println(map);
		map.put(26, "ppp");
		System.out.println(map);
		System.out.println(map.keySet());
		System.out.println(map.values());
		System.out.println(map.entrySet());
		System.out.println(map.containsKey(null));
		System.out.println(map.containsValue("fff"));
		map.remove(17);
		System.out.println(map);
		Map<Integer, String> m = new HashMap<>();
		m.put(16, "kkk");
		m.put(15, "lll");
		map.putAll(m);
		System.out.println("putAll " + map);
		System.out.println(map.get(15));
		map.clear();
		System.out.println(map);
	}
}
