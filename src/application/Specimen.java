// Title: Specimen.java
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

public class Specimen implements Comparable<Specimen>, UniqueIdentifier {
  private int specimenKey;
  private int occurrenceKey;
  private String specimenPart;
  private String speciesName; // add ID if you can find a map species IDs
  private double maxMa;
  private double minMa;

  /**
   * Blank constructor
   */
  public Specimen() {
  }

  /**
   * Constructor for a specimen
   * 
   * @param specimenKey
   * @param occurrenceKey
   * @param specimenPart
   * @param speciesName
   * @param maxMa
   * @param minMa
   */
  public Specimen(int specimenKey, int occurrenceKey, String specimenPart, String speciesName,
      double maxMa, double minMa) {
    this.specimenKey = specimenKey;
    this.occurrenceKey = occurrenceKey;
    this.specimenPart = specimenPart;
    this.speciesName = speciesName;
    this.maxMa = maxMa;
    this.minMa = minMa;
  }
  
  @Override
  public int compareTo(Specimen o) {
    Integer thisKey = new Integer(this.getKey());
    Integer otherKey = new Integer(o.getKey());
    return thisKey.compareTo(otherKey);
  }

  @Override
  public String toString() {
    return this.specimenKey + " " + this.speciesName;
  }
  
  public int getKey() {
    return specimenKey;
  }

  public void setSpecimenKey(int specimenKey) {
    this.specimenKey = specimenKey;
  }

  public int getOccurrenceKey() {
    return occurrenceKey;
  }

  public void setOccurrenceKey(int occurrenceKey) {
    this.occurrenceKey = occurrenceKey;
  }

  public String getSpecimenPart() {
    return specimenPart;
  }

  public void setSpecimenPart(String specimenPart) {
    this.specimenPart = specimenPart;
  }

  public String getSpeciesName() {
    return speciesName;
  }

  public void setSpeciesName(String speciesName) {
    this.speciesName = speciesName;
  }

  public double getMaxMa() {
    return maxMa;
  }

  public void setMaxMa(double maxMa) {
    this.maxMa = maxMa;
  }

  public double getMinMa() {
    return minMa;
  }

  public void setMinMa(double minMa) {
    this.minMa = minMa;
  }
  
  

  public static void main(String[] args) {
    Specimen spc1 = new Specimen();
    spc1.setSpecimenKey(12345);
    spc1.setSpeciesName("Tyrannosaurus Rex");
    Specimen spc2 = new Specimen();
    spc2.setSpecimenKey(12344);
    spc2.setSpeciesName("Velociraptor");
    System.out.println(spc1.compareTo(spc2));
  }
  
}
