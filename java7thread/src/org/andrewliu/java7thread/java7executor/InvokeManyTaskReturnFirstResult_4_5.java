package org.andrewliu.java7thread.java7executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * 通过ExecutorService的invokeAny方法执行多个任务，并返回第一个先完成任务的结果,即：多中选一快
 * invokeAny的另一版本：
 * invokeAny(Collection<? extends Callable<T>> tasks,long timeout,TimeUnit unit):
 * 这个方法执行所有任务，如果在续写的超时期满之间某个任务已经成功完成，则返回其结束，否则到时就结束
 * @author de
 *
 */
public class InvokeManyTaskReturnFirstResult_4_5 {

	public static void main(String[] args) {
		String username = "test";
		String password = "test";
		UserValidator ldapValidator = new UserValidator("LDAP");
		UserValidator dbValidator = new UserValidator("DataBase");
		TaskValidator ldapTask = new TaskValidator(ldapValidator,username,password);
		TaskValidator dbTask = new TaskValidator(dbValidator,username,password);
		
		List<TaskValidator>  taskList = new ArrayList<>();
		taskList.add(ldapTask);
		taskList.add(dbTask);
		ExecutorService executor = Executors.newCachedThreadPool();
		String result ;
		
		try{
			//让他执行这taskList中的所有任务，但当有一个返回了结果后（而不是抛出异常），另一个任务将自动结束。
			result = executor.invokeAny(taskList);
			System.out.printf("Main: Result: %s\n", result);
			
		}catch(InterruptedException e){
			e.printStackTrace();
		}catch(ExecutionException e){
			e.printStackTrace();
		}
		executor.shutdown();
		System.out.printf("Main: End of the Execution\n");
	}
}

class UserValidator{
	private String name;
	public UserValidator(String name){
		this.name = name;
	}
	
	public boolean validate(String name,String password){
		Random random = new Random();
		try{
			long duration  = (long)(Math.random()*10);
			System.out.printf("Validator %s: Validating a user during %d seconds\n", this.name,duration);
			TimeUnit.SECONDS.sleep(duration);
		}catch(InterruptedException e){
			return false;
		}
		
		return random.nextBoolean();
	}
	
	public String getName(){
		return name;
	}
}

class TaskValidator implements Callable<String>{
	private UserValidator validator;
	private String user;
	private String  password;
	public TaskValidator(UserValidator validator, String user, String password) {
		super();
		this.validator = validator;
		this.user = user;
		this.password = password;
	}
	
	@Override
	public String call() throws Exception {
		if(!validator.validate(user, password)){
			System.out.printf("%s: The user has not been found\n",validator.getName());
			throw new Exception("Error validaing user");
		}
		System.out.printf("%s: The user has been found\n",validator.getName());
		return  validator.getName();
	}
	
}
