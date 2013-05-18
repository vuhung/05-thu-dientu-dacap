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

import com.inet.web.registry.AbstractBeanDefinition;

/**
 * CacheExpireBeanDefinition
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: CacheExpireBeanDefinition.java 2008-12-25 15:30:00z nguyen_dv $
 * 
 * Create date: Dec 25, 2008
 * <pre>
 *  Initialization CacheExpireBeanDefinition class.
 * </pre>
 */
public class CacheExpireBeanDefinition extends AbstractBeanDefinition {
	//~ Static fields =========================================================
	/* cache bean definition name. */
	public static final String CACHE_BEAN_DEFINITION_NAME = CacheExpireBeanDefinition.class.getName() ;
	
	/* expire value. */
	private int expire ;
	
	/* expire type. */
	private int[] expireType ;
	
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.web.registry.BeanDefinition#getName()
	 */
	public String getName() {
		return CACHE_BEAN_DEFINITION_NAME;
	}
	
	/**
	 * @return the expire value.
	 */
	public int getExpire() {
		return expire;
	}
	
	/**
	 * Set the expire value.
	 * 
	 * @param expire the expire value to set.
	 */
	public void setExpire(int expire) {
		this.expire = expire;
	}
	
	/**
	 * @return the expire type
	 */
	public int[] getExpireType() {
		return expireType;
	}
	
	/**
	 * Set the expire type.
	 * 
	 * @param expireType the expire type to set
	 */
	public void setExpireType(int[] expireType) {
		this.expireType = expireType;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.web.registry.AbstractBeanDefinition#toString()
	 */
	@Override
	public String toString() {
		// create buffer.
		StringBuffer buffer = new StringBuffer() ;
		
		// prepare data.
		buffer.append(this.getClass().getName()).append("[")
			  .append("Name: ").append(CACHE_BEAN_DEFINITION_NAME)
			  .append("Expire Value: ").append(expire)
			  .append("]") ;
		
		// return cache expire value as string.
		return buffer.toString();
	}
}
