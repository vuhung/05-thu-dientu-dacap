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
package com.inet.mail.core;

import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.inet.base.IDisposable;
import com.inet.base.concurrent.CollectionFactory;
import com.inet.base.concurrent.ConcurrentMap;
import com.inet.base.service.Assert;
import com.inet.mail.PoolObjectManager;
import com.inet.mail.core.cache.Cache;
import com.inet.mail.core.cache.support.WebOSCacheSupport;
import com.inet.mail.exception.MailException;
import com.inet.mail.spam.ScanEngine;
import com.inet.mail.spam.beans.SpamMailBeanDefinition;
import com.inet.web.application.AbstractApplicationServerProvider;
import com.inet.web.beans.NoSuchBeanDefinitionException;
import com.inet.web.cache.RegionManager;
import com.inet.web.cache.support.ZipRegionManagerSupport;
import com.inet.web.cfg.Configuration;
import com.inet.web.context.WebOSApplicationContext;
import com.inet.web.exception.WebOSException;

/**
 * MailApplicationContext
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: MailApplicationContext.java 2008-12-08 17:36:59Z nguyen_dv $
 * 
 * Create date: Dec 8, 2008
 * <pre>
 *  Initialization MailApplicationContext class.
 * </pre>
 */
public final class MailApplicationContext implements IDisposable{
	//~ Static fields =========================================================
        /* class logger. */
        protected final Logger logger = Logger.getLogger(getClass());
        /* spring configuration. */
	private static final String SPRING_CONFIGURATION = "/META-INF/spring/basic-application.xml" ;
	
	/* core configuration. */
	private static final String CORE_CONFIGURATION = "/META-INF/mail.xml" ;
	
	/* self instance. */
	private static final MailApplicationContext SOLE_INSTANCE = new MailApplicationContext() ;
	
	//~ Static fields =========================================================
	/* pool object manager. */
	private static final ConcurrentMap POOL_OBJECT_MANAGER = CollectionFactory.createConcurrentMap(1);
	
	//~ Instance fields =======================================================
	/* web application context. */
	private WebOSApplicationContext context ;
	
