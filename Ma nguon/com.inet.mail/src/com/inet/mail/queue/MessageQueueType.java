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
package com.inet.mail.queue;

/**
 * 
 * MessageQueueType.
 * 
 * @author <a href="mailto:hiennguyen@truthinet.com">Hien Nguyen</a>
 * @author <a href="mailto:lqtung@truthinet.com">Tung Luong</a>
 * @version 0.2i
 */
public interface MessageQueueType {
	public final String QUEUE_TYPE = "javax.jms.Queue";
	public final String QUEUE_MESSAGE_DELIVERY = "queue/iwebos/message/delivery";
	public final String QUEUE_MESSAGE_ASYNCHRONIZE = "queue/iwebos/message/asynchronize";
}
