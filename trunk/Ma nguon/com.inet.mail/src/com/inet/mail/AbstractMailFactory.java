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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.inet.mail.conf.beans.MailFactoryBeanDefinition;
import com.inet.mail.core.MailApplicationContext;
import com.inet.mail.core.UIDStore;
import com.inet.mail.data.MailFactoryType;
import com.inet.mail.data.MailProtocol;
import com.inet.mail.data.MailSecurity;
import com.inet.mail.exception.MailException;
import com.inet.mail.exception.MailParserException;
import com.inet.mail.parser.AbstractMessageFactory;
import com.inet.mail.parser.IMessageComposer;

/**
 * AbstractMailFactory
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: AbstractMailFactory.java 2008-12-11 22:09:05Z nguyen_dv $
 * 
 *          Create date: Dec 11, 2008
 * 
 *          <pre>
 *  Initialization AbstractMailFactory class.
 * </pre>
 */
public abstract class AbstractMailFactory implements IMailFactory {
  // ~ Static fields =========================================================
  /* The IP pattern. */
  private static final Pattern IP_PATTERN = 
      Pattern.compile("(\\d+{1,3})\\.(\\d+{1,3})\\.(\\d+{1,3})\\.(\\d+{1,3})");

  /* the mail factory bean definition. */
  private static MailFactoryBeanDefinition factoryBeanDefinition;

  /* the thread local. */
  private static ThreadLocal<Boolean> contextHolder = new ThreadLocal<Boolean>();

  // ~ Instance fields =======================================================
  /**
   * class logger.
   */
  protected final Logger logger = Logger.getLogger(getClass());

  /**
   * the mail security.
   */
  private MailSecurity security = MailSecurity.NONE;
  /**
   * the mail protocol.
   */
  private MailProtocol protocol = MailProtocol.SMTP;
  /**
   * The canonical host.
   */
  private String host;
  /**
   * the mail port.
   */
  private int port;
  /**
   * the mail user name.
   */
  private String username;
  /**
   * the mail password.
   */
  private String password;

  // ~ Constructors ==========================================================
  /**
   * Create SMTP mail factory.
   * 
   * @param host
   *          the given server host.
   * @throws MailException
   *           when an error occurs during creating MailFactory.
   */
  protected AbstractMailFactory(String host) throws MailException {
    this(MailSecurity.NONE, host, MailProtocol.SMTP.getPort());
  }

  /**
   * Create SMTP mail factory
   * 
   * @param host
   *          the given server host.
   * @param port
   *          the given server port.
   * 
   * @throws MailException
   *           when an error occurs during creating MailFactory.
   */
  protected AbstractMailFactory(String host, int port) throws MailException {
    this(MailSecurity.NONE, host, port);
  }

  /**
   * Create SMTP mail factory
   * 
   * @param security
   *          the given mail security.
   * @param host
   *          the given server host.
   * 
   * @throws MailException
   *           when an error occurs during creating MailFactory.
   */
  protected AbstractMailFactory(MailSecurity security, String host) throws MailException {
    this(security, host, MailProtocol.SMTP.getPort());
  }

  /**
   * Create SMTP mail factory
   * 
   * @param security
   *          the given mail security.
   * @param host
   *          the given server host.
   * @param port
   *          the given server port.
   * 
   * @throws MailException
   *           when an error occurs during creating MailFactory.
   */
  protected AbstractMailFactory(MailSecurity security, String host, int port) throws MailException {
    this(security, host, port, null, null);
  }

  /**
   * Create SMTP mail factory
   * 
   * @param security
   *          the given mail security.
   * @param host
   *          the given server host.
   * @param port
   *          the given server port.
   * @param username
   *          the given server user name.
   * @param password
   *          the given server user password.
   * 
   * @throws MailException
   *           when an error occurs during creating MailFactory.
   */
  protected AbstractMailFactory(MailSecurity security, String host, int port, String username,
      String password) throws MailException {
    // initialization data.
    setProtocol(MailProtocol.SMTP);
    setSecurity(security);
    setHost(host);
    setPort(port);
    setUsername(username);
    setPassword(password);

    postInitialization();
  }

