package com.dsc.Transaction;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

import com.dsc.Atomic.AtomicStamp;

public class Transaction {
	protected static AtomicInteger number=new AtomicInteger(1);
	//protected int number;
	protected final Transaction parent;
	protected ConcurrentHashMap<AtomicInteger,Object> localMap=new ConcurrentHashMap<AtomicInteger,Object>();
	protected static ConcurrentHashMap<AtomicInteger,Transaction> TxMap=new ConcurrentHashMap<AtomicInteger,Transaction>();
	protected static final AtomicInteger version=new AtomicInteger(0);
	//protected static ActiveTransactionsRecord mostRecentRecord=ActiveTransactionsRecord
	//��������
	protected int running;
	
	//����״̬   0,1,2 ���У��ύ���ع�
	public AtomicInteger state;
	
	protected Transaction getParent() {
		return parent;
	}

	public Transaction(Transaction parent, AtomicInteger number) {
		this.parent = parent;
		this.number = number;	
	}

	public Transaction(AtomicInteger number) {
		this(null, number);
	}

	public Transaction(Transaction parent) {
		this(parent, parent.getNumber());
	}

	public AtomicInteger getNumber() {
		return number;
	}

	protected void setNumber(AtomicInteger number) {
		this.number = number;
	}
	
	public void setState(AtomicInteger state){
		this.state=state;
	}
	public AtomicInteger getState(){
		return state;
	}

	/**
	 * AtomicStamp��
	 */
	public static AtomicStamp atomicS;

	public AtomicStamp getAtomicS() {
		return atomicS;
	}

	public void setAtomicS(AtomicStamp atomicS) {
		this.atomicS = atomicS;
	}

	protected static final ThreadLocal<Transaction> current = new ThreadLocal<Transaction>();

	public static Transaction getCurrent() {
		return current.get();
	}
	public static void beginTransaction(){
			Transaction tx=current.get();
		   if(tx==null||tx.getState().get()==0){
			   tx=new Transaction(number);
				tx.state=new AtomicInteger(1);
				tx.running=0;
				current.set(tx);   
		    }else{
		    	rollBackTransaction();
		    }
	}

	/**
	 * ��������
	 */
	public void startTransaction() {

		Transaction tx = current.get();
		tx.running++;

	}

	/**
	 * �ύ����
	 */
	public void commitTransaction(boolean flag) {

		 Transaction tx = current.get();
		 		 
		 if(atomicS.getState()==2||!flag)
			 rollBackTransaction();
		 		
		 else{
			 atomicS.setState(2);
			 System.out.println("����---"+tx.getNumber()+"---�ύ�ɹ���ֵΪ��"+atomicS.getAtomicStampedRef().getReference()
					 +"Stampֵ��"+atomicS.getAtomicStampedRef().getStamp());
		 }
	}
	//д����
	public void commitTransaction(AtomicStamp num) {
		 boolean flag;
		 Transaction tx = current.get();
		 
		 if(tx.getState().get()!=1){
			 rollBackTransaction();
		 }else{  
		 flag=num.getAtomicStampedRef().compareAndSet(num.getAtomicStampedRef().getReference(), tx.getTail(),
				num.getAtomicStampedRef().getStamp(), num.getAtomicStampedRef().getStamp()+1);
		 tx.running--;
		 
		 }

		 
	}
	
	public static void commitTransaction(){
		Transaction tx=current.get();
		if(tx.running!=0){
		
			rollBackTransaction();
		}else{
			tx.setState(new AtomicInteger(0));
		
		}
		
	}
	/**
	 * ��������
	 */

	public void updateTransaction(Object newReference) {
		

	}

	/**
	 * �ع�����
	 */

	public static void rollBackTransaction() {
		 
		 number.getAndIncrement(); 
		 Transaction tx = new Transaction(number);
		 TxMap.put(number, tx);
		 
		 //beginTransaction();
		
	}
	
	public void setStampValue(Object newE){
		
		localMap.put(version, newE);
		version.incrementAndGet();
		
	}
	//��ȡ����ύ��ֵ
	protected Object getTail(){
		Iterator<Entry<AtomicInteger, Object>> iterator = localMap.entrySet().iterator();
	    Entry<AtomicInteger, Object> tail = null;
	    while (iterator.hasNext()) {
	        tail = iterator.next();
	    }
	    return tail.getValue();
	}
	protected static ExecutorService threadPool = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2, new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r);
					t.setDaemon(true);
					return t;
				}
			});

	public void initThreadPool(int numberThreads) {
		threadPool = Executors.newFixedThreadPool(numberThreads, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setDaemon(true);
				return t;
			}
		});
	}
	
}
