/*****************************************************************
 Copyright 2007 by Hien Nguyen (hiennguyen@truthinet.com)

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
package com.inet.mail.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.inet.base.ejb.persistence.BasePersistence;

/**
 * AddressBookInfo.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @author <a href="mailto:lqtung@truthinet.com">Tung Luong</a>
 * @version 0.2i
 */
@Entity
@Table(name = "MAIL_ADDRESS_BOOK_INFO")
@NamedQueries(value = {
		@NamedQuery(name ="AddressBookInfo.getAddressBookInfo",query = "SELECT abInfo FROM AddressBookInfo abInfo WHERE abInfo.owner = :owner AND abInfo.emailAddress = :emailAddress"),
		@NamedQuery(name ="AddressBookInfo.getAllUserContact",query = "SELECT abInfo FROM AddressBookInfo abInfo WHERE abInfo.owner = :owner"),
		@NamedQuery(name ="AddressBookInfo.searchContact",query = "SELECT abInfo FROM AddressBookInfo abInfo WHERE abInfo.owner = :owner AND ( abInfo.fullName LIKE :fullName OR abInfo.emailAddress LIKE :email )"),
		@NamedQuery(name ="AddressBookInfo.getAllGroup",query = "SELECT DISTINCT(abInfo.groupName) FROM AddressBookInfo abInfo WHERE abInfo.owner = :owner"),
		@NamedQuery(name ="AddressBookInfo.searchContactWithGroup",query = "SELECT abInfo FROM AddressBookInfo abInfo WHERE abInfo.owner = :owner AND abInfo.groupName = :groupName AND ( abInfo.fullName LIKE :fullName OR abInfo.emailAddress LIKE :email )"),
		@NamedQuery(name ="AddressBookInfo.getAll",query = "SELECT abInfo FROM AddressBookInfo abInfo")
})
public class AddressBookInfo extends BasePersistence<Long> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8987441061016796273L;
	private String groupName;
	private String firstName;
	private String fullName;
	private String lastName;
	private String department;
	private String emailAddress;
	private String postalAddress;
	private String city;
	private String district;
	private String workNumber;
	private String faxNumber;
	private String mobileNumber;
	private String homeNumber;
	private String pagerNumber;
	private String displayName;
	
	private Long owner;
	
	public AddressBookInfo()
	{		
	}
	
	/**
	 * Get the identifier of AddressBookInfo
	 * @return
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	public Long getId() {
		return id;
	}

	/**
	 * Get the city of user in address book
	 * @return
	 */
	@Column(length = 48)
	public String getCity() {
		return this.city;
	}

	/**
	 * Get the owner this address
	 * @return
	 */
	public Long getOwner() {
		return owner;
	}

	/**
	 * Set the owner this address
	 * @param owner
	 */
	public void setOwner(Long owner) {
		this.owner = owner;
	}

	/**
	 * Set the city of user in address book
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Get the department
	 * @return
	 */
	@Column(length = 128)
	public String getDepartment() {
		return this.department;
	}

	/**
	 * Set the department
	 * @param department
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * Get district 
	 * @return
	 */
	@Column(length = 48)
	public String getDistrict() {
		return this.district;
	}

	/**
	 * Set the district
	 * @param district
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 * Get email address
	 * @return
	 */
	@Column(length = 128)
	public String getEmailAddress() {
		return this.emailAddress;
	}

	/**
	 * Set the email address
	 * @param emailAddress
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * Get fax-number
	 * @return
	 */
	@Column(length = 16)
	public String getFaxNumber() {
		return this.faxNumber;
	}

	/**
	 * Set fax-number
	 * @param faxNumber
	 */
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	/**
	 * Get first name
	 * @return
	 */
	@Column(length = 32)
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Set first name
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Get group name
	 * @return
	 */
	@Column(length = 64)
	public String getGroupName() {
		return this.groupName;
	}

	/**
	 * Set group name
	 * @param groupName
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * Get home phone
	 * @return
	 */
	@Column(length = 16)
	public String getHomeNumber() {
		return this.homeNumber;
	}

	/**
	 * Set home phone
	 * @param homeNumber
	 */
	public void setHomeNumber(String homeNumber) {
		this.homeNumber = homeNumber;
	}

	/**
	 * Get last name
	 * @return
	 */
	@Column(length = 32)
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Set last name
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Get full name
	 * @return
	 */
	@Column(length = 32)
	public String getFullName() {
		return this.fullName;
	}

	/**
	 * Set full name
	 * @param fullName
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Get mobile number
	 * @return
	 */
	@Column(length = 16)
	public String getMobileNumber() {
		return this.mobileNumber;
	}

	/**
	 * Set mobile number
	 * @param mobileNumber
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * Get paper number
	 * @return
	 */
	@Column(length = 16)
	public String getPagerNumber() {
		return this.pagerNumber;
	}

	/**
	 * Set paper number
	 * @param pageNumber
	 */
	public void setPagerNumber(String pageNumber) {
		this.pagerNumber = pageNumber;
	}

	/**
	 * Get work phone
	 * @return
	 */
	@Column(length = 16)
	public String getWorkNumber() {
		return this.workNumber;
	}

	/**
	 * Set work phone
	 * @param phoneNumber
	 */
	public void setWorkNumber(String phoneNumber) {
		this.workNumber = phoneNumber;
	}

	/**
	 * Get postal address
	 * @return
	 */
	@Column(length = 256)
	public String getPostalAddress() {
		return this.postalAddress;
	}

	/**
	 * Set postal address
	 * @param postalAddress
	 */
	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}

	/**
	 * Get display name
	 * @return
	 */
	@Column(length = 32)
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Set display name
	 * @param displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}
