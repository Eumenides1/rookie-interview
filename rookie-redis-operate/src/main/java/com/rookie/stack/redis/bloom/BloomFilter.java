package com.rookie.stack.redis.bloom;

import java.util.BitSet;

/**
 * @author eumenides
 * @description
 * @date 2024/5/15
 */
public class BloomFilter {
    private BitSet hashes;
    private int size;
    private int numHashes;

    public BloomFilter(int size, int numHashes) {
        this.hashes = new BitSet(size);
        this.size = size;
        this.numHashes = numHashes;
    }

    private int hash(String item, int i) {
        int hashCode = item.hashCode();
        return Math.abs(hashCode + i * hashCode % size) % size;
    }

    public void add(String item) {
        for (int i = 0; i < numHashes; i++) {
            int position = hash(item, i);
            hashes.set(position);
        }
    }

    public boolean mightContain(String item) {
        for (int i = 0; i < numHashes; i++) {
            int position = hash(item, i);
            if (!hashes.get(position)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        BloomFilter filter = new BloomFilter(1000, 10);
        filter.add("hello");
        filter.add("world");

        System.out.println("Test 'hello': " + filter.mightContain("hello")); // 应该输出 true
        System.out.println("Test 'world': " + filter.mightContain("world")); // 应该输出 true
        System.out.println("Test 'test': " + filter.mightContain("test"));   // 可能输出 false 或 true（误判）
    }
}

