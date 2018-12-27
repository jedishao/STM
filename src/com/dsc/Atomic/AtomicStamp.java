package com.dsc.Atomic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

import com.dsc.Transaction.Transaction;

public class AtomicStamp  {
	/**
	 * 邮标值，ref改变时改变
	 */
	int stampValue;
	Transaction t;
	AtomicStamp stamp;
	/**
	 * ref
	 */
	Object initialRef;
	/**
	 * 当前类   事务状态  0 初始  1  更改中    2已提交  
	 */
    int state=0;
    public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
    /**
	 *  父结点  未更改前的
	 */
	AtomicStamp atomicStampPar;
	
	
	public int getStampValue() {
		return stampValue;
	}
	public void setStampValue(int stampValue) {
		this.stampValue = stampValue;
	}
	public Object getInitialRef() {
		return initialRef;
	}
	public void setInitialRef(Object initialRef) {
		this.initialRef = initialRef;
	}
	
	public AtomicStamp getAtomicStampPar() {
		return atomicStampPar;
	}
	public void setAtomicStampPar(AtomicStamp atomicStampPar) {
		this.atomicStampPar = atomicStampPar;
	}
	
	
	public AtomicStamp(Object initialRef,int stamp) {
		this.stampValue=stamp;
		this.initialRef=initialRef;
		this.setAtomicStampedRef();	 
	}	
	public AtomicStamp(Object initialRef,Transaction t,int stamp) {
		this.stampValue=stamp;
		this.initialRef=initialRef;
		this.setAtomicStampedRef();	
		this.t=t;
		t.setAtomicS(this);
	}	
	public AtomicStamp(AtomicStamp initialRef,Transaction t) {	
		
		this.t=t;
		t.setAtomicS(initialRef);
		this.stamp=initialRef.stamp;
		this.atomicStampedRef=initialRef.atomicStampedRef;
		//this.setAtomicStampedRef();
	}
	/**
	 * AtomicStampedReference
	 */
	private  AtomicStampedReference<Object> atomicStampedRef;
	/**
	 * 获取AtomicStampedReference
	 * @return
	 */
	public AtomicStampedReference<Object> getAtomicStampedRef() {
		return atomicStampedRef;
	}
	public void setAtomicStampedRef() {
		this.atomicStampedRef = new AtomicStampedReference<Object>(initialRef, stampValue);
	} 
	
	public void put(Object newE){
		Transaction tx=Transaction.getCurrent(); 
		if(tx==null){
			
			tx=new Transaction(new AtomicInteger(0));
			tx.startTransaction();
			tx.setStampValue(newE);
			//this.state=2;
			//tx.commitTransaction(this);
		}else{
			tx.setStampValue(newE);
			tx.startTransaction();
			//this.state=2;
			//tx.commitTransaction(this);
		}
		tx.commitTransaction(this);
		
	}


	 
}
