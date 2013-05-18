/*****************************************************************
   Copyright 2006 by Tung Luong (lqtung@truthinet.com)

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
package com.inet.web.service.mail.utils;

import com.inet.base.service.FileService;

/**
 * DocumentFormatHelper
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: DocumentFormatHelper.java 2008-12-13 14:44:17z nguyen_dv $
 * 
 * Create date: Dec 13, 2008
 * <pre>
 *  Initialization DocumentFormatHelper class.
 * </pre>
 */
public abstract class DocumentFormatHelper {
	//Distance of OpenOffice and Microsoft Office
	public static final int NEXT_FORMAT				= 6;
	public static final int BOUND_CAN_EDIT			= 14;
	public static final int START_EDIT				= 0;
	
	//FORMAT OF OPENOFFICE DOCUMENT
	public static final int OPEN_OFFICE_WORD 		= 0;
	public static final int OPEN_OFFICE_EXCEL   	= 1;
	public static final int OPEN_OFFICE_POWERPOINT  = 2;
	public static final int OPEN_OFFICE_GRAPPH   	= 3;
	public static final int OPEN_OFFICE_MATH   		= 4;
	public static final int OPEN_OFFICE_DATABASE   	= 5;
	
	//FORMAT OF MICROSOFT OFFICE DOCUMENT
	public static final int MS_OFFICE_WORD			= 6;
	public static final int MS_OFFICE_EXCEL			= 7;
	public static final int MS_OFFICE_POWERPOINT	= 8;
	public static final int MS_OFFICE_ACCESS		= 9;
	public static final int MS_OFFICE_PROJECT		= 10;
	public static final int MS_OFFICE_VISIO			= 11;
	
	//FORMAT OF SOME DOCUMENT CAN EDIT
	public static final int MIC_PLAIN_TEXT			= 12;
	public static final int MIC_HTML				= 13;
	public static final int MIC_XML					= 14;
	
	//FORMAT OF SOME DOCUMENT ONLY VIEW
	public static final int START_VIEW				= 15;
	public static final int PDF						= 15;
	public static final int IMAGE					= 16;
	
	// FORMAT OF SOME DOCUMENT ONLY VIEW
	public static final int START_INET				= 17;
	public static final int INET_OFFICE				= 17;
	public static final int OTHER				   	= 18;
	
	public static final String[] DOCEDIT_FORMAT_ID = new String[]{
		"odt","ods","odp","odg","odf","odb","doc","xls","ppt","mdb","mpp","vsd","txt","html","xml"
	};
	public static final String[] DOCVIEW_FORMAT_ID = new String[]{
		"pdf","jpg","oth"
	};
	public static final String[] DOCALL_FORMAT_ID = new String[]{
		"odt","ods","odp","odg","odf","odb","doc","xls","ppt","mdb","mpp","vsd","txt","html","xml","pdf","jpg","dtt", "oth"
	};
	
	public static final String[] DOCEDIT_FORMAT_NAME = new String[]{
		"T\u00e0i li\u1ec7u v\u0103n b\u1ea3n(*.odt)",
		"B\u1ea3ng t\u00ednh(*.ods)",
		"Tr\u00ecnh chi\u1ebfu(*.odp)",
		"B\u1ed9 v\u1ebd \u0111\u1ed3 h\u1ecda(*.odg)",
		"T\u1ea1o c\u00f4ng th\u1ee9c to\u00e1n h\u1ecdc(*.odf)",
		"C\u01a1 s\u1edf d\u1eef li\u1ec7u(*.odb)",
		
		"Microsoft Office Word(*.doc)",
		"Microsoft Office Excel(*.xls)",
		"Microsoft Office PowerPoint(*.ppt)",
		"Microsoft Office Access(*.mdb)",
		"Microsoft Office Project(*.mpp)",
		"Microsoft Office Visio(*.vsd)",
		
		"Plain text(*.txt)",
		"HTML(*.html)",
		"XML(*.xml)"
	};
	
	public static final String[] DOCVIEW_FORMAT_NAME = new String[]{
		"T\u00e0i li\u1ec7u pdf(*.pdf)",
		"H\u00ecnh \u1ea3nh(*.jpg)"
	};
	
	public static final String[] DOCINET_FORMAT_NAME = new String[]{
		"Inet solutions office(*.dtt)"
	};
	
