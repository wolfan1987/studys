package org.andrewliu.jvm.test;

/**
 * �˴�����ʾ�����㣺
 * 1����������ڱ�GCʱ��������
 * 2����������Ļ���ֻ��һ�Σ���Ϊһ�������finalize()�������ֻ�ᱻ�Զ�����һ��
 * 
 * ���´����У���������ȫһ���Ĵ���Ƭ�Σ�ִ�н��ȴʱһ�����ѳɹ���һ��ʧ�ܣ�������Ϊ�κ�һ�������
 * finalize() ������ֻ�ᱻϵͳ�Զ�����һ�Σ��������������һ�λ��գ�����finalize()��������
 * ���ٴ�ִ�У���˵ڶ��δ�����Ծ��ж�ʧ���ˡ�
 * finalize() �����д��۸߰�����ȷ���Դ��޷���֤��������ĵ���˳�����������Ĺ�����ʹ��
 * try-finally��������ʽ���������ø��á�����ʱ��������ȫ��������java�����л��������������.
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
		
		//�����һ�γɹ������Լ�
		SAVE_HOOK = null;
		System.gc();
		//��Ϊfinalizer�������ȼ��ܵͣ���ͣ0.5�룬�Եȴ���
		Thread.sleep(500);
		if(SAVE_HOOK != null){
			SAVE_HOOK.isAlive();
		}else{
			System.out.println(" no, i am dead :(");
		}
		
		//������δ������������ȫ��ͬ����������Ծ�ȴʧ����
		SAVE_HOOK = null;
		System.gc();
		//��ΪFinalizer ����������ܵͣ���ͣ0.5�룬�Եȴ���
		Thread.sleep(500);
		if(SAVE_HOOK != null){
			SAVE_HOOK.isAlive();
		}else{
			System.out.println("no,i am dead :(");
		}
	}
	
	
	
	
}













