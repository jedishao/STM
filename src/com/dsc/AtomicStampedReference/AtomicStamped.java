package com.dsc.AtomicStampedReference;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;



public class AtomicStamped {

	private static AtomicInteger atomicInt = new AtomicInteger(100);
	private static AtomicStampedReference atomicStampedRef = new AtomicStampedReference(100, 0);

	public static void main(String[] args) throws InterruptedException {
	

		Thread refT1 = new Thread(new Runnable() {
			@Override
			public void run() {
				
				System.out.println("��ʼ���"+atomicStampedRef.getStamp()+" ֵ:"+atomicStampedRef.getReference());
				boolean c1 =atomicStampedRef.compareAndSet(100, 101, atomicStampedRef.getStamp(), atomicStampedRef.getStamp() + 1);
				System.out.println(c1+"���һ�α��"+atomicStampedRef.getStamp()+" ֵ:"+atomicStampedRef.getReference());
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
				}
				boolean c2 =atomicStampedRef.compareAndSet(101, 100, atomicStampedRef.getStamp(), atomicStampedRef.getStamp() + 1);
				System.out.println(c2+"���ñ��"+atomicStampedRef.getStamp()+" ֵ:"+atomicStampedRef.getReference());
				String name;
			}
		});

		Thread refT2 = new Thread(new Runnable() {
			@Override
			public void run() {
				int stamp = atomicStampedRef.getStamp();
				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
				}
				System.out.println("��ǰ��־: "+stamp+" ֵ:"+atomicStampedRef.getReference());
				boolean c3 = atomicStampedRef.compareAndSet(100, 101, stamp, stamp + 1);
				System.out.println(c3+"��ǰ��־"+atomicStampedRef.getStamp()+" ֵ:"+atomicStampedRef.getReference()); // false
			}
		});

		refT1.start();
		refT2.start();
		
		
		
		Thread intT1 = new Thread(new Runnable() {
			@Override
			public void run() {
				atomicInt.compareAndSet(100, 101);
				atomicInt.compareAndSet(101, 100);
			}
		});

		Thread intT2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
				}
				boolean c3 = atomicInt.compareAndSet(100, 101);
//				System.out.println(c3);
			}
		});

		intT1.start();
		intT2.start();
		intT1.join();
		intT2.join();
	}
}
