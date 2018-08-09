package org.andreliu.ds.binarytrue;



/**
 *   红黑树是一种经典的数据结构，在linux内存管理、nginx 等很多地方用到它。主
 *   要操作包括插入、删除，其中插入6种情况，删除8种情况，详细的思路就不说了，
 *   如果不太明白的请参考算法导论13章，看的时候一定要把每一种插入、
 *   删除的情况在纸上自己画出来，这样会节省你很多时间。
 * @author de
 *
 */
public class RBTree {
	private final RBNode NIL = new RBNode(null,null,null,Color.BLACK,-1);  
    private RBNode root;  
      
    public RBTree() {  
        root = NIL;  
    }  
      
    public RBTree(RBNode  root) {  
        this.root = root;  
    }  
      
    //插入节点  
    public void rbInsert(RBNode node) {  
          
        RBNode previous = NIL;  
        RBNode temp = root;  
          
        while (temp != NIL) {  
            previous = temp;  
            if (temp.getValue() < node.getValue()) {  
                temp = temp.getRight();  
            } else {  
                temp = temp.getLeft();  
            }  
        }  
        node.setParent(previous);  
          
        if (previous == NIL) {  
            root = node;  
            root.setParent(NIL);  
        } else  if (previous.getValue() > node.getValue()) {  
            previous.setLeft(node);  
        } else {  
            previous.setRight(node);  
        }  
          
        node.setLeft(NIL);  
        node.setRight(NIL);  
        node.setColor(Color.RED);  
        rb_Insert_Fixup(node);  
          
    }  
      
    //插入节点后的调整  
    private void rb_Insert_Fixup(RBNode node) {  
      
        while (node.getParent().getColor() == Color.RED) {  
              
            if (node.getParent() == node.getParent().getParent().getLeft()) {  
                  
                RBNode rightNuncle = node.getParent().getParent().getRight();  
                  
                if (rightNuncle.getColor() == Color.RED) {         //Case 1  
                      
                    rightNuncle.setColor(Color.BLACK);  
                    node.getParent().setColor(Color.BLACK);  
                    node.getParent().getParent().setColor(Color.RED);  
                    node = node.getParent().getParent();  
                                          
                } else if (node == node.getParent().getRight()) {  //case 2  
                      
                    node = node.getParent();  
                    leftRotate(node);  
                      
                } else {                                          //case 3  
                      
                    node.getParent().setColor(Color.BLACK);  
                    node.getParent().getParent().setColor(Color.RED);  
                      
                    rightRotate(node.getParent().getParent());  
                      
                }  
                                  
            } else {  
                  
                RBNode leftNuncle = node.getParent().getParent().getLeft();  
                  
                if (leftNuncle.getColor() == Color.RED) {     //case 4  
                      
                    leftNuncle.setColor(Color.BLACK);  
                    node.getParent().setColor(Color.BLACK);  
                    node.getParent().getParent().setColor(Color.RED);  
                    node = node.getParent().getParent();  
                  
                } else if (node == node.getParent().getLeft()) { //case 5  
                  
                    node = node.getParent();  
                    rightRotate(node);  
                                          
                } else {                                          // case 6  
                      
                    node.getParent().setColor(Color.BLACK);  
                    node.getParent().getParent().setColor(Color.RED);  
                    leftRotate(node.getParent().getParent());  
                              
                }  
                                  
            }  
              
              
        }  
          
        root.setColor(Color.BLACK);  
          
    }  
      
      
    //删除节点  
    public RBNode rbDelete(int data) {  
          
        RBNode node = search(data);  
        RBNode temp = NIL;  
        RBNode child = NIL;  
        if (node == null) {  
            return null;  
        } else {  
            if (node.getLeft() == NIL || node.getRight() == NIL) {  
                temp = node;              
            } else {  
                temp = successor(node);  
            }  
              
            if (temp.getLeft() != NIL) {  
                child = temp.getLeft();  
            } else {  
                child = temp.getRight();  
            }  
              
            child.setParent(temp.getParent());  
              
            if (temp.getParent() == NIL) {  
                root = child;  
            } else if (temp == temp.getParent().getLeft()) {  
                temp.getParent().setLeft(child);  
            } else {  
                temp.getParent().setRight(child);  
            }  
              
            if (temp != node) {  
                node.setValue(temp.getValue());  
            }  
              
            if (temp.getColor() == Color.BLACK) {  
                rb_Delete_Fixup(child);  
            }  
            return temp;  
        }  
          
          
          
          
    }  
      