  // ~ Methods ===============================================================
  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.IMailFactory#getMailFactory()
   */
  public MailFactoryType getMailFactory() {
    // get bean factory.
    MailFactoryBeanDefinition definition = getMailFactoryBeanDefinition();

    // return mail factory.
    return definition.getFactory();
  }

  /**
   * @return the message factory.
   */
  public AbstractMessageFactory getMessageFactory() throws MailException {
    return AbstractMessageFactory.getInstance();
  }

  /**
   * Create SMTP mail factory.
   * 
   * @param host
   *          the given server host.
   * @throws MailException
   *           when an error occurs during creating MailFactory.
   */
  public static AbstractMailFactory getInstance(final String host) throws MailException {
    // check security.
    if (System.getSecurityManager() == null) {
      return AbstractMailFactory.createInstance(host);
    }

    // running on security environment.
    return AccessController.doPrivileged(new PrivilegedAction<AbstractMailFactory>() {
      /**
       * @see java.security.PrivilegedAction#run()
       */
      public AbstractMailFactory run() {
        return AbstractMailFactory.createInstance(host);
      }
    });
  }

  /**
   * Create SMTP mail factory.
   * 
   * @param host
   *          the given server host.
   * @param port
   *          the given server port.
   * 
   * @throws MailException
   *           when an error occurs during creating MailFactory.
   */
  public static AbstractMailFactory getInstance(final String host, final int port)
      throws MailException {
    // check security.
    if (System.getSecurityManager() == null) {
      return AbstractMailFactory.createInstance(host, port);
    }

    // running on security environment.
    return AccessController.doPrivileged(new PrivilegedAction<AbstractMailFactory>() {
      /**
       * @see java.security.PrivilegedAction#run()
       */
      public AbstractMailFactory run() {
        return AbstractMailFactory.createInstance(host, port);
      }
    });
  }

  /**
   * Create SMTP mail factory.
   * 
   * @param security
   *          the given mail security.
   * @param host
   *          the given server host.
   * 
   * @throws MailException
   *           when an error occurs during creating MailFactory.
   */
  public static AbstractMailFactory getInstance(final MailSecurity security, final String host)
      throws MailException {
    // check security.
    if (System.getSecurityManager() == null) {
      return AbstractMailFactory.createInstance(security, host);
    }

    // running on security environment.
    return AccessController.doPrivileged(new PrivilegedAction<AbstractMailFactory>() {
      /**
       * @see java.security.PrivilegedAction#run()
       */
      public AbstractMailFactory run() {
        return AbstractMailFactory.createInstance(security, host);
      }
    });
  }

  /**
   * Create SMTP mail factory.
   * 
   * @param security
   *          the given mail security.
   * @param host
   *          the given server host.
   * @param port
   *          the given server port.
   * 
   * @throws MailException
   *           when an error occurs during creating MailFactory.
   */
  public static AbstractMailFactory getInstance(final MailSecurity security, final String host,
      final int port) throws MailException {
    // check security.
    if (System.getSecurityManager() == null) {
      return AbstractMailFactory.createInstance(security, host, port);
    }

    // running on security environment.
    return AccessController.doPrivileged(new PrivilegedAction<AbstractMailFactory>() {
      /**
       * @see java.security.PrivilegedAction#run()
       */
      public AbstractMailFactory run() {
        return AbstractMailFactory.createInstance(security, host, port);
      }
    });
  }

