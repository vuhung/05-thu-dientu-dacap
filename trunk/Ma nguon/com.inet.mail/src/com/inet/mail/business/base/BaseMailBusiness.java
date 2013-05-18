/*****************************************************************
   Copyright 2006 by Hien Nguyen (hiennguyen@truthinet.com)

   Licensed under the iNet SolutioAddressBookRemoteSLns Corp.,;
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.truthinet.com/licenses

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*****************************************************************/
package com.inet.mail.business.base;

import java.util.List;

import com.inet.base.ejb.exception.EJBException;

/**
 * BaseMailBusiness.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @author <a href="mailto:lqtung@truthinet.com">Tung Luong</a>
 * @version 0.2i
 * @param <T>
 */
public interface BaseMailBusiness<T> {
	/**
	 * Saves the T information to database.
	 * 
	 * @param attach T - the given object information.
	 * @throws EJBException
	 */
	public T save(T attach) throws EJBException;

	/**
	 * Delete the attachment from the given object identifier.
	 * 
	 * @param attachId int - the given object identifier.
	 * @throws EJBException
	 */
	public void delete(long bizId) throws EJBException;

	/**
	 * Updates the attachment from the given object information.
	 * 
	 * @param attach T - the given object information.
	 * @throws EJBException
	 */
	public T update(T attach) throws EJBException;

	/**
	 * Loads attachment information form the given object identifier.
	 * 
	 * @param attachId int - the given object identifier.
	 * @return T - the given object information.
	 * @throws EJBException
	 */
	public T load(long bizId) throws EJBException;
	
	/**
	 * Search and result the list of entity that match the given criteria.
	 * 
	 * @param search	T - the given criteria.
	 * @return List<T> - list of entity.
	 * @throws EjbException
	 */
	public List<T> query(T search) throws EJBException;
	
	/**
	 * Search and result the list of entity that match the given criteria.
	 * 
	 * @param search	T - the given criteria.
	 * @param startAt 	int - the given start item position.
	 * @param maxItem	int - the given max items.
	 * @return List<T> - list of entity.
	 * @throws EjbException
	 */
	public List<T> query(T search, int startAt, int maxItems) throws EJBException;
}
