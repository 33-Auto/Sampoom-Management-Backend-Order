package com.sampoom.backend.common.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum ErrorStatus {

    // 400 BAD_REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    MISSING_EMAIL_VERIFICATION_EXCEPTION(HttpStatus.BAD_REQUEST, "이메일 인증을 진행해주세요."),
    ALREADY_REGISTER_EMAIL_EXCEPTION(HttpStatus.BAD_REQUEST, "이미 가입된 이메일 입니다."),
    INVALID_REQUESTER(HttpStatus.BAD_REQUEST, "잘못된 주문 요청 주체입니다"),
    INVALID_ORDER_TYPE(HttpStatus.BAD_REQUEST, "잘못된 주문 타입입니다."),
    INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, "잘못된 배송 상태입니다"),
    NO_QUERY_PARAMETER(HttpStatus.BAD_REQUEST, "요청 경로가 없습니다."),
    INVALID_QUERY_PARAMETER(HttpStatus.BAD_REQUEST, "요청 경로가 잘못되었습니다."),
    NO_CATEGORY_REQUEST(HttpStatus.BAD_REQUEST, "요청에 카테고리 정보가 없습니다."),
    NO_GROUP_REQUEST(HttpStatus.BAD_REQUEST, "요청에 그룹 정보가 없습니다."),
    NO_PART_REQUEST(HttpStatus.BAD_REQUEST, "요청에 부품 정보가 없습니다."),
    SHIPPING_CANT_CANCEL(HttpStatus.BAD_REQUEST, "배송 중인 주문은 취소할 수 없습니다."),

    // 401 UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),

    // 403 FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // 404 NOT_FOUND
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다."),



    // 409 CONFLICT
    CONFLICT(HttpStatus.CONFLICT, "충돌이 발생했습니다."),

    // 500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    public int getStatusCode() {
        return this.httpStatus.value();
    }

}
