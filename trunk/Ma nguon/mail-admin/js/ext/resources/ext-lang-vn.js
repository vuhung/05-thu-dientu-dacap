/*****************************************************************
   Copyright 2008 by Nguyen Thanh Vy (ntvy@truthinet.com.vn)

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
Ext.UpdateManager.defaults.indicatorText = '<div class="loading-indicator">Đang xử lý...</div>';

if(Ext.View){
   Ext.View.prototype.emptyText = "";
}

if(Ext.grid.Grid){
   Ext.grid.Grid.prototype.ddText = "{0} dòng được chọn";
}

if(Ext.TabPanelItem){
   Ext.TabPanelItem.prototype.closeText = "Đóng thẻ này";
}

if(Ext.form.Field){
   Ext.form.Field.prototype.invalidText = "Giá trị của ô này không hợp lệ.";
}

if(Ext.LoadMask){
    Ext.LoadMask.prototype.msg = "Đang xử lý...";
}

Date.monthNames = [
   "Tháng 1",
   "Tháng 2",
   "Tháng 3",
   "Tháng 4",
   "Tháng 5",
   "Tháng 6",
   "Tháng 7",
   "Tháng 8",
   "Tháng 9",
   "Tháng 10",
   "Tháng 11",
   "Tháng 12"
];

Date.dayNames = [
   "Chủ nhật",
   "Thứ hai",
   "Thứ ba",
   "Thứ tư",
   "Thứ năm",
   "Thứ sáu",
   "Thứ bảy"
];

if(Ext.MessageBox){
   Ext.MessageBox.buttonText = {
      ok     : "Đồng ý",
      cancel : "Hủy bỏ",
      yes    : "Có",
      no     : "Không"
   };
}

if(Ext.util.Format){
   Ext.util.Format.date = function(v, format){
      if(!v) return "";
      if(!(v instanceof Date)) v = new Date(Date.parse(v));
      return v.dateFormat(format || "d/m/Y");
   };
}

if(Ext.DatePicker){
   Ext.apply(Ext.DatePicker.prototype, {
      todayText         : "Hôm nay",
      minText           : "Ngày này nhỏ hơn ngày nhỏ nhất",
      maxText           : "Ngày này lớn hơn ngày lớn nhất",
      disabledDaysText  : "",
      disabledDatesText : "",
      monthNames	: Date.monthNames,
      dayNames		: Date.dayNames,
      nextText          : 'Tháng sau (Control+Right)',
      prevText          : 'Tháng trước (Control+Left)',
      monthYearText     : 'Chọn một tháng (Control+Up/Down để thay đổi năm)',
      todayTip          : "{0} (Spacebar - Phím trắng)",
      format            : "d/m/y"
   });
}

if(Ext.PagingToolbar){
   Ext.apply(Ext.PagingToolbar.prototype, {
      beforePageText : "Trang",
      afterPageText  : "của {0}",
      firstText      : "Trang đầu",
      prevText       : "Trang trước",
      nextText       : "Trang sau",
      lastText       : "Trang cuối",
      refreshText    : "Tải lại",
      displayMsg     : "Hiển thị {0} - {1} của {2}",
      emptyMsg       : 'Không có dữ liệu để hiển thị'
   });
}

if(Ext.form.TextField){
   Ext.apply(Ext.form.TextField.prototype, {
      minLengthText : "Chiều dài tối thiểu của ô này là {0}",
      maxLengthText : "Chiều dài tối đa của ô này là {0}",
      blankText     : "Ô này cần phải nhập giá trị",
      regexText     : "",
      emptyText     : null
   });
}

if(Ext.form.NumberField){
   Ext.apply(Ext.form.NumberField.prototype, {
      minText : "Giá trị nhỏ nhất của ô này là {0}",
      maxText : "Giá trị lớn nhất của ô này là  {0}",
      nanText : "{0} không phải là một số hợp lệ"
   });
}

if(Ext.form.DateField){
   Ext.apply(Ext.form.DateField.prototype, {
      disabledDaysText  : "Vô hiệu",
      disabledDatesText : "Vô hiệu",
      minText           : "Ngày nhập trong ô này phải sau ngày {0}",
      maxText           : "Ngày nhập trong ô này phải trước ngày {0}",
      invalidText       : "{0} không phải là một ngày hợp lệ - phải có dạng {1}",
      format            : "d/m/y"
   });
}

if(Ext.form.ComboBox){
   Ext.apply(Ext.form.ComboBox.prototype, {
      loadingText       : "Đang xử lý...",
      valueNotFoundText : undefined
   });
}

if(Ext.form.VTypes){
   Ext.apply(Ext.form.VTypes, {
      emailText    : 'Giá trị của ô này phải là một địa chỉ email có dạng như "info@truthinet.com.vn"',
      urlText      : 'Giá trị của ô này phải là một địa chỉ web(URL) hợp lệ, có dạng như "http:/'+'/www.truthinet.com.vn"',
      alphaText    : 'Ô này chỉ được nhập các kí tự và gạch dưới(_)',
      alphanumText : 'Ô này chỉ được nhập các kí tự, số và gạch dưới(_)'
   });
}

if(Ext.grid.GridView){
   Ext.apply(Ext.grid.GridView.prototype, {
      sortAscText  : "Tăng dần",
      sortDescText : "Giảm dần",
      lockText     : "Khóa cột",
      unlockText   : "Bỏ khóa cột",
      columnsText  : "Các cột"
   });
}

if(Ext.grid.PropertyColumnModel){
   Ext.apply(Ext.grid.PropertyColumnModel.prototype, {
      nameText   : "Tên",
      valueText  : "Giá trị",
      dateFormat : "j/m/Y"
   });
}

if(Ext.layout.BorderLayout.SplitRegion){
   Ext.apply(Ext.layout.BorderLayout.SplitRegion.prototype, {
      splitTip            : "Kéo giữ chuột để thay đổi kích thước.",
      collapsibleSplitTip : "Kéo giữ chuột để thay đổi kích thước. Nhấp đúp để ẩn đi."
   });
}

// idesk.webos.message
idesk.webos.message.navigate.first="Trang đầu";
idesk.webos.message.navigate.prev="Trang trước";
idesk.webos.message.navigate.next="Trang kế";
idesk.webos.message.navigate.last="Trang cuối";

// idesk.webos.message.navigate
idesk.webos.message.language = "Ngôn ngữ...";
idesk.webos.message.load_data = "Đang xử lý...";

// idesk.webos.message.paperwork
idesk.webos.message.paperwork.function_title = "Chức năng";
idesk.webos.message.paperwork.report_title = "Báo cáo";

idesk.webos.message.paperwork.title = "HỒ SƠ CÔNG VĂN";

idesk.webos.message.paperwork.notbook='Chưa vào sổ';
idesk.webos.message.paperwork.booked='Đã vào sổ';

idesk.webos.message.paperwork.record = "HỒ SƠ";
idesk.webos.message.paperwork.record_fr = "Tiếp nhận";
idesk.webos.message.paperwork.record_fd = "Ban hành";
idesk.webos.message.paperwork.infor="Thông tin chung";
idesk.webos.message.paperwork.content="Nội dung toàn văn";
idesk.webos.message.paperwork.record_rin = "Sổ công văn đến";
idesk.webos.message.paperwork.record_rout = "Sổ công văn đi";
idesk.webos.message.paperwork.indoc = "Văn bản đến";
idesk.webos.message.paperwork.outdoc = "Văn bản đi";
idesk.webos.message.paperwork.print='In sổ';
idesk.webos.message.paperwork.process='Xử lý';
idesk.webos.message.paperwork.processtip='Chuyển sang xử lý';

idesk.webos.message.paperwork.processdocin='Xử lý công văn đến';
idesk.webos.message.paperwork.processpaperwork='Xử lý hồ sơ công việc';
idesk.webos.message.paperwork.processtask='Xử lý công việc';


idesk.webos.message.paperwork.search_header='Thông tin chi tiết' ;
idesk.webos.message.paperwork.search_title = "Tìm kiếm";
idesk.webos.message.paperwork.search_basic = "Tìm kiếm cơ bản";
idesk.webos.message.paperwork.search_more = "Tìm kiếm nâng cao";

idesk.webos.message.paperwork.publishdoc='Phát hành công văn';
idesk.webos.message.paperwork.toolbar_sendout='Gửi CQ ngoài';
idesk.webos.message.paperwork.toolbar_sendouttip='Gửi cơ quan ngoài';
idesk.webos.message.paperwork.toolbar_sendin='Gửi CQ nội bộ';
idesk.webos.message.paperwork.toolbar_sendintip='Gửi cơ quan nội bộ';
idesk.webos.message.paperwork.toolbar_doctransfer='Chuyển';
idesk.webos.message.paperwork.toolbar_doctransfertip='Chuyển công văn đi xử lý';
idesk.webos.message.paperwork.toolbar_combine='Phối hợp';
idesk.webos.message.paperwork.toolbar_combinetip='Phối hợp chia sẻ công văn cho người khác';
idesk.webos.message.paperwork.toolbar_catvb='Cất Vb';
idesk.webos.message.paperwork.toolbar_catvbtip='Cất văn bản vào sổ';

idesk.webos.message.paperwork.receivedoc='Tiếp nhận công văn';
idesk.webos.message.paperwork.newdoc='Tạo VBĐT';
idesk.webos.message.paperwork.newdoctip='Tạo văn bản điện tử';
idesk.webos.message.paperwork.transferdoc='Chuyển VBBH';
idesk.webos.message.paperwork.transferdoctip='Chuyển văn bản sang ban hành';
idesk.webos.message.paperwork.newdocdelete='Xóa VBĐT';
idesk.webos.message.paperwork.newdocdeletetip='Xoá văn bản điện tử';

idesk.webos.message.paperwork.dialog_list= 'Danh sách đơn vị';
idesk.webos.message.paperwork.dialog_columnname="Tên đơn vị";
idesk.webos.message.paperwork.dialog_receiveinfo="Thông tin đơn vị nhận";
idesk.webos.message.paperwork.dialog_selectlist= 'Chọn đơn vị';
idesk.webos.message.paperwork.dialog_ttitle= 'Chọn người dùng';
idesk.webos.message.paperwork.dialog_btitle= 'Chọn người dùng/nhóm người dùng.';
idesk.webos.message.paperwork.dialog_rtitle= 'Tất cả các cấp';
idesk.webos.message.paperwork.dialog_ok='Chấp nhận';
idesk.webos.message.paperwork.dialog_cancel='Bỏ qua';
idesk.webos.message.paperwork.dialog_uldgtitle='Các cấp trong hệ thống';
idesk.webos.message.paperwork.dialog_print='Điều kiện in sổ';

idesk.webos.message.paperwork.save="Lưu";
idesk.webos.message.paperwork.savetip="Lưu văn bản";

//form infor
idesk.webos.message.paperwork.eddesc='Trích yếu';
idesk.webos.message.paperwork.edgroup='Thuộc nhóm';
idesk.webos.message.paperwork.edbook='Sổ văn bản';
idesk.webos.message.paperwork.edurgent='Độ khẩn';
idesk.webos.message.paperwork.edsymbol='Số/ký hiệu VB';
idesk.webos.message.paperwork.edname='Tên văn bản';
idesk.webos.message.paperwork.edtype='Loại văn bản';
idesk.webos.message.paperwork.edstatus='Tình trạng';
idesk.webos.message.paperwork.edsecurity='Đô mật';
idesk.webos.message.paperwork.edscope='Lĩnh vực';
idesk.webos.message.paperwork.eddate='Ngày văn bản';
idesk.webos.message.paperwork.level_publish_agency='Cấp CQBH';
idesk.webos.message.paperwork.publish_agency='CQ ban hành';
idesk.webos.message.paperwork.formatsend='Hình thức gửi';
idesk.webos.message.paperwork.keyword='Từ khóa';
idesk.webos.message.paperwork.edsigner='Người ký';
idesk.webos.message.paperwork.edposition='Chức vụ';
idesk.webos.message.paperwork.eddateof='Ngày đến';
idesk.webos.message.paperwork.edexpiry_date='Ngày hết hạn';
idesk.webos.message.paperwork.sdate='';
idesk.webos.message.paperwork.edate='Đến ngày';

idesk.webos.message.paperwork.edinbook='Sổ CV đến';
idesk.webos.message.paperwork.edinnumber='Số đến';
idesk.webos.message.paperwork.fnumber='Từ số';
idesk.webos.message.paperwork.tnumber='Đến số';
idesk.webos.message.paperwork.typebook='Loại sổ';
idesk.webos.message.paperwork.receiveb='Đơn vị nhận';
idesk.webos.message.paperwork.ednote='Ghi chú';
/****************************Xu ly******************/
idesk.webos.message.paperwork.know='Để biết';
idesk.webos.message.paperwork.process_success='Xử lý xong';
idesk.webos.message.paperwork.create_ppw='Lập HSCV';
idesk.webos.message.paperwork.create_ppwtip='Lập hồ sơ công việc';
idesk.webos.message.paperwork.linked_ppw='Gắn vào HSCV';
idesk.webos.message.paperwork.linked_ppwtip='Gắn vào hồ sơ công việc';
idesk.webos.message.paperwork.linked_notification='Gắn vào HSHB';
idesk.webos.message.paperwork.linked_notificationtip='Gắn vào hồ sơ hồi báo';
idesk.webos.message.paperwork.edimportant='Độ quan trọng';
idesk.webos.message.paperwork.edperson='Người gửi';
idesk.webos.message.paperwork.ppdelete="Xoá";
idesk.webos.message.paperwork.ppdeletetip="Xoá hồ sơ công việc ";
idesk.webos.message.paperwork.pfinish='Hoàn thành';
idesk.webos.message.paperwork.predoc='Công văn tạm hoàn thành';

