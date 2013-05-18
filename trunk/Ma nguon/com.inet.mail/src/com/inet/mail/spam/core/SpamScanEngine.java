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
package com.inet.mail.spam.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.inet.mail.spam.DNSResolver;
import com.inet.mail.spam.InetAddressResolver;
import com.inet.mail.spam.ProbabilityCalculator;
import com.inet.mail.spam.ProbabilityResult;
import com.inet.mail.spam.ReceivedHeaderParser;
import com.inet.mail.spam.ScanEngine;
import com.inet.mail.spam.ScanResult;
import com.inet.mail.spam.SpamException;
import com.inet.mail.spam.SpamPlugin;
import com.inet.mail.spam.plugin.support.SpamPluginException;
import com.inet.mail.spam.support.ScanResultSupport;

/**
 * SpamScanEngine
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: SpamScanEngine.java 2009-01-10 20:59:27z nguyen_dv $
 * 
 * Create date: Jan 10, 2009
 * <pre>
 *  Initialization SpamScanEngine class.
 * </pre>
 */
public class SpamScanEngine implements ScanEngine {
	//~ Static fields =========================================================
        /* class logger. */
        protected final Logger logger = Logger.getLogger(SpamScanEngine.class);
	//~ Instance fields =======================================================
	/* address resolver. */
	private InetAddressResolver addressResolver ;
	
	/* DNS resolver. */
	private DNSResolver dNSResolver ;
	
	/* probability calculator. */
	private ProbabilityCalculator probabilityCalculator ;
	
	/* received header parser. */
	private ReceivedHeaderParser receivedHeaderParser ;
	
	/* list of spam plugin. */
	private List<SpamPlugin> spamPlugins ;
	
	/* maximize spam level. */
	private double level ;
	//~ Methods ===============================================================
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ScanEngine#getAddressResolver()
	 */
	public InetAddressResolver getAddressResolver() {
		return addressResolver;
	}
	
	/**
	 * Sets the given {@link InetAddressResolver} instance.
	 * 
	 * @param addressResolver the given {@link InetAddressResolver} to set.
	 */
	public void setAddressResolver(InetAddressResolver addressResolver) {
		this.addressResolver = addressResolver;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ScanEngine#getDNSResolver()
	 */
	public DNSResolver getDNSResolver() {
		return dNSResolver;
	}
	
	/**
	 * Sets the given {@link DNSResolver} instance.
	 * 
	 * @param dnsResolver the given {@link DNSResolver} to set.
	 */
	public void setDNSResolver(DNSResolver dNSResolver) {
		this.dNSResolver = dNSResolver;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ScanEngine#getProbabilityCalculator()
	 */
	public ProbabilityCalculator getProbabilityCalculator() {
		return probabilityCalculator;
	}
	
	/**
	 * Sets the given {@link ProbabilityCalculator} instance.
	 * 
	 * @param probabilityCalculator the given {@link ProbabilityCalculator} to set.
	 */
	public void setProbabilityCalculator(ProbabilityCalculator probabilityCalculator) {
		this.probabilityCalculator = probabilityCalculator;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ScanEngine#getReceivedHeaderParser()
	 */
	public ReceivedHeaderParser getReceivedHeaderParser() {
		return receivedHeaderParser;
	}
	
	/**
	 * Sets the given {@link ReceivedHeaderParser} instance.
	 * 
	 * @param receivedHeaderParser the given {@link ReceivedHeaderParser} instance.
	 */
	public void setReceivedHeaderParser(ReceivedHeaderParser receivedHeaderParser) {
		this.receivedHeaderParser = receivedHeaderParser;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ScanEngine#getSpamPlugins()
	 */
	public List<SpamPlugin> getSpamPlugins() {
		return spamPlugins;
	}
	
	/**
	 * Sets the given list of {@link SpamPlugin} instance.
	 * 
	 * @param spamPlugins the given list of {@link SpamPlugin} instance.
	 */
	public void setSpamPlugins(List<SpamPlugin> spamPlugins) {
		this.spamPlugins = spamPlugins;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ScanEngine#getLevel()
	 */
	public double getLevel() {
		return level;
	}
	
	/**
	 * Set SPAM level.
	 * 
	 * @param level the given SPAM level to set.
	 */
	public void setLevel(double level) {
		this.level = level;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.inet.mail.spam.ScanEngine#scan(java.lang.Object)
	 */
	public ScanResult scan(Object message) throws SpamException {
		// create scan result.
		ScanResultSupport resultSupport = new ScanResultSupport() ;
		
		// scan spam.
		if(spamPlugins != null && spamPlugins.size() > 0){
			List<Double> probabilities = new ArrayList<Double>() ;

			ProbabilityResult probabilityResult ; 
			for(SpamPlugin plugin : spamPlugins){
				try{
					probabilityResult = plugin.evaluate(this, message) ;
					
					if(probabilityResult.getProbability() > 0.0d){
						if(probabilityResult.isAbsolute()){
							// absolute result.
							resultSupport.setProbability(probabilityResult.getProbability()) ;
							
							return resultSupport ;
						}else{
							probabilities.add(probabilityResult.getProbability()) ;
						}
					}
				}catch(SpamPluginException spex){
					String error = "SpamPluginException: plugin: [" + plugin.getClass().getName() + "], message: [" + spex.getMessage() + "]";
					resultSupport.addErrors(error) ;
					
					// show warning.
					logger.warn(error) ;
				}catch(SpamException sex){
					String error = "SpamException: plugin: [" + plugin.getClass().getName() + "], message: [" + sex.getMessage() + "]" ;
					resultSupport.addErrors(error) ;
					
					// show warning.
					logger.warn(error) ;
				}
			}
			
			// calculate probabilities.
			if(probabilities != null && probabilities.size() > 0){
				double[] values = new double[probabilities.size()] ;
				for(int index = 0; index < probabilities.size(); index++){
					values[index] = probabilities.get(index).doubleValue() ;
				}
				
				resultSupport.setProbability(probabilityCalculator.calculate(values)) ;
			}
		}
				
		// return scan result.
		return resultSupport;
	}

}
