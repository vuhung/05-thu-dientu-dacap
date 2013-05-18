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
/**
 * @class DocumentFormat
 * @extends Object.
 * 
 * support control document format.
 */
iNet.DocumentFormat = {
	/**
	 * distance of openoffice and msoffice.
	 */
	NEXT_FORMAT: 6,
	/**
	 * OpenOffice Word
	 */
	OO_WORD: 0 ,
	/**
	 * OpenOffice Excel.
	 */
	OO_EXCEL: 1,
	/**
	 * OpenOffice Power Point.
	 */
	OO_POWERPOINT: 2,
	/**
	 * OpenOffice Graph.
	 */
	OO_GRAPH: 3,
	/**
	 * OpenOffice Math.
	 */
	OO_MATH: 4,
	/**
	 * OpenOffice Database.
	 */
	OO_DATABASE: 5,
	/**
	 * Microsoft Word.
	 */
	MS_WORD: 6,
	/**
	 * Microsoft Excel.
	 */
	MS_EXCEL: 7,
	/**
	 * Microsoft Power Point.
	 */
	MS_POWERPOINT: 8,
	/**
	 * Microsoft Access.
	 */
	MS_ACCESS: 9,
	/**
	 * Microsoft Project.
	 */
	MS_PROJECT: 10,
	/**
	 * Microsoft Visio.
	 */
	MS_VISIO: 11,
	/**
	 * Text document.
	 */
	DOC_TEXT: 12,
	/**
	 * HTML document.
	 */
	DOC_HTML: 13,
	/**
	 * XML document.
	 */
	DOC_XML: 14,
	/**
	 * PDF document.
	 */
	DOC_PDF: 15,
	/**
	 * Image document.
	 */
	DOC_IMG: 16,
	/**
	 * iNet document.
	 */
	DOC_INET: 17,
	/**
	 * Other document
	 */
	DOC_OTHER: 18,
	/**
	 * Document format
	 */
	FORMAT:['oo-word','oo-excel','oo-powerpoint','oo-graph','oo-math','oo-database','ms-word','ms-excel','ms-powerpoint','ms-access','ms-project','ms-visio','doc-text','doc-html','doc-xml','doc-pdf','doc-img','doc-inet','doc-other'],
	/**
	 * Document extension format.
	 */
	EXT_FORMAT:['odt','ods','odp','odg', 'odf', 'odb', 'doc', 'xls', 'ppt', 'mdb', 'mpp', 'vsd', 'txt', 'html', 'xml', 'pdf', 'jpg', 'dtt', 'oth'],
	/**
	 * Document format name.
	 */
	FORMAT_NAME:[
		'Open Office Word',
		'Open Office Excel',
		'Open Office Powerpoint',
		'Open Office Graph',
		'Open Office Math',
		'Open Office Database',
		'Microsoft Word',
		'Microsoft Excel',
		'Microsoft Powerpoint',
		'Microsoft Access',
		'Microsoft Project',
		'Microsoft Visio',
		'Text Document',
		'HTML Document',
		'XML Document',
		'PDF Document',
		'Image Document',
		'iNet Document',
		'Others'
	],
	
	/**
	 * Document status
	 */
	STATUS_NAME:[
	     iwebos.message.doc.status.update,
	     iwebos.message.doc.status.review,
	     iwebos.message.doc.status.publish,
	     iwebos.message.doc.status.notuse
	],
	/**
	 * @return <code>true</code> if the given format is open office document, otherwise return <code>false</code>
	 */
	isOpenOffice:function(format){
		return (format >= 0) && (format <= 5) ;
	},
	
	/**
	 * @return <code>true</code> if the given format is microsoft office document, otherwise reuturn <code>false</code>
	 */
	isMSOffice:function(format){
		return (format >= 6) && (format <= 11) ;
	},
	
	/**
	 * @return the file extension.
	 */
	getExtension : function(file){
		if(file == undefined || file == '') return '' ;
		var position = file.lastIndexOf('.') ;
		if(position == -1) return '' ;
		
		// get file extension.
		return file.substr(position + 1, file.length);
	},
	
	/**
	 * @return the format value.
	 */
	getFormat : function(extension){
		for(var index = 0; index < this.EXT_FORMAT.length; index++){
			if(extension == this.EXT_FORMAT[index]) return index ;
		}
		
		// return document other.
		return this.DOC_OTHER ;
	}
};