/***************************Theo doi****************************/
idesk.webos.message.paperwork.widname='Theo dõi công văn đến';
idesk.webos.message.paperwork.wodname='Theo dõi công văn đi';
idesk.webos.message.paperwork.wpublish='Xuất bản';
/********************context menu Co*******************************/
idesk.webos.message.paperwork.followup='Bật cờ';
idesk.webos.message.paperwork.today='Hôm nay';
idesk.webos.message.paperwork.tomorrow='Ngày mai';
idesk.webos.message.paperwork.weekin='Tuần này';
idesk.webos.message.paperwork.weeknext='Tuần tới';
idesk.webos.message.paperwork.weekunavailable='Không xác định';

/*****************Grouping item*************************/
idesk.webos.message.paperwork.editem='Công văn';
idesk.webos.message.paperwork.ppwitem='Công việc ';
idesk.webos.message.paperwork.task = 'Công việc' ;

idesk.webos.message.paperwork.emptyresult='Không có dữ liệu để hiển thị';
/**
 * Su dung resource iwebos.
 */
/******************************************************************/
//iwebos.message.paperwork
iwebos.message.paperwork.today='Hôm nay';
iwebos.message.paperwork.yesterday='Hôm qua';
iwebos.message.paperwork.currentweek='Tuần này';
iwebos.message.paperwork.lastweek='Tuần trước';
iwebos.message.paperwork.lastmonth='Tháng trước';
iwebos.message.paperwork.currentmonth='Tháng này';
iwebos.message.paperwork.lasttwomonth='Hai tháng trước';
iwebos.message.paperwork.lastyear='Năm trước';
iwebos.message.paperwork.currentyear='Năm nay';

iwebos.message.paperwork.other='Khác';
iwebos.message.paperwork.unavailable='Không xác định';

//iwebos.message.paperwork.edexpirydate='Ngày hết hạn xử lý';
//iwebos.message.paperwork.edreceive='Nơi nhận';

//using for grid and preview
iwebos.message.paperwork.task='Tác vụ';
iwebos.message.paperwork.gridview='Xem theo';
iwebos.message.paperwork.gridgroupby='Nhóm theo';
iwebos.message.paperwork.makework='Công việc sắp tới ';
iwebos.message.paperwork.unknown='Chưa xác định' ;
iwebos.message.paperwork.gridcontent='Nội dung toàn văn';
iwebos.message.paperwork.dateof='Ngày đến';
iwebos.message.paperwork.outdate='Ngày đi';
iwebos.message.paperwork.numberout='Số đi';
iwebos.message.paperwork.receiveindoc='Đơn vị nhận';
iwebos.message.paperwork.documentin='Sổ công văn đến';
iwebos.message.paperwork.documentout='Sổ công văn đi';
iwebos.message.paperwork.works_message='Các công việc được nhắc nhở';

iwebos.message.paperwork.message='Tin nhắn';
iwebos.message.paperwork.summary='Thống kê';
iwebos.message.paperwork.document='Văn bản';

iwebos.message.paperwork.receive_unit='Người/Đơn vị nhận';
iwebos.message.paperwork.doc_book_in='Sổ CV đến';
iwebos.message.paperwork.doc_book_out='Sổ CV đi';
iwebos.message.paperwork.number_in='Số đến';
iwebos.message.paperwork.number_out='Số đi';
iwebos.message.paperwork.create_date='Ngày văn bản';
iwebos.message.paperwork.deadline='Ngày hết hạn xử lý';
iwebos.message.paperwork.signer='Người ký';
iwebos.message.paperwork.doccode='Số ký hiệu VB';

/**
 * electronic document.
 * iwebos.message.paperwork.ed
 */
iwebos.message.paperwork.ed.created='Vừa tạo';
iwebos.message.paperwork.ed.processing='Đang xử xử lý';
iwebos.message.paperwork.ed.published='Xuất bản';
iwebos.message.paperwork.ed.completed='Hoàn thành';
iwebos.message.paperwork.ed.status='Trạng thái';
iwebos.message.paperwork.ed.toknow='Để biết';
iwebos.message.paperwork.ed.precompleted='Tạm hoàn thành';
iwebos.message.paperwork.ed.returning='Trả lại';
iwebos.message.paperwork.ed.save='Cất';
iwebos.message.paperwork.ed.processing='Xứ lý';
iwebos.message.paperwork.ed.toknow='Để biết';
iwebos.message.paperwork.ed.iodatecol ='Ngày đến/đi';
iwebos.message.paperwork.ed.ionumbercol='Số đến/đi'; 
iwebos.message.paperwork.ed.sendercol='Người gửi';
iwebos.message.paperwork.ed.idatecol='Ngày đến';
iwebos.message.paperwork.ed.odatecol='Ngày đi';
iwebos.message.paperwork.ed.inumbercol='Số đến';
iwebos.message.paperwork.ed.onumbercol='Số đi';

//task action
iwebos.message.paperwork.ed.create='Tạo văn bản';
iwebos.message.paperwork.ed.transfer='Chuyển văn bản';
iwebos.message.paperwork.ed.store='Cất văn bản';
iwebos.message.paperwork.ed.remove='Xóa văn bản';
iwebos.message.paperwork.ed.sendout='Gửi cơ quan ngoài';
iwebos.message.paperwork.ed.nouserselected='Không có người dùng nào được chọn.';

//dialog search
iwebos.message.paperwork.ed.systemsearch='Tìm tài liệu bên trong hệ thống';
iwebos.message.paperwork.ed.docsystem='Chọn từ hệ thống';
iwebos.message.paperwork.ed.uploadtosystem='Nhập từ bên ngoài';
iwebos.message.paperwork.ed.scansystem='Nhập từ máy quét';
iwebos.message.paperwork.ed.newdoc='Tạo mới';
iwebos.message.paperwork.ed.viewdoc='Xem tài liệu';
iwebos.message.paperwork.ed.deletedoc='Xoá văn bản';
iwebos.message.paperwork.ed.downloaddoc='Tải về';
iwebos.message.paperwork.ed.title='Tiêu đề';
iwebos.message.paperwork.ed.doccode='Số/Ký hiệu';
iwebos.message.paperwork.ed.createdate='Ngày tạo';
iwebos.message.paperwork.ed.from='Từ ngày';
iwebos.message.paperwork.ed.to='Đến ngày';
iwebos.message.paperwork.ed.subject='Chủ đề';
iwebos.message.paperwork.ed.creator='Người tạo';
iwebos.message.paperwork.ed.doctype='Loại văn bản';
iwebos.message.paperwork.ed.office='Phòng ban';
iwebos.message.paperwork.ed.keyword='Từ khóa';
iwebos.message.paperwork.ed.template='Mẫu';
iwebos.message.paperwork.ed.notpublish='Chưa lưu hành';
iwebos.message.paperwork.ed.filetype='Định dạng';
iwebos.message.paperwork.ed.content='Nội dung';
iwebos.message.paperwork.ed.allfolder='Tìm cả trong tủ';
iwebos.message.paperwork.ed.publicfolder='Tủ tài liệu công ty';
iwebos.message.paperwork.ed.noneadd='Không thể thêm mới';
iwebos.message.paperwork.ed.nonedelete='Không thể xoá được';
iwebos.message.paperwork.ed.noneupdate='Không thể cập nhật được';
iwebos.message.paperwork.ed.nonestore='Không thể cất văn bản';
iwebos.message.paperwork.ed.error='Lỗi';
iwebos.message.paperwork.ed.ok='Đồng ý';
iwebos.message.paperwork.ed.cancel='Bỏ qua';
iwebos.message.paperwork.ed.transfersuccess='Chuyển xử lý thành công';
iwebos.message.paperwork.ed.deleteconfirm='Bạn thật sự muốn xoá văn bản điện tử đã chọn ?';
iwebos.message.paperwork.ed.originaldocument='Bản gốc ';
iwebos.message.paperwork.ed.copydocument='Văn bản sao lục';
iwebos.message.paperwork.ed.draftdocument='Văn bản dự thảo';
/** resource file */
// iwebos.message.paperwork.file
iwebos.message.paperwork.file.filename='Tên tài liệu';
iwebos.message.paperwork.file.filetype='Loại tài liệu';

