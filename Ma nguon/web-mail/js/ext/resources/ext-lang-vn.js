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
iwebos.message.mail.mail_reply_all = "Trả lời tất cả";
iwebos.message.mail.mail_reply_all_tip = "Trả lời tất cả";
iwebos.message.mail.mail_forward = "Chuyển tiếp";
iwebos.message.mail.mail_forward_tip = "Chuyển tiếp thư đến địa chỉ khác";
iwebos.message.mail.mail_print = "In";
iwebos.message.mail.mail_print_tip = "In ra";
iwebos.message.mail.mail_more_action = "Thao tác khác";
iwebos.message.mail.mail_more_action_tip = "Các thao tác khác";
iwebos.message.mail.mail_group= "Nhóm thư";
iwebos.message.mail.mail_group_tip= "Nhóm thư";
iwebos.message.mail.mail_more_action_groupbyday = "Nhóm theo ngày nhận";
iwebos.message.mail.mail_more_action_groupbyfrom = "Nhóm theo người gửi";
iwebos.message.mail.mail_more_action_groupbysubject = "Nhóm theo tiêu đề";
iwebos.message.mail.mail_send = "Gửi";
iwebos.message.mail.mail_send_tip = "Gửi thư";
iwebos.message.mail.mail_attachment = "Đính kèm";
iwebos.message.mail.mail_attachment_tip = "Đính kèm theo tài liệu";
iwebos.message.mail.mail_save_draf = "Lưu";
iwebos.message.mail.mail_save_draf_tip ="Lưu dưới dạng bản nháp";
iwebos.message.mail.announcement ="Thông báo";
iwebos.message.mail.confirm_delete_email="Bạn có thực sự muốn xóa thư điện tử này không?";
iwebos.message.mail.create_folder="Tạo thư mục";
iwebos.message.mail.delete_folder="Xóa thư mục";
iwebos.message.mail.rename_folder="Đổi tên thư mục";
iwebos.message.mail.view_mail="Xem thư";
iwebos.message.mail.subject="Chủ đề";
iwebos.message.mail.server='Máy chủ';
iwebos.message.mail.close='Đóng';
iwebos.message.mail.mail_spam='Thư rác';
iwebos.message.mail.mail_not_spam='Không phải thư rác';
iwebos.message.mail.mail_view='Xem theo';
iwebos.message.mail.mail_unread='Thư chưa đọc';
iwebos.message.mail.mail_attched='Thư có đính kèm';
iwebos.message.mail.mail_priority_high='Thư quan trọng';

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
iwebos.message.mail.context_filter = "Đánh dấu thư rác";
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
iwebos.message.mail.context_empty_folder = "Xóa tất cả thư";

//Mail Compose
iwebos.message.mail.compose_to ="Gửi đến";
iwebos.message.mail.compose_cc ="Cc   ";
iwebos.message.mail.compose_bcc ="Bcc   ";
iwebos.message.mail.compose_subject ="Tiêu đề";
iwebos.message.mail.remove="Gỡ bỏ";
iwebos.message.mail.msg_reply='Vào lúc %s, %s đã viết:';
iwebos.message.mail.priority='Độ khẩn';
iwebos.message.mail.priority_high='Cao';
iwebos.message.mail.priority_normal='Bình thường';
iwebos.message.mail.priority_low='Thấp';
iwebos.message.mail.show_bcc='Hiện Bcc';
iwebos.message.mail.show_bcc_tip='Ẩn/hiện địa chỉ Bcc';
iwebos.message.mail.hide_bcc='Ẩn Bcc';

iwebos.message.mail.error_subject_empty="Bạn chưa điền chủ đề của email.";
iwebos.message.mail.error_email_address_empty="Bạn chưa điền địa chỉ email cần gửi.";
iwebos.message.mail.error_email_address="Địa chỉ email ";
iwebos.message.mail.error_email_address_invalid=" không hợp lệ.";
iwebos.message.mail.error_config_fetch_mail="Tài khoản chưa được thiết lập cấu hình. Bạn hãy vào chức năng \"Cấu hình\" để khởi tạo tài khoản.";
iwebos.message.mail.error_signature_duplicate="Tên chữ ký bị trùng. Bạn vui lòng nhập lại tên khác.";
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

