/**
 * @author tntan
 */
(function() {
  //~ Defined MailService class ====================================
  MailService = {};
  
  //~ Setting up create parameter ===========================================
  
  //~ Defined shortcut function =============================================
  
  //~ Setting up the mailservice =================================
  Ext.apply(MailService, {
    getCssAttachment: function(ext) {
      var __ext = ext.toLowerCase();
      if (__ext == 'avi' || __ext == 'wmv') {
        return 'icon-email-attach-avi';
      }
      else 
        if (__ext == 'bmp' || __ext == 'png') {
          return 'icon-email-attach-bmp';
        }
        else 
          if (__ext == 'doc') {
            return 'icon-email-attach-doc';
          }
          else 
            if (__ext == 'xls') {
              return 'icon-email-attach-xls';
            }
            else 
              if (__ext == 'exe') {
                return 'icon-email-attach-exe';
              }
              else 
                if (__ext == 'gif') {
                  return 'icon-email-attach-gif';
                }
                else 
                  if (__ext == 'html') {
                    return 'icon-email-attach-html';
                  }
                  else 
                    if (__ext == 'jpg') {
                      return 'icon-email-attach-jpg';
                    }
                    else 
                      if (__ext == 'mdb') {
                        return 'icon-email-attach-mdb';
                      }
                      else 
                        if (__ext == 'mp3' || __ext == 'wma') {
                          return 'icon-email-attach-mp3';
                        }
                        else 
                          if (__ext == 'mpg') {
                            return 'icon-email-attach-mpg';
                          }
                          else 
                            if (__ext == 'mpp') {
                              return 'icon-email-attach-mpp';
                            }
                            else 
                              if (__ext == 'odb') {
                                return 'icon-email-attach-odb';
                              }
                              else 
                                if (__ext == 'odg') {
                                  return 'icon-email-attach-odg';
                                }
                                else 
                                  if (__ext == 'odp') {
                                    return 'icon-email-attach-odp';
                                  }
                                  else 
                                    if (__ext == 'ods') {
                                      return 'icon-email-attach-ods';
                                    }
                                    else 
                                      if (__ext == 'odt') {
                                        return 'icon-email-attach-odt';
                                      }
                                      else 
                                        if (__ext == 'pdf') {
                                          return 'icon-email-attach-pdf';
                                        }
                                        else 
                                          if (__ext == 'ppt') {
                                            return 'icon-email-attach-ppt';
                                          }
                                          else 
                                            if (__ext == 'rar') {
                                              return 'icon-email-attach-rar';
                                            }
                                            else 
                                              if (__ext == 'swf') {
                                                return 'icon-email-attach-swf';
                                              }
                                              else 
                                                if (__ext == 'txt') {
                                                  return 'icon-email-attach-txt';
                                                }
                                                else 
                                                  if (__ext == 'vsd') {
                                                    return 'icon-email-attach-vsd';
                                                  }
                                                  else 
                                                    if (__ext == 'xml') {
                                                      return 'icon-email-attach-xml';
                                                    }
                                                    else 
                                                      if (__ext == 'zip' || __ext == 'tar' ||
                                                      __ext == 'tar.bz2' ||
                                                      __ext == 'tar.gz' ||
                                                      __ext == 'tar.lzma' ||
                                                      __ext == 'cbz' ||
                                                      __ext == 'lzma' ||
                                                      __ext == 'gz') {
                                                        return 'icon-email-attach-zip';
                                                      }
                                                      else 
                                                        if (__ext == 'eml') {
                                                          return 'icon-email-attach-eml';
                                                        }
      
      return 'icon-email-attach-unknown';
    },
    
    download: function(hid, name, fkey, ext) {
      if (hid == 0) {
        MailService.downloadReviewAttach(name, fkey);
      }
      else {
        MailService.downloadAttach(hid, fkey, ext);
      }
    },
    
    downloadAttach: function(contentId, fkey, ext) {
      if (contentId == 0) 
        return;
      
      var __ext = ext || '';
      __ext = __ext.toLowerCase();
      if (__ext == 'eml') {
        MailService.openEml(contentId, fkey);
      }
      else {
        var __form = document.getElementById('viewdownloadAttach_');
        __form.action = 'mail-download-file';
        __form.contentId.value = contentId;
        __form.fkey.value = unescape(fkey);
        __form.style.value = '';
        __form.target = '';
        __form.type.value = 'ORIGINAL';
        __form.bean.value = 'mailBean';
        
        // submit form.
        __form.submit();
      }
    },
    
    downloadReviewAttach: function(name, fkey) {
      var __form = document.getElementById('viewdownload_');
      __form.action = 'mail-download-file';
      __form.name.value = unescape(name);
      __form.code.value = unescape(fkey);
      __form.bean.value = 'mailReviewBean';
      
      // submit form.
      __form.submit();
    },
    
    downloadEmlAttach: function(contentId, eml, fkey, ext) {
      var __form = document.getElementById('viewdownloadAttach_');
      __form.action = 'mail-download-file';
      __form.contentId.value = contentId;
      __form.fkey.value = unescape(fkey);
      __form.emlkey.value = unescape(eml);
      __form.style.value = '';
      __form.target = '';
      __form.type.value = 'ORIGINAL';
      __form.bean.value = 'mailEmlBean';
      
      // submit form.
      __form.submit();
    },
    
    
    openEml: function(contentId, fkey) {
    
      var __handlerFn = function(response, option) {
      
        if (!response) 
          return;
        
        var __result = eval('(' + response.responseText + ')');
        
        var __success = (__result.success == undefined);
        if (!__success) 
          return;
        
        var __mainTab = Ext.getCmp('main-tabs');
        var __tab = Ext.getCmp('mail-reader');
        
        if (!__tab) {
          __tab = new iNet.iwebos.ui.mail.MailReader();
          __mainTab.add(__tab).show();
        }
        
        __mainTab.setActiveTab(__tab);
        
        // set content id
        __result.contentId = contentId;
        __result.emlKey = fkey;
        __tab.loadEmlInfo(__result);
      };
      
      var __baseParams = {
        iwct: 'mailContent',
        iwm: 'MAIL',
        iwc: 'READ_WRITE',
        iwa: 'openEml',
        action: 'openEml',
        contentId: contentId,
        fkey: unescape(fkey)
      };
      // start icon loading
      iNet.Ajax.request({
        url: 'jsonajax',
        params: __baseParams,
        scope: this,
        method: 'POST',
        success: __handlerFn,
        failure: function(response, options) {
        }
      });
    },
    
    showError: function(content) {
      Ext.MessageBox.show({
        title: iwebos.message.mail.announcement,
        msg: content,
        buttons: Ext.MessageBox.OK,
        icon: Ext.MessageBox.ERROR
      });
    },
    
    showInfo: function(content) {
      Ext.MessageBox.show({
        title: iwebos.message.mail.announcement,
        msg: content,
        buttons: Ext.MessageBox.OK,
        icon: Ext.MessageBox.INFO
      });
    },
    
    /**
     * stop fetch mail
     */
    stopFetch: function() {
      var __okHandler = function(btn) {
        if (btn === 'ok' || btn === 'yes') {
          var __panel = Ext.getCmp('main-tabs');
          __panel.stopFetchMail();
        }
      };
      // show confirmation.
      Ext.MessageBox.confirm(iwebos.message.mail.announcement, iwebos.message.mail.confirm_stop_fetch, __okHandler);
    },
    
    /**
     * find tree node from given target
     */
    findTreeNodeByTarget: function(parent, target) {
      // check parent node
      if (parent.getUI().getTextEl() == target) {
        return parent;
      }
      
      // check node has child
      if (parent.firstChild != null) {
        var __children = parent.childNodes;
        var __node = null;
        for (var __index = 0; __index < __children.length; __index++) {
          __node = MailService.findTreeNodeByTarget(__children[__index], target);
          if (__node != null) 
            return __node;
        }
      }
      else {
        return null;
      }
      
      return null;
    },
    
    /**
     * find record by id
     * @param {Store} store
     * @param {ind} id
     * @return {Record}
     */
    findById: function(store, id) {
      for (var __index = 0; __index < store.getCount(); __index++) {
        var __record = store.getAt(__index);
        if (__record.data.id === id) 
          return __record;
      }
      
      return null;
    },
    
    /**
     * Add spam
     */
    addSpam: function(folderId, records) {
      // load spam
      var __handlerFn = function(response, options) {
        var __result = Ext.util.JSON.decode(response.responseText);
        if (__result == undefined || __result.rows == undefined) {
          return;
        }
        var __spams = __result.rows;
        // check spam exist
        var __checkSpamExist = function(email, units) {
          var __unit = {};
          for (var i = 0; i < units.length; i++) {
            __unit = units[i]
            if (email == __unit.data && __unit.object == 'SENDER' && __unit.operator == 'CONTAINS') {
              return true;
            }
          }
          return false;
        };
        
        var __getEmailAddress = function(fullAdd) {
          var __email = "";
          
          var __start = fullAdd.lastIndexOf('&lt');
          var __end = fullAdd.lastIndexOf('&gt');
          if (__start > 0 && __end > 0) {
            __email = fullAdd.substring(__start + 4, __end);
          }
          else {
            __email = fullAdd;
          }
          return __email;
        };
        // find filter has name SPAM
        var __spam = null;
        for (var i = 0; i < __spams.length; i++) {
        
          if (__spams[i].name === 'Spam') {
            __spam = __spams[i];
            break;
          }
        }
        
        if (__spam == null) {
          __spam = {
            id: 0,
            name: 'Spam',
            fId: folderId
          };
          __spam.clause = {
            operator: 'OR',
            units: []
          };
        }
        
        
        // add new spam			
        var __units = __spam.clause.units || [];
        for (var __index = 0; __index < records.length; __index++) {
          var __email = __getEmailAddress(records[__index].data.sender);
          if (__email != undefined && __email.trim() != '') {
            if (!__checkSpamExist(__email, __units)) {
              __units[__units.length] = {
                object: 'SENDER',
                operator: 'CONTAINS',
                data: __email
              };
            }
          }
        }
        __spam['clause'] = {
          operator: 'OR',
          units: __units
        };
        
        // base param
        var __param = {
          iwct: 'mailFilterContent',
          iwm: 'MAIL',
          iwc: 'READ_WRITE',
          iwa: 'save',
          action: 'save'
        };
        
        __param['object'] = Ext.util.JSON.encode(__spam);
        
        // send request to server.
        iNet.Ajax.request({
          url: 'jsonajax',
          params: __param,
          scope: this,
          method: 'POST',
          success: function(response, request) {
          },
          failure: function(response, request) {
          }
        });
      };
      
      var __baseParams = {
        iwct: 'loadMailFilterContent',
        iwm: 'MAIL',
        iwc: 'READ_ONLY',
        iwa: 'search',
        action: 'search'
      };
      
      // send request to server.
      iNet.Ajax.request({
        url: 'jsonajax',
        params: __baseParams,
        scope: this,
        method: 'POST',
        success: __handlerFn,
        failure: function(response, request) {
        }
      });
    },
    
    removeSpam: function(records) {
      // load spam
      var __handlerFn = function(response, options) {
        var __result = Ext.util.JSON.decode(response.responseText);
        if (__result == undefined || __result.rows == undefined) {
          return;
        }
        var __spams = __result.rows;
        // check spam exist
        var __removeSpamExist = function(email, units) {
          var __temp = [];
          var __unit = {};
          for (var i = 0; i < units.length; i++) {
            __unit = units[i]
            if (email == __unit.data && __unit.object == 'SENDER' && __unit.operator == 'CONTAINS') {
              continue;
            }
            else {
              __temp[__temp.length] = __unit;
            }
          }
          return __temp;
        };
        
        var __getEmailAddress = function(fullAdd) {
          var __email = "";
          
          var __start = fullAdd.lastIndexOf('&lt');
          var __end = fullAdd.lastIndexOf('&gt');
          if (__start > 0 && __end > 0) {
            __email = fullAdd.substring(__start + 4, __end);
          }
          else {
            __email = fullAdd;
          }
          return __email;
        };
        // find filter has name SPAM
        var __spam = null;
        for (var i = 0; i < __spams.length; i++) {
          if (__spams[i].name == 'Spam') {
            __spam = __spams[i];
            break;
          }
        }
        
        if (__spam == null) {
          return;
        }
        
        
        // remove spam			
        var __units = __spam.clause.units || [];
        for (var __index = 0; __index < records.length; __index++) {
          var __email = __getEmailAddress(records[__index].data.sender);
          __units = __removeSpamExist(__email, __units);
        }
        
        if (__units.length > 0) {
          __spam['clause'] = {
            operator: 'OR',
            units: __units
          };
          
          // base param
          var __param = {
            iwct: 'mailFilterContent',
            iwm: 'MAIL',
            iwc: 'READ_WRITE',
            iwa: 'save',
            action: 'save'
          };
          
          __param['object'] = Ext.util.JSON.encode(__spam);
          
          // send request to server.
          iNet.Ajax.request({
            url: 'jsonajax',
            params: __param,
            scope: this,
            method: 'POST',
            success: function(response, request) {
            },
            failure: function(response, request) {
            }
          });
        }
        else {
          var __param = {
            iwct: 'mailFilterContent',
            iwm: 'MAIL',
            iwc: 'READ_WRITE',
            iwa: 'delete',
            action: 'delete',
            id: __spam.id
          };
          
          // send request to server.
          iNet.Ajax.request({
            url: 'jsonajax',
            params: __param,
            scope: this,
            method: 'POST',
            success: function(response, request) {
            },
            failure: function(response, request) {
            }
          });
        }
        
      };
      
      var __baseParams = {
        iwct: 'loadMailFilterContent',
        iwm: 'MAIL',
        iwc: 'READ_ONLY',
        iwa: 'search',
        action: 'search'
      };
      
      // send request to server.
      iNet.Ajax.request({
        url: 'jsonajax',
        params: __baseParams,
        scope: this,
        method: 'POST',
        success: __handlerFn,
        failure: function(response, request) {
        }
      });
    },
    
    escape: function(data) {
      var attachments = data.attachments;
      if (!!attachments && attachments.length > 0) {
        for (var i = 0; i < attachments.length; i++) {
          attachments[i].fkey = escape(attachments[i].fkey);
        }
        data.attachments = attachments;
      }
      //body
      data.body = data.body.replace(/<javascript/g, '<javascriptss');
      data.body = data.body.replace(/<JAVASCRIPT/g, '<JAVASCRIPTSS');
      data.body = data.body.replace(/<style/g, '<styless');
      data.body = data.body.replace(/<STYLE/g, '<STYLESS');
      return data;
    }
  });
})();
