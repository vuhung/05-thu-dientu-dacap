/*****************************************************************
   Copyright 2006 by Dung Nguyen (dungnguyen@truthinet.com)

   Licensed under the iNet Solutions Corp.,;
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.truthinet.com/licenses

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*****************************************************************/
package com.inet.mail.conf.beans;

import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.inet.base.concurrent.CollectionFactory;
import com.inet.base.concurrent.ConcurrentMap;
import com.inet.base.service.StringService;
import com.inet.mail.AbstractMailFactory;
import com.inet.mail.data.MailFactoryType;
import com.inet.mail.data.MailSecurity;
import com.inet.mail.exception.MailException;
import com.inet.mail.parser.AbstractMessageFactory;
import com.inet.web.registry.AbstractBeanDefinition;

/**
 * MailFactoryBeanDefinition
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: MailFactoryBeanDefinition.java 2008-12-07 22:14:48Z nguyen_dv $
 * 
 * Create date: Dec 7, 2008
 * <pre>
 *  Initialization MailFactoryBeanDefinition class.
 * </pre>
 */
public class MailFactoryBeanDefinition extends AbstractBeanDefinition {
	//~ Static fields =========================================================	
	/* MailFactoryBeanDefinition name. */
	public static final String MAIL_FACTORY_BEAN_DEFINITION_NAME = MailFactoryBeanDefinition.class.getName() ;
	
	/* constructor with 1 parameter. */
	private static final String CONSTRUCTOR_1 = "CONSTRUCTOR#1" ;
	
	/* constructor with 2 parameters. */
	private static final String CONSTRUCTOR_2 = "CONSTRUCTOR#2" ;
	
	/* constructor with 3 parameters. */
	private static final String CONSTRUCTOR_3 = "CONSTRUCTOR#3" ;
	
	/* constructor with 4 parameters. */
	private static final String CONSTRUCTOR_4 = "CONSTRUCTOR#4" ;
	
	/* constructor with 5 parameters. */
	private static final String CONSTRUCTOR_5 = "CONSTRUCTOR#5" ;	
	
	/* message factory. */
	private static final String MESSAGE_FACTORY = "MESSAGE_FACTORY" ;
	
	//~ Instance fields =======================================================
	/* mail factory. */
	private MailFactoryType factory = MailFactoryType.SUN;
	
