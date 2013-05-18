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
package com.inet.mail.supports;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.inet.base.concurrent.CollectionFactory;
import com.inet.base.concurrent.ConcurrentMap;
import com.inet.base.jsr305.concurrent.ThreadSafe;
import com.inet.mail.PoolObject;
import com.inet.mail.PoolObjectManager;
import com.inet.mail.exception.PoolObjectException;

/**
 * PoolObjectManagerSupport
 * 
 * @author <a href="mailto:dungnguyen@truthinet.com">Dung Nguyen</a>
 * @version $Id: PoolObjectManagerSupport.java 2009-01-13 17:51:46z nguyen_dv $
 * 
 *          Create date: Jan 13, 2009
 * 
 *          <pre>
 *  Initialization PoolObjectManagerSupport class.
 * </pre>
 */
@ThreadSafe
public class PoolObjectManagerSupport<T> implements PoolObjectManager<T> {
  // ~ Instance fields =======================================================
  /* class logger. */
  protected final Logger logger = Logger.getLogger(getClass());

  /* executor service. */
  private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

  /* concurrent hash map. */
  private final ConcurrentMap concurrentMap = CollectionFactory.createConcurrentMap(16);

  /* context holder. */
  private final ThreadLocal<Boolean> contextHolder = new ThreadLocal<Boolean>();

  /* system is running. */
  private boolean starting = false;

  // ~ Methods ===============================================================

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.PoolObjectManager#get(java.lang.String)
   */
  @SuppressWarnings({ "unchecked" })
  public PoolObject<T> get(String name) {
    PoolObject<T> object = (PoolObject<T>) concurrentMap.get(name);

    // return pool object.
    if (object != null) {
      object.markIsUsed();
    }

    return object;
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.PoolObjectManager#put(com.inet.mail.PoolObject)
   */
  @SuppressWarnings({ "unchecked" })
  public void put(PoolObject<T> poolObject) {
    poolObject.reset();

    // put to holder.
    PoolObject<T> another = (PoolObject<T>) concurrentMap.putIfAbsent(poolObject.getName(),
        poolObject);
    if (another != null && !another.equals(poolObject)) {
      poolObject.dispose();
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.PoolObjectManager#start()
   */
  public void start() throws PoolObjectException {
    if (contextHolder.get() == null) {
      synchronized (this) {
        if (!starting) {
          try {
            executorService.scheduleAtFixedRate(new Runnable() {
              /**
               * {@inheritDoc}
               * 
               * @see java.lang.Runnable#run()
               */
              @SuppressWarnings({ "unchecked" })
              public void run() {
                Collection<PoolObject<T>> objects = concurrentMap.values();
                List<PoolObject<T>> removes = new ArrayList<PoolObject<T>>();

                // collect used object.
                if (objects != null && objects.size() > 0) {
                  Iterator<PoolObject<T>> iterator = objects.iterator();
                  while (iterator.hasNext() && starting) {
                    PoolObject<T> object = iterator.next();
                    if (!object.isUsed() && expire(object)) {
                      removes.add(object);
                    }
                  }
                }

                // remove unused object.
                if (starting && removes.size() > 0) {
                  for (PoolObject<T> object : removes) {
                    if (!object.isUsed()) {
                      if (concurrentMap.remove(object.getName(), object)) {
                        object.dispose();
                      }
                    }
                  }

                  // clear remove list.
                  removes.clear();
                }
              }

              /**
               * @return if the given object is expire.
               */
              protected boolean expire(PoolObject<T> object) {
                return ((System.currentTimeMillis() - object.getTimestamp()) > 10000);
              }
            }, 10, 10, TimeUnit.SECONDS);
          } catch (Exception ex) {
            logger.warn("could not start the PoolObjectManager, message: [" + ex.getMessage()
                + "]");
          }

          starting = true;
        }
      }

      // initialization successful.
      contextHolder.set(Boolean.TRUE);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see com.inet.mail.PoolObjectManager#stop()
   */
  @SuppressWarnings({ "unchecked" })
  public void stop() throws PoolObjectException {
    starting = false;

    // stopping schedule.
    try {
      if (executorService != null)
        executorService.shutdown();

      // disposed all object.
      Collection<PoolObject<T>> objects = concurrentMap.values();
      if (objects != null) {
        Iterator<PoolObject<T>> iterator = objects.iterator();
        while (iterator.hasNext()) {
          PoolObject<T> object = iterator.next();
          object.dispose();
        }
      }

      // clear concurrent map.
      concurrentMap.clear();
    } catch (Exception ex) {
      logger.warn("could not stop the PoolObjectManager, message: [" + ex.getMessage() + "]");
    }
  }
}
