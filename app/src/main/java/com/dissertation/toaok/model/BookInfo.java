package com.dissertation.toaok.model;

import java.io.Serializable;

/**
 * Created by TOAOK on 2017/9/19.
 */

public class BookInfo implements Serializable {
    private String bookIsbn;
    private String bookName;
    private String bookAuthor;
    private String bookPublish;
    private String bookPrice;
    private String bookCnum;
    private String bookSnum;
    private String bookClassify;
    private String bookSummary;
    private String bookCover;

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookPublish() {
        return bookPublish;
    }

    public void setBookPublish(String bookPublish) {
        this.bookPublish = bookPublish;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getBookCnum() {
        return bookCnum;
    }

    public void setBookCnum(String bookCnum) {
        this.bookCnum = bookCnum;
    }

    public String getBookSnum() {
        return bookSnum;
    }

    public void setBookSnum(String bookSnum) {
        this.bookSnum = bookSnum;
    }

    public String getBookClassify() {
        return bookClassify;
    }

    public void setBookClassify(String bookClassify) {
        this.bookClassify = bookClassify;
    }

    public String getBookSummary() {
        return bookSummary;
    }

    public void setBookSummary(String bookSummary) {
        this.bookSummary = bookSummary;
    }

    public String getBookCover() {
        return bookCover;
    }

    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }

    @Override
    public String toString() {
        return "BookInfo{" +
                "bookIsbn='" + bookIsbn + '\'' +
                ", bookName='" + bookName + '\'' +
                ", bookAuthor='" + bookAuthor + '\'' +
                ", bookPublish='" + bookPublish + '\'' +
                ", bookPrice='" + bookPrice + '\'' +
                ", bookCnum='" + bookCnum + '\'' +
                ", bookSnum='" + bookSnum + '\'' +
                ", bookClassify='" + bookClassify + '\'' +
                ", bookSummary='" + bookSummary + '\'' +
                ", bookCover='" + bookCover + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookInfo bookInfo = (BookInfo) o;

        if (bookIsbn != null ? !bookIsbn.equals(bookInfo.bookIsbn) : bookInfo.bookIsbn != null)
            return false;
        if (bookName != null ? !bookName.equals(bookInfo.bookName) : bookInfo.bookName != null)
            return false;
        if (bookAuthor != null ? !bookAuthor.equals(bookInfo.bookAuthor) : bookInfo.bookAuthor != null)
            return false;
        if (bookPublish != null ? !bookPublish.equals(bookInfo.bookPublish) : bookInfo.bookPublish != null)
            return false;
        if (bookPrice != null ? !bookPrice.equals(bookInfo.bookPrice) : bookInfo.bookPrice != null)
            return false;
        if (bookCnum != null ? !bookCnum.equals(bookInfo.bookCnum) : bookInfo.bookCnum != null)
            return false;
        if (bookSnum != null ? !bookSnum.equals(bookInfo.bookSnum) : bookInfo.bookSnum != null)
            return false;
        if (bookClassify != null ? !bookClassify.equals(bookInfo.bookClassify) : bookInfo.bookClassify != null)
            return false;
        if (bookSummary != null ? !bookSummary.equals(bookInfo.bookSummary) : bookInfo.bookSummary != null)
            return false;
        return bookCover != null ? bookCover.equals(bookInfo.bookCover) : bookInfo.bookCover == null;

    }

    @Override
    public int hashCode() {
        int result = bookIsbn != null ? bookIsbn.hashCode() : 0;
        result = 31 * result + (bookName != null ? bookName.hashCode() : 0);
        result = 31 * result + (bookAuthor != null ? bookAuthor.hashCode() : 0);
        result = 31 * result + (bookPublish != null ? bookPublish.hashCode() : 0);
        result = 31 * result + (bookPrice != null ? bookPrice.hashCode() : 0);
        result = 31 * result + (bookCnum != null ? bookCnum.hashCode() : 0);
        result = 31 * result + (bookSnum != null ? bookSnum.hashCode() : 0);
        result = 31 * result + (bookClassify != null ? bookClassify.hashCode() : 0);
        result = 31 * result + (bookSummary != null ? bookSummary.hashCode() : 0);
        result = 31 * result + (bookCover != null ? bookCover.hashCode() : 0);
        return result;
    }
}
