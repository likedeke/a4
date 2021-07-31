package org.atomic.infrastructure.collection;

import java.util.Iterator;

/**
 * Description: 自适应容量堆栈 <br>
 * <pre>
 *     基于数组实现的栈（自动扩容）
 * </pre>
 *
 * @author <a href="mailto:likelovec@gmail.com">like</a>
 * @date 2021-07-31 18:46:23
 * @see FixedCapacityStack
 */
public class ResizingArrayStack<Item> extends FixedCapacityStack<Item> implements Iterable<Item> {

    protected int n;
    protected Item[] a;

    // Initialization method start
    public ResizingArrayStack(int capacity) {
        super(capacity);
    }

    public ResizingArrayStack<Item> of(int capacity) {
        return new ResizingArrayStack<>(capacity);
    }
    // Initialization method end

    @Override
    public void push(Item item) {
        if (n == a.length) resize(a.length * 2);
        super.push(item);
    }

    @Override
    public Item pop() {
        final Item item = super.pop();
        a[n] = null; // 移除在数组中的item，避免对象游离。
        if (n > 0 && n == a.length / 4) resize(a.length / 2);
        return item;
    }

    @Override
    public Iterator<Item> iterator() {
        return new ReverseArrayIterator();
    }

    /**
     * 扩容 / 缩容
     *
     * @param maxCapacity 最大容量
     */
    private void resize(int maxCapacity) {
        Item[] temp = ( Item[] ) new Object[maxCapacity];
        if (n >= 0)
            System.arraycopy(a, 0, temp, 0, n);
        a = temp;
    }

    /**
     * Description: 反向迭代器 <br>
     * <pre>
     *     根据栈设计实现
     * </pre>
     *
     * @author <a href="mailto:likelovec@gmail.com">like</a>
     * @date 2021-07-31 19:02:31
     * @see Iterator
     */
    private class ReverseArrayIterator implements Iterator<Item> {

        private int index = n;

        @Override
        public boolean hasNext() {
            return index > 0;
        }

        @Override
        public Item next() {
            return a[--index];
        }
    }
}
