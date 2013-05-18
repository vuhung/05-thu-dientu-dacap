/*****************************************************************
 Copyright 2007 by Hien Nguyen (hiennguyen@truthinet.com)

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
package com.inet.mail.service;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ejb.Local;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.jboss.annotation.ejb.Management;
import org.jboss.annotation.ejb.Service;

import com.inet.base.ejb.exception.EJBException;
import com.inet.base.service.IOService;
import com.inet.mail.business.sr.MailHeaderSL;
import com.inet.mail.business.template.TemplateDetailInfoSL;
import com.inet.mail.conf.beans.CacheExpireBeanDefinition;
import com.inet.mail.core.CacheManager;
import com.inet.mail.core.MailApplicationContext;
import com.inet.mail.core.TemplateEntry;
import com.inet.mail.data.PairValueDTO;
import com.inet.mail.data.TemplateStatus;
import com.inet.mail.persistence.TemplateDetailInfo;
import com.inet.mail.util.MailService;

/**
 * 
 * This service will be loaded automatically into memory and load all templates
 * from the system then put into CACHE
 * 
 * The default user (sending email) will be loaded as well to make a connection
 * to the server based on file configuration
 * 
 * The template data will be handled by using Velocity engine
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @author <a href="mailto:lqtung@truthinet.com">Tung Luong</a>
 * @version 0.2i
 */
@Service(objectName = "inet:service=ServiceVTE")
@Local(TemplateEngineLocal.class)
@Management(TemplateEngineManagement.class)
public class TemplateEngine implements TemplateEngineLocal, TemplateEngineManagement {
	//~ Static fields ==========================================================
	/* velocity context. */
	private static VelocityContext ctx = null;
	
	/* scheduled executor service. */
	private static ScheduledExecutorService executorService;	

	//~ Instance fields ========================================================	
	/* template details. */
	@EJB(name = "TemplateDetailInfoSLBean")
	private TemplateDetailInfoSL templateBO;

	/* mail header. */
	@EJB(name="MailHeaderSLBean")
	private MailHeaderSL mailBean;

	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.cms.service.TemplateEngineManagement#getContext()
	 */
	public VelocityContext getContext() throws EJBException {
		return ctx;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.cms.service.TemplateEngineManagement#create()
	 */
	public void create() throws Exception {
		Properties props = IOService.getResourceAsProperties("META-INF/velocity.properties");

		// load velocity file configure
		Velocity.init(props);

		/*
		 * Make a context object and populate with the data. This is where the
		 * Velocity engine gets the data to resolve the references (ex. $list)
		 * in the template
		 */
		ctx = new VelocityContext();
		
		// load all templates which contained APPROVAL status and add into cache
		List<TemplateDetailInfo> templates = templateBO
				.queryAll(TemplateStatus.APPROVAL);
		for (TemplateDetailInfo template : templates) {
			String keyCache = MailService.createTicket(
						template.getId().toString(), 
						template.getName()
					);
			CacheManager.getInstance().setItem(
					keyCache,
					new TemplateEntry(template.getTemplate(), template
							.getFormat()));
		}		

		// Running the cache expire service.
		MailApplicationContext context = MailApplicationContext.getInstance() ;
		if(context != null && context.getContext() != null){
			final CacheExpireBeanDefinition definition = context.getContext().getBean(
										CacheExpireBeanDefinition.CACHE_BEAN_DEFINITION_NAME,
										CacheExpireBeanDefinition.class
									) ;
			
			if(definition != null){
				// create executor service.
				executorService = Executors.newScheduledThreadPool(1) ;
				
				// get expire type.
				final int[] expireType = definition.getExpireType() ;
				
				// define the schedule.
				executorService.scheduleAtFixedRate(new Runnable(){
					/**
					 * @see java.lang.Runnable#run()
					 */
					public void run() {
						try{
							// get calendar.
							Calendar calendar = Calendar.getInstance() ;
							
							// compute the expire time.
							calendar.add(expireType[1], -definition.getExpire()) ;
							
							// try to dispose the cache.
							mailBean.cacheExpired(calendar.getTime()) ;
						}catch(Exception ex){}
					}
				}, 1, 1, TimeUnit.values()[expireType[0]]) ;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.cms.service.TemplateEngineManagement#destroy()
	 */
	public void destroy() {
		// empty Velocity context
		ctx = null;
		
		// try to shutdown executor service.
		try{
			if (executorService != null){
				executorService.shutdown();
			}
			
			executorService = null;
		}catch(Exception ex){}
		
		// dispose the mail application context.
		MailApplicationContext applicationContext = MailApplicationContext.getInstance() ;
		if(applicationContext != null){
			applicationContext.dispose() ;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.cms.service.TemplateEngineManagement#start()
	 */
	public void start() throws Exception {}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.cms.service.TemplateEngineManagement#stop()
	 */
	public void stop() {}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.service.TemplateEngineLocal#makeupContent(java.lang.Long, java.lang.String, java.util.List)
	 */
	public String makeupContent(Long templateID, String templateName,
			List<PairValueDTO<String>> params) throws EJBException {

		String keyCache = MailService.createTicket(templateID.toString(),
				templateName);
		TemplateEntry template = (TemplateEntry) CacheManager.getInstance()
				.getItem(keyCache);

		if (template != null) {
			final StringWriter write = new StringWriter();
			merge(template.getContent(), write, params);
			return write.getBuffer().toString();
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.service.TemplateEngineLocal#makeupContent(java.lang.String, java.util.List)
	 */
	public String makeupContent(String content,
			List<PairValueDTO<String>> params) throws EJBException {
		final StringWriter write = new StringWriter();
		merge(content, write, params);
		return write.getBuffer().toString();
	}

	//~ Helper methods ========================================================
	/**
	 * generate data from template using VELOCITY engine
	 * 
	 * @param template the given template
	 * @param writer the given {@link Writer} instance.
	 * @param params the given {@link List} of parameters.
	 * 
	 * @throws EJBException when error occurs during merging the data.
	 */
	private void merge(String template, Writer writer,
			List<PairValueDTO<String>> params) throws EJBException {

		// setup the value to the context
		for (PairValueDTO<String> param : params){
			ctx.put(param.getName(), param.getValue());
		}

		try {
			Velocity.evaluate(ctx, writer, " ", template);
			writer.flush();
		} catch (ParseErrorException e) {
			throw new EJBException("Parse error:", e);
		} catch (MethodInvocationException e) {
			throw new EJBException("Method Invocation error:", e);
		} catch (ResourceNotFoundException e) {
			throw new EJBException("Resource not found:", e);
		} catch (IOException e) {
			throw new EJBException(e);
		}
	}
}
