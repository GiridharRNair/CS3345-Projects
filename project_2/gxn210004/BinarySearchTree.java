package gxn210004;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Scanner;

/**
 * A binary search tree (BST) implementation.
 * @param <T> The type of elements stored in the tree.
 */
public class BinarySearchTree<T extends Comparable<? super T>> implements Iterable<T> {

    /**
     * Represents a node (entry) in the binary search tree.
     * @param <T> Type of the element.
     */
    static class Entry<T> {

        T element;
        Entry<T> left, right;

        /**
         * Constructs a new node with the specified element and children.
         * @param x element of the node
         * @param left left child of the node
         * @param right right child of the node
         */
        public Entry(T x, Entry<T> left, Entry<T> right) {
            this.element = x;
            this.left = left;
            this.right = right;
        }
    }

    Entry<T> root;
    int size;
    ArrayDeque<Entry<T>> stack;

    /**
     * Constructs an empty binary search tree.
     */
    public BinarySearchTree() {
        root = null;
        size = 0;
        stack = new ArrayDeque<>();
    }

    /**
     * Checks if the tree contains the specified element.
     * @param x element to check
     * @return true if the tree contains the element, false otherwise
     */
    public boolean contains(T x) {
        Entry<T> node = find(x);
        return node != null && node.element.equals(x);
    }

    /**
     * Creates a new node with the specified element and children.
     * @param x element of the node
     * @param left left child of the node
     * @param right right child of the node
     * @return the newly created node
     */
    public Entry<T> createEntry(T x, Entry<T> left, Entry<T> right) {
        return new Entry<>(x, left, right);
    }

    /**
     * Adds an element to the tree if it is not already present.
     * @param x element to add
     * @return true if the element is added, false if it's already present
     */
    public boolean add(T x) {
        if (size == 0) {
            root = createEntry(x, null, null);
            size++;
            return true;
        }

        Entry<T> node = find(x);
        if (node != null && node.element.equals(x)) return false;

        Entry<T> newNode = createEntry(x, null, null);
        Entry<T> parent = stack.isEmpty() ? null : stack.peek();

        if (parent != null) {
            if (x.compareTo(parent.element) < 0) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
        }

        size++;
        return true;
    }

    /**
     * Removes an element from the tree if present.
     * @param x element to remove
     * @return the removed element if found, otherwise null
     */
    public T remove(T x) {
        if (size == 0) return null;

        Entry<T> nodeToRemove = find(x);
        if (nodeToRemove == null) return null;

        if (nodeToRemove.left == null || nodeToRemove.right == null) {
            splice(nodeToRemove);
        } else {
            stack.push(nodeToRemove);
            Entry<T> successor = findMin(nodeToRemove.right);
            nodeToRemove.element = successor.element;
            splice(successor);
        }

        size--;
        return x;
    }

    /**
     * Finds the minimum node in the subtree rooted at the given node.
     * @param node the root of the subtree
     * @return the minimum entry in the subtree
     */
    private Entry<T> findMin(Entry<T> node) {
        while (node.left != null) {
            stack.push(node);
            node = node.left;
        }

        return node;
    }

    /**
     * Splices the given node from the tree.
     * @param node the node to splice
     */
    public void splice(Entry<T> node) {
        Entry<T> parent = stack.isEmpty() ? null : stack.peek();
        Entry<T> child = (node.left != null) ? node.left : node.right;

        if (node == root) {
            root = child;
        } else if (parent.left == node) {
            parent.left = child;
        } else {
            parent.right = child;
        }
    }

    /**
     * Finds the specified element in the tree.
     * @param x element to find
     * @return the node containing the element, or null if not found
     */
    public Entry<T> find(T x) {
        stack.clear();
        Entry<T> t = root;

        while (t != null) {
            int cmp = x.compareTo(t.element);

            if (cmp < 0) {
                stack.push(t);
                t = t.left;
            } else if (cmp > 0) {
                stack.push(t);
                t = t.right;
            } else {
                return t;
            }
        }

        return null;
    }

    // Start of Optional problems

    /** Optional problem : Iterate elements in sorted order of keys
     Solve this problem without creating an array using in-order traversal (toArray()).
     */
    public Iterator<T> iterator() {
        return null;
    }

    // Optional problem
    public T min() {
        return null;
    }

    public T max() {
        return null;
    }

    // Optional problem.  Find largest key that is no bigger than x.  Return null if there is no such key.
    public T floor(T x) {
        return null;
    }

    // Optional problem.  Find smallest key that is no smaller than x.  Return null if there is no such key.
    public T ceiling(T x) {
        return null;
    }

    // Optional problem.  Find predecessor of x.  If x is not in the tree, return floor(x).  Return null if there is no such key.
    public T predecessor(T x) {
        return null;
    }

    // Optional problem.  Find successor of x.  If x is not in the tree, return ceiling(x).  Return null if there is no such key.
    public T successor(T x) {
        return null;
    }

    // Optional: Create an array with the elements using in-order traversal of tree
    @SuppressWarnings("rawtypes")
    public Comparable[] toArray() {
        Comparable[] arr = new Comparable[size];
        /* write code to place elements in array here */
        return arr;
    }

    // End of Optional problems

    /**
     * Main method to test the BinarySearchTree class.
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
                case "Contains": {
                    operand = sc.nextInt();
                    if (bst.contains(operand)) {
                        result = (result + 1) % modValue;
                    }
                    break;
                }
            }
        }

        timer.end();
        sc.close();
        System.out.println(result);
        System.out.println(timer);
    }

    /**
     * Prints the tree in in-order sequence.
     */
    public void printTree() {
        System.out.print("[" + size + "]");
        printTree(root);
        System.out.println();
    }

    /**
     * Helper method to print the tree in in-order sequence.
     * @param node root of the tree
     */
    void printTree(Entry<T> node) {
        if (node != null) {
            printTree(node.left);
            System.out.print(" " + node.element);
            printTree(node.right);
        }
    }
}
