// npx prettier . --write 
// javac gxn210004/AVLTreeDriver.java 

package gxn210004;

public class AVLTree<T extends Comparable<? super T>> extends BinarySearchTree<T> {

    static class Entry<T> extends BinarySearchTree.Entry<T> {

        int height;

        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            height = 0; //test change
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
            root = rebalance((Entry<T>) root);
            return true;
        }
        return false;
    }

    private Entry<T> rebalance(Entry<T> node) {
        if (node == null) return null;
        
        // First rebalance children
        node.left = rebalance((Entry<T>) node.left);
        node.right = rebalance((Entry<T>) node.right);

        updateHeight(node);
        int balance = getBalance(node);
    
        // Left Heavy
        if (balance > 1) {
            if (getBalance((Entry<T>) node.left) >= 0) {
                // Left-Left case
                return rightRotate(node);
            } else {
                // Left-Right case
                node.left = leftRotate((Entry<T>) node.left);
                return rightRotate(node);
            }
        }
    
        // Right Heavy
        if (balance < -1) {
            if (getBalance((Entry<T>) node.right) <= 0) {
                // Right-Right case
                return leftRotate(node);
            } else {
                // Right-Left case
                node.right = rightRotate((Entry<T>) node.right);
                return leftRotate(node);
            }
        }
    
        // No balancing needed
        return node;
    }    
    

    private int getHeight(Entry<T> node) {
        return (node == null) ? -1 : node.height;
    }

    private void updateHeight(Entry<T> node) {
        node.height = 1 +
        Math.max(getHeight((Entry<T>) node.left), getHeight((Entry<T>) node.right));
    }

    private int getBalance(Entry<T> node) {
        return getHeight((Entry<T>) node.left) - getHeight((Entry<T>) node.right);
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
        T node = super.remove(x);
        if (node != null) {
            root = rebalance((Entry<T>) root);
            return node;
        }
        return null;
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