//Error
iwebos.message.mail.error_check_mail ="Có lỗi trong quá trình kiểm tra thư điện tử.";
iwebos.message.mail.error_delete_mail ="Có lỗi trong quá trình xóa thư điện tử.";
iwebos.message.mail.error_task ="Có lỗi trong quá trình thực hiện tác vụ.";
iwebos.message.mail.error_smtp = "Bạn chưa cấu hình tài khoản SMTP để gửi mail.";
iwebos.message.mail.error_smtp2 = "Bạn chưa cấu hình tài khoản để nhận mail.";

//Search email
iwebos.message.mail.search_all="Tất cả";
iwebos.message.mail.search_subject="Theo chủ đề";
iwebos.message.mail.search_date="Theo ngày gửi";
iwebos.message.mail.search_sender="Theo người gửi";

//Config Account
iwebos.message.mail.email='Địa chỉ email';
iwebos.message.mail.cancel='Huỷ bỏ';
iwebos.message.mail.save='Lưu';
iwebos.message.mail.create_account='Tạo tài khoản';
iwebos.message.mail.add_configuration='Thêm cấu hình';
iwebos.message.mail.account_name='Tên tài khoản';
iwebos.message.mail.fullname='Họ và tên';
iwebos.message.mail.address='Địa chỉ';
iwebos.message.mail.org='Tổ chức';
iwebos.message.mail.signature='Chữ ký';
iwebos.message.mail.configure='Cấu hình';
iwebos.message.mail.info_common='Thông tin chung';
iwebos.message.mail.account='Tài khoản';
iwebos.message.mail.pass='Mật khẩu';
iwebos.message.mail.pass_confirm='Xác nhận MK';
iwebos.message.mail.port='Cổng';
iwebos.message.mail.security='Bảo mật';
iwebos.message.mail.security_mode='Chế độ bảo mật';
iwebos.message.mail.auto_check='Gửi/nhận thư sau';
iwebos.message.mail.minute='Phút';
iwebos.message.mail.configure_account='Cấu hình tài khoản';
iwebos.message.mail.error_pass_confirm='Mật khẩu tài khoản email không trùng khớp.';
iwebos.message.mail.error_save_configure='Có lỗi xảy ra trong quá trình lưu cấu hình';
iwebos.message.mail.error_load_configure='Có lỗi xảy ra trong quá trình nạp cấu hình';
iwebos.message.mail.error_load_configure_default='Có lỗi xảy ra trong quá trình lấy cấu hình mặc định';
iwebos.message.mail.success_save_configure='Cấu hình thư điện tử đã được lưu.';
iwebos.message.mail.server_type='Loại máy chủ';
iwebos.message.mail.leave_message_on_server='Lưu thư trên máy chủ';
iwebos.message.mail.authenticate_n_security='Chứng thực và chế độ bảo mật';
iwebos.message.mail.setup_properties_for_current_account='Thiết lập các thuộc tính cho tài khoản hiện đang sử dụng';
iwebos.message.mail.delete_account='Xóa tài khoản';
iwebos.message.mail.create_new_signature='....Tạo mới';
iwebos.message.mail.active='Kích hoạt';
iwebos.message.mail.description='Mô tả';
iwebos.message.mail.smtp_default='SMTP mặc định';
iwebos.message.mail.new_mail='Email mới';
iwebos.message.mail.old_mail='Email cũ';
iwebos.message.mail.check_mail='Kiểm tra thư';
iwebos.message.mail.receive_mail='Nhận thư';
iwebos.message.mail.receive_pop3='<br/><b>Nhận Mail(POP3/IMAP):</b><br/>';
iwebos.message.mail.send_smtp='<br/><b>Gửi Mail (SMTP):</b><br/>';
iwebos.message.mail.protocol_type='Loại';
iwebos.message.mail.confirm='Xác nhận';
// mail folder
iwebos.message.mail.folder_parent='Thư mục cha';
iwebos.message.mail.folder_name='Tên thư mục';
iwebos.message.mail.error_blank_folder_name='Tên thư mục không được để trống.';
iwebos.message.mail.confirm_delete_folder='Bạn có thực sự muốn xóa thư mục này không?';
iwebos.message.mail.error_mark_as_read_folder='Có lỗi xảy ra trong quá trình đánh dấu đã đọc thư mục';
// mail contact
iwebos.message.mail.address_global='Địa chỉ công ty';
iwebos.message.mail.address_personal='Địa chỉ cá nhân';
iwebos.message.mail.select_contact='Chọn người dùng';
iwebos.message.mail.ok='Đồng ý';
iwebos.message.mail.search='Tìm kiếm';
iwebos.message.mail.dialog_select_contact='Hộp thoại chọn người dùng';
iwebos.message.mail.setup_add_email='Thiết lập thêm địa chỉ email khi gửi email này';
// mail signature 
iwebos.message.mail.signature_name='Tên chữ ký';
iwebos.message.mail.create_signature='Tạo chữ ký';
iwebos.message.mail.create_signature_for_user='Tạo chữ ký cho người dùng';
iwebos.message.mail.error_input_signature_name='Bạn chưa nhập tên của chữ ký.';
// mail filter
iwebos.message.mail.filter_mail='Bộ lọc thư';
iwebos.message.mail.filter_info='Thông tin hiện tại về bộ lọc thư';
iwebos.message.mail.filter_detail='Mô tả chi tiết:';
iwebos.message.mail.filter_operator_and='Nếu có tất cả các chuẩn';
iwebos.message.mail.filter_operator_or='Nếu có bất kỳ chuẩn nào';
iwebos.message.mail.filter_action_send_to_folder='Chuyển đến thư mục';
iwebos.message.mail.filter_action_delete='Xóa thư';
iwebos.message.mail.filter_name='Tên bộ lọc';
iwebos.message.mail.filter_search_follow_standar='Tìm kiếm những thư theo chuẩn sau';
iwebos.message.mail.filter_unit_add='Thêm';
iwebos.message.mail.filter_search_email='Tìm những thư';
iwebos.message.mail.filter_object_sender='Người gửi';
iwebos.message.mail.filter_object_recipient='Người nhận';
iwebos.message.mail.filter_object_subject='Chủ đề';
iwebos.message.mail.filter_object_content='Nội dung';
iwebos.message.mail.filter_object_date_sent='Ngày gửi';
iwebos.message.mail.filter_object_date_received='Ngày';
iwebos.message.mail.filter_object_all='Tất cả';

