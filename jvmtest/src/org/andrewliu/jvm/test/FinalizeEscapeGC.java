package org.andrewliu.jvm.test;

/**
 * 此代码演示了两点：
 * 1、对象可以在被GC时自我拯救
 * 2、这种自求的机会只有一次，因为一个对象的finalize()方法最多只会被自动调用一次
 * 
 * 以下代码中，有两段完全一样的代码片段，执行结果却时一次逃脱成功，一次失败，这是因为任何一个对象的
 * finalize() 方法都只会被系统自动调用一次，如果对象面临下一次回收，它的finalize()方法不会
 * 被再次执行，因此第二段代码的自救行动失败了。
 * finalize() 的运行代价高昂，不确定性大，无法保证各个对象的调用顺序。它所能做的工作，使用
 * try-finally或其他方式都可以做得更好、更及时。我们完全可以忘掉java语言中还有这个方法存在.
 * @author AndrewLiu
 *
 */
public class FinalizeEscapeGC {

	public static FinalizeEscapeGC SAVE_HOOK = null;
	
	public void  isAlive(){
		System.out.println("yes ,i am staill alive :)");
	}
	
	protected void finalize() throws Throwable{
		super.finalize();
		System.out.println("finalize method executed!");
		FinalizeEscapeGC.SAVE_HOOK = this;
	}
	
	public static void main(String[] args) throws Throwable {
		SAVE_HOOK = new FinalizeEscapeGC();
		
		//对象第一次成功拯救自己
		SAVE_HOOK = null;
		System.gc();
		//因为finalizer方法优先级很低，暂停0.5秒，以等待它
		Thread.sleep(500);
		if(SAVE_HOOK != null){
			SAVE_HOOK.isAlive();
		}else{
			System.out.println(" no, i am dead :(");
		}
		
		//下面这段代码与上面的完全相同，但是这次自救却失败了
		SAVE_HOOK = null;
		System.gc();
		//因为Finalizer 方法优先组很低，暂停0.5秒，以等待它
		Thread.sleep(500);
		if(SAVE_HOOK != null){
			SAVE_HOOK.isAlive();
		}else{
			System.out.println("no,i am dead :(");
		}
	}
	
	
	
	
}