/**Tao moi van ban*/
iwebos.message.paperwork.ed.newdocument.desc='Trích yếu';
iwebos.message.paperwork.ed.newdocument.group='Thuộc nhóm';
iwebos.message.paperwork.ed.newdocument.book='Sổ văn bản';
iwebos.message.paperwork.ed.newdocument.urgent='Độ khẩn';
iwebos.message.paperwork.ed.newdocument.symbol='Số ký hiệu VB';
iwebos.message.paperwork.ed.newdocument.name='Tên văn bản';
iwebos.message.paperwork.ed.newdocument.type='Loại văn bản';
iwebos.message.paperwork.ed.newdocument.status='Tình trạng';
iwebos.message.paperwork.ed.newdocument.security='Đô mật';
iwebos.message.paperwork.ed.newdocument.scope='Lĩnh vực';
iwebos.message.paperwork.ed.newdocument.date='Ngày văn bản';
iwebos.message.paperwork.ed.newdocument.level_publish_agency='Cấp CQBH';
iwebos.message.paperwork.ed.newdocument.publish_agency='CQ ban hành';
iwebos.message.paperwork.ed.newdocument.formatsend='Hình thức gửi';
iwebos.message.paperwork.ed.newdocument.keyword='Từ khóa';
iwebos.message.paperwork.ed.newdocument.signer='Người ký';
iwebos.message.paperwork.ed.newdocument.position='Chức vụ';
iwebos.message.paperwork.ed.newdocument.dateof='Ngày đến';
iwebos.message.paperwork.ed.newdocument.date_out='Ngày đi';
iwebos.message.paperwork.ed.newdocument.date_all='Ngày đến/đi';
iwebos.message.paperwork.ed.newdocument.expiry_date='Ngày hết hạn';
iwebos.message.paperwork.ed.newdocument.sdate='Từ ngày';
iwebos.message.paperwork.ed.newdocument.edate='Đến ngày';
iwebos.message.paperwork.ed.newdocument.public_info="Thông tin chung";
iwebos.message.paperwork.ed.newdocument.process_info="Thông tin xử lý";
iwebos.message.paperwork.ed.newdocument.bookin='Sổ CV đến';
iwebos.message.paperwork.ed.newdocument.bookout='Sổ CV đi';
iwebos.message.paperwork.ed.newdocument.receive_unit='Đơn vị nhận';
iwebos.message.paperwork.ed.newdocument.unit_in='Chọn đơn vị nội bộ';
iwebos.message.paperwork.ed.newdocument.unit_out='Chọn đơn vị bên ngoài';
iwebos.message.paperwork.ed.newdocument.unit_info='Thông tin đơn vị nhận';
iwebos.message.paperwork.ed.newdocument.unit_list='Danh sách đơn vị';
/************Phan Xu ly****************************/

/**Xu ly Ho so cong viec*/
// iwebos.message.paperwork.ed.process.docwork
iwebos.message.paperwork.ed.process.docwork.title='Xử lý hồ sơ công việc';
iwebos.message.paperwork.ed.process.docwork.workname='Tên công việc';
iwebos.message.paperwork.ed.process.docwork.paperwork='Tên hồ sơ công việc ';
iwebos.message.paperwork.ed.process.docwork.sdate='Bắt đầu';
iwebos.message.paperwork.ed.process.docwork.edate='Kết thúc';
iwebos.message.paperwork.ed.process.docwork.tasker='Người giao việc';
iwebos.message.paperwork.ed.process.docwork.realize='Người xử lý';
iwebos.message.paperwork.ed.process.docwork.important= 'Độ quan trọng';
iwebos.message.paperwork.ed.process.docwork.type= 'Loại';
iwebos.message.paperwork.ed.process.docwork.status='Tình trạng';
iwebos.message.paperwork.ed.process.docwork.create_date='Ngày tạo';
iwebos.message.paperwork.ed.process.docwork.combine_unit='Đơn vị xử lý';
iwebos.message.paperwork.ed.process.docwork.remove='Xoá hố sơ';
iwebos.message.paperwork.ed.process.docwork.reason_title='Lý do trả lại';
iwebos.message.paperwork.ed.process.docwork.reason='Lý do';
iwebos.message.paperwork.ed.process.docwork.combine_process='Đồng xử lý';
iwebos.message.paperwork.ed.process.docwork.desc="Mô tả";
iwebos.message.paperwork.ed.process.docwork.delete_confirm='Bạn thật sự muốn công việc đã chọn ?';
/**Tao ho so cong viec*/
// iwebos.message.paperwork.ed.process.newdocwork
iwebos.message.paperwork.ed.process.newdocwork.title='Tạo hồ sơ công việc';
iwebos.message.paperwork.ed.process.newdocwork.cadre_process='Cán bộ đồng Xl';
iwebos.message.paperwork.ed.process.newdocwork.desc='Mô tả';
iwebos.message.paperwork.ed.process.newdocwork.assign='Phân công';
iwebos.message.paperwork.ed.process.newdocwork.assign_process='Phân công xử lý';
iwebos.message.paperwork.ed.process.newdocwork.result='Kết quả';
iwebos.message.paperwork.ed.process.newdocwork.realize='Thưc hiện';
iwebos.message.paperwork.ed.process.newdocwork.list_doc='Danh mục văn bản trong hồ sơ';
iwebos.message.paperwork.ed.process.newdocwork.title_doc='Văn bản';
iwebos.message.paperwork.ed.process.newdocwork.title_work='Công việc';
iwebos.message.paperwork.ed.process.newdocwork.title_combine='Phối hợp xử lý';
iwebos.message.paperwork.ed.process.newdocwork.number_in='Số đến';
iwebos.message.paperwork.ed.process.newdocwork.number_out='Số đi';
iwebos.message.paperwork.ed.process.newdocwork.number_all='Số đến/đi';
iwebos.message.paperwork.ed.process.newdocwork.sender='Người gửi';
iwebos.message.paperwork.ed.process.newdocwork.docin='Văn bản đến';
iwebos.message.paperwork.ed.process.newdocwork.docout='Văn bản ban hành';
iwebos.message.paperwork.ed.process.newdocwork.draft_document='Văn bản dự thảo';
iwebos.message.paperwork.ed.process.newdocwork.relation_document='Văn bản liên quan';
iwebos.message.paperwork.ed.process.newdocwork.ed_document='Văn bản điện tử';
iwebos.message.paperwork.ed.process.newdocwork.out_document='Văn bản bên ngoài';
iwebos.message.paperwork.ed.process.newdocwork.process_info='Thông tin xử lý';
iwebos.message.paperwork.ed.process.newdocwork.page_copy='Phiếu sao lục';
iwebos.message.paperwork.ed.process.newdocwork.page_exchange='Phiếu trao đổi';
iwebos.message.paperwork.ed.process.newdocwork.page_submit='Phiếu trình duyệt';
iwebos.message.paperwork.ed.process.newdocwork.send_info='Gửi thông tin';
iwebos.message.paperwork.ed.process.newdocwork.date_send='Ngày gửi';
/**xu ly cong van den*/
// iwebos.message.paperwork.ed.process.docin
iwebos.message.paperwork.ed.process.docin.title='Xử lý công văn đến';
iwebos.message.paperwork.ed.process.docin.toknow='Để biết';
iwebos.message.paperwork.ed.process.docin.process_success='Xử lý xong';
iwebos.message.paperwork.ed.process.docin.create_ppw='Lập HSCV';
iwebos.message.paperwork.ed.process.docin.create_ppw_tip='Lập hồ sơ công việc';
iwebos.message.paperwork.ed.process.docin.linked_ppw='Gắn vào HSCV';
iwebos.message.paperwork.ed.process.docin.linked_ppw_tip='Gắn vào hồ sơ công việc';
iwebos.message.paperwork.ed.process.docin.linked_notification='Gắn vào HSHB';
iwebos.message.paperwork.ed.process.docin.linked_notification_tip='Gắn vào hồ sơ hồi báo';
iwebos.message.paperwork.ed.process.docin.note='Ghi chú';
iwebos.message.paperwork.ed.process.docin.return_title='Nguyên nhân trả lại:' ;
/**xy ly cong viec*/
// iwebos.message.paperwork.ed.process.task
iwebos.message.paperwork.ed.process.task.title='Xác nhận hồ sơ công việc';
iwebos.message.paperwork.ed.process.task.toreturn='Xử lý';
iwebos.message.paperwork.ed.process.task.success='Hoàn thành';
iwebos.message.paperwork.ed.process.task.none_success='Chưa hoàn thành';
iwebos.message.paperwork.ed.process.task.process='Xử lý công việc';
iwebos.message.paperwork.ed.process.task.record_complete='Xử lý hồ sơ công việc tạm hoàn thành';
iwebos.message.paperwork.ed.process.task.about='Về việc';
/**xy ly cong van tam hoan thanh*/
// iwebos.message.paperwork.ed.process.predoc
iwebos.message.paperwork.ed.process.predoc.title='Công văn tạm hoàn thành';
iwebos.message.paperwork.ed.process.predoc.process='Xử lý';
/**xy ly hoi bao*/// iwebos.message.paperwork.ed.process.notification
iwebos.message.paperwork.ed.process.notification.title='Xử lý hồi báo';
/*****Theo doi***/
// iwebos.message.paperwork.ed.watch
iwebos.message.paperwork.ed.watch.title='Theo dõi';
iwebos.message.paperwork.ed.watch.docin='Theo dõi văn bản đến';
iwebos.message.paperwork.ed.watch.docout='Theo dõi văn bản đi';
iwebos.message.paperwork.ed.watch.draft='Theo dõi văn bản dự thảo';
iwebos.message.paperwork.ed.watch.process='Theo dõi xử lý công việc';
iwebos.message.paperwork.ed.watch.paperwork='Theo dõi hồ sơ công việc';

