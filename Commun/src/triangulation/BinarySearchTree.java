package triangulation;

import base.Segment;

public class BinarySearchTree {
	private BinarySearchTree left;
	
	private BinarySearchTree right;
	
	private Segment node;
	
	public BinarySearchTree() {
		
	}
	
	public BinarySearchTree(Segment s) {
		node = s;
	}
	
	public void insert(Segment s) {
		if(node == null)
			node = s;
	}
}
