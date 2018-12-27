package com.dsc.test;

import java.util.concurrent.atomic.AtomicInteger;

import com.dsc.Atomic.AtomicStamp;
import com.dsc.Transaction.Transaction;


public class Test04 {
	
		private AtomicStamp num1;
		//private AtomicStamp num2;
		public Test04(){
			
		}
		
		class Data {
			AtomicInteger i=new AtomicInteger(0);
			public Data(int num){
			
				num1=new AtomicStamp(1,1);
				//num2=new AtomicStamp(1,1);
			}
//			
//			
			public void increase(){   
				    num1.put(i);
				    //num2.put(i);
				    i.incrementAndGet();
			}
//			
			public void isEqual(){
				System.out.println("nums1="+num1.getAtomicStampedRef().getReference());
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
				for(int i=0;i<100;i++){
					//Transaction.beginTransaction();
					data.increase();
					//Transaction.commitTransaction();
			}	
		}
		}
		
		public static void main(String[] args) {
				
				Test04 test=new Test04();
				Test04.Data data=test.new Data(1);
				
				Worker w1=new Worker(data);
				Worker w2=new Worker(data);
				Thread t1=new Thread(w1);
				Thread t2=new Thread(w2);
				t1.start();
				t2.start();
				
				
					try {
						t1.join();
						t2.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			  data.isEqual();
				}
				
			
}