	public static final String[] DOCVIEW_ALL_FORMAT_NAME = new String[]{
		"T\u00e0i li\u1ec7u pdf(*.pdf)",
		"H\u00ecnh \u1ea3nh(*.jpg)",
		"Inet solutions office(*.dtt)"
	};
	
	public static final String[] DOCALL_FORMAT_NAME = new String[]{
		"T\u00e0i li\u1ec7u v\u0103n b\u1ea3n(*.odt)",
		"B\u1ea3ng t\u00ednh(*.ods)",
		"Tr\u00ecnh chi\u1ebfu(*.odp)",
		"B\u1ed9 v\u1ebd \u0111\u1ed3 h\u1ecda(*.odg)",
		"T\u1ea1o c\u00f4ng th\u1ee9c to\u00e1n h\u1ecdc(*.odf)",
		"C\u01a1 s\u1edf d\u1eef li\u1ec7u(*.odb)",
		
		"Microsoft Office Word(*.doc)",
		"Microsoft Office Excel(*.xls)",
		"Microsoft Office PowerPoint(*.ppt)",
		"Microsoft Office Access(*.mdb)",
		"Microsoft Office Project(*.mpp)",
		"Microsoft Office Visio(*.vsd)",
		
		"Plain text(*.txt)",
		"HTML(*.html)",
		"XML(*.xml)",
		
		"T\u00e0i li\u1ec7u pdf(*.pdf)",
		"H\u00ecnh \u1ea3nh(*.jpg)",
		
		"Inet solutions office(*.dtt)",
		
		"T\u1ea5t c\u1ea3 \u0111\u1ecbnh d\u1ea1ng(*.*)"
	};
		
	
	public static final String[] DOCEDIT_FORMAT_NAME_SEARCH = new String[]{"",
		"T\u00e0i li\u1ec7u v\u0103n b\u1ea3n(.odt)",
		"B\u1ea3ng t\u00ednh(.ods)",
		"Tr\u00ecnh chi\u1ebfu(.odp)",
		"B\u1ed9 v\u1ebd \u0111\u1ed3 h\u1ecda(.odg)",
		"T\u1ea1o c\u00f4ng th\u1ee9c to\u00e1n h\u1ecdc(.odf)",
		"C\u01a1 s\u1edf d\u1eef li\u1ec7u(.odb)",
		
		"Microsoft Office Word(*.doc)",
		"Microsoft Office Excel(*.xls)",
		"Microsoft Office PowerPoint(*.ppt)",
		"Microsoft Office Access(*.mdb)",
		"Microsoft Office Project(*.mpp)",
		"Microsoft Office Visio(*.vsd)",
		
		"Plain text(*.txt)",
		"HTML(*.html)",
		"XML(*.xml)"
	};
	
	public static final String[] DOCVIEW_FORMAT_NAME_SEARCH = new String[]{"",
		"T\u00e0i li\u1ec7u pdf",
		"H\u00ecnh \u1ea3nh(*.jpg)"
	};
	
	public static final String[] DOCINET_FORMAT_NAME__SEARCH = new String[]{"",
		"Inet solutions office(*.dtt)"
	};
	
