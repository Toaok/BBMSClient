package com.dissertation.toaok.model;

import java.io.Serializable;

/**
 * Created by TOAOK on 2017/9/13.
 */

public class Subscribe implements Serializable {
    private boolean subscribeStatus;
    private BookInfo mBookInfo;
    private int accountId;

    public boolean isSubscribeStatus() {
        return subscribeStatus;
    }

    public void setSubscribeStatus(boolean subscribeStatus) {
        this.subscribeStatus = subscribeStatus;
    }

    public BookInfo getBookInfo() {
        return mBookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        mBookInfo = bookInfo;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subscribe subscribe = (Subscribe) o;

        if (subscribeStatus != subscribe.subscribeStatus) return false;
        if (accountId != subscribe.accountId) return false;
        return mBookInfo != null ? mBookInfo.equals(subscribe.mBookInfo) : subscribe.mBookInfo == null;

    }

    @Override
    public int hashCode() {
        int result = (subscribeStatus ? 1 : 0);
        result = 31 * result + (mBookInfo != null ? mBookInfo.hashCode() : 0);
        result = 31 * result + accountId;
        return result;
    }
}
