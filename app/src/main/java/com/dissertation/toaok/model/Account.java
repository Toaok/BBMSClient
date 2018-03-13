package com.dissertation.toaok.model;

import java.io.Serializable;

/**
 * Created by TOAOK on 2017/9/13.
 */

public class Account implements Serializable{

    private int mAccountId;
    private String mAccount;
    private String mPassword;

    public int getAccountId() {
        return mAccountId;
    }

    public void setAccountId(int accountId) {
        mAccountId = accountId;
    }

    public String getAccount() {
        return mAccount;
    }

    public void setAccount(String account) {
        mAccount = account;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (mAccount != null ? !mAccount.equals(account.mAccount) : account.mAccount != null)
            return false;
        return mPassword != null ? mPassword.equals(account.mPassword) : account.mPassword == null;

    }

    @Override
    public int hashCode() {
        int result = mAccount != null ? mAccount.hashCode() : 0;
        result = 31 * result + (mPassword != null ? mPassword.hashCode() : 0);
        return result;
    }
}
