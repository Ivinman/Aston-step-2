package Module1;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class AnotherMap<K, V> implements Map<K, V> {
	private int size;
	private Entry<K, V>[] array;

	public AnotherMap() {
		size = 0;
		array = new Entry[16];
	}

	public AnotherMap(int capacity) {
		size = 0;
		array = new Entry[capacity];
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
		return keySet().stream().anyMatch(k -> k.equals(key));
	}

	@Override
	public boolean containsValue(Object value) {
		return values().stream().anyMatch(v -> v.equals(value));
	}

	@Override
	public V get(Object key) {
		Entry<K, V> e = null;

		for (int i = Math.abs(key.hashCode()) % array.length; i < array.length; i++) {
			e = array[i];
			if (e == null || e.key.equals(key)) break;
		}

		return e != null ? e.value : null;
	}

	@Override
	public V put(K key, V value) {
		if (key == null) throw new NullPointerException("key cannot be null");
		for (int i = Math.abs(key.hashCode()) % array.length; i < array.length; i++) {
			if (i == array.length - 1) {
				Entry<K, V>[] temp = getPresent().toArray(Entry[]::new);
				array = new Entry[array.length + 10];
				size = 0;
				for (Entry<K, V> e : temp) {
					put(e.key, e.value);
				}
			}

			Entry<K, V> currentE = array[i];
			if (currentE == null) {
				array[i] = new Entry<>(key, value);
				size++;
				break;
			} else if (currentE.key.equals(key)) {
				array[i] = new Entry<>(key, value);
				break;
			}
		}

		return value;
	}

	@Override
	public V remove(Object key) {
		Entry<K, V> e = null;
		for (int i = Math.abs(key.hashCode()) % array.length; i < array.length; i++) {
			Entry<K, V> currentE = array[i];
			if (currentE == null) break;
			else if (currentE.key.equals(key)) {
				e = currentE;
				array[i] = null;
				size--;
				break;
			}
		}
		return Optional.ofNullable(e).isPresent() ? e.value : null;
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
		return getPresent().map(Entry::getKey).collect(Collectors.toSet());
	}

	@Override
	public Collection<V> values() {
		return getPresent().map(Entry::getValue).toList();
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return getPresent().collect(Collectors.toSet());
	}

	private Stream<Entry<K, V>> getPresent() {
		return Arrays.stream(array).filter(Objects::nonNull);
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
		return "size= " + size + getPresent().toList();
	}
}
