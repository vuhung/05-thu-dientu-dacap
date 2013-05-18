/*****************************************************************
   Copyright 2007 by Luong Thi Cao Van (ltcvan@truthinet.com)

   Licensed under the iNet Solutions Corp.,;
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.truthinet.com.vn/licenses

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*****************************************************************/
package com.inet.web.service.mail.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.inet.base.logging.INetLogger;
import com.inet.base.service.DateService;
import com.inet.base.service.StringService;

/**
 * WebCommonService.
 * 
 * @author <a href="mailto:ltcvan@truthinet.com.vn"> Luong Thi Cao Van</a>
 * @version 0.2i
 */
public abstract class WebCommonService {
	/**
	 * class logger.
	 */
	private static final INetLogger logger = INetLogger.getLogger(WebCommonService.class) ;
	
	/**
	 * format date parameter.
	 */
	public static final String FORMAT_DATE_PARAM = "fdate" ;
	
	/**
	 * delimit character.
	 */
	public static final String DELIMIT = ";" ;
	
	/**
	 * default format.
	 */
	private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy" ;
	
	/**
	 * Convert list of identifier representing as string to list of long.
	 * <pre>
	 * For example: the given data. 1;2;3;4;4;4;4;5 --> [1,2,3,4,5]
	 * </pre>
	 * 
	 * @param data String - the given list of identifier representing as string.
	 * @return the list of identifier.
	 */
	public static List<Long> toArray(String data){
		// split the given data to list of values.
		String[] values = data.split(DELIMIT);
		List<Long> ids = new ArrayList<Long>();
		
		// convert each value to long value.
		for(String value : values){
			long id = toLong(value) ;
			if(!ids.contains(id)) {
				ids.add(id) ;
			}
 		}
		
		// return list of identifier.
		return ids;
	} 
	
	/**
	 * Convert the identifier representing as string to long value.
	 * 
	 * @param id String - the given identifier to convert.
	 * @return the identifier value.
	 */
	public static long toLong(String id) {
		try {
			return Long.parseLong(id) ;
		}catch(Exception ex) {
			logger.warning("could not convert [" + id + "] to long value") ;
			
			// return default value.
			return 0L ;
		}
	}

	/**
	 * Convert the value representing as string to int value.
	 * 
	 * @param value String - the given value to convert.
	 * @param defaultValue int - the given default value to be returned when error 
	 * occurs during converting.
	 * @return the identifier value or default value.
	 */
	public static int toInt(String value, int defaultValue) {
		try {
			return Integer.parseInt(value) ;
		}catch(Exception ex) {
			logger.warning("could not convert value [" + value + "] to integer.") ;			
			return defaultValue ;
		}
	}
	
	/**
	 * Get date from the given request and date parameter.
	 * 
	 * 
	 * @param request {@link HttpServletRequest} - the given servlet request.
	 * @param dateParam String - the given date parameter.
	 * @param fmParam String - the given format date parameter.
	 * @param defaultValue Date - the given default value when convert date error.
	 * @return the date or default value.
	 */
	public static Date toDate(HttpServletRequest request, String dateParam, String fmParam, Date defaultValue){
		try{
			String format = request.getParameter(fmParam) ;
			format = StringService.hasLength(format) ? format : DEFAULT_DATE_FORMAT ;
			
			// get date value.
			String date = request.getParameter(dateParam);
			if(!StringService.hasLength(date)) return defaultValue ;
			return DateService.getDate(date, format) ;
		}catch(Exception ex){}
		
		// return default value.
		return defaultValue ;
	}

	/**
	 * Convert data from the given date and format.
	 * 
	 * @param object JSONObject - the given JSONObject value.
	 * @param dateKey String - the given date.
	 * @param defaultValue Date - the given default date.
	 * @return the date object.
	 */
	public static Date toDate(JSONObject object, String dateKey,  Date defaultValue) {
		try {
			String date = toString(object, dateKey, StringService.EMPTY_STRING) ;
			if(!StringService.hasLength(date)) return defaultValue ;
			String format = toString(object, FORMAT_DATE_PARAM, DEFAULT_DATE_FORMAT) ;
			
			// return date value.
			return DateService.getDate(date, format) ;
		}catch(Exception ex) {}
		
		// return default value.
		return defaultValue ;
	}
	
	/**
	 * @return the JSON array data from the given request and parameter.
	 */
	public static JSONArray toJSONArray(HttpServletRequest request, String param) {
		String value = request.getParameter(param) ;
		if(StringService.hasLength(value)) {
			try {
				return JSONArray.fromObject(value) ;
			}catch(Exception ex) {}
		}
		
		// could not get value.
		return null ;
	}
	
	/**
	 * @return the JSON object from he given request and parameter.
	 */
	public static JSONObject toJSONObject(HttpServletRequest request, String param) {
		String value = request.getParameter(param) ;
		if(StringService.hasLength(value)) {
			try {
				return JSONObject.fromObject(value) ;
			}catch(Exception ex) {}
		}
		
		// could not get value.
		return null ;
	}

