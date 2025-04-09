import Module1.AnotherMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AnotherMapTest {
	Map<Integer, String> map;

	@BeforeEach
	public void fillMap() {
		map = new AnotherMap<>();
		map.put(0, "zero");
		map.put(-1, "minus");
	}

	@Test
	public void putTest() {
		map.put(0, "newZero");
		map.put(Integer.valueOf("1"), "one");
		assertThrows(NullPointerException.class,
				() -> map.put(null, "null"),
				"Null pointer exception expected");

		assertEquals("newZero", map.get(0));
		assertEquals("minus", map.get(-1));
		assertEquals("one", map.get(1));
		assertEquals(3, map.size());
	}

	@Test
	public void putAllTest() {
		Map<Integer, String> hashMap = new HashMap<>();
		hashMap.put(0, "newZero");
		hashMap.put(18, "eighteen");
		map.putAll(hashMap);

		String[] expected = {"newZero", "minus", "eighteen"};
		assertArrayEquals(map.values().toArray(),
				expected);
	}

	@Test
	public void containsKeyTest() {
		assertTrue(map.containsKey(0));
		assertFalse(map.containsKey(1));
		assertFalse(map.containsKey(null));
	}

	@Test
	public void containsValueTest() {
		assertTrue(map.containsValue("zero"));
		assertFalse(map.containsValue("value"));
		assertFalse(map.containsValue(0));
		assertFalse(map.containsValue(null));
	}

	@Test
	public void removeTest() {
		map.remove(-1);

		assertNull(map.remove(8));
		assertFalse(map.containsValue("minus"));
		assertEquals(1, map.size());
	}

	@Test
	public void clearTest() {
		map.clear();

		assertTrue(map.isEmpty());
	}


}
