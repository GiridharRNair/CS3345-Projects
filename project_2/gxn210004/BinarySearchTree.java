package gxn210004;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * A binary search tree (BST) implementation
 * @author Giridhar Nair
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

    /**
     * Constructs an empty binary search tree.
     */
    public BinarySearchTree() {
        root = null;
        size = 0;
    }

    /**
     * Checks if the tree contains the specified element.
     * @param x element to check
     * @return true if the tree contains the element, false otherwise
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
     * Creates a new node with the class-specific type.
     * @param x element of the node
     * @param left left child of the node
     * @param right right child of the node
     * @return the new node
     */
    public Entry<T> createEntry(T x, Entry<T> left, Entry<T> right) {
        return new Entry<>(x, left, right);
    }

    /**
     * Adds an element to the tree if not already present.
     * @param x element to add
     * @return true if the element is added, false if it's already present
     */
    public boolean add(T x) {
        if (root == null) {
            root = createEntry(x, null, null);
            size++;
            return true;
        }
        Entry<T> node = root, parent = null;
        int cmp = 0;
        while (node != null) {
            cmp = x.compareTo(node.element);
            if (cmp == 0) return false;
            parent = node;
            node = (cmp < 0) ? node.left : node.right;
        }
        Entry<T> newNode = createEntry(x, null, null);
        if (cmp < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
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
        Entry<T> node = root, parent = null;
        while (node != null) {
            int cmp = x.compareTo(node.element);
            if (cmp == 0) {
                T result = node.element;
                if (node.left == null || node.right == null) {
                    bypassNode(node, parent);
                } else {
                    Entry<T> minRight = node.right, minRightParent = node;
                    while (minRight.left != null) {
                        minRightParent = minRight;
                        minRight = minRight.left;
                    }
                    node.element = minRight.element;
                    bypassNode(minRight, minRightParent);
                }
                size--;
                return result;
            }
            parent = node;
            node = (cmp < 0) ? node.left : node.right;
        }
        return null;
    }

    /**
     * Connects the child of a node to its parent.
     * @param node node to be removed
     * @param parent parent of the node to be removed
     */
    public void bypassNode(Entry<T> node, Entry<T> parent) {
        Entry<T> child = (node.left != null) ? node.left : node.right;
        if (parent == null) {
            root = child;
        } else if (node == parent.left) {
            parent.left = child;
        } else {
            parent.right = child;
        }
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
