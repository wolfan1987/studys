package org.andrewliu.thread.pipestream;

public class InteactBackupTest {

	public static void main(String[] args) {
		DBTools dbtools = new DBTools();
		for(int i = 0; i < 20; i++){
			BackupB output = new BackupB(dbtools);
			output.start();
			BackupA  input = new BackupA(dbtools);
			input.start();
		}
	}
	
}

class BackupA extends Thread{
	private DBTools dbtools;
	
	public BackupA(DBTools dbtools){
		super();
		this.dbtools = dbtools;
	}
	
	@Override
	public void run(){
		dbtools.backupA();
	}
}

class BackupB extends Thread{
	private DBTools dbtools;
	
	public BackupB(DBTools dbtools){
		super();
		this.dbtools = dbtools;
	}
	
	@Override
	public void run(){
		dbtools.backupB();
	}
}