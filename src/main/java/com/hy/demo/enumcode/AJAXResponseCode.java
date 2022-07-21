package com.hy.demo.enumcode;


public enum AJAXResponseCode {

    /**
     * 성공시 코드
     */
    OK("1"),
    /**
     * 잘못된 값으로 인한 실패
     */
    FAIL("2"),
    /**
     * 내부로직오류 파일저장 ... , 실패 종류 2번째 구분 용도
     */
    ERROR("3");

    final String code;

    AJAXResponseCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
