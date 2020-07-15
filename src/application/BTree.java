// Title: BTree.java
// Files: BTree.java, BTreeTest.java, Main.java, PaleontologyCollection.java,
// Specimen.java, UniqueIdentifier.java, DuplicateKeyException.java
// Course: Programming III, Fall 2019
//
// Author: George Li
// Email: gli245@wisc.edu
// Lecturer's Name: Andrew Kuemmel
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully
// acknowledge and credit those sources of help here. Instructors and TAs do
// not need to be credited here, but tutors, friends, relatives, room mates,
// strangers, and others do. If you received no outside help from either type
// of source, then please explicitly indicate NONE.
//
// Persons: None
// Online Sources: None

package application;

import java.util.List;
import java.util.ArrayList;

public class BTree<V extends Comparable<V> & UniqueIdentifier> {
  private BTNode<V> root;
  private int bFactor;
  private int height;
  private int size;

  public BTree() {
    this.root = null;
    this.bFactor = 3;
  }

  private class BTNode<V extends Comparable<V> & UniqueIdentifier> {
    private ArrayList<V> values;
    private BTNode<V> leftChild;
    private BTNode<V> middleChild;
    private BTNode<V> rightChild;
    private BTNode<V> tempChild; // for overloaded nodes
    private String tempPos;

    /**
     * Constructor for B Tree node
     */
    public BTNode() {
      this.values = new ArrayList<V>();
      this.tempPos = "";
    }

    /**
     * Insert a key and value into a B-tree node
     * 
     * @param key
     * @param value
     */
    private void insertVal(V value) {
      // inserted in sorted order
      for (int i = 0; i < this.values.size(); i++) {
        if (value.compareTo(this.values.get(i)) < 0) {
          this.values.add(i, value);
          return;
        }
      }

      this.values.add(value);

    }

    /**
     * Remove a key and value from B-Tree node
     * 
     * @param key int key to remove
     */
    private void removeVal(int key) {
      for (int i = 0; i < this.values.size(); i++) {
        if (key == this.values.get(i).getKey()) {
          this.values.remove(i);
          return;
        }
      }
    }

  }

  /**
   * Insert into a B-Tree, no duplicate keys allowed
   * 
   * @param key
   * @param value
   */
  public void insert(V value) throws IllegalArgumentException, DuplicateKeyException {
    if (value == null || value.getKey() <= 0) {
      throw new IllegalArgumentException("Value cannot be null, ID must be positive.");
    }

    this.root = insert(value, this.root, null);
  }