	//~ Constructors ==========================================================
	/**
	 * Create a new <tt>MailApplicationContext</tt> instance.
	 */
	private MailApplicationContext(){
		try{
			Configuration configuration = new Configuration() ;
			
			// get core configuration.
			InputStream inputStream = MailApplicationContext.class.getResourceAsStream(CORE_CONFIGURATION) ;
						
			// setting spring application context.
			ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
						new String[]{ SPRING_CONFIGURATION }
					) ;
			
			// loading core configuration.
			context = configuration.configure(inputStream)
								   .configure(applicationContext)
								   .buildApplicationContext() ;			
		}catch(Exception ex){
			logger.error("There are some error during create configuration, message: [" + ex.getMessage() + "]", ex) ;
		}
	}
	
	//~ Methods ===============================================================
	/**
	 * @return the <tt>MailApplicationContext</tt> instance.
	 */
	public static MailApplicationContext getInstance(){
		return SOLE_INSTANCE ;
	}
	
	/**
	 * @return the {@link WebOSApplicationContext} instance.
	 */
	public WebOSApplicationContext getContext() {
		return context;
	}
	
	/**
	 * @return the object instance from the given object name.
	 */
	public Object getObject(String name){
		Assert.hasLength(name, "The bean name must be set.") ;
		
		// the application context does not exist.
		if(context == null) return null ;
		if(context.getSpringContext() == null) return null ;
		
		// lookup the bean instance and return to the subsystem.
		return context.getSpringContext().getBean(name) ;
	}
	
	/**
	 * Register the given {@link PoolObjectManager} from the name.
	 * 
	 * @param name the given {@link PoolObjectManager} name.
	 * @param poolManager the given {@link PoolObjectManager} instance.
	 * @return the {@link PoolObjectManager} instance.
	 */
	@SuppressWarnings({"unchecked"})
	public <T> PoolObjectManager<T> register(String name, PoolObjectManager<T> poolManager){
		try{
			PoolObjectManager<T> manager = (PoolObjectManager<T>)POOL_OBJECT_MANAGER.putIfAbsent(name, poolManager) ;
			return (manager == null ? poolManager : manager) ;
		}catch(Exception ex){
			logger.warn("could not register the PoolObjectManager: [" + name + "], message: [" + poolManager + "]") ;
		}
		
		return null ;
	}
	
	/**
	 * Unregister the {@link PoolObjectManager} from the given name.
	 * 
	 * @param name the given {@link PoolObjectManager} name.
	 * @return the {@link PoolObjectManager} instance.
	 */
	@SuppressWarnings({"unchecked"})
	public <T> PoolObjectManager<T> unregister(String name){
		try{
			return (PoolObjectManager<T>)POOL_OBJECT_MANAGER.remove(name) ;
		}catch(Exception ex){
			logger.warn("could not unregister the PoolObjectManager: [" + name + "], message: [" + ex.getMessage() + "]") ;
		}
		
		return null ;
	}
	
	/**
	 * @return the {@link PoolObjectManager} from the given the name.
	 */
	@SuppressWarnings({"unchecked"})
	public <T> PoolObjectManager<T> get(String name){
		try{
			return (PoolObjectManager<T>)POOL_OBJECT_MANAGER.get(name) ;
		}catch(Exception ex){
			logger.warn("could not get the PoolObjectManager: [" + name + "], message: [" + ex.getMessage() + "]") ;
		}
		return null ;
	}
	
	/**
	 * @return the object instance from the given object name and classes.
	 */
	public <T> T getObject(String name, Class<T> clazz){
		Assert.hasLength(name, "The bean name must be set.") ;
		Assert.isNotNull(clazz, "Object class must be set.") ;
		
		// the application context has not loaded yet.
		if(context == null) return null ;
		if(context.getSpringContext() == null) return null ;
	
		// lookup the bean instance and return to the subsystem.
		return clazz.cast(context.getSpringContext().getBean(name)) ;
	}	
	
	/**
	 * Get the region manager from the given region name.
	 * 
	 * @param region the given region name.
	 * @return the {@link RegionManager} instance; may be null.
	 * @throws MailException when error occurs during getting region cache.
	 */
	public RegionManager getRegionManager(String region) throws MailException{
		Assert.hasLength(region, "The region name must be set.") ;
		
		if(context == null) return null ;
		try{
			return ZipRegionManagerSupport.getInstance(context, region) ;
		}catch(WebOSException wex){
			throw new MailException("Could not create region manager, message: [" + wex.getMessage() + "]", wex) ;
		}
	}
	
	/**
	 * Get the cache from the given region cache manager.
	 * 
	 * @param region the given region name.
	 * @return the {@link Cache} instance.
	 * @throws MailException when error occurs during creating cache instance.
	 */
	public Cache getCache(String region) throws MailException{
		Assert.hasLength(region, "The region name must be set.") ;
		
		if(context == null) return null ;
		try{
			return new WebOSCacheSupport(ZipRegionManagerSupport.getInstance(context, region)) ;
		}catch(WebOSException wex){
			throw new MailException("Could not create cache, message: [" + wex.getMessage() + "]", wex) ;			
		}
	}
	
	/**
	 * Return the {@link ScanEngine} instance.
	 * 
	 * @return the given {@link ScanEngine} instance.
	 * @throws MailException when error occurs during getting spam engine.
	 */
	public ScanEngine getSpamEngine() throws MailException{
		if(context == null) return null ;
		
		try{
			// get bean definition.
			SpamMailBeanDefinition beanDefinition = context.getBean(
							SpamMailBeanDefinition.SPAM_MAIL_BEAN_DEFINITION_NAME, 
							SpamMailBeanDefinition.class
						) ;
			
			// get scan engine.
			if(beanDefinition == null) return null;
			return beanDefinition.getScanEngine() ;
		}catch(NoSuchBeanDefinitionException nex){
			logger.warn("Could not configuration SPAM engine, message: [" + nex.getMessage() + "]") ;
			return null ;
		}
	}
	
	/**
	 * @return the {@link AbstractApplicationServerProvider} instance; may be null.
	 */
	public AbstractApplicationServerProvider getApplicationServerProvider() {
		if(context == null) return null;
		return context.getApplicationServerProvider() ;
	}	
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.base.IDisposable#dispose()
	 */
	@SuppressWarnings({"unchecked"})
	public void dispose() {
		// dispose the WebOSApplicationContext.
		try{
			if(context != null) context.dispose() ;
		}catch(Exception ex){
			logger.warn("could not dispose the context, message: [" + ex.getMessage() + "]") ;
		}
		
		// dispose the pool object manager.
		try{
			if(!POOL_OBJECT_MANAGER.isEmpty()){
				Collection<PoolObjectManager<?>> objects = POOL_OBJECT_MANAGER.values() ;
				
				// stop the pool manager.
				Iterator<PoolObjectManager<?>> iterator = objects.iterator() ;
				while(iterator.hasNext()){
					PoolObjectManager<?> manager = iterator.next() ;
					manager.stop() ;
				}
			}
		}catch(Exception ex){
			logger.warn("could not dispose the PoolObjectManager, message: [" + ex.getMessage() + "]") ;
		}
	}
}
