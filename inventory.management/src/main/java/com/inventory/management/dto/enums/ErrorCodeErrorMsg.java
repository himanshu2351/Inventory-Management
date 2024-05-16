package com.inventory.management.dto.enums;

public enum ErrorCodeErrorMsg {

    INTERNAL_SERVER_ERROR("ERR_100", "Internal Server Error"),
    APPLICATION_ERROR("ERR_101", "Application Error"),
    INVALID_CUSTOMER("ERR_102", "Customer ID is invalid"),
    INVALID_PRODUCT("ERR_103", "Invalid product Id"),
    INVALID_PRODUCT_QUANTITY("ERR_104", "Invalid product quantity"),
    INVALID_ORDER_ID("ERR_105", "Invalid Order ID !!");


    private String errCode;
    private String errMsg;

    ErrorCodeErrorMsg(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
