package com.dsc.test;
import java.util.concurrent.atomic.AtomicInteger;

import com.dsc.Atomic.*;
import com.dsc.Transaction.Transaction;
public class Test03 {
	//private VBox<Integer> nums1;
	//private VBox<Integer> nums2;
	private AtomicStamp num1;
	private AtomicStamp num2;
	public Test03(){
		
	}
	
	class Data {
		AtomicInteger i=new AtomicInteger(1);
		public Data(int num){
		
			num1=new AtomicStamp(1,1);
			num2=new AtomicStamp(1,1);
		}
		
		
		public void increase(){   
			    num1.put(i);
			    num2.put(i);
			    i.incrementAndGet();
		}
		
		public synchronized void isEqual(){
			System.out.println("nums1="+num1.getAtomicStampedRef().getReference()+"nums2="+num2.getAtomicStampedRef().getReference());
		}
	}
	
	static class Worker implements Runnable{
		private Data data;
		int a=0;
		public Worker(Data data){
			this.data=data;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true){
				Transaction.beginTransaction();
				data.increase();
				Transaction.commitTransaction();
		}	
	}
	}
	
	public static void main(String[] args) {
			
			Test03 test=new Test03();
			Test03.Data data=test.new Data(1);
			
			Worker w1=new Worker(data);
			Worker w2=new Worker(data);
			Thread t1=new Thread(w1);
			Thread t2=new Thread(w2);
			t1.start();
			t2.start();
			
			while(true){
				//Transaction.begin(true);
				data.isEqual();
				//Transaction.commit();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
			
		}

}