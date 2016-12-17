import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.util.*;

class TestTreeNode {
  
  Integer data;
  TestTreeNode left, right;
  
  public TestTreeNode(Integer data, TestTreeNode l, TestTreeNode r) {
    this.data = data;
    this.right = r;
    this.left = l;
  }

  static TestTreeNode add(TestTreeNode node, int data) {
    
    if (node == null) {
      node = new TestTreeNode (data, null, null);
    } else {
      // Place lower elem values on left
      if (data < node.data) {
        node.left = add(node.left, data);
      } else {
        node.right = add(node.right, data);
      }
    }
    return node;

  }

  static void preOrder(List <Integer> lst, TestTreeNode node) {
    
    if (node == null) return;

    lst.add(node.data);
    if (node.left != null) preOrder(lst, node.left);
    if (node.right != null) preOrder(lst, node.right);

  }

  static void inOrder(List <Integer> lst, TestTreeNode node) {
    
    if (node == null) return;

    if (node.left != null) inOrder(lst, node.left);
    lst.add(node.data);
    if (node.right != null) inOrder(lst, node.right);

  }

  static void postOrder(List <Integer> lst, TestTreeNode node) {
    
    if (node == null) return;

    if (node.left != null) postOrder(lst, node.left);
    if (node.right != null) postOrder(lst, node.right);
    lst.add(node.data);

  }

  static void levelOrder(List <Integer> lst, TestTreeNode node) {

    Deque <TestTreeNode> q = new ArrayDeque<>();
    if (node != null) q.offer(node);

    while(!q.isEmpty()) {

      node = q.poll();
      lst.add(node.data);
      if (node.left != null) q.offer(node.left);
      if (node.right != null) q.offer(node.right);

    }

  }

}

public class BinarySearchTreeTest {

  static final int LOOPS = 500;

  @Before
  public void setup() { }

  @Test public void testIsEmpty() {
    
    BinarySearchTree <String> tree = new BinarySearchTree<>();
    assertTrue( tree.isEmpty() );
    
    tree.add("Hello World!");
    assertFalse( tree.isEmpty() );
    
  }

  @Test public void testSize() {
    BinarySearchTree <String> tree = new BinarySearchTree<>();
    assertEquals( tree.getSize(), 0 );
    
    tree.add("Hello World!");
    assertEquals( tree.getSize(), 1 );
  }

  @Test public void testHeight() {
    BinarySearchTree <String> tree = new BinarySearchTree<>();
    
    // Tree should look like:
    //        M
    //      J  S
    //    B   N Z
    //  A
    
    // No tree
    assertEquals( tree.height(), 0 );
    
    // Layer One
    tree.add("M");
    assertEquals( tree.height(), 1 );
    
    // Layer Two
    tree.add("J");
    assertEquals( tree.height(), 2 );
    tree.add("S");
    assertEquals( tree.height(), 2 );    
    
    // Layer Three
    tree.add("B");
    assertEquals( tree.height(), 3 );
    tree.add("N");
    assertEquals( tree.height(), 3 );        
    tree.add("Z");
    assertEquals( tree.height(), 3 );
    
    // Layer 4
    tree.add("A");
    assertEquals( tree.height(), 4 );    
    
  }

  @Test public void testAdd() {

    // Add element which does not yet exist
    BinarySearchTree<Character> tree = new BinarySearchTree<>();
    assertTrue(tree.add('A'));

    // Add duplicate element
    assertFalse(tree.add('A'));

    // Add a second element which is not a duplicate
    assertTrue(tree.add('B'));

  }

  @Test public void testRemove() {
   
    // Try removing an element which doesn't exist
    BinarySearchTree<Character> tree = new BinarySearchTree<>();
    tree.add('A');
    assertEquals(tree.getSize(), 1);
    assertFalse(tree.remove('B'));
    assertEquals(tree.getSize(), 1);
    
    // Try removing an element which does exist
    tree.add('B');
    assertEquals(tree.getSize(), 2);
    assertTrue(tree.remove('B'));
    assertEquals(tree.getSize(), 1);
    assertEquals(tree.height(), 1);

    // Try removing the root
    assertTrue(tree.remove('A'));
    assertEquals(tree.getSize(), 0);
    assertEquals(tree.height(), 0);

  }

