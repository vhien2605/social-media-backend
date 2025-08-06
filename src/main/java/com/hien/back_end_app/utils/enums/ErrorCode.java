package com.hien.back_end_app.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Getter
public enum ErrorCode {
    OAuth2CheckingException(400, "oauth2 user is null or can't get email from oauth2 user", HttpStatus.BAD_REQUEST),
    OAuth2InvalidProvider(400, "seem that your email is linked with other auth provider, check again", HttpStatus.BAD_REQUEST),
    JWT_SIGN_ERROR(500, "seem that server can't handle sign jwt", HttpStatus.INTERNAL_SERVER_ERROR),
    TOKEN_INVALID(401, "given token is invalid,try to login again to call to refresh token", HttpStatus.BAD_REQUEST),
    TOKEN_SIGNATURE_INVALID(401, "given token signature is invalid", HttpStatus.BAD_REQUEST),
    TOKEN_DISABLED(401, "given token is disable by logged out", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(401, "given token is expired", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(400, "can't find any user with given input", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(401, "user unathorized", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(403, "you are not allowed to do this action", HttpStatus.FORBIDDEN),
    AUTHORITY_TYPE_INVALID(400, "authority type is invalid, try again", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXIST(400, "permission not existed", HttpStatus.BAD_REQUEST),
    PERMISSION_EXIST(400, "permission is existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXIST(400, "role is not exist", HttpStatus.BAD_REQUEST),
    ROLE_EXIST(400, "role is existed", HttpStatus.BAD_REQUEST),
    REGEX_INVALID(400, "regex is invalid", HttpStatus.BAD_REQUEST),
    USER_NOT_SUBSCRIBE(400, "user dont subrice this port", HttpStatus.BAD_REQUEST),
    USER_NOT_HAVE_CONVERSATION(400, "user not have conversation", HttpStatus.BAD_REQUEST),
    CONVERSATION_NOT_EXIST(400, "conversation is not exist", HttpStatus.BAD_REQUEST),
    UPLOAD_FILE_FAILED(503, "service unavailable", HttpStatus.SERVICE_UNAVAILABLE),
    CONVERSATION_SIZE_INVALID(400, "conversation size invalid", HttpStatus.BAD_REQUEST),
    POST_NOT_EXIST(400, "can't find any post with given id, please try other ids", HttpStatus.BAD_REQUEST),
    COMMENT_NOT_EXIST(400, "this comment is not existed anymore", HttpStatus.BAD_REQUEST),
    GROUP_NOT_EXIST(400, "group is not exist", HttpStatus.BAD_REQUEST),
    REQUEST_NOT_EXIST(400, "request not exist", HttpStatus.BAD_REQUEST),
    USER_ALREADY_IN_GROUP(409, "user is already in group", HttpStatus.CONFLICT),
    TOKEN_BLACK_LIST(401, "token is in black list, check other tokens again", HttpStatus.BAD_REQUEST),
    USER_EXISTED(400, "user has already existed", HttpStatus.BAD_REQUEST),
    JSON_INVALID(400, "json is invalid", HttpStatus.BAD_REQUEST),
    ALBUM_EXISTED(400, "album title is existed,select other names", HttpStatus.BAD_REQUEST);
    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