    //删除节点后的调整  
    private void rb_Delete_Fixup(RBNode node) {  
          
        while (node != root && node.getColor() == Color.BLACK) {  
              
            if (node == node.getParent().getLeft()) {  
                  
                RBNode rightBrother = node.getParent().getRight();  
                if (rightBrother.getColor() == Color.RED) {          //case 1 node节点为左孩子，node节点的兄弟为RED  
                    rightBrother.setColor(Color.BLACK);  
                    node.getParent().setColor(Color.RED);  
                    leftRotate(node.getParent());  
                    rightBrother = node.getParent().getRight();  
                }  
                  
                if (rightBrother.getLeft().getColor() == Color.BLACK && rightBrother.getRight().getColor() == Color.BLACK) {  
                    rightBrother.setColor(Color.RED);  
                    node = node.getParent();  
                } else if (rightBrother.getRight().getColor() == Color.BLACK) {  
                    rightBrother.getLeft().setColor(Color.BLACK);  
                    rightBrother.setColor(Color.RED);  
                    rightRotate(rightBrother);  
                    rightBrother = node.getParent().getRight();  
                } else {  
                    rightBrother.setColor(node.getParent().getColor());  
                    node.getParent().setColor(Color.BLACK);  
                    rightBrother.getRight().setColor(Color.BLACK);  
                    leftRotate(node.getParent());  
                    node = root;  
                }  
                  
                  
            } else {  
                  
                RBNode leftBrother = node.getParent().getLeft();  
                if (leftBrother.getColor() == Color.RED) {  
                    leftBrother.setColor(Color.BLACK);  
                    node.getParent().setColor(Color.RED);  
                    rightRotate(node.getParent());  
                    leftBrother = node.getParent().getLeft();  
                }   
                  
                if (leftBrother.getLeft().getColor() == Color.BLACK && leftBrother.getRight().getColor() == Color.BLACK) {  
                    leftBrother.setColor(Color.RED);  
                    node = node.getParent();  
                                                      
                } else if (leftBrother.getLeft().getColor() == Color.BLACK) {  
                      
                    leftBrother.setColor(Color.RED);  
                    leftBrother.getRight().setColor(Color.BLACK);  
                    leftRotate(leftBrother);  
                    leftBrother = node.getParent().getLeft();  
                      
                } else {  
                      
                    leftBrother.setColor(node.getParent().getColor());  
                    node.getParent().setColor(Color.BLACK);  
                    leftBrother.getLeft().setColor(Color.BLACK);  
                    rightRotate(node.getParent());  
                    node = root;  
                                                              
                }  
                                  
            }  
                      
        }  
              
        node.setColor(Color.BLACK);  
    }  
      
      
    //查找节点node的后继节点  
  
