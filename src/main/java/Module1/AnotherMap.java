package Module1;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class AnotherMap<K, V> implements Map<K, V> {
	private int size = 0;
	private Entry<K, V>[] array;

	public AnotherMap() {
		array = new Entry[16];
	}

	public AnotherMap(int capacity) {
		array = new Entry[capacity];
	}

	public void ensureCapacity(int capacity) {
		Entry<K, V>[] temp = getNonEmptyEntries().toArray(Entry[]::new);
		array = new Entry[capacity];
		size = 0;
		for (Entry<K, V> e : temp) {
			put(e.key, e.value);
		}
	}

	private void ensureCapacity() {
		ensureCapacity((int) (array.length * 1.5 + 1));
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		for (int i = getHash1(key); i < array.length; i += getHash2(key)) {
			if (array[i] == null) continue;
			if (array[i].key.equals(key)) return true;
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		return values().stream().anyMatch(v -> v.equals(value));
	}

	@Override
	public V get(Object key) {
		Entry<K, V> e = null;

		for (int i = getHash1(key); i < array.length; i += getHash2(key)) {
			e = array[i];
			if (e == null) continue;
			if (e.key.equals(key)) break;
		}

		return e != null ? e.value : null;
	}

	@Override
	public V put(K key, V value) {
		int i = getHash1(key);

		while (array[i] != null) {
			if (array[i].key.equals(key)) {
				array[i] = new Entry<>(key, value);
				return value;
			}
			if (array.length <= (i += getHash2(key))) ensureCapacity();
		}
		array[i] = new Entry<>(key, value);
		size++;
		return value;
	}

	@Override
	public V remove(Object key) {
		for (int i = getHash1(key); i < array.length; i += getHash2(key)) {
			Entry<K, V> e = array[i];
			if (e == null) return null;
			else if (e.key.equals(key)) {
				array[i] = null;
				size--;
				return e.value;
			}
		}
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		map.forEach(this::put);
	}

	@Override
	public void clear() {
		Arrays.fill(array, null);
		size = 0;
	}

	@Override
	public Set<K> keySet() {
		return getNonEmptyEntries().map(Entry::getKey).collect(Collectors.toSet());
	}

	@Override
	public Collection<V> values() {
		return getNonEmptyEntries().map(Entry::getValue).toList();
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return getNonEmptyEntries().collect(Collectors.toSet());
	}

	private Stream<Entry<K, V>> getNonEmptyEntries() {
		return Arrays.stream(array).filter(Objects::nonNull);
	}

	private int getHash1(Object key) {
		if (key == null) throw new NullPointerException("key cannot be null");
		return Math.abs(key.hashCode() % array.length);
	}

	private int getHash2(Object key) {
		return Math.abs((key.hashCode()) / getHash1(key)) % array.length + 1;
	}

	private static class Entry<K, V> implements Map.Entry<K, V> {
		private final K key;
		private V value;

		private Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			this.value = value;
			return value;
		}

		@Override
		public String toString() {
			return key.toString() + "=" + value.toString();
		}
	}

	@Override
	public String toString() {
		return "size=" + size + getNonEmptyEntries().toList();
	}
}
