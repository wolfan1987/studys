package org.andreliu.ds.binarytrue;

public class BTNode<E> {
    private E data; //存储在结点的数据
    private BTNode<E> left; //左孩子
    private BTNode<E> right; //右孩子
     
    public BTNode(E initialData, BTNode<E> initialLeft, BTNode<E> initialRight ){
        data = initialData;
        left = initialLeft;
        right = initialRight;
    }
    public void print(int deep){
        for(int i = 1; i <= deep; i++){
            System.out.print("-");
        }
        System.out.print(  data + "\n");
        if(left != null)
            left.print(deep + 1);
        if(right != null)
            right.print(deep + 1);
    }
    
    public BTNode<E> getLeft() {
		return left;
	}
	public void setLeft(BTNode<E> left) {
		this.left = left;
	}
	public BTNode<E> getRight() {
		return right;
	}
	public void setRight(BTNode<E> right) {
		this.right = right;
	}
	public static void main(String[] args) {
        BTNode<String> a = new BTNode<String>("A", null, null);
        BTNode<String> b = new BTNode<String>("B", null, null);
        BTNode<String> c = new BTNode<String>("C", null, null);
        BTNode<String> d = new BTNode<String>("D", null, null);
        BTNode<String> e = new BTNode<String>("E", null, null);
        BTNode<String> f = new BTNode<String>("F", null, null);
         
        a.setLeft(b);
        a.setRight(c);
        b.setLeft(d);
        b.setRight(e);
        c.setLeft(f);
         
        a.print(0);
    }
    
}