  /**
   * Create SMTP mail factory.
   * 
   * @param security
   *          the given mail security.
   * @param host
   *          the given server host.
   * @param port
   *          the given server port.
   * @param username
   *          the given server user name.
   * @param password
   *          the given server password.
   * 
   * @throws MailException
   *           when an error occurs during creating MailFactory.
   */
  public static AbstractMailFactory getInstance(final MailSecurity security, final String host,
      final int port, final String username, final String password) throws MailException {
    // check security.
    if (System.getSecurityManager() == null) {
      return AbstractMailFactory.createInstance(security, host, port, username, password);
    }

    // running on security environment.
    return AccessController.doPrivileged(new PrivilegedAction<AbstractMailFactory>() {
      /**
       * @see java.security.PrivilegedAction#run()
       */
      public AbstractMailFactory run() {
        return AbstractMailFactory.createInstance(security, host, port, username, password);
      }
    });
  }

  // ~ Class Methods =========================================================
  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.IMailFactory#getHost()
   */
  public String getHost() {
    return host;
  }

  /**
   * Set the server canonical host.
   * 
   * @param host
   *          the given canonical host.
   */
  protected void setHost(String host) {
    // get canonical host.
    String canonicalHost = validateHost(host);
    this.host = canonicalHost;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.IMailFactory#getPassword()
   */
  public String getPassword() {
    return password;
  }

  /**
   * Set the mail password.
   * 
   * @param password
   *          the given mail password.
   */
  protected void setPassword(String password) {
    this.password = password;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.IMailFactory#getPort()
   */
  public int getPort() {
    return port;
  }

  /**
   * Set the mail server port.
   * 
   * @param port
   *          the given server port.
   */
  protected void setPort(int port) {
    this.port = port;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.IMailFactory#getProtocol()
   */
  public MailProtocol getProtocol() {
    return protocol;
  }

  /**
   * Set the mail protocol.
   * 
   * @param protocol
   *          the given mail protocol.
   */
  protected void setProtocol(MailProtocol protocol) {
    this.protocol = protocol;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.IMailFactory#getSecurity()
   */
  public MailSecurity getSecurity() {
    return security;
  }

  /**
   * Set the mail security.
   * 
   * @param security
   *          the given mail security.
   */
  protected void setSecurity(MailSecurity security) {
    this.security = security;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.IMailFactory#getUsername()
   */
  public String getUsername() {
    return username;
  }

  /**
   * Set the mail user name.
   * 
   * @param username
   *          the given mail user name.
   */
  protected void setUsername(String username) {
    this.username = username;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.IMailFactory#getSession()
   */
  public Object getSession() {
    return null;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.IMailFactory#fetch(com.inet.mail.core.UIDStore, boolean)
   */
  public List<IMessageComposer> fetch(UIDStore store, boolean removed) throws MailException,
      MailParserException {
    return fetch(null, store, removed);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.IMailFactory#getMessage(java.lang.String, boolean,
   *      boolean)
   */
  public IMessageComposer getMessage(String uid, boolean fetchBody, boolean fetchAttach)
      throws MailException {
    return getMessage(null, uid, fetchBody, fetchAttach);
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.IMailFactory#fetch(int, com.inet.mail.core.UIDStore,
   *      boolean)
   */
  public List<IMessageComposer> fetch(int maxItems, UIDStore store, boolean removed)
      throws MailException, MailParserException {
    return fetch(null, maxItems, store, removed);
  }

  /**
   * Check the given host is validate or not.
   * 
   * @param host
   *          String - the given host.
   * @return the canonical host name.
   * @throws MailException
   *           if an error occurs during being validate host.
   */
  protected String validateHost(String host) throws MailException {
    // create matcher.
    Matcher matcher = IP_PATTERN.matcher(host);

    try {
      // get inet address.
      InetAddress address = null;
      if (matcher.find()) {
        address = InetAddress.getByAddress(new byte[] { (byte) Integer.parseInt(matcher.group(1)),
            (byte) Integer.parseInt(matcher.group(2)), (byte) Integer.parseInt(matcher.group(3)),
            (byte) Integer.parseInt(matcher.group(4)) });
      } else {
        address = InetAddress.getByName(host);
      }

      // return the canonical host name.
      return address.getCanonicalHostName();
    } catch (UnknownHostException uhex) {
      // throw exception.
      throw new MailException(uhex.getMessage(), uhex);
    }
  }

  /**
   * Post initialization.
   */
  protected abstract void postInitialization();

  // ~ Helper methods ========================================================
  /**
   * Create SMTP mail factory.
   * 
   * @param host
   *          the given server host.
   * @throws MailException
   *           when an error occurs during creating MailFactory.
   */
  private static AbstractMailFactory createInstance(String host) throws MailException {
    try {
      // get MailFactoryBeanDefinition instance.
      MailFactoryBeanDefinition definition = getMailFactoryBeanDefinition();

      // get the constructor.
      Constructor<AbstractMailFactory> constructor = definition.createMailFactoryConstructorWithH();

      // return the MailFactory.
      return constructor.newInstance(host);
    } catch (InstantiationException iex) {
      // throw exception.
      throw new MailException(iex.getMessage(), iex);
    } catch (IllegalAccessException iaex) {
      // throw exception.
      throw new MailException(iaex.getMessage(), iaex);
    } catch (SecurityException sex) {
      // throw exception.
      throw new MailException(sex.getMessage(), sex);
    } catch (IllegalArgumentException iaex) {
      // throw exception.
      throw new MailException(iaex.getMessage(), iaex);
    } catch (InvocationTargetException itex) {
      // throw exception.
      throw new MailException(itex.getMessage(), itex);
    }
  }

  /**
   * Create SMTP mail factory.
   * 
   * @param host
   *          the given server host.
   * @param port
   *          the given server port.
   * @throws MailException
   *           when an error occurs during creating MailFactory.
   */
  private static AbstractMailFactory createInstance(String host, int port) throws MailException {
    try {
      // get MailFactoryBeanDefinition instance.
      MailFactoryBeanDefinition definition = getMailFactoryBeanDefinition();

      // get the constructor.
      Constructor<AbstractMailFactory> constructor = definition
          .createMailFactoryConstructorWithHP();

      // return the MailFactory.
      return constructor.newInstance(host, port);
    } catch (InstantiationException iex) {
      // throw exception.
      throw new MailException(iex.getMessage(), iex);
    } catch (IllegalAccessException iaex) {
      // throw exception.
      throw new MailException(iaex.getMessage(), iaex);
    } catch (SecurityException sex) {
      // throw exception.
      throw new MailException(sex.getMessage(), sex);
    } catch (IllegalArgumentException iaex) {
      // throw exception.
      throw new MailException(iaex.getMessage(), iaex);
    } catch (InvocationTargetException itex) {
      // throw exception.
      throw new MailException(itex.getMessage(), itex);
    }
  }

  /**
   * Create SMTP mail factory.
   * 
   * @param security
   *          the given mail security.
   * @param host
   *          the given server host.
   * @throws MailException
   *           when an error occurs during creating MailFactory.
   */
  private static AbstractMailFactory createInstance(MailSecurity security, String host)
      throws MailException {
    try {
      // get MailFactoryBeanDefinition instance.
      MailFactoryBeanDefinition definition = getMailFactoryBeanDefinition();

      // get the constructor.
      Constructor<AbstractMailFactory> constructor = definition
          .createMailFactoryConstructorWithSH();

      // return the MailFactory.
      return constructor.newInstance(security, host);
    } catch (InstantiationException iex) {
      // throw exception.
      throw new MailException(iex.getMessage(), iex);
    } catch (IllegalAccessException iaex) {
      // throw exception.
      throw new MailException(iaex.getMessage(), iaex);
    } catch (SecurityException sex) {
      // throw exception.
      throw new MailException(sex.getMessage(), sex);
    } catch (IllegalArgumentException iaex) {
      // throw exception.
      throw new MailException(iaex.getMessage(), iaex);
    } catch (InvocationTargetException itex) {
      // throw exception.
      throw new MailException(itex.getMessage(), itex);
    }
  }

  /**
   * Create SMTP mail factory.
   * 
   * @param security
   *          the given mail security.
   * @param host
   *          the given server host.
   * @param port
   *          the given server port.
   * @throws MailException
   *           when an error occurs during creating MailFactory.
   */
  private static AbstractMailFactory createInstance(MailSecurity security, String host, int port)
      throws MailException {
    try {
      // get MailFactoryBeanDefinition instance.
      MailFactoryBeanDefinition definition = getMailFactoryBeanDefinition();

      // get the constructor.
      Constructor<AbstractMailFactory> constructor = definition
          .createMailFactoryConstructorWithSHP();

      // return the MailFactory.
      return constructor.newInstance(security, host, port);
    } catch (InstantiationException iex) {
      // throw exception.
      throw new MailException(iex.getMessage(), iex);
    } catch (IllegalAccessException iaex) {
      // throw exception.
      throw new MailException(iaex.getMessage(), iaex);
    } catch (SecurityException sex) {
      // throw exception.
      throw new MailException(sex.getMessage(), sex);
    } catch (IllegalArgumentException iaex) {
      // throw exception.
      throw new MailException(iaex.getMessage(), iaex);
    } catch (InvocationTargetException itex) {
      // throw exception.
      throw new MailException(itex.getMessage(), itex);
    }
  }

  /**
   * Create SMTP mail factory.
   * 
   * @param security
   *          the given mail security.
   * @param host
   *          the given server host.
   * @param port
   *          the given server port.
   * @param username
   *          the given SMTP user name.
   * @param password
   *          the given SMTP password.
   * 
   * @throws MailException
   *           when an error occurs during creating MailFactory.
   */
  private static AbstractMailFactory createInstance(MailSecurity security, String host, int port,
      String username, String password) throws MailException {
    try {
      // get MailFactoryBeanDefinition instance.
      MailFactoryBeanDefinition definition = getMailFactoryBeanDefinition();

      // get the constructor.
      Constructor<AbstractMailFactory> constructor = definition
          .createMailFactoryConstructorWithSHPUN();

      // return the MailFactory.
      return constructor.newInstance(security, host, port, username, password);
    } catch (InstantiationException iex) {
      // throw exception.
      throw new MailException(iex.getMessage(), iex);
    } catch (IllegalAccessException iaex) {
      // throw exception.
      throw new MailException(iaex.getMessage(), iaex);
    } catch (SecurityException sex) {
      // throw exception.
      throw new MailException(sex.getMessage(), sex);
    } catch (IllegalArgumentException iaex) {
      // throw exception.
      throw new MailException(iaex.getMessage(), iaex);
    } catch (InvocationTargetException itex) {
      // throw exception.
      throw new MailException(itex.getMessage(), itex);
    }
  }

  /**
   * @return the {@link MailFactoryBeanDefinition} instance.
   * 
   * @throws MailException
   *           when error occurs during getting
   *           {@link MailFactoryBeanDefinition} instance.
   */
  private static MailFactoryBeanDefinition getMailFactoryBeanDefinition() throws MailException {
    if (contextHolder.get() == null) {
      synchronized (contextHolder) {
        if (factoryBeanDefinition == null) {
          // mail application context.
          MailApplicationContext context = MailApplicationContext.getInstance();
          if (context == null || context.getContext() == null) {
            throw new MailException("The WebOSApplicationContext must be set.");
          }

          factoryBeanDefinition = context.getContext().getBean(
              MailFactoryBeanDefinition.MAIL_FACTORY_BEAN_DEFINITION_NAME,
              MailFactoryBeanDefinition.class);
        }

        contextHolder.set(Boolean.TRUE);
      }
    }

    // the MailFactoryBeanDefinition bean.
    return factoryBeanDefinition;
  }
}
