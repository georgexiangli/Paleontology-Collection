// Title: BTreeTest.java
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

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.Timeout;

class BTreeTest {
  private BTree<Specimen> plc;
  private Specimen spc1;
  private Specimen spc2;
  private Specimen spc3;
  private Specimen spc4;
  private Specimen spc5;
  private Specimen spc6;
  private Specimen spc7;
  private Specimen spc8;
  private Specimen spc9;
  
  @Rule
  public Timeout globalTimeout = new Timeout(20000, TimeUnit.MILLISECONDS);

  @Before
  public void setUp() throws Exception {
    this.plc = new BTree<Specimen>();

    this.spc1 = new Specimen();
    this.spc1.setSpecimenKey(1000);
    
    this.spc2 = new Specimen();
    spc2.setSpecimenKey(500);
    
    this.spc3 = new Specimen();
    spc3.setSpecimenKey(1500);
    
    this.spc4 = new Specimen();
    spc4.setSpecimenKey(250);
    
    this.spc5 = new Specimen();
    spc5.setSpecimenKey(100);
    
    this.spc6 = new Specimen();
    spc6.setSpecimenKey(2000);
    
    this.spc7 = new Specimen();
    spc7.setSpecimenKey(2500);
    
    this.spc8 = new Specimen();
    spc8.setSpecimenKey(3000);
    
    this.spc9 = new Specimen();
    spc9.setSpecimenKey(750);
  }

  @After
  public void tearDown() throws Exception {
    this.plc = null;
  }

  @Test
  public void test01_insertRoot() {

    try {
      setUp();

      this.plc.insert(spc1); // 1000
      List<Specimen> compareList = this.plc.toList();
      List<Specimen> targetList = new ArrayList<Specimen>();
      targetList.add(spc1);
      assertEquals("test01: failed - B-Tree insertion incorrect", compareList, targetList);
    } catch (DuplicateKeyException e1) {
      fail("test01: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test01: failed - unexpected exception occurred");
    }
  }

  @Test
  public void test02_insertSecond() {
    try {
    setUp();

    
      this.plc.insert(spc1); // 1000
      this.plc.insert(spc2); // 500
      
      List<Specimen> compareList = this.plc.toList();
      
      List<Specimen> targetList = new ArrayList<Specimen>();
      
      // insert in order
      targetList.add(spc2); // 500  
      targetList.add(spc1); // 1000
      
      assertEquals("test02: failed - B-Tree insertion incorrect", compareList, targetList);
    } catch (DuplicateKeyException e1) {
      fail("test02: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test02: failed - unexpected exception occurred");
    }
  }

  @Test
  public void test03_insertPushUp() {
    try {
      setUp();
    
      this.plc.insert(spc1); // 1000
      this.plc.insert(spc2); // 500
      this.plc.insert(spc3); // 1500
      
      List<Specimen> compareList = this.plc.toList();
      
      List<Specimen> targetList = new ArrayList<Specimen>();
      
      // insert in order
      targetList.add(spc2); // 500
      targetList.add(spc1); // 1000
      targetList.add(spc3); // 1500
      
      assertEquals("test03: failed - B-Tree insertion incorrect", compareList, targetList);
    } catch (DuplicateKeyException e1) {
      fail("test03: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test03: failed - unexpected exception occurred");
    }
  }

  @Test
  public void test04_insertDuplicate() {
    try {
      setUp();
    
      this.plc.insert(spc1); // 1000
      this.plc.insert(spc1); // 1000
      
    } catch (DuplicateKeyException e1) {
        assertTrue(true);
    } catch (Exception e2) {
      fail("test04: failed - unexpected exception occurred");
    }
  }
  
  @Test
  public void test05_regularLeaf() {
    try {
      setUp();
    
      this.plc.insert(spc1); // 1000
      this.plc.insert(spc2); // 500
      this.plc.insert(spc3); // 1500
      this.plc.insert(spc4); // 250
      
      List<Specimen> compareList = this.plc.toList();
      
      List<Specimen> targetList = new ArrayList<Specimen>();
      
      // insert in order
      targetList.add(spc4); // 250
      targetList.add(spc2); // 500
      targetList.add(spc1); // 1000
      targetList.add(spc3); // 1500
      
      assertEquals("test05: failed - B-Tree insertion incorrect", compareList, targetList);
    } catch (DuplicateKeyException e1) {
      fail("test05: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test05: failed - unexpected exception occurred");
    }
  }
  
