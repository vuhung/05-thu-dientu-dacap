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
package com.inet.mail.spam.parser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.inet.base.service.CompareService;
import com.inet.web.xml.XmlService;

/**
 * AbstractXMLElementParser
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: AbstractXMLElementParser.java 2009-01-10 16:01:45z nguyen_dv $
 * 
 * Create date: Jan 10, 2009
 * <pre>
 *  Initialization AbstractXMLElementParser class.
 * </pre>
 */
public abstract class AbstractXMLElementParser {
	//~ Methods ===============================================================
	/**
	 * Get attribute value from the given {@link Element}, attribute name and 
	 * default attribute value.
	 * 
	 * @param element the given {@link Element} instance.
	 * @param attribute the given attribute name.
	 * @param defaultValue the given default attribute value.
	 * @return the attribute value.
	 */
	protected String getAttribute(Element element, String attribute, String defaultValue){
		if(element == null) return defaultValue ;
		String value = element.getAttribute(attribute) ;
		return (value == null) ? defaultValue : value ;
	}
	
	/**
	 * Get attribute value as integer from the given {@link Element}, attribute name 
	 * and default attribute value.
	 * 
	 * @param element the given {@link Element} instance.
	 * @param attribute the given attribute name.
	 * @param defaultValue the given default attribute value.
	 * @return the attribute value.
	 */
	protected int getIntAttribute(Element element, String attribute, int defaultValue){
		String value = getAttribute(element, attribute, String.valueOf(defaultValue)) ;
		try{
			return Integer.parseInt(value) ;
		}catch(NumberFormatException ex){}
		return defaultValue ;
	}
	
	/**
	 * Get attribute value as double from the given {@link Element}, attribute name
	 * and default attribute value.
	 * 
	 * @param element the given {@link Element} instance.
	 * @param attribute the given attribute name.
	 * @param defaultValue the given default attribute value.
	 * @return the attribute value.
	 */
	protected double getDoubleAttribute(Element element, String attribute, double defaultValue){
		String value = getAttribute(element, attribute, String.valueOf(defaultValue)) ;
		try{
			return Double.parseDouble(value) ;
		}catch(NumberFormatException nex){}
		return defaultValue ;
	}
	
	/**
	 * Get attribute value as {@link Class} instance from the given {@link Element} 
	 * and attribute name.
	 * 
	 * @param element the given {@link Element} instance.
	 * @param attribute the given attribute name.
	 * @return the {@link Class} instance or null.
	 */
	protected Class<?> getClass(Element element, String attribute){
		String value = getAttribute(element, attribute, null) ;
		if(value == null) return null ;
		
		// get class name.
		try {
			return Class.forName(value) ;
		} catch (ClassNotFoundException cnfex) {}
		return null ;
	}
	
	/**
	 * Get {@link Element} from the given element name.
	 * 
	 * @param element the given {@link Element} contain the expected {@link Element}
	 * @param name the given element name.
	 * @return the expected {@link Element} instance or null.
	 */
	protected Element getElement(Element element, String name){
		return XmlService.getSubElement(element, name) ;
	}
	
	/**
	 * Get list of {@link Element} from the given element name.
	 * 
	 * @param element the given {@link Element} content list of expected {@link Element}
	 * @param name the given element name.
	 * @return the list of expected {@link Element} instance or empty array.
	 */
	protected List<Element> getElements(Element element, String name){
		List<Element> elements = new ArrayList<Element>() ;
		NodeList nodes = element.getChildNodes() ;
		
		for(int index = 0; index < nodes.getLength(); index++) {
			Node node = nodes.item(index) ;
			if(node.getNodeType() == Node.ELEMENT_NODE && 
					CompareService.equals(name, ((Element)node).getTagName())){
				elements.add((Element)node) ;
			}
		}
		
		return elements ;		
	}
	
	/**
	 * Get attribute value as boolean from the given {@link Element}, attribute name and 
	 * default attribute value.
	 * 
	 * @param element the given {@link Element} instance.
	 * @param attribute the given attribute name.
	 * @param defaultValue the given default attribute value.
	 * @return the attribute value.
	 */
	protected boolean getBoolAttribute(Element element, String attribute, boolean defaultValue){
		String value = getAttribute(element, attribute, String.valueOf(defaultValue)) ;
		try{
			return Boolean.valueOf(value) ;
		}catch(Exception ex){}
		return defaultValue ;
	}	
}
