package com.inet.mail.data;

import java.io.Serializable;

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

/**
 * @author hiennguyen
 * 
 */
public class TopicPostDTO implements Serializable, Comparable<TopicPostDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7665198944604762005L;
	private String author;
	private int number;

	public TopicPostDTO(String author,int number) {
		this.author = author;
		this.number = number;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode(){
		return author.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj){
		if (obj instanceof TopicPostDTO)
			return author.equals(((TopicPostDTO)obj).author);
		
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(TopicPostDTO obj) {
		return (-1) * ((Integer)number).compareTo(obj.number);
	}

}