  @Test public void testFind() {
    
    // Setup tree
    BinarySearchTree<Character> tree = new BinarySearchTree<>();

    tree.add('B');
    tree.add('A');
    tree.add('C');

    // Try looking for an element which doesn't exist
    assertFalse(tree.find('D'));

    // Try looking for an element which exists in the root
    assertTrue(tree.find('B'));

    // Try looking for an element which exists as the left child of the root
    assertTrue(tree.find('A'));
    
    // Try looking for an element which exists as the right child of the root
    assertTrue(tree.find('C'));

  }
  
  @Test public void randomRemove() {

    for (int i = 0; i < LOOPS; i++) {
      
      int size = i;
      BinarySearchTree <Integer> tree = new BinarySearchTree<>();
      List <Integer> lst = genRandList(size);
      for (Integer value : lst) tree.add(value);

      Collections.shuffle(lst);
      // Remove all the elements we just placed in the tree
      for (int j = 0; j < size; j++) {
        
        Integer value = lst.get(j);
        assertTrue(tree.remove(value));
        assertFalse(tree.find(value));

      }

      assertTrue(tree.isEmpty());

    }


  }

  // Tests a mixture of methods working together
  @Test public void testGeneralCase() {
    
  }

  static List <Integer> genRandList(int sz) {
    List <Integer> lst = new ArrayList<>(sz);
    for (int i = 0; i < sz; i++) lst.add( i );
    Collections.shuffle( lst );
    return lst;
  }

  public boolean validateTreeTraversal( TreeTraversalOrder trav_order, List <Integer> input ) {

    List <Integer> out = new ArrayList<>();
    List <Integer> expected = new ArrayList<>();
    
    TestTreeNode testTree = null;
    BinarySearchTree <Integer> tree = new BinarySearchTree<>();

    // Construct Binary Tree and test tree
    for (Integer value : input ) {
      testTree = TestTreeNode.add( testTree, value);
      tree.add(value);
    }

    // Generate the expected output for the particular traversal
    switch (trav_order) {
      case PRE_ORDER:   TestTreeNode.preOrder(expected, testTree);   break;
      case IN_ORDER:    TestTreeNode.inOrder(expected, testTree);    break;
      case POST_ORDER:  TestTreeNode.postOrder(expected, testTree);  break;
      case LEVEL_ORDER: TestTreeNode.levelOrder(expected, testTree); break;
    }

    // Get traversal output
    Iterator <Integer> iter = tree.traverse(trav_order);
    while(iter.hasNext()) out.add(iter.next());

    // The output and the expected size better be the same size
    if (out.size() != expected.size()) return false;

    // Compare output to expected
    for (int i = 0; i < out.size(); i++ )
      if ( !expected.get(i).equals(out.get(i)) )
        return false;

    return true;
    
  }
  
  // Process self, then left subtree, then right subtree
  @Test public void testPreOrderTraversal() {

    for (int i = 0; i < LOOPS; i++) {
      List <Integer> input = genRandList(i);
      assertTrue( validateTreeTraversal(TreeTraversalOrder.PRE_ORDER, input ) );
    }

  }
  
  // Process left subtree, then self, then right
  @Test public void testInOrderTraversal() {
    
    for (int i = 0; i < LOOPS; i++) {
      List <Integer> input = genRandList(i);
      assertTrue( validateTreeTraversal(TreeTraversalOrder.IN_ORDER, input ) );
    }

  }

  // Process left subtree, then right subtree, then left
  @Test public void testPostOrderTraversal() {

    for (int i = 0; i < LOOPS; i++) {
      List <Integer> input = genRandList(i);
      assertTrue( validateTreeTraversal(TreeTraversalOrder.POST_ORDER, input ) );
    }

  }  

  // Process left subtree, then right subtree, then left
  @Test public void testLevelOrderTraversal() {
    
    for (int i = 0; i < LOOPS; i++) {
      List <Integer> input = genRandList(i);
      assertTrue( validateTreeTraversal(TreeTraversalOrder.LEVEL_ORDER, input ) );
    }
    
  } 
  

}












