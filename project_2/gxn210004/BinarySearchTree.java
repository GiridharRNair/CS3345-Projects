/**
 * Name: Giridhar Nair
 * NetID: gxn210004
 * Date: 10/19/2024
 */

package gxn210004;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

public class BinarySearchTree<T extends Comparable<? super T>>
    implements Iterable<T> {

    static class Entry<T> {

        T element;
        Entry<T> left, right;

        public Entry(T x, Entry<T> left, Entry<T> right) {
            this.element = x;
            this.left = left;
            this.right = right;
        }
    }

    Entry<T> root;
    int size;

    static class BSTIterator<T> implements Iterator<T> {

        Stack<Entry<T>> stack = new Stack<>();

        public BSTIterator(Entry<T> root) {
            pushLeft(root);
        }

        public boolean hasNext() {
            return !stack.isEmpty();
        }

        public T next() {
            Entry<T> node = stack.pop();
            pushLeft(node.right);
            return node.element;
        }

        public void pushLeft(Entry<T> node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }
    }

    /**
     * Create an empty binary search tree
     */
    public BinarySearchTree() {
        root = null;
        size = 0;
    }

    /**
     * Check if the tree contains the element x
     * @param x element to check
     * @return true if the element is present in the tree, false otherwise
     */
    public boolean contains(T x) {
        Entry<T> node = root;
        while (node != null) {
            int cmp = x.compareTo(node.element);
            if (cmp == 0) {
                return true;
            } else if (cmp < 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return false;
    }

    /**
     * Add x to tree. If tree contains x, do nothing
     * @param x element to add
     * @return true if x is a new element added to the tree, false otherwise
     */
    public boolean add(T x) {
        if (root == null) {
            root = new Entry<>(x, null, null);
            size++;
            return true;
        }
        Entry<T> node = root;
        Entry<T> parent = null;
        int cmp = 0;
        while (node != null) {
            cmp = x.compareTo(node.element);
            if (cmp == 0) {
                return false;
            } else if (cmp < 0) {
                parent = node;
                node = node.left;
            } else {
                parent = node;
                node = node.right;
            }
        }
        Entry<T> newNode = new Entry<>(x, null, null);
        if (cmp < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        size++;
        return true;
    }

    /**
     * Remove x from tree. Return x if found, otherwise return null
     * @param x element to remove
     * @return x if found, otherwise return null
     */
    public T remove(T x) {
        Entry<T> node = root;
        Entry<T> parent = null;
        while (node != null) {
            int cmp = x.compareTo(node.element);
            if (cmp == 0) {
                T result = node.element;
                if (node.left == null || node.right == null) {
                    connectChild(node, parent);
                } else {
                    Entry<T> minRight = node.right;
                    Entry<T> minRightParent = node;
                    while (minRight.left != null) {
                        minRightParent = minRight;
                        minRight = minRight.left;
                    }
                    node.element = minRight.element;
                    connectChild(minRight, minRightParent);
                }
                size--;
                return result;
            } else if (cmp < 0) {
                parent = node;
                node = node.left;
            } else {
                parent = node;
                node = node.right;
            }
        }
        return null;
    }

    /**
     * Connect the child of the node to the parent
     * @param node node to be removed
     * @param parent parent of the node to be removed
     */
    public void connectChild(Entry<T> node, Entry<T> parent) {
        Entry<T> child = (node.left != null) ? node.left : node.right;
        if (parent == null) {
            root = child;
        } else if (node == parent.left) {
            parent.left = child;
        } else {
            parent.right = child;
        }
    }

    /**
     * Return an iterator to the tree
     * @return iterator to the tree
     */
    public Iterator<T> iterator() {
        return new BSTIterator<>(root);
    }

    /**
     * Find the smallest element in the tree
     * @return smallest element in the tree
     */
    public T min() {
        if (root == null) {
            return null;
        }
        Entry<T> node = root;
        while (node.left != null) {
            node = node.left;
        }
        return node.element;
    }

    /**
     * Find the largest element in the tree
     * @return largest element in the tree
     */
    public T max() {
        if (root == null) {
            return null;
        }
        Entry<T> node = root;
        while (node.right != null) {
            node = node.right;
        }
        return node.element;
    }

    /**
     * Find the largest element that is no larger than x
     * @param x element to compare
     * @return largest element that is no larger than x
     */
    public T floor(T x) {
        if (size == 0) {
            return null;
        }
        Entry<T> node = root;
        Entry<T> floor = null;
        while (node != null) {
            int cmp = x.compareTo(node.element);
            if (cmp == 0) {
                return node.element;
            } else if (cmp < 0) {
                node = node.left;
            } else {
                floor = node;
                node = node.right;
            }
        }
        return floor == null ? null : floor.element;
    }

    /**
     * Find the smallest element that is no smaller than x
     * @param x element to compare
     * @return smallest element that is no smaller than x
     */
    public T ceiling(T x) {
        if (size == 0) {
            return null;
        }
        Entry<T> node = root;
        Entry<T> ceiling = null;
        while (node != null) {
            int cmp = x.compareTo(node.element);
            if (cmp == 0) {
                return node.element;
            } else if (cmp < 0) {
                ceiling = node;
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return ceiling == null ? null : ceiling.element;
    }

    /**
     * Find the predecessor of x, if x is not in the tree, return floor(x)
     * @param x element to compare
     * @return predecessor of x
     */
    public T predecessor(T x) {
        Entry<T> node = root;
        Entry<T> predecessor = null;
        while (node != null) {
            int cmp = x.compareTo(node.element);
            if (cmp == 0) {
                if (node.left != null) {
                    node = node.left;
                    while (node.right != null) {
                        node = node.right;
                    }
                    return node.element;
                } else {
                    return predecessor == null ? null : predecessor.element;
                }
            } else if (cmp > 0) {
                predecessor = node;
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return predecessor == null ? floor(x) : predecessor.element;
    }

    /**
     * Find the successor of x, if x is not in the tree, return ceiling(x)
     * @param x element to compare
     * @return successor of x
     */
    public T successor(T x) {
        Entry<T> node = root;
        Entry<T> successor = null;
        while (node != null) {
            int cmp = x.compareTo(node.element);
            if (cmp == 0) {
                if (node.right != null) {
                    node = node.right;
                    while (node.left != null) {
                        node = node.left;
                    }
                    return node.element;
                } else {
                    return successor == null ? null : successor.element;
                }
            } else if (cmp < 0) {
                successor = node;
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return successor == null ? ceiling(x) : successor.element;
    }

    /**
     * Convert the tree to an array
     * @return array representation of the tree
     */
    @SuppressWarnings("rawtypes")
    public Comparable[] toArray() {
        Comparable[] arr = new Comparable[size];
        Iterator<T> it = iterator();
        int i = 0;
        while (it.hasNext()) {
            arr[i++] = it.next();
        }
        return arr;
    }

    /**
     * Main method to test the BinarySearchTree class
     * @param args command line arguments
     * @throws FileNotFoundException if file not found
     */
    public static void main(String[] args) throws FileNotFoundException {
        BinarySearchTree<Long> bst = new BinarySearchTree<>();
        Scanner sc;
        if (args.length > 0) {
            File file = new File(args[0]);
            sc = new Scanner(file);
        } else {
            sc = new Scanner(System.in);
        }
        String operation = "";
        long operand = 0;
        int modValue = 999983;
        long result = 0;
        Timer timer = new Timer();

        while (!((operation = sc.next()).equals("End"))) {
            switch (operation) {
                case "Add": {
                    operand = sc.nextInt();
                    if (bst.add(operand)) {
                        result = (result + 1) % modValue;
                    }
                    break;
                }
                case "Remove": {
                    operand = sc.nextInt();
                    if (bst.remove(operand) != null) {
                        result = (result + 1) % modValue;
                    }
                    break;
                }
                case "Min": {
                    Long min = bst.min();
                    if (min != null) {
                        result = (result + min) % modValue;
                    }
                    break;
                }
                case "Max": {
                    Long max = bst.max();
                    if (max != null) {
                        result = (result + max) % modValue;
                    }
                    break;
                }
                case "Floor": {
                    operand = sc.nextInt();
                    Long floor = bst.floor(operand);
                    if (floor != null) {
                        result = (result + floor) % modValue;
                    }
                    break;
                }
                case "Ceiling": {
                    operand = sc.nextInt();
                    Long ceiling = bst.ceiling(operand);
                    if (ceiling != null) {
                        result = (result + ceiling) % modValue;
                    }
                    break;
                }
                case "Predecessor": {
                    operand = sc.nextInt();
                    Long predecessor = bst.predecessor(operand);
                    if (predecessor != null) {
                        result = (result + predecessor) % modValue;
                    }
                    break;
                }
                case "Successor": {
                    operand = sc.nextInt();
                    Long successor = bst.successor(operand);
                    if (successor != null) {
                        result = (result + successor) % modValue;
                    }
                    break;
                }
                case "Contains": {
                    operand = sc.nextInt();
                    if (bst.contains(operand)) {
                        result = (result + 1) % modValue;
                    }
                    break;
                }
            }
        }

        sc.close();

        timer.end();

        System.out.println(result);
        System.out.println(timer);
    }

    public void printTree() {
        System.out.print("[" + size + "]");
        printTree(root);
        System.out.println();
    }

    void printTree(Entry<T> node) {
        if (node != null) {
            printTree(node.left);
            System.out.print(" " + node.element);
            printTree(node.right);
        }
    }
}