iwebos.message.mail.filter_operator_contain='chứa nội dung';
iwebos.message.mail.filter_operator_does_not_contain='không chứa nội dung';
iwebos.message.mail.filter_operator_is='là';
iwebos.message.mail.filter_operator_is_not='không phải là';
iwebos.message.mail.filter_operator_start_with='bắt đầu với';
iwebos.message.mail.filter_operator_does_not_start_with='không bắt đầu với';
iwebos.message.mail.filter_operator_end_with='kết thúc với';
iwebos.message.mail.filter_operator_does_not_end_with='không kết thúc với';
iwebos.message.mail.filter_remove='Gỡ bỏ';
iwebos.message.mail.filter_folder='Thư mục chuyển';
iwebos.message.mail.filter_add='Thêm bộ lọc mới';
iwebos.message.mail.filter_delete='Xóa bộ lọc';
iwebos.message.mail.filter_add_new='Tạo bộ lọc thư mới';
iwebos.message.mail.filter_create='Tạo bộ lọc thư';
iwebos.message.mail.filter_create_new_to_management_email='Tạo mới bộ lọc thư để dễ dàng quản lý thư gửi đến';
iwebos.message.mail.select_folder_for_filter='Chọn thư mục cho bộ lọc';
iwebos.message.mail.select_folder_to_move='Chọn thư mục để chuyển thư đến';
iwebos.message.mail.select='Chọn';
iwebos.message.mail.close='Đóng';