iwebos.message.paperwork.ed.watch.publish='Xuất bản';
iwebos.message.paperwork.ed.watch.sender='Người chuyển đến';
iwebos.message.paperwork.ed.watch.require_content='Nội dung yêu cầu';
iwebos.message.paperwork.ed.watch.from_number='Từ số';
iwebos.message.paperwork.ed.watch.to_number='Đến số';
//van ban trinh
iwebos.message.paperwork.ed.watch.process_title='Theo dõi xử lý công việc';
iwebos.message.paperwork.ed.watch.info_title='Thông tin trình';
iwebos.message.paperwork.ed.watch.leader='Lãnh đạo đơn vị';
iwebos.message.paperwork.ed.watch.unit_copy='Cơ quan sao lục';
iwebos.message.paperwork.ed.watch.receive='Nơi nhận';
//ho so cong viec
iwebos.message.paperwork.ed.watch.paperwork_title='Theo dõi hồ sơ công việc';
iwebos.message.paperwork.ed.watch.transfer_to='Chuyển tới';
//hoi bao
iwebos.message.paperwork.ed.watch.notification_title='Theo dõi hồi báo';

/**thong bao*/
// iwebos.message.paperwork.ed.msg
iwebos.message.paperwork.ed.msg.title='Thông báo';
iwebos.message.paperwork.ed.msg.create_number_error='Không thể tạo được số đến/đi';
iwebos.message.paperwork.ed.msg.success_notice='Sau khi kết thúc bạn sẽ không được phép thao tác trên công văn đã chọn nữa. Bạn thật sự muốn kết thúc xử lý ?';
iwebos.message.paperwork.ed.msg.success_work='Sau khi kết thúc bạn sẽ không được phép thao tác trên HSCV đã chọn nữa. Bạn thật sự muốn kết thúc công việc ?';
iwebos.message.paperwork.ed.msg.limit='Chức năng này bị hạn chế trong phiên bản thử nghiệm. Vui lòng liên hệ đến INet Solutions để có phiên bản đầy đủ.';

// iwebos.message.doc
iwebos.message.doc.ttitle='Tìm kiếm tài liệu.';
iwebos.message.doc.btitle='Tìm kiếm tài liệu từ hệ thống và gán vào cho công văn/hồ sơ công việc.';

// iwebos.message.doc.status
iwebos.message.doc.status.update='Cập nhật' ;
iwebos.message.doc.status.review='Xem xét';
iwebos.message.doc.status.publish='Xuất bản';
iwebos.message.doc.status.notuse='Không sử dụng';
// iwebos.message.doc.priority
iwebos.message.doc.priority.low = 'Thấp';
iwebos.message.doc.priority.normal = 'Bình thường';
iwebos.message.doc.priority.high = 'Khẩn';
iwebos.message.doc.priority.very_high='Thượng khẩn';
iwebos.message.doc.priority.work='Công việc khẩn';
iwebos.message.doc.priority.document='Công văn khẩn';

// iwebos.message.doc.secure
iwebos.message.doc.secure.normal = 'Bình thường';
iwebos.message.doc.secure.secure ='Mật';
iwebos.message.doc.secure.very_secure = 'Tuyệt mật';
iwebos.message.doc.secure.extreme_secure = 'Tối mật';

//iwebos.message.doc.important
iwebos.message.doc.important.low='Bình thường';
iwebos.message.doc.important.normal='Thường khẩn';
iwebos.message.doc.important.high='Khẩn';


// iwebos.message.doc.book
iwebos.message.doc.book.booked = 'Đã vào sổ';
iwebos.message.doc.book.notbook ='Chưa vào sổ';
iwebos.message.doc.book.allbook = 'Tất cả';

//iwebos.message.dialog
iwebos.message.dialog.priority='Độ khẩn';
iwebos.message.dialog.deadline='Hạn xử lý';
iwebos.message.dialog.note="Ghi chú";
iwebos.message.dialog.process_info="Thông tin xử lý";
iwebos.message.dialog.select_users="Chọn người dùng/nhóm nguời dùng";

//upload dialog
Ext.ux.UploadDialog.Dialog.i18n.title='Tải tài liệu lên hệ thống';
Ext.ux.UploadDialog.Dialog.i18n.state_col_title='Tình trạng';
Ext.ux.UploadDialog.Dialog.i18n.state_col_width=70;
Ext.ux.UploadDialog.Dialog.i18n.filename_col_title='Tên tài liệu';
Ext.ux.UploadDialog.Dialog.i18n.filename_col_width=230;
Ext.ux.UploadDialog.Dialog.i18n.note_col_title='Ghi chú';
Ext.ux.UploadDialog.Dialog.i18n.note_col_width=150;
Ext.ux.UploadDialog.Dialog.i18n.add_btn_text='Thêm';
Ext.ux.UploadDialog.Dialog.i18n.add_btn_tip='Thêm vào danh sách.';
Ext.ux.UploadDialog.Dialog.i18n.remove_btn_text='Xoá';
Ext.ux.UploadDialog.Dialog.i18n.remove_btn_tip='Xoá khỏi danh sách.';
Ext.ux.UploadDialog.Dialog.i18n.reset_btn_text='Xoá tất cả';
Ext.ux.UploadDialog.Dialog.i18n.reset_btn_tip='Xoá tất cả danh sách.';
Ext.ux.UploadDialog.Dialog.i18n.upload_btn_start_text="Tải lên";
Ext.ux.UploadDialog.Dialog.i18n.upload_btn_stop_text="Dừng";
Ext.ux.UploadDialog.Dialog.i18n.upload_btn_start_tip="Tải tất cả danh sách lên máy chủ.";
Ext.ux.UploadDialog.Dialog.i18n.upload_btn_stop_tip="Dừng tải tài liệu";
Ext.ux.UploadDialog.Dialog.i18n.close_btn_text='Đóng';
Ext.ux.UploadDialog.Dialog.i18n.close_btn_tip='Đóng hộp thoại.';
Ext.ux.UploadDialog.Dialog.i18n.progress_waiting_text='Đang chờ xử lý';
Ext.ux.UploadDialog.Dialog.i18n.progress_uploading_text="Tải lên thành công: {0}/{1} tài liệu.";
Ext.ux.UploadDialog.Dialog.i18n.error_msgbox_title='Có lỗi';
Ext.ux.UploadDialog.Dialog.i18n.permitted_extensions_join_str= ',';
Ext.ux.UploadDialog.Dialog.i18n.err_file_type_not_permitted="Định dạng bạn chọn không được phép tải.<br/>Vui lòng chọn trong những định dạng sau: {1}";
Ext.ux.UploadDialog.Dialog.i18n.note_queued_to_upload='Đang chờ xử lý';
Ext.ux.UploadDialog.Dialog.i18n.note_processing='Đang tải...';
Ext.ux.UploadDialog.Dialog.i18n.note_upload_success='Thành công';
Ext.ux.UploadDialog.Dialog.i18n.note_upload_failed='Máy chủ không tồn tại hoặc bị lỗi khi tải.';
Ext.ux.UploadDialog.Dialog.i18n.note_upload_error='Có lỗi.';
Ext.ux.UploadDialog.Dialog.i18n.note_aborted='Huỷ bỏ trong khi tải.';
Ext.ux.UploadDialog.Dialog.i18n.note_canceled='Huỷ bỏ';

/**
 * flag information.
 */
iwebos.message.followup.important='Công việc khẩn';
iwebos.message.followup.today='Hôm nay';
iwebos.message.followup.tomorrow='Ngày mai';
iwebos.message.followup.week='Tuần này';
iwebos.message.followup.nextweek='Tuần tới';
iwebos.message.followup.others='Không xác định';
iwebos.message.followup.flags='Bật cờ';
iwebos.message.followup.remove='Xoá';
iwebos.message.followup.view='Xem công việc';
iwebos.message.followup.time='Thời gian';
iwebos.message.followup.none_saved='Không thể thêm mới công việc vào cơ sở dữ liệu.' ;
iwebos.message.followup.none_updated='Không thể cập nhật công việc vào cơ sở dữ liệu.';
iwebos.message.followup.none_deleted='Không thể xoá công việc từ cơ sở dữ liệu.';

/**
 * document work message.
 */
iwebos.message.paperwork.dw.created='Vừa tạo';
iwebos.message.paperwork.dw.processing='Đang xử lý';
iwebos.message.paperwork.dw.precompleted='Tạm hoàn thành';
iwebos.message.paperwork.dw.completed='Hoàn thành';
iwebos.message.paperwork.dw.watched='Theo dõi';
iwebos.message.paperwork.dw.published='Xuất bản';
iwebos.message.paperwork.dw.deleted='Xoá';
iwebos.message.paperwork.dw.approved='Đã duyệt';
iwebos.message.paperwork.dw.view_only='Chỉ được xem';

//menu document.
iwebos.message.paperwork.dw.mnu_doc_view='Xem văn bản' ;
iwebos.message.paperwork.dw.mnu_doc_delete='Xóa văn bản' ;
iwebos.message.paperwork.dw.mnu_doc_add_doc_in='Thêm văn bản đến';
iwebos.message.paperwork.dw.mnu_doc_add_doc_draft='Thêm văn bản dự thảo';
iwebos.message.paperwork.dw.mnu_doc_add_doc_related='Thêm văn bản liên quan';
iwebos.message.paperwork.dw.mnu_doc_add_doc_related_ed='Văn bản điện tử';
iwebos.message.paperwork.dw.mnu_doc_add_doc_related_file='Văn bản bên ngoài' ;
iwebos.message.paperwork.dw.mnu_doc_create_work='Tạo công việc';
iwebos.message.paperwork.dw.mnu_doc_create_work_co='Tạo phiếu sao lục';
iwebos.message.paperwork.dw.mnu_doc_create_work_ex='Tạo phiếu trao đổi';
iwebos.message.paperwork.dw.mnu_doc_create_work_rv='Tạo phiếu trình duyệt';
//menu work.
iwebos.message.paperwork.dw.mnu_work_view='Xem công việc';
iwebos.message.paperwork.dw.mnu_work_delete='Xóa công việc';
iwebos.message.paperwork.dw.mnu_work_load='Lấy kết quả công việc';
iwebos.message.paperwork.dw.mnu_work_add_work_co='Thêm phiếu sao lục';
iwebos.message.paperwork.dw.mnu_work_add_work_ex='Thêm phiếu trao đổi';
iwebos.message.paperwork.dw.mnu_work_add_work_rv='Thêm phiếu trình duyệt';
iwebos.message.paperwork.dw.mnu_work_add_work_st='Thêm phiếu gửi thông tin';
//menu combine.
iwebos.message.paperwork.dw.mnu_combine_add='Thêm phối hợp xử lý';
iwebos.message.paperwork.dw.mnu_combine_load='Lấy kết quả công việc';
iwebos.message.paperwork.dw.mnu_combine_delete='Xoá phối hợp xử lý';

