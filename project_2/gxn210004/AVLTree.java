package gxn210004;

public class AVLTree<T extends Comparable<? super T>>
    extends BinarySearchTree<T> {

    static class Entry<T> extends BinarySearchTree.Entry<T> {

        int height;

        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            height = 0; // Newly added nodes are initialized with height 0
        }
    }

    AVLTree() {
        super();
    }

    // TO DO
    @Override
    public boolean add(T x) {
        root = add((Entry<T>) root, x);
        return true;
    }

    private Entry<T> add(Entry<T> node, T x) {
        if (node == null) {
            return new Entry<>(x, null, null);
        }

        int cmp = x.compareTo(node.element);
        if (cmp < 0) {
            node.left = add((Entry<T>) node.left, x);
        } else if (cmp > 0) {
            node.right = add((Entry<T>) node.right, x);
        } else {
            return node;
        }

        int leftHeight = (node.left == null)
            ? -1
            : ((Entry<T>) node.left).height;
        int rightHeight = (node.right == null)
            ? -1
            : ((Entry<T>) node.right).height;
        node.height = 1 + Math.max(leftHeight, rightHeight);

        return balance(node);
    }

    private Entry<T> balance(Entry<T> node) {
        int balanceFactor = getBalance(node);
        if (balanceFactor > 1) {
            if (getBalance((Entry<T>) node.left) >= 0) {
                return rightRotate(node); // Left-Left case
            } else {
                node.left = leftRotate((Entry<T>) node.left); // Left-Right case
                return rightRotate(node);
            }
        }

        if (balanceFactor < -1) {
            if (getBalance((Entry<T>) node.right) <= 0) {
                return leftRotate(node); // Right-Right case
            } else {
                node.right = rightRotate((Entry<T>) node.right); // Right-Left case
                return leftRotate(node);
            }
        }

        return node; // Return balanced node
    }

    private int getBalance(Entry<T> node) {
        if (node == null) return 0;
        int leftHeight = (node.left == null)
            ? -1
            : ((Entry<T>) node.left).height;
        int rightHeight = (node.right == null)
            ? -1
            : ((Entry<T>) node.right).height;
        return leftHeight - rightHeight;
    }

    public Entry<T> rightRotate(Entry<T> y) {
        Entry<T> x = (Entry<T>) y.left;
        Entry<T> T2 = (Entry<T>) x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        y.height = 1 +
        Math.max(
            (y.left == null) ? -1 : ((Entry<T>) y.left).height,
            (y.right == null) ? -1 : ((Entry<T>) y.right).height
        );
        x.height = 1 +
        Math.max(
            (x.left == null) ? -1 : ((Entry<T>) x.left).height,
            (x.right == null) ? -1 : ((Entry<T>) x.right).height
        );

        // Return new root
        return x;
    }

    public Entry<T> leftRotate(Entry<T> x) {
        Entry<T> y = (Entry<T>) x.right;
        Entry<T> T2 = (Entry<T>) y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        x.height = 1 +
        Math.max(
            (x.left == null) ? -1 : ((Entry<T>) x.left).height,
            (x.right == null) ? -1 : ((Entry<T>) x.right).height
        );
        y.height = 1 +
        Math.max(
            (y.left == null) ? -1 : ((Entry<T>) y.left).height,
            (y.right == null) ? -1 : ((Entry<T>) y.right).height
        );

        // Return new root
        return y;
    }

    // Optional. Complete for extra credit
    @Override
    public T remove(T x) {
        return super.remove(x);
    }

    // TO DO
    boolean verify() {
        return verifyAVLTree((Entry<T>) root) != -2;
    }

    int verifyAVLTree(Entry<T> node) {
        if (node == null) return -1;

        int leftHeight = verifyAVLTree((Entry<T>) node.left);
        int rightHeight = verifyAVLTree((Entry<T>) node.right);

        if (leftHeight == -1 || rightHeight == -1) return -1;

        if (Math.abs(leftHeight - rightHeight) > 1) return -1;

        if (
            node.left != null && node.element.compareTo(node.left.element) < 0
        ) return -1;

        if (
            node.right != null && node.element.compareTo(node.right.element) > 0
        ) return -1;

        return 1 + Math.max(leftHeight, rightHeight);
    }
}
