package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int INITIAL_CAPACITY = 16;
    public static final float LOAD_FACTOR = 0.75f;
    private int size = 0;
    private Object[] hashMapArray = new Object[INITIAL_CAPACITY];

    @Override
    public void put(K key, V value) {
        int index = getIndex(key);
        Node<K, V> newNode = new Node<>(key, value);
        putInsideBucket(newNode, index);
        if (size >= hashMapArray.length * LOAD_FACTOR) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        return getInsideBucket(key, index);
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putInsideBucket(Node<K, V> newNode, int index) {
        if (hashMapArray[index] != null) {
            Node<K, V> currentNode = (Node<K, V>) hashMapArray[index];
            Node<K, V> previousNode = null;
            while (currentNode != null) {
                if (Objects.equals(currentNode.getKey(), newNode.getKey())) {
                    currentNode.setValue(newNode.getValue());
                    break;
                }
                previousNode = currentNode;
                currentNode = currentNode.next;
            }
            if (currentNode == null) {
                previousNode.next = newNode;
                size++;
            }
        } else {
            hashMapArray[index] = newNode;
            size++;
        }
    }

    private V getInsideBucket(K key, int index) {
        if (hashMapArray[index] != null) {
            Node<K, V> currentNode = (Node<K, V>) hashMapArray[index];
            do {
                if (Objects.equals(currentNode.getKey(), key)) {
                    return currentNode.getValue();
                }
                currentNode = currentNode.next;
            } while (currentNode != null);
        }
        return null;
    }

    private int getIndex(K key) {
        if (key == null) {
            return 0;
        }
        return Math.floorMod(key.hashCode(), hashMapArray.length);
    }

    private void resize() {
        Object[] tempArray = hashMapArray;
        hashMapArray = new Object[tempArray.length << 1];
        for (int i = 0; i < tempArray.length; i++) {
            if (tempArray[i] == null) {
                continue;
            }
            Node<K, V> nodeElement = (Node<K, V>) tempArray[i];
            while (nodeElement != null) {
                put(nodeElement.getKey(), nodeElement.getValue());
                nodeElement = nodeElement.next;
                size--;
            }
        }
    }

    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
        
        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}
