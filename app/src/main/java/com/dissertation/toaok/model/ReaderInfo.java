package com.dissertation.toaok.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by TOAOK on 2017/10/24.
 */
public class ReaderInfo implements Serializable {

    public static final String READER_ID = "readerId";
    public static final String READER_NAME = "readerName";
    public static final String READER_SEX = "readerSex";
    public static final String READER_SPEC = "readerSpec";
    public static final String READER_PHONE = "readerPhone";
    public static final String READER_HANDING_TIME = "readerHandingTime";
    public static final String READER_TYPE = "readerType";
    public static final String READER_STATUS = "readerStatus";
    public static final String READER_DEBIT_AMOUNT = "reader_debit_amount";
    public static final String READER_TIME_LIMIT = "reader_time_limit";
    public static final String READER_SCHOOL = "readerSchool";
    public static final String READER_QIANMING = "readerQianming";
    public static final String READER_IMAGE = "readerImage";

    private Account readerId;
    private String readerName;
    private String readerSex;
    private String readerSpec;
    private String readerPhone;
    private Timestamp readerHandingTime;
    private String readerType;
    private String readerStatus;
    private Integer readerDebitAmount;
    private Integer readerTimeLimit;
    private String readerSchool;
    private String readerQianming;
    private byte[] readerImage;

    public Account getReaderId() {
        return readerId;
    }

    public void setReaderId(Account readerId) {
        this.readerId = readerId;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public String getReaderSex() {
        return readerSex;
    }

    public void setReaderSex(String readerSex) {
        this.readerSex = readerSex;
    }

    public String getReaderSpec() {
        return readerSpec;
    }

    public void setReaderSpec(String readerSpec) {
        this.readerSpec = readerSpec;
    }

    public String getReaderPhone() {
        return readerPhone;
    }

    public void setReaderPhone(String readerPhone) {
        this.readerPhone = readerPhone;
    }

    public Timestamp getReaderHandingTime() {
        return readerHandingTime;
    }

    public void setReaderHandingTime(Timestamp readerHandingTime) {
        this.readerHandingTime = readerHandingTime;
    }

    public String getReaderType() {
        return readerType;
    }

    public void setReaderType(String readerType) {
        this.readerType = readerType;
    }

    public String getReaderStatus() {
        return readerStatus;
    }

    public void setReaderStatus(String readerStatus) {
        this.readerStatus = readerStatus;
    }

    public Integer getReaderDebitAmount() {
        return readerDebitAmount;
    }

    public void setReaderDebitAmount(Integer readerDebitAmount) {
        this.readerDebitAmount = readerDebitAmount;
    }

    public Integer getReaderTimeLimit() {
        return readerTimeLimit;
    }

    public void setReaderTimeLimit(Integer readerTimeLimit) {
        this.readerTimeLimit = readerTimeLimit;
    }

    public String getReaderSchool() {
        return readerSchool;
    }

    public void setReaderSchool(String readerSchool) {
        this.readerSchool = readerSchool;
    }

    public String getReaderQianming() {
        return readerQianming;
    }

    public void setReaderQianming(String readerQianming) {
        this.readerQianming = readerQianming;
    }

    public byte[] getReaderImage() {
        return readerImage;
    }

    public void setReaderImage(byte[] readerImage) {
        this.readerImage = readerImage;
    }
}