/**
 * document work search.
 */
iwebos.message.paperwork.dw.status_all='Tất cả';
iwebos.message.paperwork.dw.status_create='Vừa tạo';
iwebos.message.paperwork.dw.status_view_only='Chỉ được xem';
iwebos.message.paperwork.dw.status_processing='Đang xử lý';

/**
 * document work resources
 */
iwebos.resources.paperwork.dw.create_new='Tạo hồ sơ công việc.' ;
iwebos.resources.paperwork.dw.assigned_by='Được giao việc bởi ' ;
iwebos.resources.paperwork.dw.coworked_with='Đồng xử lý với ';
iwebos.resources.paperwork.dw.combined_with='Phối hợp xử lý với %s, về việc: "%s".' ;
iwebos.resources.paperwork.dw.create_draft_doc='Tạo văn bản dự thảo về việc ' ;
iwebos.resources.paperwork.dw.transfer_task='Chuyển xử lý cho %s với yêu cầu: %s.';
iwebos.resources.paperwork.dw.reason_pre_completed='Tạm hoàn thành công việc với lý do: ';
iwebos.resources.paperwork.dw.related_doc='Văn bản liên quan';
iwebos.resources.paperwork.dw.report='Báo cáo';
iwebos.resources.paperwork.dw.undefined_group='Nhóm chưa xác định';
/**
 * document work title dialog.
 */
iwebos.resources.paperwork.dw.ed_in_dw_dlg_title='Văn bản điện tử';
iwebos.resources.paperwork.dw.ed_dlg_title='Văn bản điện tử';
/**
 * document work error resources.
 */
iwebos.resources.paperwork.dw.error_dw_title='Hồ sơ công việc' ;
iwebos.resources.paperwork.dw.error_dw_reason_precompleted='Lý do tạm hoàn thành:';
iwebos.resources.paperwork.dw.error_dw_non_delete='Có lỗi xảy ra trong quá trình xóa hồ sơ công việc, hoặc bạn không có quyền để thực thi tác vụ này.';
iwebos.resources.paperwork.dw.error_dw_non_transfer='Có lỗi xảy ra trong quá trình chuyển xử lý hồ sơ công việc, hoặc bạn không có quyền để thực thi tác vụ này.' ;
iwebos.resources.paperwork.dw.error_dw_non_pre_completed='Có lỗi xảy ra trong quá trình tạm hoàn thành hồ sơ công việc, hoặc bạn không có quyền để thực thi tác vụ này.' ;
iwebos.resources.paperwork.dw.error_dw_non_select_doc='Bạn phải chọn ít nhất một văn bản để thực hiện phối xử lý.';
iwebos.resources.paperwork.dw.error_dw_non_select_ad='Bạn phải chọn một Văn thư để xử lý công việc.';
iwebos.resources.paperwork.dw.error_dw_non_select_combiner='Bạn phải chọn ít nhất một cán bộ để phối hợp xử lý công việc.';
iwebos.resources.paperwork.dw.error_dw_non_select_processor='Bạn phải chọn ít nhất một cán bộ để chuyển xử lý';
iwebos.resources.paperwork.dw.error_dw_non_select_co_worker='Bạn phải chọn ít nhất một cán bộ để đồng xử lý' ;
iwebos.resources.paperwork.dw.error_dw_non_select_assigner='Bạn phải chọn cán bộ giao việc.' ;

/**
 * Page copy
 */
iwebos.message.paperwork.ed.pagecopy.title='Phiếu sao lục';
iwebos.message.paperwork.ed.pagecopy.grid_title='Văn bản sao y';
iwebos.message.paperwork.ed.pagecopy.unit='Cơ quan';
iwebos.message.paperwork.ed.pagecopy.subject='Về việc';
iwebos.message.paperwork.ed.pagecopy.number='Số';
iwebos.message.paperwork.ed.pagecopy.sy='/SY';
iwebos.message.paperwork.ed.pagecopy.type='Hình thức sao';
iwebos.message.paperwork.ed.pagecopy.authority='Quyền hạn';
iwebos.message.paperwork.ed.pagecopy.place='Địa danh';
iwebos.message.paperwork.ed.pagecopy.date='Ngày tháng';
iwebos.message.paperwork.ed.pagecopy.unit_receive='Cơ quan nhận';
/**
 * Page Exchange
 */
iwebos.message.paperwork.ed.pageexchange.title='Phiếu trao đổi';
iwebos.message.paperwork.ed.pageexchange.grid_title='Văn bản tham khảo ý kiến';
iwebos.message.paperwork.ed.pageexchange.sendto='Kính chuyển';
iwebos.message.paperwork.ed.pageexchange.subject='Về việc';
iwebos.message.paperwork.ed.pageexchange.reason='Lý do';
iwebos.message.paperwork.ed.pageexchange.content='Nội dung trao đổi';
iwebos.message.paperwork.ed.pageexchange.msg_reply='Gửi phúc đáp (%s) về việc "%s"';
iwebos.message.paperwork.ed.pageexchange.msg_transfer='Đã gửi thông tin (%s) về việc "%s" đến %s';
iwebos.message.paperwork.ed.pageexchange.msg_submit='Nhờ xử lý văn bản trình (%s) về việc "%s"';
iwebos.message.paperwork.ed.pageexchange.msg_note='Gửi trao đổi (%s) về việc "%s" đến %s';
iwebos.message.paperwork.ed.pageexchange.msg_save='Đã tạo một phiếu (%s) về việc "%s"';
/**
 * Page Submit
 */
iwebos.message.paperwork.ed.pagesubmit.title='Phiếu trình duyệt';
iwebos.message.paperwork.ed.pagesubmit.grid_title='Văn bản trình';
iwebos.message.paperwork.ed.pagesubmit.sendto='Kính chuyển';
iwebos.message.paperwork.ed.pagesubmit.subject='Vấn đề trình';
iwebos.message.paperwork.ed.pagesubmit.content='Nội dung trình';
iwebos.message.paperwork.ed.pagesubmit.idea_submitter='Kiến nghị của ngưới trình sau khi thẩm tra';
iwebos.message.paperwork.ed.pagesubmit.idea_belong='Ý kiến các bộ phận thuộc đơn vị';
iwebos.message.paperwork.ed.pagesubmit.idea_leader='Ý kiến của lãnh đạo thông qua';
iwebos.message.paperwork.ed.pagesubmit.idea_decision='Giải quyết của lãnh đạo';
iwebos.message.paperwork.ed.pagesubmit.part='Bộ phận trình';

/**
 * Page send info
 */
iwebos.message.paperwork.ed.pageinfo.title='Gửi thông tin';
iwebos.message.paperwork.ed.pageinfo.subject='Chủ đề';
iwebos.message.paperwork.ed.pageinfo.content='Ý kiến';
iwebos.message.paperwork.ed.pageinfo.sender='Người gửi';
iwebos.message.paperwork.ed.pageinfo.attach='Đính kèm';
iwebos.message.paperwork.ed.pageinfo.priority='Độ khẩn';
iwebos.message.paperwork.ed.pageinfo.deadline='Hạn xử lý';

/**
 * action message.
 */
iwebos.message.action.title='Tiêu đề' ;
iwebos.message.action.sender='Người gửi' ;
iwebos.message.action.date='Ngày gửi';
/**
 * gear & firebugs.
 */
iwebos.message.notices='Để nâng cao tốc độ  của chương trình, chúng tôi khuyên bạn nên cài  Google Gear và tắt Firebug.';
iwebos.message.title='iWebOS - Thông báo';

/*** Mail box ***/
//iwebos.message.mail
iwebos.message.mail.title_mailbox = "Hộp thư";
iwebos.message.mail.title_mailview = "Kiểu xem";
iwebos.message.mail.title_mailcompose = "Soạn thư mới";
iwebos.message.mail.mail_delete = "Xoá";
iwebos.message.mail.mail_delete_tip = "Xoá thư";
iwebos.message.mail.mail_reply = "Trả lời";
iwebos.message.mail.mail_reply_tip = "Trả lời người gửi";
iwebos.message.mail.mail_forward = "Chuyển tiếp";
iwebos.message.mail.mail_forward_tip = "Chuyển tiếp thư đến địa chỉ khác";
iwebos.message.mail.mail_print = "In";
iwebos.message.mail.mail_print_tip = "In ra";
iwebos.message.mail.mail_more_action = "Thao tác khác";
iwebos.message.mail.mail_more_action_tip = "Các thao tác khác";
iwebos.message.mail.mail_more_action_groupbyday = "Nhóm theo ngày gửi";
iwebos.message.mail.mail_more_action_groupbyfrom = "Nhóm theo người gửi";
iwebos.message.mail.mail_more_action_groupbysubject = "Nhóm theo tiêu đề";
iwebos.message.mail.mail_send = "Gửi";
iwebos.message.mail.mail_send_tip = "Gửi thư";
iwebos.message.mail.mail_attachment = "Tài liệu";
iwebos.message.mail.mail_attachment_tip = "Đính kèm theo tài liệu";
iwebos.message.mail.mail_save_draf = "Lưu";
iwebos.message.mail.mail_save_draf_tip ="Lưu dưới dạng bản nháp";
//Grid
iwebos.message.mail.grid_sender = "Người gửi";
iwebos.message.mail.grid_sent = "Ngày gửi";
iwebos.message.mail.grid_subject = "Tiêu đề";
iwebos.message.mail.grid_size = "Dung lượng";
iwebos.message.mail.grid_groupLabel = "Thư";
iwebos.message.mail.template_mailto = "Người nhận";
iwebos.message.mail.template_attach = "Đính kèm";