	/* concurrent map. */
	private final static ConcurrentMap cache = CollectionFactory.createConcurrentMap(5) ;
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return MAIL_FACTORY_BEAN_DEFINITION_NAME;
	}
	
	/**
	 * @return the factory name.
	 */
	public MailFactoryType getFactory() {
		return factory;
	}
	
	/**
	 * Set the factory name.
	 * 
	 * @param factory the factory name.
	 */
	public void setFactory(MailFactoryType factory) {
		this.factory = factory;
	}
	
	//~ Business methods ======================================================
	/**
	 * @return the default {@link AbstractMessageFactory} class.
	 */
	@SuppressWarnings({"unchecked"})
	public Class<AbstractMessageFactory> createMessageFactory() throws MailException{
		// get future object.
		Future<Class<AbstractMessageFactory>> future = (Future<Class<AbstractMessageFactory>>)cache.get(MESSAGE_FACTORY) ;
		
		if(future == null){
			/**
			 * Create the class instance and return to subsystem.
			 */
			Callable<Class<AbstractMessageFactory>> evaluate = new Callable<Class<AbstractMessageFactory>>(){
				/**
				 * @see java.util.concurrent.Callable#call()
				 */
				public Class<AbstractMessageFactory> call() throws Exception {
					// get factory class.
					String factoryClazz = factory.getFactory() ;
					
					// if the class does not find.
					if(!StringService.hasLength(factoryClazz)){
						throw new MailException("Does not find any message factory class.") ;
					}
					
					try {
						// get message factory class.			
						Class<AbstractMessageFactory> msgFactory = (Class<AbstractMessageFactory>)Class.forName(factoryClazz) ;
						
						// return factory class.
						return msgFactory;
					}catch (ClassNotFoundException cnfex) {
						// throw exception.
						throw new MailException(cnfex.getMessage(), cnfex) ;
					}
				}
			} ;
			
			//  create future task to invoke data.
			FutureTask<Class<AbstractMessageFactory>> futureTask = 
					new FutureTask<Class<AbstractMessageFactory>>(evaluate);
			
			// put to cache.
			future = (Future<Class<AbstractMessageFactory>>)cache.putIfAbsent(MESSAGE_FACTORY, futureTask) ;
			
			// there is no value.
			if(future == null){
				future = futureTask ;
				futureTask.run() ;
			}
		}		
		
		try {
			return future.get() ;
		} catch (CancellationException cex) {
			// remove the cache.
			cache.remove(MESSAGE_FACTORY, future) ;
			throw new MailException("Could not create MessageFactory.", cex) ;
		} catch (ExecutionException eex){
			throw new MailException("Could not create MessageFactory.", eex) ;
		} catch (InterruptedException itex){
			throw new MailException("Could not create MessageFactory.", itex) ;
		}
	}
	
	/**
	 * Create and return the {@link Constructor} of {@link AbstractMailFactory} instance.
	 * 
	 * @return the {@link Constructor} of {@link AbstractMailFactory} instance.
	 */
	@SuppressWarnings({"unchecked"})
	public Constructor<AbstractMailFactory> createMailFactoryConstructorWithH(){
		// get constructor class.
		Future<Constructor<AbstractMailFactory>> future = (Future<Constructor<AbstractMailFactory>>)cache.get(CONSTRUCTOR_1) ;
		
		// create new future if does not exist.
		if(future == null){
			Callable<Constructor<AbstractMailFactory>> evaluate = new Callable<Constructor<AbstractMailFactory>>(){
				/**
				 * @see java.util.concurrent.Callable#call()
				 */
				public Constructor<AbstractMailFactory> call() throws Exception {
					// get factory class.
					String factoryClazz = factory.getMailFactory() ;
					
					// if the class does not find.
					if(!StringService.hasLength(factoryClazz)){
						throw new MailException("Does not find any mail factory class.") ;
					}
					
					try {
						// get message factory class.			
						Class<AbstractMailFactory> msgFactory = (Class<AbstractMailFactory>)Class.forName(factoryClazz) ;
						
						// create message factory class.
						Constructor<AbstractMailFactory> constructor = msgFactory.getDeclaredConstructor(String.class) ;
						
						// return the mail factory instance.
						constructor.setAccessible(true) ;
						
						// return the constructor to subsystem.
						return constructor ;
					}catch(ClassNotFoundException cnfex){
						throw new MailException(cnfex.getMessage(), cnfex) ;
					}catch(SecurityException sex){
						throw new MailException(sex.getMessage(), sex) ;
					}catch(NoSuchMethodException nsmex){
						throw new MailException(nsmex.getMessage(), nsmex) ;
					}
				}
			} ;
			
			// create future task
			FutureTask<Constructor<AbstractMailFactory>> futureTask = new FutureTask<Constructor<AbstractMailFactory>>(evaluate) ;
			
			// put value to cache.
			future = (Future<Constructor<AbstractMailFactory>>)cache.putIfAbsent(CONSTRUCTOR_1, futureTask) ;
			if(future == null){
				future = futureTask ;
				futureTask.run() ;
			}
		}
		
		try{
			return future.get() ;
		}catch(CancellationException cex){
			cache.remove(CONSTRUCTOR_1, future) ;			
			throw new MailException("Could not create MailFactory.", cex) ;
		}catch(ExecutionException eex){
			throw new MailException("Could not create MailFactory.", eex) ;
		}catch(InterruptedException itex){
			throw new MailException("Could not create MailFactory.", itex) ;
		}
	}
	
	/**
	 * Create and return the {@link Constructor} of {@link AbstractMailFactory} instance.
	 * 
	 * @return the {@link Constructor} of {@link AbstractMailFactory} instance.
	 */
	@SuppressWarnings({"unchecked"})
	public Constructor<AbstractMailFactory> createMailFactoryConstructorWithHP(){
		// get constructor class.
		Future<Constructor<AbstractMailFactory>> future = (Future<Constructor<AbstractMailFactory>>)cache.get(CONSTRUCTOR_2) ;
		
		// create new future if does not exist.
		if(future == null){
			Callable<Constructor<AbstractMailFactory>> evaluate = new Callable<Constructor<AbstractMailFactory>>(){
				/**
				 * @see java.util.concurrent.Callable#call()
				 */
				public Constructor<AbstractMailFactory> call() throws Exception {
					// get factory class.
					String factoryClazz = factory.getMailFactory() ;
					
					// if the class does not find.
					if(!StringService.hasLength(factoryClazz)){
						throw new MailException("Does not find any mail factory class.") ;
					}
					
					try {
						// get message factory class.			
						Class<AbstractMailFactory> msgFactory = (Class<AbstractMailFactory>)Class.forName(factoryClazz) ;
						
						// create message factory class.
						Constructor<AbstractMailFactory> constructor = msgFactory.getDeclaredConstructor(String.class, int.class) ;
						
						// return the mail factory instance.
						constructor.setAccessible(true) ;
						
						// return the constructor to subsystem.
						return constructor ;
					}catch(ClassNotFoundException cnfex){
						throw new MailException(cnfex.getMessage(), cnfex) ;
					}catch(SecurityException sex){
						throw new MailException(sex.getMessage(), sex) ;
					}catch(NoSuchMethodException nsmex){
						throw new MailException(nsmex.getMessage(), nsmex) ;
					}
				}
			} ;
			
			// create future task
			FutureTask<Constructor<AbstractMailFactory>> futureTask = new FutureTask<Constructor<AbstractMailFactory>>(evaluate) ;
			
			// put value to cache.
			future = (Future<Constructor<AbstractMailFactory>>)cache.putIfAbsent(CONSTRUCTOR_2, futureTask) ;
			if(future == null){
				future = futureTask ;
				futureTask.run() ;
			}
		}
		
		try{
			return future.get() ;
		}catch(CancellationException cex){
			cache.remove(CONSTRUCTOR_2, future) ;			
			throw new MailException("Could not create MailFactory.", cex) ;
		}catch(ExecutionException eex){
			throw new MailException("Could not create MailFactory.", eex) ;
		}catch(InterruptedException itex){
			throw new MailException("Could not create MailFactory.", itex) ;
		}
	}	
	
	/**
	 * Create and return the {@link Constructor} of {@link AbstractMailFactory} instance.
	 * 
	 * @return the {@link Constructor} of {@link AbstractMailFactory} instance.
	 */
	@SuppressWarnings({"unchecked"})
	public Constructor<AbstractMailFactory> createMailFactoryConstructorWithSH(){
		// get constructor class.
		Future<Constructor<AbstractMailFactory>> future = (Future<Constructor<AbstractMailFactory>>)cache.get(CONSTRUCTOR_3) ;
		
		// create new future if does not exist.
		if(future == null){
			Callable<Constructor<AbstractMailFactory>> evaluate = new Callable<Constructor<AbstractMailFactory>>(){
				/**
				 * @see java.util.concurrent.Callable#call()
				 */
				public Constructor<AbstractMailFactory> call() throws Exception {
					// get factory class.
					String factoryClazz = factory.getMailFactory() ;
					
					// if the class does not find.
					if(!StringService.hasLength(factoryClazz)){
						throw new MailException("Does not find any mail factory class.") ;
					}
					
					try {
						// get message factory class.			
						Class<AbstractMailFactory> msgFactory = (Class<AbstractMailFactory>)Class.forName(factoryClazz) ;
						
						// create message factory class.
						Constructor<AbstractMailFactory> constructor = msgFactory.getDeclaredConstructor(MailSecurity.class, String.class) ;
						
						// return the mail factory instance.
						constructor.setAccessible(true) ;
						
						// return the constructor to subsystem.
						return constructor ;
					}catch(ClassNotFoundException cnfex){
						throw new MailException(cnfex.getMessage(), cnfex) ;
					}catch(SecurityException sex){
						throw new MailException(sex.getMessage(), sex) ;
					}catch(NoSuchMethodException nsmex){
						throw new MailException(nsmex.getMessage(), nsmex) ;
					}
				}
			} ;
			
			// create future task
			FutureTask<Constructor<AbstractMailFactory>> futureTask = new FutureTask<Constructor<AbstractMailFactory>>(evaluate) ;
			
			// put value to cache.
			future = (Future<Constructor<AbstractMailFactory>>)cache.putIfAbsent(CONSTRUCTOR_3, futureTask) ;
			if(future == null){
				future = futureTask ;
				futureTask.run() ;
			}
		}
		
		try{
			return future.get() ;
		}catch(CancellationException cex){
			cache.remove(CONSTRUCTOR_3, future) ;			
			throw new MailException("Could not create MailFactory.", cex) ;
		}catch(ExecutionException eex){
			throw new MailException("Could not create MailFactory.", eex) ;
		}catch(InterruptedException itex){
			throw new MailException("Could not create MailFactory.", itex) ;
		}
	}
	
	/**
	 * Create and return the {@link Constructor} of {@link AbstractMailFactory} instance.
	 * 
	 * @return the {@link Constructor} of {@link AbstractMailFactory} instance.
	 */
	@SuppressWarnings({"unchecked"})
	public Constructor<AbstractMailFactory> createMailFactoryConstructorWithSHP(){
		// get constructor class.
		Future<Constructor<AbstractMailFactory>> future = (Future<Constructor<AbstractMailFactory>>)cache.get(CONSTRUCTOR_4) ;
		
		// create new future if does not exist.
		if(future == null){
			Callable<Constructor<AbstractMailFactory>> evaluate = new Callable<Constructor<AbstractMailFactory>>(){
				/**
				 * @see java.util.concurrent.Callable#call()
				 */
				public Constructor<AbstractMailFactory> call() throws Exception {
					// get factory class.
					String factoryClazz = factory.getMailFactory() ;
					
					// if the class does not find.
					if(!StringService.hasLength(factoryClazz)){
						throw new MailException("Does not find any mail factory class.") ;
					}
					
					try {
						// get message factory class.			
						Class<AbstractMailFactory> msgFactory = (Class<AbstractMailFactory>)Class.forName(factoryClazz) ;
						
						// create message factory class.
						Constructor<AbstractMailFactory> constructor = msgFactory.getDeclaredConstructor(MailSecurity.class, String.class, int.class) ;
						
						// return the mail factory instance.
						constructor.setAccessible(true) ;
						
						// return the constructor to subsystem.
						return constructor ;
					}catch(ClassNotFoundException cnfex){
						throw new MailException(cnfex.getMessage(), cnfex) ;
					}catch(SecurityException sex){
						throw new MailException(sex.getMessage(), sex) ;
					}catch(NoSuchMethodException nsmex){
						throw new MailException(nsmex.getMessage(), nsmex) ;
					}
				}
			} ;
			
			// create future task
			FutureTask<Constructor<AbstractMailFactory>> futureTask = new FutureTask<Constructor<AbstractMailFactory>>(evaluate) ;
			
			// put value to cache.
			future = (Future<Constructor<AbstractMailFactory>>)cache.putIfAbsent(CONSTRUCTOR_4, futureTask) ;
			if(future == null){
				future = futureTask ;
				futureTask.run() ;
			}
		}
		
		try{
			return future.get() ;
		}catch(CancellationException cex){
			cache.remove(CONSTRUCTOR_4, future) ;			
			throw new MailException("Could not create MailFactory.", cex) ;
		}catch(ExecutionException eex){
			throw new MailException("Could not create MailFactory.", eex) ;
		}catch(InterruptedException itex){
			throw new MailException("Could not create MailFactory.", itex) ;
		}
	}
	
	/**
	 * Create and return the {@link Constructor} of {@link AbstractMailFactory} instance.
	 * 
	 * @return the {@link Constructor} of {@link AbstractMailFactory} instance.
	 */
	@SuppressWarnings({"unchecked"})
	public Constructor<AbstractMailFactory> createMailFactoryConstructorWithSHPUN(){
		// get constructor class.
		Future<Constructor<AbstractMailFactory>> future = (Future<Constructor<AbstractMailFactory>>)cache.get(CONSTRUCTOR_5) ;
		
		// create new future if does not exist.
		if(future == null){
			Callable<Constructor<AbstractMailFactory>> evaluate = new Callable<Constructor<AbstractMailFactory>>(){
				/**
				 * @see java.util.concurrent.Callable#call()
				 */
				public Constructor<AbstractMailFactory> call() throws Exception {
					// get factory class.
					String factoryClazz = factory.getMailFactory() ;
					
					// if the class does not find.
					if(!StringService.hasLength(factoryClazz)){
						throw new MailException("Does not find any mail factory class.") ;
					}
					
					try {
						// get message factory class.			
						Class<AbstractMailFactory> msgFactory = (Class<AbstractMailFactory>)Class.forName(factoryClazz) ;
						
						// create message factory class.
						Constructor<AbstractMailFactory> constructor = msgFactory.getDeclaredConstructor(MailSecurity.class, String.class, int.class, String.class, String.class) ;
						
						// return the mail factory instance.
						constructor.setAccessible(true) ;
						
						// return the constructor to subsystem.
						return constructor ;
					}catch(ClassNotFoundException cnfex){
						throw new MailException(cnfex.getMessage(), cnfex) ;
					}catch(SecurityException sex){
						throw new MailException(sex.getMessage(), sex) ;
					}catch(NoSuchMethodException nsmex){
						throw new MailException(nsmex.getMessage(), nsmex) ;
					}
				}
			} ;
			
			// create future task
			FutureTask<Constructor<AbstractMailFactory>> futureTask = new FutureTask<Constructor<AbstractMailFactory>>(evaluate) ;
			
			// put value to cache.
			future = (Future<Constructor<AbstractMailFactory>>)cache.putIfAbsent(CONSTRUCTOR_5, futureTask) ;
			if(future == null){
				future = futureTask ;
				futureTask.run() ;
			}
		}
		
		try{
			return future.get() ;
		}catch(CancellationException cex){
			cache.remove(CONSTRUCTOR_5, future) ;			
			throw new MailException("Could not create MailFactory.", cex) ;
		}catch(ExecutionException eex){
			throw new MailException("Could not create MailFactory.", eex) ;
		}catch(InterruptedException itex){
			throw new MailException("Could not create MailFactory.", itex) ;
		}
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder() ;
		
		// append data.
		builder.append(this.getClass().getName()).append("[")
			   .append("Factory: ").append(factory)
			   .append("]") ;
		
		return builder.toString();
	}
}