iwebos.message.mail.filter_error_input_name='Bạn chưa nhập tên của bộ lọc.';
iwebos.message.mail.filter_error_input_folder='Bạn chưa chọn thư mục chuyển đến của thư.';
iwebos.message.mail.filter_error_save='Có lỗi xảy ra trong quá trình lưu bộ lọc.';
iwebos.message.mail.filter_error_delete='Có lỗi xảy ra trong quá trình xóa bộ lọc.';
iwebos.message.mail.view_mail_error='Thư này đã bị xoá hoặc có lỗi kết nối đến máy chủ.';
iwebos.message.mail.check_mail_error_tootip='Kiểm tra thư không hoàn thành vì cấu hình của những tài khoản sau không đúng';
//follow up
iwebos.message.mail.follow_up_make_work='Công việc sắp tới ';
//validator
iwebos.message.mail.confirm_stop_fetch='Bạn có muốn dừng việc gửi/nhận thư không?';
iwebos.message.mail.confirm_empty_folder='Bạn có muốn xóa tất cả thư trong thư mục này không?';
iwebos.message.mail.error_empty_mail='Địa chỉ email không được để trống';
iwebos.message.mail.error_empty_content='Nội dung mô tả không được để trống';
iwebos.message.mail.error_empty_server='Bạn cần phải nhập tên máy chủ nhận mail';
iwebos.message.mail.error_empty_receive_mail='Bạn chưa nhập tên tài khoản nhận mail';
iwebos.message.mail.error_empty_password='Bạn chưa nhập mật khẩu nhận mail';
iwebos.message.mail.error_empty_port= 'Bạn chưa nhập cổng nhận mail';
iwebos.message.mail.error_empty_send_mail='Bạn chưa nhập tên tài khoản gửi mail';
iwebos.message.mail.error_empty_password_send='Bạn chưa nhập mật khẩu gửi mail';
iwebos.message.mail.error_empty_server_send='Bạn chưa điền máy chủ gửi mail';
iwebos.message.mail.error_empty_port_send= 'Bạn chưa nhập cổng gửi mail';
iwebos.message.mail.enter_password='Nhập mật khẩu';
iwebos.message.mail.confirm_password='Xác nhận mật khẩu';
iwebos.message.mail.error_enter_password='Hiện tại tài khoản bạn đưa vào đã sai mật khẩu, vui lòng nhập lại.';
iwebos.message.mail.get_config='Lấy cấu hình';
iwebos.message.mail.select='Chọn';
iwebos.message.mail.close='Đóng';

/**
 * AddressBook
 */
iwebos.message.mail.contact.title='Sổ địa chỉ';
iwebos.message.mail.contact.dialog_title='Thông tin liên hệ';
iwebos.message.mail.contact.dialog_desc='Thông tin chi tiết.';
iwebos.message.mail.contact.grid_group_label='người';
iwebos.message.mail.contact.department='Nhóm';
iwebos.message.mail.contact.global='Hệ thống';
iwebos.message.mail.contact.personal='Cá nhân';
iwebos.message.mail.contact.add='Tạo mới';
iwebos.message.mail.contact.remove='Xoá';
iwebos.message.mail.contact.file_import='Kết nhập';
iwebos.message.mail.contact.file_export='Kết xuất';
iwebos.message.mail.contact.group_info='Thông tin';
iwebos.message.mail.contact.lname='Họ';
iwebos.message.mail.contact.fname='Tên';
iwebos.message.mail.contact.dname='Tên hiển thị';
iwebos.message.mail.contact.email='Email';
iwebos.message.mail.contact.group_phone='Thông tin chi tiết';
iwebos.message.mail.contact.hphone='Nhà riêng';
iwebos.message.mail.contact.mphone='Di động';
iwebos.message.mail.contact.fax='Fax';
iwebos.message.mail.contact.pager='Pager';
iwebos.message.mail.contact.group_other='Thông tin khác';
iwebos.message.mail.contact.group='Nhóm';
iwebos.message.mail.contact.addr='Địa chỉ';
iwebos.message.mail.contact.all_group='Tất cả nhóm';
iwebos.message.mail.contact.delete_confirm='Bạn có thật sự muốn xoá không ?';
iwebos.message.mail.contact.delete_error='Bạn chỉ được chọn một người khi xoá.';
iwebos.message.mail.contact.mail_member='Email thành viên';
iwebos.message.mail.contact.error_valid_data='Dữ liệu nhập chưa phù hợp.Xin vui lòng kiểm tra lại';
iwebos.message.mail.contact.add_error='Người dùng đã tồn tại trong hệ thống hoặc có lỗi xảy ra trong quá trình cập nhật dữ liệu.';
iwebos.message.mail.contact.update_error='Có lỗi xảy ra trong quá trình cập nhật dữ liệu.';
iwebos.message.mail.contact.send_message='Gửi thư đến ...';
/** Html plugin **/
iwebos.message.mail.table_title='Chèn bảng';
iwebos.message.mail.table_insert ='Chèn';
iwebos.message.mail.table_rows ='Hàng';
iwebos.message.mail.table_colum ='Cột';
iwebos.message.mail.table_border ='Đường viền';
iwebos.message.mail.table_cell_label ='Nhãn trên ô';