	/**
	 * Get key value from the given JSONObject and key.
	 * 
	 * @param object JSONObject - the given JSONObject to read data.
	 * @param key String - the given object key.
	 * @return the object value or empty.
	 */
	public static String toString(JSONObject object, String key) {
		return toString(object, key, StringService.EMPTY_STRING) ;
	}	
	
	/**
	 * Get key value from the given JSONObject and key.
	 * 
	 * @param object JSONObject - the given JSONObject to read data.
	 * @param key String - the given object key.
	 * @param defaultValue String - the given default value.
	 * @return the object value or default value.
	 */
	public static String toString(JSONObject object, String key, String defaultValue) {
		if(object == null) return defaultValue ;
		if(!object.has(key)) return defaultValue ;
		try {
			return object.getString(key) ;
		}catch(Exception ex) {}
		return defaultValue ;
	}
	
	/**
	 * Get key value as integer from the given JSONObject and key.
	 * 
	 * @param object JSONObject - the given JSONObject to read data.
	 * @param key String - the given object key.
	 * @return the object value as integer or -1.
	 */
	public static int toInt(JSONObject object, String key) {
		return toInt(object, key, -1) ;
	}	
	
	/**
	 * Get key value as integer from the given JSONObject and key.
	 * 
	 * @param object JSONObject - the given JSONObject to read data.
	 * @param key String - the given object key.
	 * @param defaultValue int - the given default value.
	 * @return the object value as integer or default value.
	 */
	public static int toInt(JSONObject object, String key, int defaultValue) {
		if(object == null) return defaultValue ;
		if(!object.has(key)) return defaultValue ;
		
		try {
			return object.getInt(key) ;
		}catch(Exception ex) {}		
		return defaultValue ;
	}
	
	/**
	 * Get key value as long from the given JSONObject and key.
	 * 
	 * @param object JSONObject - the given JSONObject to read data.
	 * @param key String - the given object key.
	 * @return the object value as integer or -1.
	 */
	public static long toLong(JSONObject object, String key) {
		return toLong(object, key, -1L) ;
	}		
	
	/**
	 * Get key value as long from the given JSONObject and key.
	 * 
	 * @param object JSONObject - the given JSONObject to read data.
	 * @param key String - the given object key.
	 * @param defaultValue long - the given default value.
	 * @return the object value as integer or default value.
	 */
	public static long toLong(JSONObject object, String key, long defaultValue) {
		if(object == null) return defaultValue ;
		if(!object.has(key)) return defaultValue ;
		
		try {
			return object.getLong(key) ;
		}catch(Exception ex) {}		
		return defaultValue ;
	}	
	
	/**
	 * Get key value as boolean from the given JSONObject and key.
	 * 
	 * @param object JSONObject - the given JSONObject instance.
	 * @param key String - the given key.
	 * @return the object value as boolean or false.
	 */
	public static boolean toBool(JSONObject object, String key){
		return toBool(object, key, false) ;
	}
	
	/**
	 * Get key value as boolean from the given JSONObject and key.
	 * 
	 * @param object JSONObject - the given JSONObject instance.
	 * @param key String - the given key.
	 * @param defaultValue boolean - the given default value.
	 * @return the object value as boolean or default value.
	 */
	public static boolean toBool(JSONObject object, String key, boolean defaultValue){
		if(object == null) return defaultValue ;
		if(!object.has(key)) return defaultValue ;
		
		try {
			return object.getBoolean(key) ;
		}catch(Exception ex) {}		
		return defaultValue ;
	}
	
	/**
	 * Get object value as JSONObject from the given JSONObject and key.
	 * 
	 * @param object JSONObject - the given JSONObject.
	 * @param key String - the given object key.
	 * @return the JSONObject or null.
	 */
	public static JSONObject toObject(JSONObject object, String key) {
		if(object == null) return null ;
		if(!object.has(key)) return null ;
		try {
			return JSONObject.fromObject(object.getString(key)) ;
		}catch(Exception ex) {
			if(logger.isDebugEnabled()) logger.debug("could not get JSONObject in object: [" + object + "] with key: [" + key + "]") ;
		}
		return null ;
	}
	
	/**
	 * Get object value as array from the given JSONObject and key.
	 * 
	 * @param object JSONObject - the given JSONObject to read data.
	 * @param key String - the given object key.
	 * @return the object value; may be null.
	 */
	public static JSONArray toArray(JSONObject object, String key) {
		if(object == null) return null ;
		if(!object.has(key)) return null ;
		try {
			return JSONArray.fromObject(object.getString(key)) ;
		}catch(Exception ex) {
			if(logger.isDebugEnabled()) logger.debug("could not get JSONArray in object: [" + object + "] with key: [" + key + "]") ;
		}
		return null ;
	}
}
