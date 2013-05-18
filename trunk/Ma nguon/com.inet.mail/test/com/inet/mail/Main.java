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
package com.inet.mail;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import com.inet.base.service.IOService;
import com.inet.mail.conf.beans.PopBeanDefinition;
import com.inet.mail.core.MailApplicationContext;
import com.inet.mail.core.UIDStore;
import com.inet.mail.data.Address;
import com.inet.mail.data.MailProtocol;
import com.inet.mail.parser.IMessageComposer;
import com.inet.mail.parser.MessageHeader;

/**
 * Main
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: Main.java Dec 8, 2008 nguyen_dv $
 * 
 * Create date: Dec 8, 2008
 * 
 * <pre>
 *  Initialization Main class.
 * </pre>
 */
public class Main {
  /**
   * Build the main action.
   * 
   * @param args the given parameters.
   */
  public static void main(String[] args) throws Exception {
    parseMail("/Users/dungnguyen/Desktop/mail.eml");
  }

  /**
   * Parse mail from the given mail file.
   * 
   * @param mailPath the given mail path.
   * @throws Exception if any error occurs during parsing the mail.
   */
  protected static void parseMail(String mailPath) throws Exception {
    // read mail data.
    byte[] mailData = IOService.readToBytes(new File(mailPath));

    // create connect to server from the default setting.
    AbstractMailFactory mailFactory = MailConfigureFactory.createFactory();

    // set message.
    mailFactory.connect();

    // create composer.
    IMessageComposer composer = mailFactory.createMessage(mailData, true, false);

    // show address.
    System.out.println(composer.getSubject());

    List<Address> addresses = composer.getTo();
    if (addresses != null && addresses.size() > 0) {
      final Iterator<Address> iterator = addresses.iterator();
      while (iterator.hasNext()) {
        System.out.println(iterator.next().toUnicodeString());
      }
    }

    // get CC address.
    addresses = composer.getCC();
    if (addresses != null && addresses.size() > 0) {
      final Iterator<Address> iterator = addresses.iterator();
      while (iterator.hasNext()) {
        System.out.println(iterator.next().toUnicodeString());
      }
    }

    System.out.println(composer.getBody());
    IOService.writeFile("/Users/dungnguyen/Desktop/mail1.html", composer.getBody().getBytes("UTF-8"));
  }

  /**
   * Fetches all mails from the configuration.
   * 
   * @throws Exception if any error occurs during fetching the mail.
   */
  protected static void fetchMail() throws Exception {
    // create connect to server from the default setting.
    AbstractMailFactory mailFactory = MailConfigureFactory.createFactory();

    // get pop3 configuration.
    MailApplicationContext context = MailApplicationContext.getInstance();
    if (context == null || context.getContext() == null)
      return;

    // get pop3 configuration.
    PopBeanDefinition popDefinition = context.getContext().getBean(
        PopBeanDefinition.POP_BEAN_DEFINITION_NAME, PopBeanDefinition.class);

    // connect to pop3.
    mailFactory.connect(MailProtocol.POP3, popDefinition.getSecurity(), popDefinition.getHost(),
        popDefinition.getPort(), popDefinition.getUsername(), popDefinition.getPassword());

    // create store.
    UIDStore store = new UIDStore();
    UIDStore newStore = (UIDStore) store.clone();

    // get message.
    int count = mailFactory.count(null, newStore);

    // show information.
    System.out.println("Thread: [" + Thread.currentThread().getId() + "] new message: [" + count
        + "]");
    for (int index = 0; index < count; index++) {
      // fetch message.
      List<IMessageComposer> composers = mailFactory.fetch(1, store, false);

      // get the composer.
      IMessageComposer composer = composers.get(0);

      // message header.
      MessageHeader header = composer.getHeader();
      if (header.isSpam()) {
        System.err.println("Mail [" + index + "] is SPAM: [" + header + "]");
      } else {
        System.out.println("Mail [" + index + "] is NOT SPAM: [" + header + "]");
      }
    }

    // close the connection.
    mailFactory.close();
  }
}