//Context Menu
iwebos.message.mail.context_reply = "Trả lời người gửi";
iwebos.message.mail.context_replyall = "Trả lời tất cả";
iwebos.message.mail.context_forward = "Chuyển tiếp";
iwebos.message.mail.context_mark = "Thêm thẻ đánh dấu";
iwebos.message.mail.context_verb = "Nhắc nhở";
iwebos.message.mail.context_delete = "Xoá";
iwebos.message.mail.context_move = "Chuyển đến thư mục";
iwebos.message.mail.context_mark_unread = "Đánh dấu chưa đọc";
iwebos.message.mail.context_mark_read = "Đánh dấu đã đọc";
iwebos.message.mail.context_mark_priority = "Đánh dấu quan trọng";
iwebos.message.mail.context_mark_unpriority = "Bỏ dấu quan trọng";
iwebos.message.mail.context_flag = "Gán nhãn";
iwebos.message.mail.context_flag_normal = "Bình thường";
iwebos.message.mail.context_flag_work = "Công việc";
iwebos.message.mail.context_flag_personal = "Cá nhân";
iwebos.message.mail.context_flag_todo = "Phải làm";
iwebos.message.mail.context_flag_late = "Trễ";
iwebos.message.mail.context_flag_phone = "Gọi điện thoại";
iwebos.message.mail.context_flag_reply = "Sẽ trả lời";
iwebos.message.mail.context_flag_approve = "Sẽ xem xét";

//Mail Compose
iwebos.message.mail.compose_to ="Gửi đến";
iwebos.message.mail.compose_cc ="Cc   ";
iwebos.message.mail.compose_bcc ="Bcc   ";
iwebos.message.mail.compose_subject ="Tiêu đề";

//Mail Dialog
iwebos.message.mail.dialog_createfolder = "Tạo thư mục mới";
iwebos.message.mail.dialog_createfolder_ttitle = "Tạo thư mục mới trong hộp thư của bạn";
iwebos.message.mail.dialog_createfolder_btitle = "Vui lòng điền tên thư mục và chọn nút \"Tạo mới\" để tạo thư mục mới";
iwebos.message.mail.dialog_createfolder_btn_ok = "Tạo mới";
iwebos.message.mail.dialog_createfolder_btn_cancel = "Huỷ bỏ";
iwebos.message.mail.dialog_renamefolder = "Đổi tên thư mục";
iwebos.message.mail.dialog_renamefolder_ttitle = "Đổi tên thư mục bạn vừa chọn";
iwebos.message.mail.dialog_renamefolder_btitle = "Vui lòng điền tên thư mục và chọn nút \"Đổi tên\" để đổi tên thư mục";
iwebos.message.mail.dialog_renamefolder_btn_ok = "Đổi tên";
iwebos.message.mail.dialog_renamefolder_btn_cancel = "Huỷ bỏ";
iwebos.message.mail.dialog_empty_text = "Không được bỏ trống";

/**
 * report
 * namespace: iwebos.message.paperwork.ed.report
 */
iwebos.message.paperwork.ed.report.create_title='Tạo báo cáo nghiệp vụ';
iwebos.message.paperwork.ed.report.column_name='Tên báo cáo';
iwebos.message.paperwork.ed.report.column_create_date='Ngày tạo';
iwebos.message.paperwork.ed.report.column_constructor='Người tạo';
iwebos.message.paperwork.ed.report.type='Loại';
iwebos.message.paperwork.ed.report.fromdate='Từ ngày';
iwebos.message.paperwork.ed.report.todate='Đến ngày';
iwebos.message.paperwork.ed.report.list_dialog='Danh sách các báo cáo';
iwebos.message.paperwork.ed.report.dialog_ttile='Danh sách báo cáo';
iwebos.message.paperwork.ed.report.dialog_btile='Danh sách báo cáo trong hệ thống';
iwebos.message.paperwork.ed.report.execute='Thực hiện';
iwebos.message.paperwork.ed.report.exit='Thoát';
iwebos.message.paperwork.ed.report.condition_print='Điều kiện in báo cáo';
iwebos.message.paperwork.ed.report.unit_exe='Đơn vị thực hiện in báo cáo';
iwebos.message.paperwork.ed.report.unit_charge='Đơn vị chủ quản';
iwebos.message.paperwork.ed.report.unit_sub='Đơn vị trực thuộc';
iwebos.message.paperwork.ed.report.book_type='Loại sổ';
iwebos.message.paperwork.ed.report.description='Mô tả báo cáo';
iwebos.message.paperwork.ed.report.save_system='Lưu báo cáo vào hệ thống';
iwebos.message.paperwork.ed.report.from_number='Từ số';
iwebos.message.paperwork.ed.report.to_number='Đến số';

/**
 * Grouping
 */
iwebos.message.grouping.documents='Văn bản' ;
iwebos.message.grouping.works='Công việc';
iwebos.message.grouping.units='Đơn vị' ;
/**
 * document card.
 */
iwebos.message.paperwork.dc.created='Vừa tạo';
iwebos.message.paperwork.dc.processing='Đang xử lý';
iwebos.message.paperwork.dc.approved='Đã duyệt';
iwebos.message.paperwork.dc.watched='Theo dõi';
iwebos.message.paperwork.dc.completed='Hoàn thành';
iwebos.message.paperwork.dc.published='Xuất bản';
iwebos.message.paperwork.dc.deleted='Xoá';
/**
 * manage account
 */
iwebos.message.account.taskTitle='Tác vụ';
iwebos.message.account.manageAccount='Quản lý tài khoản';
iwebos.message.account.createAccount='Tạo tài khoản';
iwebos.message.account.updateAccount='Cập nhật';
iwebos.message.account.deleteAccount='Xóa tài khoản';
iwebos.message.account.lockAccount='Khóa tài khoản';
iwebos.message.account.unlockAccount='Mở khóa tài khoản';
iwebos.message.account.resetPassword='Đổi mật khẩu';
iwebos.message.account.resetPwdDialogTitle='Đổi mật khẩu';
iwebos.message.account.inputPwdDialogTitle='Nhập mật khẩu mới';
iwebos.message.account.resetPwdText='Nhập mật khẩu mới cho';