  @Test
  public void test06_leftPushUp() {
    try {
      setUp();
    
      this.plc.insert(spc1); // 1000
      this.plc.insert(spc2); // 500
      this.plc.insert(spc3); // 1500
      this.plc.insert(spc4); // 250
      this.plc.insert(spc5); // 100
      
      List<Specimen> compareList = this.plc.toList();
      
      List<Specimen> targetList = new ArrayList<Specimen>();
      
      // insert in order
      targetList.add(spc5); // 100
      targetList.add(spc4); // 250
      targetList.add(spc2); // 500
      targetList.add(spc1); // 1000
      targetList.add(spc3); // 1500
      
      assertEquals("test06: failed - B-Tree insertion incorrect", compareList, targetList);
    } catch (DuplicateKeyException e1) {
      fail("test06: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test06: failed - unexpected exception occurred");
    }
  }
  
  @Test
  public void test07_middlePushUp() {
    try {
      setUp();
    
      this.plc.insert(spc1); // 1000
      this.plc.insert(spc2); // 500
      this.plc.insert(spc3); // 1500
      this.plc.insert(spc4); // 250
      this.plc.insert(spc6); // 2000
      this.plc.insert(spc7); // 2500
      
      List<Specimen> compareList = this.plc.toList();
      
      List<Specimen> targetList = new ArrayList<Specimen>();
      
      // insert in order
      targetList.add(spc4); // 250
      targetList.add(spc2); // 500
      targetList.add(spc1); // 1000
      targetList.add(spc3); // 1500
      targetList.add(spc6); // 2000
      targetList.add(spc7); // 2500
      
      assertEquals("test06: failed - B-Tree insertion incorrect", compareList, targetList);
    } catch (DuplicateKeyException e1) {
      fail("test06: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test06: failed - unexpected exception occurred");
    }
  }
  
  @Test
  public void test08_rightPushUp() {
    try {
      setUp();
    
      this.plc.insert(spc1); // 1000
      this.plc.insert(spc2); // 500
      this.plc.insert(spc3); // 1500
      this.plc.insert(spc4); // 250
      this.plc.insert(spc5); // 100
      this.plc.insert(spc6); // 2000
      this.plc.insert(spc7); // 2500
      
      List<Specimen> compareList = this.plc.toList();
      
      List<Specimen> targetList = new ArrayList<Specimen>();
      
      // insert in order
      targetList.add(spc5); // 100
      targetList.add(spc4); // 250
      targetList.add(spc2); // 500
      targetList.add(spc1); // 1000
      targetList.add(spc3); // 1500
      targetList.add(spc6); // 2000
      targetList.add(spc7); // 2500
      
      assertEquals("test08: failed - B-Tree insertion incorrect", compareList, targetList);
    } catch (DuplicateKeyException e1) {
      fail("test08: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test08: failed - unexpected exception occurred");
    }
  }
  
  @Test
  public void test09_removeRoot() {
    try {
      setUp();
    
      this.plc.insert(spc1); // 1000
      this.plc.remove(1000);
      
      List<Specimen> compareList = this.plc.toList();
      List<Specimen> targetList = new ArrayList<Specimen>();
         
      assertEquals("test09: failed - B-Tree remove incorrect", compareList, targetList);
    } catch (DuplicateKeyException e1) {
      fail("test09: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test09: failed - unexpected exception occurred");
    }
  }
  
  @Test
  public void test10_removeRootLeaf() {
    try {
      setUp();
    
      this.plc.insert(spc1); // 1000
      this.plc.insert(spc2); // 500
      this.plc.remove(1000);
      
      List<Specimen> compareList = this.plc.toList();
      
      List<Specimen> targetList = new ArrayList<Specimen>();
      targetList.add(spc2); // 500
      
      assertEquals("test10: failed - B-Tree remove incorrect", compareList, targetList);
    } catch (DuplicateKeyException e1) {
      fail("test10: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test10: failed - unexpected exception occurred");
    }
  }
  
  @Test
  public void test11_removeFromSize2Leaf() {
    try {
      setUp();
    
      this.plc.insert(spc1); // 1000
      this.plc.insert(spc2); // 500
      this.plc.insert(spc3); // 1500
      this.plc.insert(spc4); // 250

      this.plc.remove(250);
      
      List<Specimen> compareList = this.plc.toList();
      
      List<Specimen> targetList = new ArrayList<Specimen>();
      
      // insert in order
      targetList.add(spc2); // 500
      targetList.add(spc1); // 1000
      targetList.add(spc3); // 1500
      
      assertEquals("test11: failed - B-Tree remove incorrect", compareList, targetList);
    } catch (DuplicateKeyException e1) {
      fail("test11: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test11: failed - unexpected exception occurred");
    }
  }
  
