// Title: DuplicateKeyException.java
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

/**
 * Checked exception thrown if the key being inserted already exists in the structure.
 * @author George Li
 */
@SuppressWarnings("serial")
public class DuplicateKeyException extends Exception {
  public DuplicateKeyException() {
    super();
  }
  
  public DuplicateKeyException(String message) {
    super(message);
  }
}
