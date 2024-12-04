package project_2;

/**
 * AVL Tree implementation extending BinarySearchTree.
 * Balances itself to maintain O(log n) height after insertions and deletions.
 * @param <T> The type of elements stored in the tree.
 */
public class AVLTree<T extends Comparable<? super T>> extends BinarySearchTree<T> {

    /**
     * Node in AVL tree with a height attribute.
     * @param <T> Type of the element.
     */
    static class Entry<T> extends BinarySearchTree.Entry<T> {

        int height;

        /**
         * Constructs an AVL tree node.
         * @param x Element of the node
         * @param left Left child
         * @param right Right child
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
     * Creates a new AVL tree node.
     * @param x Element of the node
     * @param left Left child
     * @param right Right child
     * @return The new node
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
     * Adds an element, balancing the tree if necessary.
     * @param x Element to add
     * @return True if the element was added, false if it already exists
     */
    @Override
    public boolean add(T x) {
        if (super.add(x)) {
            find(x);
            balanceTree();
            return true;
        }
        return false;
    }

    /**
     * Removes an element and rebalances the tree.
     * @param x Element to remove
     * @return The removed element or null if not found
     */
    @Override
    public T remove(T x) {
        T result = super.remove(x);
        if (result == null) return null;
        balanceTree();
        return result;
    }

    /**
     * Balances the AVL tree from the node to the root.
     */
    private void balanceTree() {
        while (!stack.isEmpty()) {
            Entry<T> node = (Entry<T>) stack.pop();
            updateHeight(node);
            int balance = getBalance(node);

            if (balance > 1) {
                if (getBalance((Entry<T>) node.left) >= 0) {
                    node = rightRotate(node);
                } else {
                    node.left = leftRotate((Entry<T>) node.left);
                    node = rightRotate(node);
                }
            }

            if (balance < -1) {
                if (getBalance((Entry<T>) node.right) <= 0) {
                    node = leftRotate(node);
                } else {
                    node.right = rightRotate((Entry<T>) node.right);
                    node = leftRotate(node);
                }
            }

            if (stack.isEmpty()) {
                root = node;
            } else {
                Entry<T> parent = (Entry<T>) stack.peek();
                if (parent.element.compareTo(node.element) > 0) {
                    parent.left = node;
                } else {
                    parent.right = node;
                }
            }
        }
    }

    /**
     * Gets the height of a node.
     * @param node Node to check
     * @return Height of the node
     */
    private int getHeight(Entry<T> node) {
        return (node == null) ? -1 : node.height;
    }

    /**
     * Updates the height of a node.
     * @param node Node to update
     */
    private void updateHeight(Entry<T> node) {
        node.height = 1 +
        Math.max(getHeight((Entry<T>) node.left), getHeight((Entry<T>) node.right));
    }

    /**
     * Calculates the balance factor of a node.
     * @param node Node to check
     * @return Balance factor of the node
     */
    private int getBalance(Entry<T> node) {
        return getHeight((Entry<T>) node.left) - getHeight((Entry<T>) node.right);
    }

    /**
     * Performs a right rotation on the subtree.
     * @param node Root of the subtree
     * @return New root after rotation
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
     * Performs a left rotation on the subtree.
     * @param node Root of the subtree
     * @return New root after rotation
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
     * Helper object to store AVL verification details.
     * Stores flag for validity, node height, and min/max values of subtree.
     */
    class VerifyObject {

        boolean flag;
        int height;
        T max;
        T min;

        /**
         * Constructs a VerifyObject with verification status, height, min, and max values.
         * @param flag verification flag (true if valid AVL subtree)
         * @param height height of the subtree
         * @param max maximum value in the subtree
         * @param min minimum value in the subtree
         */
        VerifyObject(boolean flag, int height, T max, T min) {
            this.flag = flag;
            this.height = height;
            this.max = max;
            this.min = min;
        }
    }

    /**
     * Verifies if the tree meets AVL properties.
     * @return True if valid, false otherwise
     */
    boolean verify() {
        if (size == 0) return true;
        return verifyAVLTree((Entry<T>) root, null, null).flag;
    }

    /**
     * Verifies AVL properties for each node recursively.
     * @param node Current node
     * @param min Minimum allowed value for the node
     * @param max Maximum allowed value for the node
     * @return Verification status, height, min, and max values in a VerifyObject
     */
    private VerifyObject verifyAVLTree(Entry<T> node, T min, T max) {
        if (node == null) {
            return new VerifyObject(true, -1, null, null);
        }

        VerifyObject left = verifyAVLTree((Entry<T>) node.left, min, node.element);
        VerifyObject right = verifyAVLTree((Entry<T>) node.right, node.element, max);

        if (
            !left.flag ||
            !right.flag ||
            (min != null && node.element.compareTo(min) <= 0) ||
            (max != null && node.element.compareTo(max) >= 0) ||
            Math.abs(left.height - right.height) > 1
        ) {
            return new VerifyObject(false, -1, null, null);
        }

        T minValue = (left.min != null) ? left.min : node.element;
        T maxValue = (right.max != null) ? right.max : node.element;
        int height = 1 + Math.max(left.height, right.height);

        return new VerifyObject(true, height, maxValue, minValue);
    }
}