	public static final String[] DOCALL_FORMAT_NAME_SEARCH = new String[]{"",
		"T\u00e0i li\u1ec7u v\u0103n b\u1ea3n(*.odt)",
		"B\u1ea3ng t\u00ednh(*.ods)",
		"Tr\u00ecnh chi\u1ebfu(*.odp)",
		"B\u1ed9 v\u1ebd \u0111\u1ed3 h\u1ecda(*.odg)",
		"T\u1ea1o c\u00f4ng th\u1ee9c to\u00e1n h\u1ecdc(*.odf)",
		"C\u01a1 s\u1edf d\u1eef li\u1ec7u(*.odb)",
		
		"Microsoft Office Word(*.doc)",
		"Microsoft Office Excel(*.xls)",
		"Microsoft Office PowerPoint(*.ppt)",
		"Microsoft Office Access(*.mdb)",
		"Microsoft Office Project(*.mpp)",
		"Microsoft Office Visio(*.vsd)",
		
		"Plain text(*.txt)",
		"HTML(*.html)",
		"XML(*.xml)",
		
		"T\u00e0i li\u1ec7u pdf(*.pdf)",
		"H\u00ecnh \u1ea3nh(*.jpg)",
		
		"iNet Solutions Office(*.dtt)",
		
		"T\u1ea5t c\u1ea3 \u0111\u1ecbnh d\u1ea1ng(*.*)"
	};
	
	
	public static final String[] FILTER_EXTENTION = new String[]{
		"*.odt","*.ods","*.odp","*.odg","*.odf","*.odb",
		"*.doc","*.xls","*.ppt","*.mdb","*.mpp","*.vsd",
		"*.txt","*.html","*.xml","*.*"
	};
	public static final String[] FILTER_NAME = new String[]{
		"T\u00e0i li\u1ec7u v\u0103n b\u1ea3n(*.odt)",
		"B\u1ea3ng t\u00ednh(*.ods)",
		"Tr\u00ecnh chi\u1ebfu(*.odp)",
		"B\u1ed9 v\u1ebd \u0111\u1ed3 h\u1ecda(*.odg)",
		"T\u1ea1o c\u00f4ng th\u1ee9c to\u00e1n h\u1ecdc(*.odf)",
		"C\u01a1 s\u1edf d\u1eef li\u1ec7u(*.odb)",
		
		"Microsoft Office Word(*.doc)",
		"Microsoft Office Excel(*.xls)",
		"Microsoft Office PowerPoint(*.ppt)",
		"Microsoft Office Access(*.mdb)",
		"Microsoft Office Project(*.mpp)",
		"Microsoft Office Visio(*.vsd)",
		
		"Plain Text(*.txt)",
		"HTML(*.html)",
		"XML(*.xml)",
		
		"T\u1ea5t c\u1ea3 \u0111\u1ecbnh d\u1ea1ng(*.*)"
	};
	
	public static final String[] PDF_FILTER_EXTENTION = new String[]{"*.pdf"};
	public static final String[] PDF_FILTER_NAME = new String[]{
		"T\u00e0i li\u1ec7u pdf(*.pdf)"};
	
	public static final String[] IMAGE_FILTER_EXTENTION = new String[] {
		"*.jpg", "*.gif", "*.png", "*.bmp", "*.ico", "*.tif"
	};
	public static final String[] IMAGE_FILTER_NAME = new String[] {
		"H\u00ecnh \u1ea3nh(*.jpg)",
		"Graphics Interchange Files(*.gif)",
		"Portable Network Grapphic Files(*.png)",
		"Bitmapped Graphics Files(*.bmp)",
		"Icon Files(*.ico)",
		"Tagged Image Files(*.tif)"
	};
	
	public static final String[] INET_FILTER_EXTENTION = new String[]{"*.dtt"};
	public static final String[] INET_FILTER_NAME = new String[]{
		"iNet Solutions Office(*.dtt)"};
	
	public static final String[] ALL_FILE_FILTER_EXTENTION = new String[]{"*.*"};
	public static final String[] ALL_FILE_FILTER_NAME = new String[]{
		"T\u1ea5t c\u1ea3 \u0111\u1ecbnh d\u1ea1ng(*.*)"};
	
	/**
	 * Get the format of file name or absolute file name  
	 * @param file
	 * @return int 
	 */
	public static final int getFormat(String file){
		if (file == null || file.length() < 3) return OTHER;
		String extention = file.substring(file.length() - 3) .toLowerCase();
		for(int i=0; i<DOCALL_FORMAT_ID.length;i++)
			if (DOCALL_FORMAT_ID[i].equals(extention)) return i;
		return OTHER;
	}
	
	/**
	 * Get the extension of file name or absolute file name  
	 * @param file
	 * @return Extension 
	 */
	public static final String getExtension(String file){
		return FileService.getExtension(file) ;
	}
	
	/**
	 * Get the extension of file name or absolute file name  
	 * @param file
	 * @return extension 
	 */
	public static final String getExtension(int format){
		//Check data
		if (format >= 0 && format < DocumentFormatHelper.DOCALL_FORMAT_ID.length){
			return DocumentFormatHelper.DOCALL_FORMAT_ID[format];
		}
		return null;		
	}
	
	/**
	 * Get the extension of file name or absolute file name  
	 * @param file
	 * @return extension 
	 */
	public static final String getExtensionName(int format){
		//Check data
		if (format >= 0 && format < DocumentFormatHelper.DOCALL_FORMAT_NAME.length){
			return DocumentFormatHelper.DOCALL_FORMAT_NAME[format];
		}
		return null;		
	}
	
	
	/**
	 * Change the file *.* to *.dtt
	 * @param file
	 * @return
	 */
	public static final String changeToDTT(String file){
		if(file == null) return null ;
		
		// file separator index.
		int lastIndex = file.lastIndexOf('.') ;
		if(file.lastIndexOf('.') > 0){
			return file.substring(0, lastIndex + 1) + "dtt" ;
		}
		
		return file + ".dtt" ;
	}
	
