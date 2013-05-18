/*****************************************************************
   Copyright 2006 by Duyen Tang (tttduyen@truthinet.com)

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
package com.inet.web.service.conf;

/**
 * Configuration.
 * 
 * @author <a href="mailto:tttduyen@truthinet.com">Duyen Tang</a>
 * @version 0.2i
 */
public class Configuration {
	/////////// DOMAIN ATTRIBUTE ////////////
	private String postfixTransport;
	private String maxAlias;
	private String maxMail;
	private String maxQuota;
	
	////////// MAIL ACCOUNT ATTRIBUTE ///////
	private String domainHome;
	private String otherTransport;
	private String spamKillLevel;
	private String spamTagLevel;
	private String spamTag2Level;
	
	/////////// MAX SEARCH RESULT//////////////
	private int maxSearchResult;

	/**
	 * get the post-fix transport for domain
	 * 
	 * @return the postfixTransport
	 */
	public String getPostfixTransport() {
		return this.postfixTransport;
	}

	/**
	 * @param postfixTransport the postfixTransport to set
	 */
	public void setPostfixTransport(String postfixTransport) {
		this.postfixTransport = postfixTransport;
	}

	/**
	 * get the max alias number is permitted on domain
	 * 
	 * @return the maxAlias
	 */
	public String getMaxAlias() {
		return this.maxAlias;
	}

	/**
	 * @param maxAlias the maxAlias to set
	 */
	public void setMaxAlias(String maxAlias) {
		this.maxAlias = maxAlias;
	}

	/**
	 * @return the maxMail
	 */
	public String getMaxMail() {
		return this.maxMail;
	}

	/**
	 * @param maxMail the maxMail to set
	 */
	public void setMaxMail(String maxMail) {
		this.maxMail = maxMail;
	}

	/**
	 * @return the maxQuota
	 */
	public String getMaxQuota() {
		return this.maxQuota;
	}

	/**
	 * @param maxQuota the maxQuota to set
	 */
	public void setMaxQuota(String maxQuota) {
		this.maxQuota = maxQuota;
	}

	/**
	 * @return the domainHome
	 */
	public String getDomainHome() {
		return this.domainHome;
	}

	/**
	 * @param domainHome the domainHome to set
	 */
	public void setDomainHome(String domainHome) {
		this.domainHome = domainHome;
	}

	/**
	 * @return the otherTransport
	 */
	public String getOtherTransport() {
		return this.otherTransport;
	}

	/**
	 * @param otherTransport the otherTransport to set
	 */
	public void setOtherTransport(String otherTransport) {
		this.otherTransport = otherTransport;
	}

	/**
	 * @return the spamKillLevel
	 */
	public String getSpamKillLevel() {
		return this.spamKillLevel;
	}

	/**
	 * @param spamKillLevel the spamKillLevel to set
	 */
	public void setSpamKillLevel(String spamKillLevel) {
		this.spamKillLevel = spamKillLevel;
	}

	/**
	 * @return the spamTagLevel
	 */
	public String getSpamTagLevel() {
		return this.spamTagLevel;
	}

	/**
	 * @param spamTagLevel the spamTagLevel to set
	 */
	public void setSpamTagLevel(String spamTagLevel) {
		this.spamTagLevel = spamTagLevel;
	}

	/**
	 * @return the spamTag2Level
	 */
	public String getSpamTag2Level() {
		return this.spamTag2Level;
	}

	/**
	 * @param spamTag2Level the spamTag2Level to set
	 */
	public void setSpamTag2Level(String spamTag2Level) {
		this.spamTag2Level = spamTag2Level;
	}

	/**
	 * @return the maxSearchResult
	 */
	public int getMaxSearchResult() {
		return this.maxSearchResult;
	}

	/**
	 * @param maxSearchResult the maxSearchResult to set
	 */
	public void setMaxSearchResult(int maxSearchResult) {
		this.maxSearchResult = maxSearchResult;
	}
	
}