  @Test
  public void test12_removeNonExistantKey() {
    try {
      setUp();
    
      this.plc.insert(spc1); // 1000
      this.plc.insert(spc2); // 500
      this.plc.insert(spc3); // 1500

      this.plc.remove(250);
      
      List<Specimen> compareList = this.plc.toList();
      
      List<Specimen> targetList = new ArrayList<Specimen>();
      
      // insert in order
      targetList.add(spc2); // 500
      targetList.add(spc1); // 1000
      targetList.add(spc3); // 1500
      
      assertEquals("test12: failed - B-Tree remove incorrect", compareList, targetList);
    } catch (DuplicateKeyException e1) {
      fail("test12: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test12: failed - unexpected exception occurred");
    }
  }
  
  @Test
  public void test13_removeLeftMerge() {
    try {
      setUp();
    
      this.plc.insert(spc1); // 1000
      this.plc.insert(spc2); // 500
      this.plc.insert(spc3); // 1500

      this.plc.remove(500);
      
      List<Specimen> compareList = this.plc.toList();
      
      List<Specimen> targetList = new ArrayList<Specimen>();
      
      // insert in order
      targetList.add(spc1); // 1000
      targetList.add(spc3); // 1500
      
      assertEquals("test13: failed - B-Tree remove incorrect", compareList, targetList);
    } catch (DuplicateKeyException e1) {
      fail("test13: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test13: failed - unexpected exception occurred");
    }
  }
  
  @Test
  public void test14_removeLeftRotate() {
    try {
      setUp();
    
      this.plc.insert(spc1); // 1000
      this.plc.insert(spc2); // 500
      this.plc.insert(spc3); // 1500
      this.plc.insert(spc6); // 2000

      this.plc.remove(500);
      
      List<Specimen> compareList = this.plc.toList();
      
      List<Specimen> targetList = new ArrayList<Specimen>();
      
      // insert in order
      targetList.add(spc1); // 1000
      targetList.add(spc3); // 1500
      targetList.add(spc6); // 2000
      
      assertEquals("test14: failed - B-Tree remove incorrect", compareList, targetList);
    } catch (DuplicateKeyException e1) {
      fail("test14: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test14: failed - unexpected exception occurred");
    }
  }
  
  @Test
  public void test15_removeMiddleMerge() {
    try {
      setUp();
    
      this.plc.insert(spc1); // 1000
      this.plc.insert(spc2); // 500
      this.plc.insert(spc3); // 1500

      this.plc.remove(1500);
      
      List<Specimen> compareList = this.plc.toList();
      
      List<Specimen> targetList = new ArrayList<Specimen>();
      
      // insert in order
      targetList.add(spc2); // 500
      targetList.add(spc1); // 1000
      
      assertEquals("test15: failed - B-Tree remove incorrect", compareList, targetList);
    } catch (DuplicateKeyException e1) {
      fail("test15: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test15: failed - unexpected exception occurred");
    }
  }
  
  @Test
  public void test16_removeMiddleRotateRight() {
    try {
      setUp();
    
      this.plc.insert(spc1); // 1000
      this.plc.insert(spc2); // 500
      this.plc.insert(spc3); // 1500
      this.plc.insert(spc4); // 250
      
      this.plc.remove(1500);
      
      List<Specimen> compareList = this.plc.toList();
      
      List<Specimen> targetList = new ArrayList<Specimen>();
      
      // insert in order
      targetList.add(spc4); // 250
      targetList.add(spc2); // 500
      targetList.add(spc1); // 1000
      
      assertEquals("test16: failed - B-Tree remove incorrect", compareList, targetList);
    } catch (DuplicateKeyException e1) {
      fail("test16: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test16: failed - unexpected exception occurred");
    }
  }
  
  @Test
  public void test17_removeMiddleRotateLeft() {
    try {
      setUp();
    
      this.plc.insert(spc1); // 1000
      this.plc.insert(spc2); // 500
      this.plc.insert(spc3); // 1500
      this.plc.insert(spc6); // 2000
      this.plc.insert(spc7); // 2500 
      this.plc.insert(spc8); // 3000
      
      this.plc.remove(1500);
      
      List<Specimen> compareList = this.plc.toList();
      
      List<Specimen> targetList = new ArrayList<Specimen>();
      
      // insert in order
      targetList.add(spc2); // 500
      targetList.add(spc1); // 1000
      targetList.add(spc6); // 2000
      targetList.add(spc7); // 2500
      targetList.add(spc8); // 3000
      
      assertEquals("test17: failed - B-Tree remove incorrect", compareList, targetList);
    } catch (DuplicateKeyException e1) {
      fail("test17: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test17: failed - unexpected exception occurred");
    }
  }
  
