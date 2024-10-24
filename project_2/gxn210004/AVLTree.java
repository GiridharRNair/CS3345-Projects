package gxn210004;

/**
 * AVL Tree implementation that extends BinarySearchTree.
 * @param <T> The type of elements stored in the tree.
 * @author Giridhar Nair
 */
public class AVLTree<T extends Comparable<? super T>> extends BinarySearchTree<T> {

    /**
     * Represents a node in the AVL tree with a height attribute.
     * @param <T> Type of the element.
     */
    static class Entry<T> extends BinarySearchTree.Entry<T> {

        int height;

        /**
         * Constructs an AVL tree node.
         * @param x element of the node
         * @param left left child
         * @param right right child
         */
        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            height = 0;
        }
    }

    /**
     * Constructs an empty AVL tree.
     */
    AVLTree() {
        super();
    }

    /**
     * Creates a new node with the class-specific type.
     * @param x element of the node
     * @param left left child of the node
     * @param right right child of the node
     * @return the new node
     */
    @Override
    public Entry<T> createEntry(
        T x,
        BinarySearchTree.Entry<T> left,
        BinarySearchTree.Entry<T> right
    ) {
        return new Entry<>(x, (Entry<T>) left, (Entry<T>) right);
    }

    /**
     * Adds an element and rebalances the tree if needed.
     * @param x element to add
     * @return true if the element was added, false if it already exists
     */
    @Override
    public boolean add(T x) {
        if (super.add(x)) {
            root = rebalance((Entry<T>) root);
            return true;
        }
        return false;
    }

    /**
     * Rebalances the subtree rooted at the given node.
     * @param node root of the subtree
     * @return the new root after rebalancing
     */
    private Entry<T> rebalance(Entry<T> node) {
        if (node == null) return null;

        node.left = rebalance((Entry<T>) node.left);
        node.right = rebalance((Entry<T>) node.right);

        updateHeight(node);
        int balance = getBalance(node);

        if (balance > 1) {
            if (getBalance((Entry<T>) node.left) >= 0) {
                return rightRotate(node);
            } else {
                node.left = leftRotate((Entry<T>) node.left);
                return rightRotate(node);
            }
        }

        if (balance < -1) {
            if (getBalance((Entry<T>) node.right) <= 0) {
                return leftRotate(node);
            } else {
                node.right = rightRotate((Entry<T>) node.right);
                return leftRotate(node);
            }
        }

        return node;
    }

    /**
     * Gets the height of a node.
     * @param node node to check
     * @return height of the node
     */
    private int getHeight(Entry<T> node) {
        return (node == null) ? -1 : node.height;
    }

    /**
     * Updates the height of a node.
     * @param node node to update
     */
    private void updateHeight(Entry<T> node) {
        node.height = 1 +
        Math.max(getHeight((Entry<T>) node.left), getHeight((Entry<T>) node.right));
    }

    /**
     * Gets the balance factor of a node.
     * @param node node to check
     * @return balance factor of the node
     */
    private int getBalance(Entry<T> node) {
        return getHeight((Entry<T>) node.left) - getHeight((Entry<T>) node.right);
    }

    /**
     * Rotates the subtree to the right.
     * @param node root of the subtree
     * @return new root after right rotation
     */
    private Entry<T> rightRotate(Entry<T> node) {
        Entry<T> newRoot = (Entry<T>) node.left;

        node.left = newRoot.right;
        newRoot.right = node;

        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    /**
     * Rotates the subtree to the left.
     * @param node root of the subtree
     * @return new root after left rotation
     */
    private Entry<T> leftRotate(Entry<T> node) {
        Entry<T> newRoot = (Entry<T>) node.right;

        node.right = newRoot.left;
        newRoot.left = node;

        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    /**
     * Removes an element and rebalances the tree.
     * @param x element to remove
     * @return the removed element or null if not found
     */
    @Override
    public T remove(T x) {
        T node = super.remove(x);
        if (node != null) {
            root = rebalance((Entry<T>) root);
            return node;
        }
        return null;
    }

    /**
     * Verifies if the tree is a valid AVL tree.
     * @return true if valid, false otherwise
     */
    boolean verify() {
        return verifyAVLTree((Entry<T>) root, null, null) != -1;
    }

    /**
     * Verifies if the tree is a valid AVL tree.
     * @param node node to verify
     * @param min minimum value of the node
     * @param max maximum value of the node
     * @return -1 if the tree is invalid, otherwise the height of the tree
     */
    int verifyAVLTree(Entry<T> node, T min, T max) {
        if (node == null) return 0;

        if (min != null && node.element.compareTo(min) <= 0) return -1;
        if (max != null && node.element.compareTo(max) >= 0) return -1;

        int leftHeight = verifyAVLTree((Entry<T>) node.left, min, node.element);
        int rightHeight = verifyAVLTree((Entry<T>) node.right, node.element, max);

        if (leftHeight == -1 || rightHeight == -1) return -1;

        if (Math.abs(leftHeight - rightHeight) > 1) return -1;

        node.height = 1 + Math.max(leftHeight, rightHeight);

        return node.height;
    }
}
