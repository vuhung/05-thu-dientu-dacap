/***
 Copyright 2008 by Nguyen Hoang Tu (nhtu@truthinet.com.vn)
 Licensed under the iNet Solutions Corp.,;
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.truthinet.com/licenses
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
Ext.app.SearchField = Ext.extend(Ext.form.TwinTriggerField, {
  initComponent: function() {
    this._limit = 0;
    Ext.app.SearchField.superclass.initComponent.call(this);
    this.on('specialkey', function(f, e) {
      if (e.getKey() == e.ENTER) {
        this.onTrigger2Click();
      }
    }, this);
  },
  validationEvent: false,
  validateOnBlur: false,
  trigger1Class: 'x-form-clear-trigger',
  trigger2Class: 'x-form-search-trigger',
  hideTrigger1: true,
  hasSearch: false,
  paramName: 'key',
  onTrigger1Click: function() {
    if (this.hasSearch) {
      this.el.dom.value = '';
      var o = {
        start: 0
      };
      this.store.baseParams = this.store.baseParams || {};
      this.store.baseParams[this.paramName] = '';
      this.store.reload({
        params: o
      });
      this.triggers[0].hide();
      this.hasSearch = false;
    }
  },
  
  afterRender: function() {
    Ext.app.SearchField.superclass.afterRender.call(this);
    
    // correct top position.
    if (Ext.isIE && /msie 8/.test(navigator.userAgent.toLowerCase())) {
      this.el.setStyle("top", "1px");
    }
  },
  
  /**
   *
   * @param {Object} domain
   */
  setData: function(domain) {
    this.domain = domain;
  },
  
  /**
   * Set limit character to search
   *
   * @param {} number - the number character
   */
  setLimitSearchCharacter: function(number) {
    this._limit = number;
  },
  
  /**
   * get domain
   */
  canSearch: function() {
    return (this.domain != null && this.domain != undefined);
  }
});
