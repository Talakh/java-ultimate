package org.course;

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
     * Adds an element to the hash table. Does not support duplicate elements.
     *
     * @param element
     * @return true if it was added
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
     * Prints a hash table according to the following format
     * 0: Andrii -> Taras
     * 1: Start
     * 2: Serhii
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

    /**
     * Creates a new underlying table with a given size and add all elements to the new table.
     */
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