  /**
   * Recursive helper method for insert
   * 
   * @param value
   * @param current
   * @param parent
   * @return
   */
  public BTNode<V> insert(V value, BTNode<V> current, BTNode<V> parent)
      throws DuplicateKeyException {

    if (this.valueExists(value.getKey())) {
      throw new DuplicateKeyException();
    }

    // root case
    if (current == null) {
      current = new BTNode<V>();
      current.insertVal(value);
      return current;
    }

    // find the node

    String insertedFrom = "";

    if (!isLeaf(current)) {
      boolean isRightChild = true;
      for (int i = 0; i < current.values.size(); i++) {
        if (value.compareTo(current.values.get(i)) < 0) {
          isRightChild = false;
          if (i == 0) {
            current.leftChild = insert(value, current.leftChild, current);
            insertedFrom = "left";
            break;
          } else if (i == 1) {
            current.middleChild = insert(value, current.middleChild, current);
            insertedFrom = "middle";
            break;
          }
        }
      }

      if (isRightChild) {
        if (current.values.size() == 1) {
          current.middleChild = insert(value, current.middleChild, current);
          insertedFrom = "middle";
        } else { // only insert into right child when current has two keys
          current.rightChild = insert(value, current.rightChild, current);
          insertedFrom = "right";
        }
      }

    } else { // leaf
      current.insertVal(value);
    }
    
    /*************************** Rebalancing ***************************/

    // node too big, make sure children are assigned correctly
    if (current.values.size() >= bFactor) {
      int splitIdx = bFactor / 2;
      V rightVal = current.values.remove(splitIdx + 1);

      BTNode<V> newRightChild = new BTNode<V>();
      newRightChild.insertVal(rightVal);

      boolean newRoot = false;

      V splitValue = current.values.remove(splitIdx);
      BTNode<V> newLeftChild = current;

      // rebalancing for root
      if (parent == null) {
        parent = new BTNode<V>();
        parent.leftChild = newLeftChild;
        if (current.tempPos.equals("leftRight")) {
          newRightChild.leftChild = current.middleChild;
          newRightChild.middleChild = current.rightChild;
          newLeftChild.middleChild = current.tempChild;
        }

        if (current.tempPos.contentEquals("rightLeft")) {
          newRightChild.leftChild = current.tempChild;
          newRightChild.middleChild = current.rightChild;
        }
        parent.middleChild = newRightChild;
        newLeftChild.tempChild = null;
        newLeftChild.rightChild = null;
        newRoot = true;
      }

      parent.insertVal(splitValue);

      // new root
      if (newRoot) {
        return parent;
      }

      // Resetting children
      BTNode<V> oldLeft = current.leftChild;
      BTNode<V> oldMid = current.middleChild;
      BTNode<V> oldRight = current.rightChild;

      // node overloaded due to insertion from lower node
      if (insertedFrom.equals("left")) {
        newLeftChild.leftChild = oldLeft;
        if (current.tempPos.equals("leftRight")) {
          newLeftChild.middleChild = current.tempChild;
        }
        newRightChild.leftChild = oldMid;
        newRightChild.middleChild = oldRight;
      }

      if (insertedFrom.equals("middle") || insertedFrom.contentEquals("right")) {
        newLeftChild.leftChild = oldLeft;
        newLeftChild.middleChild = oldMid;
        newLeftChild.rightChild = null;
        if (current.tempPos.equals("rightLeft")) {
          newRightChild.leftChild = current.tempChild;
        }
        newRightChild.middleChild = oldRight;
      }

      int insertedIdx = parent.values.indexOf(splitValue);

      // parent not overloaded
      if (parent.values.size() < bFactor) {
        if (insertedIdx == 0) {
          parent.rightChild = parent.middleChild;
          parent.middleChild = newRightChild;
        }

        if (insertedIdx == 1) {
          parent.middleChild = newLeftChild;
          parent.rightChild = newRightChild;
        }

      } else {

        // parent's right child
        if (insertedIdx == 2) {
          parent.tempChild = newLeftChild;
          parent.tempPos = "rightLeft";
          return newRightChild;
        } else {
          parent.tempChild = newRightChild;
          if (insertedIdx == 0) {
            parent.tempPos = "leftRight";
          }

          if (insertedIdx == 1) {
            parent.tempPos = "rightLeft";
          }
        }
      }

    }

    return current;
  }

  /**
   * Remove a value based on key
   * 
   * @param spcKey int key to remove
   */
  public void remove(int key) {
    remove(key, this.root, null);
  }