	/**
	 * Change format of file, *.* to *.[format]
	 * 
	 * @param file String - the given file name.
	 * @param format int - the given file format identifier.
	 * @return new file format.
	 */
	public static final String changeFormat(String file, int format){
		if(file == null) return null ;
		
		// file separator index.
		int lastIndex = file.lastIndexOf('.') ;
		if(file.lastIndexOf('.') > 0){
			return file.substring(0, lastIndex + 1) + DOCALL_FORMAT_ID[format] ;
		}
		
		return file + '.' + DOCALL_FORMAT_ID[format] ;
	}
	
	
	/**
	 * Get the filter extension of file
	 * if the file is original return *.dtt
	 * Otherwise return the *.[format]
	 * @param original
	 * @param format
	 * @return
	 */
	public static String[] getFilterExtensions(boolean original, int format) {
		String[] filterExt = null;
		if (! original || format == DocumentFormatHelper.INET_OFFICE ){
			filterExt =  DocumentFormatHelper.INET_FILTER_EXTENTION;
		} else {
			if(format == DocumentFormatHelper.PDF){ 
				filterExt = DocumentFormatHelper.PDF_FILTER_EXTENTION;
			} else if(format == DocumentFormatHelper.IMAGE){
				filterExt = DocumentFormatHelper.IMAGE_FILTER_EXTENTION;
			} else if(format == DocumentFormatHelper.OTHER){
				filterExt = DocumentFormatHelper.ALL_FILE_FILTER_EXTENTION;
			} else {
				filterExt = new String[]{DocumentFormatHelper.FILTER_EXTENTION[format]};
			}			
		}
		return filterExt;
	}
	
	/**
	 * Get the filter name of file 
	 * @param original
	 * @param format
	 * @return
	 */
	public static String[] getFilterNames(boolean original, int format){
		String[] filterNames = null;
		if (! original || format == DocumentFormatHelper.INET_OFFICE ){
			filterNames =  DocumentFormatHelper.INET_FILTER_NAME;
		} else {
			if(format == DocumentFormatHelper.PDF){ 
				filterNames = DocumentFormatHelper.PDF_FILTER_NAME;
			} else if(format == DocumentFormatHelper.IMAGE){
				filterNames = DocumentFormatHelper.IMAGE_FILTER_NAME;
			} else if(format == DocumentFormatHelper.OTHER){
				filterNames = DocumentFormatHelper.ALL_FILE_FILTER_NAME;
			} else {
				filterNames = new String[]{DocumentFormatHelper.FILTER_NAME[format]};
			}			
		}
		return filterNames;		
	}
	
	/**
	 * get the format of MSOFFICE
	 * @param formatOpenOffice
	 * @return
	 */
	public static int chageFormatToMSOffice(int formatOpenOffice){
		return formatOpenOffice + NEXT_FORMAT; 
	}
	
	/**
	 * get the format of open office
	 * @param formatMSOffice
	 * @return
	 */
	public static int chageFormatToOpenOffice(int formatMSOffice){
		return formatMSOffice - NEXT_FORMAT; 
	}
	
	/**
	 * Check document is OPENOFFICE, return true if it is OPENOFFICE
	 * Otherwise return false
	 * @param format
	 * @return
	 */
	public static boolean isOpenOffice(int format){
		return (format>=0 && format<=5);
	}
	
	/**
	 * Check document is MSOFFICE, return true if it is MSOFFICE
	 * Otherwise return false
	 * @param format
	 * @return
	 */
	public static boolean isMSOffice(int format){
		return (format>=6 && format<=11);
	}
	
	/**
	 * check this format can edit.
	 * if this document can edit with editor return true,
	 * otherwise return false (it mean this document is read only)
	 * @param format : the format of document
	 * @return
	 */
	public static boolean canEdit(int format){
		return (0<=format && format <=BOUND_CAN_EDIT);
	}
	
	/**
	 * check this format can edit.
	 * if this document can edit with editor return true,
	 * otherwise return false (it mean this document is read only)
	 * @param format : the string format of document
	 * @return
	 */
	public static boolean canEdit(String ext){
		int format = getFormat(ext);
		return (0<=format && format <=BOUND_CAN_EDIT);
	}
}