    public RBNode successor(RBNode node) {  
          
        RBNode rightChild = node.getRight();  
        if  (rightChild != NIL) {  
            RBNode previous = null;  
            while (rightChild != NIL) {  
                previous = rightChild;  
                rightChild = rightChild.getLeft();  
            }  
            return previous;  
        } else {  
              
            RBNode parent = node.getParent();  
            while (parent != NIL && node != parent.getLeft()) {  
                node = parent;  
                parent = parent.getParent();  
            }  
              
            return parent;  
                          
        }  
  
    }  
      
      
    //查找节点  
    public RBNode search(int data) {  
        RBNode temp = root;  
          
        while (temp != NIL) {  
            if (temp.getValue() == data) {  
                return temp;  
            } else  if (data < temp.getValue()) {  
                temp = temp.getLeft();  
            } else {  
                temp = temp.getRight();  
            }  
        }  
        return null;  
    }  
      
      
      
      
    //左转函数  
    private void leftRotate(RBNode node) {  
          
        RBNode rightNode = node.getRight();  
          
        node.setRight(rightNode.getLeft());  
        if (rightNode.getLeft() != NIL) {  
            rightNode.getLeft().setParent(node);  
        }  
        rightNode.setParent(node.getParent());  
          
        if (node.getParent() == NIL) {  
            rightNode = root;  
        } else if (node == node.getParent().getLeft()) {  
            node.getParent().setLeft(rightNode);  
        } else {  
            node.getParent().setRight(rightNode);  
        }  
          
        rightNode.setLeft(node);  
        node.setParent(rightNode);  
          
          
    }  
      
    //右转函数  
    private void rightRotate(RBNode node) {  
          
        RBNode leftNode = node.getLeft();  
        node.setLeft(leftNode.getRight());  
          
        if (leftNode.getRight() != null) {  
            leftNode.getRight().setParent(node);  
        }  
          
        leftNode.setParent(node.getParent());  
          
        if (node.getParent() == NIL) {  
            root = leftNode;  
        } else if (node == node.getParent().getLeft()) {  
            node.getParent().setLeft(leftNode);  
        } else {  
            node.getParent().setRight(leftNode);  
        }  
          
        leftNode.setRight(node);  
        node.setParent(leftNode);  
                      
    }  
      
    //中序遍历红黑树  
    public void printTree() {  
        inOrderTraverse(root);  
    }  
      
    private void inOrderTraverse(RBNode node) {  
          
        if (node != NIL) {  
            inOrderTraverse(node.getLeft());  
            System.out.println(" 节点："+node.getValue() + "的颜色为：" + node.getColor());  
            inOrderTraverse(node.getRight());  
        }  
          
    }  
      
      
    public RBNode getNIL() {  
        return NIL;  
    }  
    
    
    public static void main(String[] args) {  
        
        RBTree rbTree = new RBTree();  
          
        rbTree.rbInsert(new RBNode(41));  
        rbTree.rbInsert(new RBNode(38));  
        rbTree.rbInsert(new RBNode(31));  
        rbTree.rbInsert(new RBNode(12));  
        rbTree.rbInsert(new RBNode(19));  
        rbTree.rbInsert(new RBNode(8));  
          
        //rbTree.printTree();  
          
          
        rbTree.rbDelete(19);  
          
        rbTree.printTree();  
          
  
    }  
  
}  
  
  
  
class RBNode {  
    private RBNode left;  
    private RBNode right;  
    private RBNode parent;  
    private Color color;  
    private int value;  
    public RBNode(RBNode left, RBNode right, RBNode parent, Color color, int value) {  
        super();  
        this.left = left;  
        this.right = right;  
        this.parent = parent;  
        this.color = color;  
        this.value = value;  
    }  
      
    public RBNode() {  
    }  
      
    public RBNode(int value) {  
        this(null,null,null,null,value);  
    }  
  
    public RBNode getLeft() {  
        return left;  
    }  
  
    public void setLeft(RBNode left) {  
        this.left = left;  
    }  
  
    public RBNode getRight() {  
        return right;  
    }  
  
    public void setRight(RBNode right) {  
        this.right = right;  
    }  
  
    public RBNode getParent() {  
        return parent;  
    }  
  
    public void setParent(RBNode parent) {  
        this.parent = parent;  
    }  
  
    public Color getColor() {  
        return color;  
    }  
  
    public void setColor(Color color) {  
        this.color = color;  
    }  
  
    public int getValue() {  
        return value;  
    }  
  
    public void setValue(int value) {  
        this.value = value;  
    }  
      
}  
  
enum Color {  
    RED,BLACK  
}  