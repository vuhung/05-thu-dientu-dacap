/*****************************************************************
   Copyright 2006 by Tan Truong (tntan@truthinet.com.vn)

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
package com.inet.mail.exception;

import com.inet.base.exception.BaseRuntimeException;


/**
 * MailParserException.
 * 
 * @author <a href="mailto:tntan@truthinet.com.vn">Tan Truong</a>
 * @version $Id: MailParserException.java Feb 19, 2011 12:09:56 PM Tan Truong $
 * 
 * @since 1.0
 */
public class MailParserException extends BaseRuntimeException {
  // ~ Static fields =========================================================
  /* serialVersionUID */
  private static final long serialVersionUID = -1189876974329110257L;
  private String uuid;

  // ~ Constructors ==========================================================
  /**
   * Create MailParserException instance.
   */
  public MailParserException() {
  }

  /**
   * Create MailParserException instance.
   * 
   * @param msg String - the given error message.
   */
  public MailParserException(String msg) {
    super(msg);
  }

  /**
   * Create MailParserException instance.
   * 
   * @param uuid String - the given uuid message.
   * @param msg String - the given error message.
   */
  public MailParserException(String uuid, String msg) {
    super(msg);
    this.uuid = uuid;
  }
  
  /**
   * Create MailParserException instance.
   * 
   * @param throwable Throwable - the given throwable instance.
   */
  public MailParserException(Throwable throwable) {
    super(throwable);
  }

  /**
   * Create MailParserException instance.
   * 
   * @param msg String - the given error message.
   * @param throwable Throwable - the given throwable instance.
   */
  public MailParserException(String msg, Throwable throwable) {
    super(msg, throwable);
  }
  
  /**
   * Create MailParserException instance.
   * @param uuid String - the given uuid message throw error.
   * @param msg String - the given error message. 
   * @param throwable Throwable - the given throwable instance.
   */
  public MailParserException(String uuid, String msg, Throwable throwable) {
    super(msg, throwable);
    this.uuid = uuid;
  }

  /**
   * @return the uuid
   */
  public String getUuid() {
    return uuid;
  }

  /**
   * @param uuid the uuid to set
   */
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }
}
