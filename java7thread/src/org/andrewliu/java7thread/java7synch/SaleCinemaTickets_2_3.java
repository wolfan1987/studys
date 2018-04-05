package org.andrewliu.java7thread.java7synch;

/**
 * �÷���������ʵ��ͬ������������ĳ���������ʽ����this)���������ٽ�����ͬһʱ�̣�ֻ��
 * һ���߳��ܷ���synchronized(obj)�еĴ��룬�˴���obj�Ƿǵ�ǰ����(��this)��������ʱ����Ϊ�˱�֤ͬһʱ��ֻ����һ������
 * ����һ��List����ʱ��������synchronized(list)��������������ô�����ж����̲߳������list,���������list״̬��һ�¡�
 * ����ģ��ര������ӰƱ���Ա�֤Ʊ������������۳������е�Ʊ���������
 * @author de
 *
 */
public class SaleCinemaTickets_2_3 {

	public static void main(String[] args) {
		Cinema cinema = new Cinema();  //��ӰԺ
		TicketOffice1 ticketOffice1 = new TicketOffice1(cinema);//��Ʊ����1
		Thread thread1 = new Thread(ticketOffice1,"TickeOffice1"); //���߳�������Ʊ
		
		TicketOffice2 ticketOffice2 = new TicketOffice2(cinema);//��Ʊ����2
		Thread thread2 = new Thread(ticketOffice2,"TicketOffice2");//���߳�������Ʊ
		
		thread1.start();
		thread2.start();
		
		
		try{
			thread1.join(); //����������
			thread2.join(); //����������
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		
		System.out.printf("Room 1 Vacancies: %d\n", cinema.getVacanciesCinema1());
		System.out.printf("Room 2 Vacancies: %d\n", cinema.getVacanciesCinema2());
		
	}
}

class Cinema{
	//��ӰƱ
	private long vacanciesCinema1;
	private long vacanciesCinema2;
	//���ƶ���������֤ͬһʱ��ֻ��һ���߳��ܸ��µ�ӰƱ��)
	private final Object  controlCinema1,controlCinema2;
	
	public Cinema(){
		controlCinema1 = new Object();
		controlCinema2 = new Object();
		vacanciesCinema1 = 20;
		vacanciesCinema2 = 20;
	}
	
	public boolean sellTickets1(int number){
		synchronized(controlCinema1){ //��ס������������߳̾Ͳ���������ô�Ͳ�����־������µ�ӰƱ�����
			if(number<vacanciesCinema1){
				vacanciesCinema1-=number;
				return true;
			}else{
				return false;
			}
		}
	}
	
	public boolean sellTickets2(int number){
		synchronized(controlCinema2){  //��ס������������߳̾Ͳ���������ô�Ͳ�����־������µ�ӰƱ�����
			if(number<vacanciesCinema2){
				vacanciesCinema2-=number;
				return true;
			}else{
				return false;
			}
		}
	}
	
	public boolean returnTickets1(int number){
		synchronized(controlCinema1){//���������Է��������߳�Ҳ����Ʊ
			vacanciesCinema1+=number;
			return true;
		}
	}
	
	
	public boolean returnTickets2 (int number){
		synchronized(controlCinema2){  //���������Է��������߳�Ҳ����Ʊ
			vacanciesCinema2+=number;
			return true;
		}
	}

	public long getVacanciesCinema1() {
		return vacanciesCinema1;
	}

	public long getVacanciesCinema2() {
		return vacanciesCinema2;
	}
	
}

/**
 * ��Ʊ����1
 * @author de
 *
 */
class TicketOffice1 implements Runnable{
	private Cinema cinema;
	public TicketOffice1 (Cinema cinema){
		this.cinema = cinema;
	}
	@Override
	public void run() {
		cinema.sellTickets1(3);
		cinema.sellTickets1(2);
		
		cinema.sellTickets2(2);
		
		cinema.returnTickets1(3);
		cinema.sellTickets1(5);
		
		cinema.sellTickets2(2);
		cinema.sellTickets2(2);
		cinema.sellTickets2(2);
	}
	
}

/**
 * ��Ʊ����2
 * @author de
 *
 */
class TicketOffice2 implements Runnable{
	private Cinema cinema;
	public TicketOffice2(Cinema cinema){
		this.cinema = cinema;
	}
	@Override
	public void run() {
		cinema.sellTickets2(2);
		cinema.sellTickets2(4);
		
		cinema.sellTickets1(2);
		cinema.sellTickets1(1);
		
		cinema.returnTickets2(2);
		
		cinema.sellTickets1(3);
		
		cinema.sellTickets2(2);
		
		cinema.sellTickets1(2);
	}
	
	
}