iwebos.message.mail.create.atitle='Thông tin người dùng';
iwebos.message.mail.create.title='Tài khoản email';
iwebos.message.mail.create.save='Lưu';
iwebos.message.mail.create.username='Tài khoản';
iwebos.message.mail.create.email='Email';
iwebos.message.mail.create.password='Mật khẩu';
iwebos.message.mail.create.confirmpassword='Xác nhận';
iwebos.message.mail.create.birthday='Ngày sinh';
iwebos.message.mail.create.sex='Giới tính';
iwebos.message.mail.create.city='Thành phố';
iwebos.message.mail.create.country='Quốc gia';
iwebos.message.mail.create.lastname='Họ';
iwebos.message.mail.create.middlename='Tên lót';
iwebos.message.mail.create.firstname='Tên';
iwebos.message.mail.create.fullname='Họ và tên';
iwebos.message.mail.create.province='Vùng';
iwebos.message.mail.create.language='Ngôn ngữ';
iwebos.message.mail.create.postcode='Mã bưu điện';
iwebos.message.mail.create.timezone='Timezone';
iwebos.message.mail.create.male='Nam';
iwebos.message.mail.create.female='Nữ';
iwebos.message.mail.create.englishLanguage='Tiếng Anh';
iwebos.message.mail.create.vietnameseLanguage='Tiếng Việt';
iwebos.message.mail.create.otherCountry='Khác';
iwebos.message.mail.create.vietnamese='Việt Nam';
iwebos.message.mail.create.department='Phòng ban';
iwebos.message.mail.create.manager='Người quản lý';
iwebos.message.mail.create.dialog_department_title='Thông tin phòng ban';
iwebos.message.mail.create.dialog_department_btitle='Nhập thông tin phòng ban cần tạo';
iwebos.message.mail.create.account_info='<b>1. Thông tin tài khoản:<br/><br/><b>';
iwebos.message.mail.create.personal_info='</br><b>2. Thông tin người dùng:<br/><br/><b>';
iwebos.message.mail.create.other_info='</br><b>3. Thông tin khác:<br/><br/><b>';
iwebos.message.mail.create.account='Tài khoản email';
iwebos.message.mail.create.email_info='<b>1. Thông tin tài khoản Email:<br/><br/><b>';
iwebos.message.mail.create.role_mail='</br><b>2. Thông tin cài đặt:<br/><br/><b>';
iwebos.message.mail.create.group_member='<br/><b>3. Nhóm thành viên</b><br/><br/>';
iwebos.message.mail.create.account_desc='<br/><i><b>Chú ý: </b><font color="#FF0000"><b>Chủ tài khoản là người sử dụng sở hữu địa chỉ email này.</b><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Chức năng tạo tài khoản email này chỉ sử dụng khi đã có tài khoản người dùng. <br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Nếu người sử dụng chưa tồn tại bạn nên chuyển qua mục "Thông tin người dùng"</b></i><br/></font><br/>';
iwebos.message.mail.create.role_smtp='Cho phép xác thực SMTP';
iwebos.message.mail.create.role_active='Kích hoạt tài khoản';
iwebos.message.mail.create.role_transfer='Cho phép chuyển tiếp';
iwebos.message.mail.create.role_virus='Kiểm tra Virus';
iwebos.message.mail.create.role_spam='Kiểm tra Spam';
iwebos.message.mail.create.sizelimit='Hạn mức(MB)';
iwebos.message.mail.create.search_account='Tìm kiếm';
iwebos.message.mail.create.alias='Tài khoản đại diện';
iwebos.message.mail.create.domain='Tên miền';
iwebos.message.mail.create.add='Tạo tài khoản email';
iwebos.message.mail.create.remove='Xoá tài khoản';
iwebos.message.mail.create.owner_account='Chủ tài khoản';
iwebos.message.mail.create.input_invalid_mail_uid='Tài khoản email không hợp lệ. Xin vui lòng kiểm tra lại.';
iwebos.message.mail.create.error_input_email='Bạn chưa nhập tài khoản email.';
iwebos.message.mail.create.error_input_domain_for_email='Bạn chưa chọn tên miền hoặc không được tạo phép tạo tài khoản email trên bất kỳ tên miền nào.';
iwebos.message.mail.create.error_input_owner='Bạn chưa chọn chủ tài khoản email.';
iwebos.message.mail.create.input_domain_info='Vui lòng điền các thông tin cần thiết cho một tên miền';
iwebos.message.mail.create.searching_mail='Tìm tài khoản mail';
iwebos.message.mail.create.searching_alias='Tìm tài khoản đại diện';
iwebos.message.mail.create.domains='Tên miền';
iwebos.message.mail.create.warning_delele_domain='Khi xoá tên miền thì tất cả tài khoản mail của tên miền này đều bị xóa. Bạn thật sự muốn xoá tên miền đã chọn?';
iwebos.message.mail.create.delete_domain_title='Xóa tên miền';
iwebos.message.account.savingTitle='Lưu thông tin';
iwebos.message.account.deleteAccountError='Không thể xóa tài khoản, có thể tài khoản của người dùng này đã bị xóa rồi trước đó.';
iwebos.message.account.deleteAccountConfirm='Nếu bạn thực hiện tác vụ này thì tất cả tài khoản email của người dùng sẽ bị xoá hết. Bạn thật sự muốn thực hiện tác vụ?';
iwebos.message.account.resetPwdSuccessful='Đã thay đổi mật khẩu của người dùng';
iwebos.message.account.resetPwdError='Có lỗi xảy ra trong quá trình thay đổi mật khẩu';
iwebos.message.account.inputNewPwd='Bạn phải nhập mật khẩu mới';
iwebos.message.account.lockAccountConfirm='Nếu bạn thực hiện tác vụ này thì tất cả tài khoản email của người dùng đều bị khoá hết. Bạn thật sự muốn thực hiện tác vụ?';
iwebos.message.account.unlockAccountConfirm='Nếu bạn thực hiện tác vụ này thì tất cả tài khoản email của người dùng sẽ được mở khoá. Bạn thật sự muốn thực hiện tác vụ?';
iwebos.message.account.taskSuccessfull='Đã thực hiện tác vụ thành công';
iwebos.message.account.taskError='Có lỗi xảy ra trong quá trình thực hiện tác vụ';
iwebos.message.account.infomationMessage='Thông báo';
// mail admin- create account 
iwebos.message.mail.email_address='Email';
iwebos.message.mail.error='Thông báo';
iwebos.message.mail.error_input_username='Bạn phải nhập tài khoản người dùng.';
iwebos.message.mail.error_input_pass='Bạn phải nhập mật khẩu của người dùng';
iwebos.message.mail.error_input_email='Bạn phải nhập địa chỉ email của người dùng';
iwebos.message.mail.error_input_country='Bạn phải nhập quốc gia của người dùng';
iwebos.message.mail.error_input_gmt='Bạn phải nhập múi giờ';
iwebos.message.mail.error_input_confirm_pass='Mật khẩu không trùng khớp';
iwebos.message.mail.error_create_account='Có lỗi xảy ra trong quá trình tạo tài khoản, Có thể tài khoản người dùng hoặc tài khoản email của người dùng đã tồn tại rồi trong hệ thống.Xin vui lòng kiểm tra lại.';
iwebos.message.mail.error_select_email='Bạn phải chọn một địa chỉ email';
iwebos.message.mail.error_create_domain='Có lỗi xảy ra trong quá trình tạo tên miền. Có thể do tên miền không hợp lệ, xin vui lòng kiểm tra lai.';
iwebos.message.mail.error_update_domain='Có lỗi xảy ra trong quá trình cập nhật tên miền.';
iwebos.message.mail.error_update_data='Có lỗi xảy ra trong quá trình cập nhật dữ liệu.';
iwebos.message.mail.error_delete_domain='Có lỗi xảy ra trong quá trình xóa tên miền.';
iwebos.message.mail.error_find_account_information='Không tìm thấy thông tin tài khoản, có thể tài khoản này đã bị xoá trước đó';
iwebos.message.mail.error_input_search_key_first='Bạn phải nhập ít nhất ';
iwebos.message.mail.error_input_search_key_second=' ký tự mới có thể tìm kiếm tài khoản';
// mail admin - create alias
iwebos.message.mail.error_input_alias_account='Bạn phải nhập tài khoản nhóm';
iwebos.message.mail.error_input_alias_pass='Mật khẩu không được để trống hoặc không trùng khớp';
iwebos.message.mail.error_input_alias_lastname='Bạn chưa nhập họ của người dùng';
iwebos.message.mail.error_input_alias_firstname='Bạn chưa nhập tên của người dùng';
iwebos.message.mail.add_email_account='Thêm email';
iwebos.message.mail.add_outside_email_account='Thêm email bên ngoài';
iwebos.message.mail.delete_email_account='Xoá tài khoản email';
// mail message
iwebos.message.mail.save_data_sucessful='Đã lưu dữ liệu thành công';
iwebos.message.mail.saving_mail_account='Lưu trữ tài khoản';
iwebos.message.mail.saving_mail_alias='Lưu trữ nhóm';
iwebos.message.mail.alias_select_email_to_delete='Bạn phải chọn email cần xoá';
iwebos.message.mail.alias_input_email_to_add_to_alias='Bạn phải nhập đỉa chì email cần thêm vào tài khoản đại diện';
iwebos.message.mail.alias_input_email_title='Nhập địa chỉ email';
iwebos.message.mail.alias_input_email_subtitle='Nhập địa chỉ email cần thêm vào tài khoản đại diện';
// mail admin - create email
iwebos.message.mail.error_input_email='Bạn phải nhập địa chỉ email của người dùng';
iwebos.message.mail.error_input_fullname='Bạn chưa nhập họ và tên người dùng';
iwebos.message.mail.error_input_limit='Bạn chưa hạn mức của hộp thư';
iwebos.message.mail.error_input_mail_drop='Bạn phải chọn thành viên thuộc nhóm.';
iwebos.message.mail.error_delete_alias='Có lỗi xảy ra trong quá trình xóa nhóm.';
iwebos.message.mail.error_delete_mail='Có lỗi xảy ra trong quá trình xóa tài khoản mail.';
iwebos.message.mail.error_create_department='Có lỗi xảy ra trong quá trình tạo phòng ban';
// mail admin - dialog create domain
iwebos.message.mail.save='Lưu';
iwebos.message.mail.ok='Đồng ý';
iwebos.message.mail.cancel='Huỷ bỏ';
iwebos.message.mail.admin='Người quản trị';
iwebos.message.mail.domain='Tên miền';
iwebos.message.mail.domain_desc='Mô tả';
iwebos.message.mail.create_domain='Thông tin tên miền';
iwebos.message.mail.error_input_fullname='Bạn chưa nhập họ và tên người dùng';
iwebos.message.mail.error_input_info='Bạn phải nhập đầy đủ thông tin';
iwebos.message.mail.enable_modify_email='Cho phép sửa tài khoản email';
iwebos.message.mail.error_select_domain_to_view='Bạn phải chọn tên miền cần xem';
// mail admin - dialog search account
iwebos.message.mail.account='Tài khoản';
iwebos.message.mail.mail_account='Tài khoản email';
iwebos.message.mail.name='Tên';
iwebos.message.mail.fullname='Họ và tên';
iwebos.message.mail.select_account='Chọn tài khoản';
iwebos.message.mail.search_account='Tìm kiếm tài khoản';
iwebos.message.mail.error_select_account='Bạn phải chọn người dùng';
iwebos.message.mail.forward_address='Địa chỉ';
iwebos.message.mail.input_address_need_to_forward='Nhập địa chỉ mail để chuyển tiếp';
iwebos.message.mail.warning_input_address_need_to_forward='Bạn phải nhập địa chỉ mail để chuyển tiếp';
// mail admin - Dialog search alias
iwebos.message.mail.search_alias='Tìm kiếm tài khoản mail';
iwebos.message.mail.select_mail_account='Chọn tài khoản mail đã có trong hệ thống';
// mail admin - Mail panel
iwebos.message.mail.confirm_logout='Bạn có chắc là muốn thoát khỏi quyền truy cập?';
iwebos.message.mail.create_date='Ngày tạo';
iwebos.message.mail.manage_account='Tài khoản email';
iwebos.message.mail.active_account='Kích hoạt tài khoản';
iwebos.message.mail.inactive_account='Không kích hoạt tài khoản';
iwebos.message.mail.autheticate_smtp_yes='Xác thực SMTP';
iwebos.message.mail.autheticate_smtp_no='Không xác thực SMTP';
iwebos.message.mail.forward_yes='Chuyển tiếp';
iwebos.message.mail.forward_no='Không chuyển tiếp';
iwebos.message.mail.check_virus='Kiểm tra virus';
iwebos.message.mail.uncheck_virus='Không kiểm tra virus';
iwebos.message.mail.check_spam='Kiểm tra spam';
iwebos.message.mail.uncheck_spam='Không kiểm tra spam';
iwebos.message.mail.create_email_account='Tạo tài khoản email';
iwebos.message.mail.create_alias_account='Tạo tài khoản đại diện';
iwebos.message.mail.delete_account='Xóa tài khoản';
iwebos.message.mail.update_account='Cập nhật';
iwebos.message.mail.delete_alias='Xóa nhóm';
iwebos.message.mail.error_perform_task='Có lỗi xảy ra trong quá trình thực hiện tác vụ';
iwebos.message.mail.confirm_delete_account_mail='Bạn thật sự muốn xóa tài khoản đã chọn?';
iwebos.message.mail.confirm_delete_account_alias='Bạn thật sự muốn xóa nhóm đã chọn?';
iwebos.message.mail.active_domain='Kích hoạt tên miền';
iwebos.message.mail.inactive_domain='Không kích hoạt tên miền';
iwebos.message.mail.update_domain='Cập nhật tên miền';
iwebos.message.mail.delete_domain='Xóa tên miền';
iwebos.message.mail.org='Tổ chức';
iwebos.message.mail.view_info_account= 'Danh sách tài khoản email của người dùng';
iwebos.message.mail.empty_fname='Bạn phải nhập họ của người dùng mới có thể tạo tài khoản mail';
iwebos.message.mail.empty_lname='Bạn phải nhập tên của người dùng mới có thể tạo tài khoản mail';
iwebos.message.mail.empty_mail_account='Bạn phải nhập ít nhất một thông tin email';
iwebos.message.mail.update_email_account='Cập nhật tài khoản email';
iwebos.message.mail.delete_email_account='Xoá tài khoản email';
iwebos.message.mail.dialog_select_account_title='Hộp thoại chọn tài khoản';
iwebos.message.mail.dialog_select_account_btitle='Chọn tài khoản để gắn vào cơ quan';
iwebos.message.mail.options='Thông tin tuỳ chọn';
iwebos.message.mail.dialog_search_user_btitle='Tìm kiếm tài khoản sẵn có';
iwebos.message.mail.domain_add_manager='Thêm người quản trị';
iwebos.message.mail.domain_delete_manager='Xóa người quản trị';
iwebos.message.mail.domain_list_domain_manager='Nhóm người quản trị tên miền';
// report
iwebos.message.mail.report_order_number='STT';
iwebos.message.mail.report_domain='Tên miền';
iwebos.message.mail.report_domain_desc='Mô tả';
iwebos.message.mail.report_total_domain='Số tên miền';
iwebos.message.mail.report_email='Tài khoản email';
iwebos.message.mail.report_total_email='Số tài khoản email';
iwebos.message.mail.report_alias='Tài khoản đại diện';
iwebos.message.mail.report_total_alias='Số tài khoản đại diện';
iwebos.message.mail.report_statistic_title='Thống kê tài khoản';
iwebos.message.mail.report_detail_button='Chi tiết';
iwebos.message.mail.report_list_account='DANH SÁCH TÀI KHOẢN';
iwebos.message.mail.report_list_account_title='Danh sách tài khoản';
iwebos.message.mail.report_select_domain='Bạn phải chọn tên miền cần xem';
iwebos.message.mail.report_loading_error='Có lỗi xảy ra trong quá trình tải dữ liệu';
iwebos.message.mail.report_statistic_system_title='THỐNG KÊ TÊN MIỀN';
iwebos.message.mail.report_statistic_common_title='Thống kê chung';
iwebos.message.mail.report_statistic_domain_title='Chi tiết trên từng tên miền';
iwebos.message.mail.report_print='Bản in';
iwebos.message.mail.report_print_tooltip='In dữ liệu';


