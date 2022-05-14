package org.course;

/**
 * A simple implementation of the Hash Table that allows storing a generic key-value pair. The table itself is based
 * on the array of {@link Node} objects.
 * <p>
 * An initial array capacity is 16.
 * <p>
 * Every time a number of elements is equal to the array size that tables gets resized
 * (it gets replaced with a new array that it twice bigger than before). E.g. resize operation will replace array
 * of size 16 with a new array of size 32. PLEASE NOTE that all elements should be reinserted to the new table to make
 * sure that they are still accessible  from the outside by the same key.
 *
 * @param <K> key type parameter
 * @param <V> value type parameter
 */
public class HashTable<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final float GROWING_FACTOR = 1.5f;
    private int size = 0;
    private Node<K, V>[] arr;

    @SuppressWarnings({"unchecked"})
    public HashTable() {
        this.arr = new Node[DEFAULT_INITIAL_CAPACITY];
    }

    @SuppressWarnings({"unchecked"})
    public HashTable(int initCapacity) {
        this.arr = new Node[initCapacity];
    }

    /**
     * Puts a new element to the table by its key. If there is an existing element by such key then it gets replaced
     * with a new one, and the old value is returned from the method. If there is no such key then it gets added and
     * null value is returned.
     *
     * @param key   element key
     * @param value element value
     * @return old value or null
     */
    public V put(K key, V value) {
        if (size >= arr.length * LOAD_FACTOR){
            resize();
        }

        int index = calculateHash(key);
        Node<K, V> curr = arr[index];
        if (curr == null) {
            arr[index] = new Node<>(key, value);
        } else {
            V oldValue = compareAndPut(curr, key, value);
            if (oldValue != null) return oldValue;

            while (curr.next != null) {
                curr = curr.next;

                oldValue = compareAndPut(curr, key, value);
                if (oldValue != null) return oldValue;
            }
            curr.next = new Node<>(key, value);
        }
        size++;
        return null;
    }

    private V compareAndPut(Node<K, V> node, K key, V value) {
        if (node.key.equals(key)) {
            V oldValue = node.value;
            node.value = value;
            return oldValue;
        }
        return null;
    }

    private int calculateHash(K key) {
        return Math.abs(key.hashCode() % arr.length);
    }

    /**
     * Prints a content of the underlying table (array) according to the following format:
     * 0: key1:value1 -> key2:value2
     * 1:
     * 2: key3:value3
     * ...
     */
    public void printTable() {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(i + ": ");
            Node<K, V> curr = arr[i];
            while (curr != null) {
                System.out.printf("%s:%s", curr.key, curr.value);
                if (curr.next != null) {
                    System.out.print(" -> ");
                }
                curr = curr.next;
            }
            System.out.println();
        }
    }

    private void resize() {
        Node<K, V>[] oldArr = arr;
        int oldSize = size;
        arr = new Node[(int) (arr.length * GROWING_FACTOR)];
        for (Node<K, V> kvNode : oldArr) {
            Node<K, V> curr = kvNode;
            while (curr != null) {
                put(curr.key, curr.value);
                curr = curr.next;
            }
        }
        size = oldSize;
    }
}