  /**
   * Recursive helper method to remove from B-Tree
   * @param key int key to remove
   * @param current node to examine
   * @param parent node's parent
   */
  private BTNode<V> remove(int key, BTNode<V> current, BTNode<V> parent) {
    // key not found
    if (current == null) {
      return null;
    }

    for (int i = 0; i < current.values.size(); i++) {
      V value = current.values.get(i);

      if (key == value.getKey()) {
        // leaves
        if (isLeaf(current)) {
          if (current.values.size() == 1) {
            if (parent == null) {
              this.root = null;
            } else {
              /*************************** Left Child ***************************/
              if (current == parent.leftChild) {
                // merge
                if (parent.middleChild.values.size() == 1) {
                  if (parent.values.size() == 1) {
                    parent.insertVal(parent.middleChild.values.get(0));
                    current = null;
                    parent.middleChild = null;
                    return current;
                  }
                  if (parent.values.size() == 2) {
                    V leftParentMidMerge = parent.values.get(0);
                    parent.removeVal(leftParentMidMerge.getKey());
                    parent.leftChild = parent.middleChild;
                    parent.middleChild = parent.rightChild;
                    parent.rightChild = null;
                    parent.leftChild.insertVal(leftParentMidMerge);
                  }
                  return parent.leftChild;
                }

                // rotate
                if (parent.middleChild.values.size() == 2) {
                  V midToLeft = parent.middleChild.values.get(0);
                  V parentToLeft = parent.values.get(0);

                  // parent to left
                  current.removeVal(current.values.get(0).getKey());
                  current.insertVal(parentToLeft);

                  // middle to parent
                  parent.removeVal(parentToLeft.getKey());
                  parent.insertVal(midToLeft);
                  parent.middleChild.removeVal(midToLeft.getKey());
                }

              }

              /*************************** Middle Child ***************************/
              
              if (current == parent.middleChild) {
                // merge
                if (parent.leftChild.values.size() == 1 && parent.rightChild == null) {
                  parent.insertVal(parent.leftChild.values.get(0));
                  current = null;
                  parent.leftChild = null;
                  return current;
                }

                if (parent.leftChild.values.size() == 1 && parent.rightChild != null
                    && parent.rightChild.values.size() == 1) {
                  V rightParentRightMerge = parent.values.get(1);
                  parent.removeVal(rightParentRightMerge.getKey());
                  parent.middleChild = parent.rightChild;
                  parent.middleChild.insertVal(rightParentRightMerge);
                  parent.rightChild = null;
                  return parent.middleChild;
                }

                // rotate
                if (parent.leftChild.values.size() == 2) {
                  V leftToParent = parent.leftChild.values.get(1);
                  V parentToMid = parent.values.get(0);

                  // parent to mid
                  current.removeVal(current.values.get(0).getKey());
                  current.insertVal(parentToMid);

                  // left to parent
                  parent.removeVal(parentToMid.getKey());
                  parent.insertVal(leftToParent);
                  parent.leftChild.removeVal(leftToParent.getKey());
                } else if (parent.rightChild.values.size() == 2) {
                  V rightToParent = parent.rightChild.values.get(0);
                  V parentToMid = parent.values.get(1);

                  // parent to mid
                  current.removeVal(current.values.get(0).getKey());
                  current.insertVal(parentToMid);

                  // left to parent
                  parent.removeVal(parentToMid.getKey());
                  parent.insertVal(rightToParent);
                  parent.rightChild.removeVal(rightToParent.getKey());
                }
              }

              /*************************** Right Child ***************************/
              
              if (current == parent.rightChild) {
                // rotate
                if (parent.middleChild.values.size() == 2) {
                  V midToParent = parent.middleChild.values.get(1);
                  V parentToRight = parent.values.get(1);

                  parent.insertVal(midToParent);
                  parent.rightChild.insertVal(parentToRight);
                  parent.middleChild.removeVal(midToParent.getKey());
                  parent.removeVal(parentToRight.getKey());
                  current.removeVal(key);
                  return parent.rightChild;
                } else if (parent.values.size() == 2) { // merge
                  V rightParentMidMerge = parent.values.get(1);
                  parent.removeVal(rightParentMidMerge.getKey());
                  parent.middleChild.insertVal(rightParentMidMerge);
                  parent.rightChild = null;
                  return parent.rightChild;
                }

              }

            }
          }

          /*************************** Leaf Node with Size 2 ***************************/
          
          if (current.values.size() == 2) {
            current.removeVal(key);
          }

        }
        // TODO Internal Nodes
      }

      /*************************** Search for Node If Not Found ***************************/
      if (i == 0 && key < value.getKey()) {
        current.leftChild = remove(key, current.leftChild, current);
      }

      if ((i == 0 && key > value.getKey() && current.values.size() == 1)
          || (i == 1 && key < value.getKey())) {
        current.middleChild = remove(key, current.middleChild, current);
      }

      if (i == 1 && key > value.getKey()) {
        current.rightChild = remove(key, current.rightChild, current);
      }
    }

    return current;

  }

  /**
   * Helper method to determine if node is a leaf
   * @param current node to examine 
   * @return true if the node is a leaf, 0 otherwise
   */
  private boolean isLeaf(BTNode<V> current) {
    return (current.leftChild == null && current.middleChild == null && current.rightChild == null);
  }

  /**
   * Find a value in the B-Tree
   * 
   * @param key integer key
   * @return value if it exists, null otherwise
   */
  public V find(int key) {
    return recursiveFind(this.root, key);
  }

  /**
   * Recursive helper method to find value in the B-Tree
   * 
   * @param current node to examine
   * @param key     integer key
   * @return value if it exists, null otherwise
   */
  private V recursiveFind(BTNode<V> current, int key) {
    if (current != null) {
      // iterate through keys
      for (int i = 0; i < current.values.size(); i++) {
        V value = current.values.get(i);

        // found
        if (key == value.getKey()) {
          return value;
        }

        // search left child
        if (i == 0 && key < value.getKey()) {
          return recursiveFind(current.leftChild, key);
        }

        // search middle child
        if ((i == 0 && key > value.getKey() && current.values.size() == 1)
            || (i == 1 && key < value.getKey())) {
          return recursiveFind(current.middleChild, key);
        }

        // search right child
        if (i == 1 && key > value.getKey()) {
          return recursiveFind(current.rightChild, key);
        }
      }
    }
    
    // null if nothing found
    return null;
  }

