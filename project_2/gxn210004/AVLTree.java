package gxn210004;

public class AVLTree<T extends Comparable<? super T>> extends BinarySearchTree<T> {

    static class Entry<T> extends BinarySearchTree.Entry<T> {

        int height;

        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            height = 0;
        }
    }

    AVLTree() {
        super();
    }

    @Override
    public Entry<T> createEntry(
        T x,
        BinarySearchTree.Entry<T> left,
        BinarySearchTree.Entry<T> right
    ) {
        return new Entry<>(x, (Entry<T>) left, (Entry<T>) right);
    }

    // TO DO
    @Override
    public boolean add(T x) {
        if (super.add(x)) {
            root = balanceTree((Entry<T>) root, x);
            return true;
        }
        return false;
    }

    private Entry<T> balanceTree(Entry<T> node, T x) {
        updateHeight(node);
        int balance = getBalance(node);

        if (balance > 1) {
            if (x.compareTo(node.left.element) < 0) {
                return rightRotate(node);
            } else {
                node.left = leftRotate((Entry<T>) node.left);
                return rightRotate(node);
            }
        }

        if (balance < -1) {
            if (x.compareTo(node.right.element) > 0) {
                return leftRotate(node);
            } else {
                node.right = rightRotate((Entry<T>) node.right);
                return leftRotate(node);
            }
        }

        return node;
    }

    private void updateHeight(Entry<T> node) {
        int leftHeight = (node.left == null) ? -1 : ((Entry<T>) node.left).height;
        int rightHeight = (node.right == null) ? -1 : ((Entry<T>) node.right).height;
        node.height = 1 + Math.max(leftHeight, rightHeight);
    }

    private int getBalance(Entry<T> node) {
        int leftHeight = (node.left == null) ? -1 : ((Entry<T>) node.left).height;
        int rightHeight = (node.right == null) ? -1 : ((Entry<T>) node.right).height;
        return leftHeight - rightHeight;
    }

    private Entry<T> rightRotate(Entry<T> node) {
        Entry<T> newRoot = (Entry<T>) node.left;
        node.left = newRoot.right;
        newRoot.right = node;

        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    private Entry<T> leftRotate(Entry<T> node) {
        Entry<T> newRoot = (Entry<T>) node.right;
        node.right = newRoot.left;
        newRoot.left = node;

        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    //Optional. Complete for extra credit
    @Override
    public T remove(T x) {
        return super.remove(x);
    }

    /** TO DO
     *	verify if the tree is a valid AVL tree, that satisfies
     *	all conditions of BST, and the balancing conditions of AVL trees.
     *	In addition, do not trust the height value stored at the nodes, and
     *	heights of nodes have to be verified to be correct.  Make your code
     *  as efficient as possible. HINT: Look at the bottom-up solution to verify BST
     */
    boolean verify() {
        return verifyAVLTree((Entry<T>) root, null, null) != -1;
    }

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
