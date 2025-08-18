package com.hien.back_end_app.utils.enums;


import lombok.Getter;


@Getter
public enum MailTemplateType {
    ACCOUNT_REGISTRATION("Đăng ký tài khoản thành công", "Cảm ơn bạn đã đăng ký. Hãy trải nghiệm khám phá thỏa thích , mong bạn vui lòng khi sử dụng dịch vụ của chúng tôi"),
    PASSWORD_RESET("Đặt lại mật khẩu", "Bạn vừa yêu cầu đặt lại mật khẩu. Nếu đó không phải là bạn, hãy thực hiện khóa tài khoản để bảo mật"),
    PROMOTION("Ưu đãi đặc biệt", "Chào bạn! Đừng bỏ lỡ các ưu đãi hấp dẫn đang chờ bạn trong tuần này."),
    STRANGE_LOGIN("Strange login detection", "we found a strange device try to access to your account, is that you?"),
    NEWSLETTER("Bản tin hàng tháng", "Đây là bản tin hàng tháng của chúng tôi với những cập nhật và thông tin hữu ích dành cho bạn."),
    SUPPORT_RESPONSE("Phản hồi hỗ trợ", "Cảm ơn bạn đã liên hệ bộ phận hỗ trợ. Đây là phản hồi từ đội ngũ của chúng tôi.");

    // these aren't be used all, user for other services in the future.
    private final String subject;
    private final String text;

    MailTemplateType(String subject, String text) {
        this.subject = subject;
        this.text = text;
    }
}