  /**
   * Determines if value exists in data structure
   * 
   * @param key integer key to find
   * @return true if value with key exists, 0 otherwise
   */
  public boolean valueExists(int key) {
    if (this.find(key) != null) {
      return true;
    }

    return false;
  }

  public List<V> toList() {
    ArrayList<V> bTreeToList = new ArrayList<V>();

    toListRecursive(this.root, bTreeToList);

    return bTreeToList;
  }

  private void toListRecursive(BTNode<V> current, ArrayList<V> bTreeToList) {
    if (current != null) {
      toListRecursive(current.leftChild, bTreeToList);
      if (current.values.size() > 0) {
        bTreeToList.add(current.values.get(0));
      }
      toListRecursive(current.middleChild, bTreeToList);
      if (current.values.size() > 1) {
        bTreeToList.add(current.values.get(1));
      }
      toListRecursive(current.rightChild, bTreeToList);
    }
  }

  /**
   * Print a tree sideways to show structure. This code is completed for you.
   */
  public void printSideways() {
    System.out.println("------------------------------------------");
    recursivePrintSideways(this.root, "");
    System.out.println("------------------------------------------");
  }

  /**
   * Print nodes in a tree. This code is completed for you. You are allowed to modify this code to
   * include balance factors or heights
   * 
   * @param current
   * @param indent
   */
  private void recursivePrintSideways(BTNode<V> current, String indent) {
    if (current != null) {
      recursivePrintSideways(current.leftChild, indent + "    ");
      if (current.values.size() > 0) {
        System.out.println(indent + current.values.get(0));
      }
      recursivePrintSideways(current.middleChild, indent + "    ");
      if (current.values.size() > 1) {
        System.out.println(indent + current.values.get(1));
      }
      recursivePrintSideways(current.rightChild, indent + "    ");
    }
  }

  public static void main(String[] args) {
    try {
      BTree<Specimen> btree = new BTree<Specimen>();
      Specimen spc1 = new Specimen();
      spc1.setSpecimenKey(1000);
      spc1.setSpeciesName("Tyrannosaurus Rex");

      Specimen spc2 = new Specimen();
      spc2.setSpecimenKey(500);
      spc2.setSpeciesName("Velociraptor");

      Specimen spc3 = new Specimen();
      spc3.setSpecimenKey(1500);
      spc3.setSpeciesName("Stegosaurus");

      Specimen spc4 = new Specimen();
      spc4.setSpecimenKey(2000);
      spc4.setSpeciesName("Diplodocus");

      Specimen spc5 = new Specimen();
      spc5.setSpecimenKey(1500);
      spc5.setSpeciesName("Triceratops");

      Specimen spc6 = new Specimen();
      spc6.setSpecimenKey(1200);
      spc6.setSpeciesName("Ankylosaurus");

      Specimen spc7 = new Specimen();
      spc7.setSpecimenKey(100);
      spc7.setSpeciesName("Coelophysis");

      Specimen spc8 = new Specimen();
      spc8.setSpecimenKey(30);
      spc8.setSpeciesName("Troodon");

      Specimen spc9 = new Specimen();
      spc9.setSpecimenKey(10);
      spc9.setSpeciesName("Edmontosaurus");

      Specimen spc10 = new Specimen();
      spc10.setSpecimenKey(5);
      spc10.setSpeciesName("Aptosaurus");

      Specimen spc11 = new Specimen();
      spc11.setSpecimenKey(35);
      spc11.setSpeciesName("Pachyrhinosaurus");

      Specimen spc12 = new Specimen();
      spc12.setSpecimenKey(120);
      spc12.setSpeciesName("Allosaurus");

      Specimen spc13 = new Specimen();
      spc13.setSpecimenKey(150);
      spc13.setSpeciesName("Utahraptor");
      
      btree.insert(spc1); // 1000    
      btree.insert(spc2); // 500
      btree.insert(spc3); // 1500
//      btree.insert(spc4); // 1500
//      btree.insert(spc5); // 1500
//      btree.insert(spc6); // 1200
      // btree.insert(spc7); // 100
      btree.printSideways();
      // btree.insert(spc8); // 30
      // btree.insert(spc9); // 10
      // btree.insert(spc10); // 5
      // btree.insert(spc11); // 35
      //
      // btree.insert(spc12); // 120
      // btree.insert(spc13); // 150
      btree.remove(500);
      btree.printSideways();


    } catch (Exception e) {
      System.out.println("Error occurred");
    }
  }
}
