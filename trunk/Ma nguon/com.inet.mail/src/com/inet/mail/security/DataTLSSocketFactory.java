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
package com.inet.mail.security;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocketFactory;

import com.inet.base.ssl.AbstractSecureProtocolSocketFactory;
import com.inet.base.ssl.SecureService;

/**
 * DataTLSSocketFactory
 *
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version 1.0i
 * 
 * @date Feb 1, 2008
 * <pre>
 *  Initialization DataTLSSocketFactory class.
 * </pre>
 */
public class DataTLSSocketFactory extends SSLSocketFactory {
	/**
	 * Class name.
	 */
	public static final String CLASSNAME = "com.inet.mail.security.DataTLSSocketFactory" ;

	/**
	 * the socket factory.
	 */
	private AbstractSecureProtocolSocketFactory socketFactory ;

	/**
	 * Create DataSSLSocketFactory instance.
	 */
	public DataTLSSocketFactory(){
		try{
			this.socketFactory = AbstractSecureProtocolSocketFactory.getInstance(SecureService.TLS) ;
		}catch(RuntimeException brex){
			// throw exception.
			System.err.println("*****ERROR [" + DataTLSSocketFactory.class.getSimpleName() + "] " + brex.getMessage()) ;
		}
	}
	
	/**
	 * @see javax.net.ssl.SSLSocketFactory#getDefaultCipherSuites()
	 */
	public String[] getDefaultCipherSuites() {
		return this.socketFactory.getDefaultCipherSuites();
	}

	/**
	 * @see javax.net.ssl.SSLSocketFactory#getSupportedCipherSuites()
	 */
	public String[] getSupportedCipherSuites() {
		return this.socketFactory.getSupportedCipherSuites();
	}
	
	/**
	 * @see javax.net.SocketFactory#createSocket()
	 */
	public Socket createSocket() throws IOException{
		return this.socketFactory.createSocket() ;
	}
	
	/**
	 * @see javax.net.SocketFactory#createSocket(java.lang.String, int)
	 */
	public Socket createSocket(String host, int port) throws IOException,
			UnknownHostException {
		return this.socketFactory.createSocket(host, port);
	}

	/**
	 * @see javax.net.SocketFactory#createSocket(java.net.InetAddress, int)
	 */
	public Socket createSocket(InetAddress host, int port) throws IOException {
		return this.socketFactory.createSocket(host.getCanonicalHostName(), port);
	}

	/**
	 * @see javax.net.SocketFactory#createSocket(java.lang.String, int, java.net.InetAddress, int)
	 */
	public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort)
			throws IOException, UnknownHostException {
		return this.socketFactory.createSocket(host, port, clientHost, clientPort);
	}

	/**
	 * @see javax.net.SocketFactory#createSocket(java.net.InetAddress, int, java.net.InetAddress, int)
	 */
	public Socket createSocket(InetAddress host, int port, InetAddress clientHost,
			int clientPort) throws IOException {
		return this.socketFactory.createSocket(host, port, clientHost, clientPort);
	}
	
	/**
	 * @see javax.net.ssl.SSLSocketFactory#createSocket(java.net.Socket, java.lang.String, int, boolean)
	 */
	public Socket createSocket(Socket s, String host, int port,
			boolean autoClose) throws IOException {
		return this.socketFactory.createSocket(s, host, port, autoClose);
	}	
}
