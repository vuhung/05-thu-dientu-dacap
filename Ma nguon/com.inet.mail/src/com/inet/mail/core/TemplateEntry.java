/*****************************************************************
   Copyright 2006 by Hien Nguyen (hiennguyen@truthinet.com)

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

import java.io.Serializable;

import com.inet.mail.data.TemplateFormat;

/**
 * TemplateEntry.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 0.2i
 */
public class TemplateEntry extends DataEntry<String> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 691456893018146202L;
	
	private TemplateFormat format = TemplateFormat.TEXT; 
	
	/**
	 * 
	 * @param content
	 * @param format
	 */
	public TemplateEntry(String content, TemplateFormat format)
	{
		super(content);
		this.format = format;
	}

	/**
	 * @return the format
	 */
	public TemplateFormat getFormat() {
		return this.format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(TemplateFormat format) {
		this.format = format;
	}	
}
