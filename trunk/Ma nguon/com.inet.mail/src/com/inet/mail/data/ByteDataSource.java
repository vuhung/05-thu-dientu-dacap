/*****************************************************************
 Copyright 2007 by hiennguyen (hiennguyen@truthinet.com)

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
package com.inet.mail.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.activation.DataSource;
import javax.activation.FileTypeMap;

/**
 * ByteDataSource.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @version 1.0i
 * 
 * @date 26.12.2007
 * <pre>
 * 	Adds comment.
 * </pre>
 */
public class ByteDataSource implements DataSource, Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 391760060073767036L;
	// attachment data.
	private byte[] datasource;
	// attachment name.
	private String name;
	// file type map.
	private FileTypeMap fileTypeMap;
	
	//------------------------------------------------------------
	// class members.
	//	
	public ByteDataSource(byte[] datasource, String name){
		this.datasource = datasource;
		this.name = name;
	}
	
	/**
	 * Returns the content type of the data source
	 * 
	 * @see javax.activation.DataSource#getContentType()
	 */
	public String getContentType() {
		if (this.fileTypeMap == null) {
			return FileTypeMap.getDefaultFileTypeMap().getContentType(this.name);
		} else {
			return this.fileTypeMap.getContentType(this.name);
		}
	}

	/**
	 * @see javax.activation.DataSource#getInputStream()
	 */
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(this.datasource);
	}

	/**
	 * @see javax.activation.DataSource#getName()
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @see javax.activation.DataSource#getOutputStream()
	 */
	public OutputStream getOutputStream() throws IOException {
		OutputStream output = new ByteArrayOutputStream();
		output.write(this.datasource);
		
		return output;
	}
	
	/**
	 * Sets the FileTypeMap associated with the data source
	 * 
	 * @param fileTypeMap
	 */
	public void setFileTypeMap(FileTypeMap fileTypeMap) {
		this.fileTypeMap = fileTypeMap;
	}
}