// account information.
iwebos.message.account.detail_info='Thông tin chi tiết' ;
iwebos.message.account.detail_info_owner_account='Chủ tài khoản';
iwebos.message.account.detail_info_username='Tài khoản';
iwebos.message.account.detail_info_options='Tuỳ chọn';
iwebos.message.account.detail_info_sizelimit='Hạn mức';
iwebos.message.account.detail_info_virus='Kiểm tra Virus';
iwebos.message.account.detail_info_spam='Kiểm tra Spam';
iwebos.message.account.detail_info_smtp='Xác thực SMTP';
iwebos.message.account.detail_info_active='Kích hoạt tài khoản';
iwebos.message.account.detail_info_transfer='Cho phép chuyển tiếp';
iwebos.message.account.accept_value='Có';
iwebos.message.account.non_accept_value='Không';

//////////////////////////////////////////////////
//organization information.
//////////////////////////////////////////////////
iwebos.message.org.message='Thông báo';
iwebos.message.org.ok='Đồng ý';

iwebos.message.org.organization='Tổ chức';

iwebos.message.org.group='Nhóm';
iwebos.message.org.group_information='Thông tin nhóm';
iwebos.message.org.group_name='Tên viết tắt';
iwebos.message.org.group_description='Tên đầy đủ';
iwebos.message.org.group_manager='Người quản trị của nhóm';
iwebos.message.org.group_member='Thành viên thuộc nhóm';
iwebos.message.org.group_management='Quản lý nhóm';
iwebos.message.org.group_create_group='Tạo nhóm';
iwebos.message.org.group_update_group='Cập nhật nhóm';
iwebos.message.org.group_delete_group='Xóa nhóm';
iwebos.message.org.group_select_new_user='Thêm mới';
iwebos.message.org.group_remove_user='Xóa';

iwebos.message.org.message_group_select_org='Bạn phải chọn tổ chức cần tạo nhóm';
iwebos.message.org.message_group_input_group_name='Bạn phải nhập tên nhóm cần tạo';
iwebos.message.org.message_group_select_group_manager='Bạn phải chọn người quản trị của nhóm';
iwebos.message.org.message_group_saving_successful='Đã lưu trữ dữ liệu thành công';
iwebos.message.org.message_group_warning_delete_group='Bạn thật sự muốn xóa nhóm đã chọn';
iwebos.message.org.message_group_select_user_for_group='Bạn phải chọn người dùng cần thêm vào nhóm';
iwebos.message.org.message_group_select_user_delete_out_of_group='Bạn phải chọn người dùng cần xóa khỏi nhóm';
iwebos.message.org.error_group_error_load_group='Có lỗi xảy ra trong quá trình tải thông tin nhóm';
iwebos.message.org.error_group_error_saving_data='Có lỗi xảy ra trong quá trình lưu trữ dữ liệu';
iwebos.message.org.error_group_delete_group='Có lỗi xảy ra trong quá trình xóa nhóm';
iwebos.message.org.error_group_load_all_group_in_org='Có lỗi xảy ra trong quá trình tải nhóm';

iwebos.message.org.user='Người dùng';
iwebos.message.org.user_task='Tác vụ';
iwebos.message.org.user_account_information='Thông tin tài khoản';
iwebos.message.org.user_name='Tài khoản';
iwebos.message.org.user_account='Tài khoản người dùng';
iwebos.message.org.user_password='Mật khẩu';
iwebos.message.org.user_confirm_password='Xác nhận lại';
iwebos.message.org.user_information='Thông tin người dùng';
iwebos.message.org.user_last_name='Họ';
iwebos.message.org.user_middle_name='Tên lót';
iwebos.message.org.user_first_name='Tên';
iwebos.message.org.user_full_name='Họ và tên';
iwebos.message.org.user_employee_number='Mã NV';
iwebos.message.org.user_email='Email';
iwebos.message.org.user_alternate_email="Email dự phòng";
iwebos.message.org.user_mobile='Di động';
iwebos.message.org.user_title='Chức vụ';
iwebos.message.org.user_word_phone='ĐT cơ quan';
iwebos.message.org.user_home_phone='ĐT nhà';
iwebos.message.org.user_add_email='Tạo tài khoản email';
iwebos.message.org.user_delete_user='Xóa người dùng ra khỏi tổ chức';
iwebos.message.org.user_select_email_to_delete='Lựa chọn phương pháp xóa thông tin đi kèm của người dùng';
iwebos.message.org.user_select_delete_email_from_grid='Chọn tài khoản email cần xóa từ danh sách';
iwebos.message.org.user_non_delete_email='Không xóa tài khoản email của người dùng này';
iwebos.message.org.user_delete_all_email='Xoá tất cả tài khoản email của người dùng này';
iwebos.message.org.user_delete_user_out_org_and_email='Bạn muốn khi thực hiện tác vụ này sẽ <b>xoá người dùng khỏi tổ chức</b> và';
iwebos.message.org.user_find_user_in_org='Tìm kiếm tài khoản người dùng trong tổ chức';
iwebos.message.org.user_select_user='Chọn người dùng';
iwebos.message.org.user_in_org='Thông tin người dùng';
iwebos.message.org.user_add_new='Tạo người dùng';
iwebos.message.org.user_update='Cập nhật';
iwebos.message.org.user_reset_password='Đổi mật khầu';
iwebos.message.org.user_reset_delete='Xóa người dùng';
iwebos.message.org.user_add_member='Thêm thành viên';
iwebos.message.org.user_remove_member='Gỡ bỏ thành viên';
iwebos.message.org.user_create_email_account_title='Tài khoản email';
iwebos.message.org.user_create_email_account_sub_title='Nhập thông tin cần thiết cho một tài khoản email';
iwebos.message.org.user_not_have_permition_to_user_function='Bạn không được phép sử dụng chức năng này';
iwebos.message.org.user_email_check='Sử dụng tài khoản này làm tài khoản email';
iwebos.message.org.user_email_forward_check='Tài khoản email chuyển tiếp';

iwebos.message.org.message_user_input_user_name='Bạn phải nhập tên tài khoản';
iwebos.message.org.message_user_input_user_password='Bạn phải nhập mật khầu của người dùng';
iwebos.message.org.message_user_password_not_match='Mật khẩu không trùng khớp';
iwebos.message.org.message_user_input_last_name='Bạn phải nhập họ của người dùng';
iwebos.message.org.message_user_input_first_name='Bạn phải nhập tên của người dùng';
iwebos.message.org.message_user_input_email='Bạn phải nhập địa chỉ email của người dùng';
iwebos.message.org.message_user_input_email_invalid='Địa chỉ email không hợp lệ';
iwebos.message.org.message_user_invalid_home_number='Số điện thoại nhà không hợp lệ';
iwebos.message.org.message_user_invalid_work_number='Số điện thoại cơ quan không hợp lệ';
iwebos.message.org.message_user_invalid_mobile_number='Số điện thoại di động không hợp lệ';
iwebos.message.org.message_user_not_permit_update_user='Bạn không được phép cập nhật thông tin của người dùng này.';
iwebos.message.org.message_user_saving_successful='Đã lưu trữ dữ liệu thành công';
iwebos.message.org.message_user_add_exist_user='Người dùng này đã tồn tại trong hệ thống, xin vui lòng kiểm tra lại.';
iwebos.message.org.message_user_delete_email='Bạn phải chọn tài khoản email cần xóa';
iwebos.message.org.message_user_can_not_delete_manager_group='Người dùng {0} đang là người quản trị của nhóm {1} nên không thể xóa được';
iwebos.message.org.message_user_can_not_remove_manager_group_out_group='Người dùng này đang là người quản lý của nhóm nên không thể gỡ khỏi nhóm được';
iwebos.message.org.message_user_invalid_user_name='Tài khoản không hợp lệ, vui lòng kiểm tra lại';
iwebos.message.org.warning_user_can_not_manage_user='Người dùng này không thuôc quyền quản lý của bạn, nên khi tạo email cho người dùng này bạn cũng không thể quản lý được tài khoản email này được.';
iwebos.message.org.warning_user_input_user_name_to_check='Bạn phải nhập tài khoản cần kiểm tra';
iwebos.message.org.error_user_cheking_user_name_to_check='Có lỗi xảy ra trong quá trình kiểm tra tài khoản';
iwebos.message.org.error_user_load_user='Có lỗi xảy ra trong quá trình tải thông tin người dùng';
iwebos.message.org.error_user_load_account='Có lỗi xày ra trong quá trình tải thông tin tài khoản';
iwebos.message.org.error_user_saving_user='Có lỗi xảy ra trong quá trình lưu trữ thông tin người dùng';
iwebos.message.org.error_user_delete_user='Có lỗi xảy ra trong quá trình xóa người dùng';
iwebos.message.org.error_user_not_permit_delete_user='Bạn không được phép xoá người dùng này vì người dùng này đang thuộc về nhiều phòng ban khác nhau';
iwebos.message.org.error_user_not_exist_in_org='Người dùng này không tồn tại trong tổ chức đang chọn';
iwebos.message.org.error_user_check_delete_user='Có lỗi xảy ra trong quá trình kiểm tra quyền xoá người dùng';
iwebos.message.org.error_user_add_member_to_group='Có lỗi xảy ra trong quá trình thêm thành viên';
iwebos.message.org.error_user_not_exist_in_group='Người dùng này không thuộc nhóm dang chọn';
iwebos.message.org.error_user_remove_member_out_group='Có lỗi xảy ra trong quá trình gỡ bỏ thành viên';