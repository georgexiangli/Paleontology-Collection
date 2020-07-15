// Title: PaleontologyCollection.java
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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PaleontologyCollection {
  private BTree<Specimen> myCollection;

  /**
   * Blank constructor for paleontology collection
   */
  public PaleontologyCollection() {
    this.myCollection = new BTree<Specimen>();
  }
  
  /**
   * Constructor for a paleontology collection
   * 
   * @param fileObj File created from CSV data source
   */
  public PaleontologyCollection(File fileObj) {
    try {
      this.myCollection = parseCSV(fileObj);
      System.out.println("-----");
      this.myCollection.printSideways();
    } catch (FileNotFoundException e) {
      System.out.println("File Not Found.");
    } catch (DuplicateKeyException e) {
      System.out.println("Duplicate Keys");
    } catch (NumberFormatException e) {
      System.out.println("Number format exception. Please validate that IDs and age data are numeric.");
    }
  }

  /**
   * Parses a CSV file
   * 
   * @return An array of specimens
   * @throws FileNotFoundException File not found
   */
  private static BTree<Specimen> parseCSV(File fileObj) throws FileNotFoundException, DuplicateKeyException, NumberFormatException {
    Scanner csvScanner = new Scanner(fileObj);
    String headerLine = csvScanner.nextLine();
    BTree<Specimen> specimens = new BTree<Specimen>();
    
    while (csvScanner.hasNextLine()) {
      String row = csvScanner.nextLine();
      // regex 
      // accounts for comma strings in the data
      String[] data = row.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
      int specimenKey = Integer.parseInt(data[0]);
      int occurrenceKey = Integer.parseInt(data[3]);
      String specimenPart = data[9];
      String speciesName = data[19];
      double maxMa = Double.parseDouble(data[22]);
      double minMa = Double.parseDouble(data[23]);
      
      // print data to console in background
      System.out.printf("%10d %10d %10s %35s %10.2f %10.2f\n", specimenKey, occurrenceKey,
          specimenPart, speciesName, maxMa, minMa);

      Specimen spc = new Specimen();
      spc.setSpecimenKey(specimenKey);
      spc.setOccurrenceKey(occurrenceKey);
      spc.setSpecimenPart(specimenPart);
      spc.setSpeciesName(speciesName);
      spc.setMaxMa(maxMa);
      spc.setMinMa(minMa);

      specimens.insert(spc);
    }

    csvScanner.close();
    return specimens;
  }
  
  /**
   * Insert into B-Tree
   * @param spc specimen
   * @throws IllegalArgumentException no null arguments
   * @throws DuplicateKeyException no duplicate keys
   */
  public void insert(Specimen spc) throws IllegalArgumentException, DuplicateKeyException, NumberFormatException {
    myCollection.insert(spc);
  }
  
  /**
   * Remove from B-Tree
   * @param specimenKey int key to remove
   */
  public void remove(int specimenKey) {
    try {
      myCollection.remove(specimenKey);
    } catch (NumberFormatException e1) {
      System.out.println("Use numeric inputs when appropriate");
    }
  }
  
  
  /**
   * Find specimens with the same occurrence
   * @param occurrenceKey occurrence key to look for
   * @return List of specimens with the same occurrence
   */
  public List<Specimen> toOccurrenceFilterList(int occurrenceKey) {
    List<Specimen> spcList = this.toList();
    return spcList.stream().filter(spc -> spc.getOccurrenceKey() == occurrenceKey).collect(Collectors.toList());
  }
  
  /**
   * Filter species within a certain time range
   * @param speciesName species filter
   * @param maxMaFilter maximum specimen age filter
   * @param minMaFilter minimum specimen age filter
   * @return List of specimens of a certain species existing within the time frame (exclusive bounds) 
   */
  public List<Specimen> toSpeciesMaFilterList(String speciesName, double maxMaFilter, double minMaFilter) {
    List<Specimen> spcList = this.toList();
    return spcList.stream()
        .filter(spc -> spc.getSpeciesName().equals(speciesName))
        .filter(spc -> maxMaFilter > spc.getMaxMa())
        .filter(spc -> minMaFilter < spc.getMinMa())
        .collect(Collectors.toList());
  }
  
  /**
   * Estimate of the oldest age when species evolved
   * @param speciesName species to examine
   * @return double in Ma of earliest evolution
   */
  public double averageSpeciesMaxMa(String speciesName) {
    List<Specimen> spcList = this.toList();
    return spcList.stream()
        .filter(spc -> spc.getSpeciesName().equals(speciesName))
        .collect(Collectors.averagingDouble(spc -> spc.getMaxMa()));
  }
  
  /**
   * Estimate of age when species went extinct
   * @param speciesName species to examine
   * @return double in Ma of extinction
   */
  public double averageSpeciesMinMa(String speciesName) {
    List<Specimen> spcList = this.toList();
    return spcList.stream()
        .filter(spc -> spc.getSpeciesName().equals(speciesName))
        .collect(Collectors.averagingDouble(spc -> spc.getMinMa()));
  }
  
  /**
   * Convert B-Tree of Specimens into a List
   * @return List of all specimens
   */
  public List<Specimen> toList() {
    return myCollection.toList();
  }
  
  /**
   * Find a specimen based on key   
   * @param key int key
   * @return Specimen
   */
  public Specimen find(int key) {
    return myCollection.find(key);
  }
  
  /**
   * Determines if value exists
   * @param key integer key
   * @return true if value exists, false otherwise
   */
  public boolean valueExists(int key) {
    return myCollection.valueExists(key);
  }
  
  /**
   * Main method for testing
   * 
   * @param args command line arguments
   */
  public static void main(String[] args) {
    try {
      System.out.println("First 25 Specimens");
      System.out.printf("%10s %10s %10s %35s %10s %10s\n", "specimenKey", "occurrenceKey",
          "specimenPart", "speciesName", "maxMa", "minMa");
      File input = new File("dinosaurData.csv");
      PaleontologyCollection plc = new PaleontologyCollection(input);
      
      Specimen spc1 = plc.find(13134);
      double age = plc.averageSpeciesMinMa("Futalognkosaurus dukei");
      System.out.println(age);
//      plc.myCollection.printSideways();
//      System.out.println(plc.toList().size());
    } catch (Exception e) {
      System.out.println("Error");
    }
  }

}