  @Test
  public void test18_removeRightMerge() {
    try {
      setUp();
    
      this.plc.insert(spc1); // 1000
      this.plc.insert(spc2); // 500
      this.plc.insert(spc3); // 1500
      this.plc.insert(spc6); // 2000
      this.plc.insert(spc7); // 2500 
      
      this.plc.remove(2500);
      
      List<Specimen> compareList = this.plc.toList();
      
      List<Specimen> targetList = new ArrayList<Specimen>();
      
      // insert in order
      targetList.add(spc2); // 500
      targetList.add(spc1); // 1000
      targetList.add(spc3); // 1500
      targetList.add(spc6); // 2000
      
      assertEquals("test18: failed - B-Tree remove incorrect", compareList, targetList);
    } catch (DuplicateKeyException e1) {
      fail("test18: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test18: failed - unexpected exception occurred");
    }
  }
  
  @Test
  public void test19_removeRightRotate() {
    try {
      setUp();
    
      this.plc.insert(spc1); // 1000
      this.plc.insert(spc2); // 500
      this.plc.insert(spc3); // 1500
      this.plc.insert(spc4); // 250
      this.plc.insert(spc5); // 100
      this.plc.insert(spc9); // 750
      
      this.plc.remove(1500);
      
      List<Specimen> compareList = this.plc.toList();
      
      List<Specimen> targetList = new ArrayList<Specimen>();
      
      // insert in order
      targetList.add(spc5); // 100
      targetList.add(spc4); // 250
      targetList.add(spc2); // 500
      targetList.add(spc9); // 750
      targetList.add(spc1); // 1000

      assertEquals("test19: failed - B-Tree remove incorrect", compareList, targetList);
    } catch (DuplicateKeyException e1) {
      fail("test19: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test19: failed - unexpected exception occurred");
    }
  }
  
  @Test
  public void test20_findNonExistent() {
    try {
      setUp();
    
      Specimen value = this.plc.find(1);

      assertEquals("test20: failed - B-Tree find incorrect", value, null);
    } catch (DuplicateKeyException e1) {
      fail("test20: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test20: failed - unexpected exception occurred");
    }
  }
  
  @Test
  public void test21_findOne() {
    try {
      setUp();
    
      this.plc.insert(spc1); // 1000
      
      Specimen value = this.plc.find(1000);

      assertEquals("test21: failed - B-Tree find incorrect", value, spc1);
    } catch (DuplicateKeyException e1) {
      fail("test21: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test21: failed - unexpected exception occurred");
    }
  }
  
  @Test
  public void test22_findParentSize1() {
    try {
      setUp();
    
      this.plc.insert(spc1); // 1000
      this.plc.insert(spc2); // 500
      this.plc.insert(spc3); // 1500
      
      Specimen value = this.plc.find(500);

      assertEquals("test22: failed - B-Tree find incorrect", value, spc2);
    } catch (DuplicateKeyException e1) {
      fail("test22: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test22: failed - unexpected exception occurred");
    }
  }
  
  @Test
  public void test23_findParentSize2() {
    try {
      setUp();
    
      this.plc.insert(spc1); // 1000
      this.plc.insert(spc2); // 500
      this.plc.insert(spc3); // 1500
      this.plc.insert(spc4); // 250
      this.plc.insert(spc5); // 100
      
      Specimen value = this.plc.find(1500);

      assertEquals("test23: failed - B-Tree find incorrect", value, spc3);
    } catch (DuplicateKeyException e1) {
      fail("test23: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test23: failed - unexpected exception occurred");
    }
  }
  
  @Test
  public void test24_exists() {
    try {
      setUp();
    
      this.plc.insert(spc1); // 1000
      this.plc.insert(spc2); // 500
      this.plc.insert(spc3); // 1500
      this.plc.insert(spc4); // 250
      this.plc.insert(spc5); // 100
      
      boolean nodeExists = this.plc.valueExists(500);

      assertTrue(nodeExists == true, "test24: failed - B-Tree valueExists incorrect");
    } catch (DuplicateKeyException e1) {
      fail("test24: failed - Duplicate Key Exception encountered");
    } catch (Exception e2) {
      fail("test24: failed - unexpected exception occurred");
    }
  }
  
}


