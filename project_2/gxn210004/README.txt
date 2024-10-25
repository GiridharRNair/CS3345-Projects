Project 2: Binary Search Tree (BST) and AVL Tree Implementation

Course: CS 3345 - Data Structures and Algorithm Analysis  
Project Due Date: 11:59 PM, Sunday, Oct 25, 2024  

Prerequisites

To compile and run this project, you need:

1. Java Runtime Environment (JRE) and Java Development Kit (JDK)
    - Verify installation by running the following in your terminal:
        ```
        java -version
        javac -version
        ```

2. Project Setup
    - Download and extract all project files.  
    - Navigate to the directory one level above the `gxn210004` folder.
        - For example, if your file structure is `folder1/folder2/gxn210004`, navigate to `folder2`.
        - You will run all the commands below from the directory above `gxn210004`.

Compilation (Although all Java files are compiled already)

Compile the Java files as follows:

    - To compile BinarySearchTree.java only:
        ```
        javac gxn210004/BinarySearchTree.java
        ```

    - To compile AVLTreeDriver.java only:
        ```
        javac gxn210004/AVLTreeDriver.java
        ```

    - To compile all Java files:
        ```
        javac gxn210004/*.java
        ```

Execution

Test cases are provided in the `p2-testcases` directory. You can execute the tests as outlined below.

1. Running Binary Search Tree (BST) Tests
    Run these commands to test `BinarySearchTree` with the provided test cases:
        ```
        java gxn210004/BinarySearchTree gxn210004/p2-testcases/bst-t01.txt
        java gxn210004/BinarySearchTree gxn210004/p2-testcases/bst-t02.txt
        java gxn210004/BinarySearchTree gxn210004/p2-testcases/bst-t03.txt
        java gxn210004/BinarySearchTree gxn210004/p2-testcases/bst-t04-no-remove.txt
        java gxn210004/BinarySearchTree gxn210004/p2-testcases/bst-t05-no-remove.txt
        java gxn210004/BinarySearchTree gxn210004/p2-testcases/bst-t06.txt
        java gxn210004/BinarySearchTree gxn210004/p2-testcases/bst-t07.txt
        java gxn210004/BinarySearchTree gxn210004/p2-testcases/bst-t08.txt
        ```

2. Running AVL Tree Tests
    Run these commands to test `AVLTreeDriver` with the provided test cases:
        ```
        java gxn210004/AVLTreeDriver gxn210004/p2-testcases/bst-t01.txt
        java gxn210004/AVLTreeDriver gxn210004/p2-testcases/bst-t02.txt
        java gxn210004/AVLTreeDriver gxn210004/p2-testcases/bst-t03.txt
        java gxn210004/AVLTreeDriver gxn210004/p2-testcases/bst-t04-no-remove.txt
        java gxn210004/AVLTreeDriver gxn210004/p2-testcases/bst-t05-no-remove.txt
        java gxn210004/AVLTreeDriver gxn210004/p2-testcases/bst-t06.txt
        java gxn210004/AVLTreeDriver gxn210004/p2-testcases/bst-t07.txt
        java gxn210004/AVLTreeDriver gxn210004/p2-testcases/bst-t08.txt
        ```

3. Running with Custom Input Files 
    You can also run the BST or AVL Tree programs with your custom input files:
        ```
        java gxn210004/AVLTreeDriver <input-file-path>
        java gxn210004/BinarySearchTree <input-file-path>
        ```

4. Running all Testcases in `p2-testcases` Directory
    Run this command to execute all test cases for `BinarySearchTree`:
        ```
        for i in $(ls gxn210004/p2-testcases/bst-t*.txt); do
            echo "Running $i"
            java gxn210004/BinarySearchTree $i
        done
        ```
    
    Run this command to execute all test cases for `AVLTreeDriver`:
        ```
        for i in $(ls gxn210004/p2-testcases/bst-t*.txt); do
            echo "Running $i"
            java gxn210004/AVLTreeDriver $i
        done
        ```