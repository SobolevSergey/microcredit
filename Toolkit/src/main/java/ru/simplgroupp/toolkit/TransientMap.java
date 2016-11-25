package ru.simplgroupp.toolkit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Map, который никогда не сериализуется, даже если имеет интерфейс Serializable
 * @author irina
 *
 * @param <K>
 * @param <V>
 */
public class TransientMap<K, V> extends HashMap<K, V> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8290308512355537833L;
	private boolean readOnly = false;

	public TransientMap() {
		super();
	}

	public TransientMap(int initialCapacity) {
		super(initialCapacity);
	}

	public TransientMap(Map<? extends K, ? extends V> m) {
		super(m);
	}

	public TransientMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		
	}
	
	private void writeObject(ObjectInputStream s) throws IOException {
		
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	@Override
	public V put(K key, V value) {
		if (readOnly) {
			return null;
		}
		return super.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		if (readOnly) {
			return;
		}
		super.putAll(m);
	}
}
