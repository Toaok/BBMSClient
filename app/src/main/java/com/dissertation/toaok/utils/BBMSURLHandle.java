package com.dissertation.toaok.utils;

import com.dissertation.toaok.model.Account;
import com.dissertation.toaok.model.Subscribe;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TOAOK on 2017/9/21.
 */

public class BBMSURLHandle {

    private static final String BASE_PATH = "http://iwtoaok.iask.in:11773/BBMSServer";
    private StringBuffer url = new StringBuffer();
    private Map map = new HashMap();

    private String encodeUrl(Map<String, String> map) {
        StringBuffer buffer = new StringBuffer();
        for (String string : map.keySet()) {
            if (buffer.length() > 0) {
                buffer.append("&");
            } else {
                buffer.append("?");
            }
            buffer.append(string);
            buffer.append("=");
            buffer.append(map.get(string));
        }
        return buffer.toString();
    }

    public String getURL(String model, String cmd, String sw, int page) {
        if (page <= 0) {
            page = 1;
        }
        map.put("pages", String.valueOf(page));
        map.put("sw", sw);
        getURL(model, cmd);
        return url.toString();
    }

    public String getURL(String model, String cmd, Account account) {
        if (account != null) {
            map.put("account", account.getAccount());
            map.put("password", account.getPassword());
        }
        getURL(model, cmd);
        return url.toString();
    }

    public String getURL(String model, String cmd, Subscribe subscribe) {
        if (subscribe != null) {
            map.put("account_id", String.valueOf(subscribe.getAccountId()));
            map.put("book_isbn", subscribe.getBookInfo().getBookIsbn());
        }
        getURL(model, cmd);
        return url.toString();
    }

    public String getURL(String model, String cmd, int accountId) {
        map.put("account_id", String.valueOf(accountId));
        getURL(model, cmd);
        return url.toString();
    }

    public String getURL(String model, String cmd) {
        getURL(model);
        map.put("cmd", cmd);
        url.append(encodeUrl(map));//请求的相对url、请求参数
        //使用过的参数情空
        map.clear();
        return url.toString();
    }

    public String getURL(String model) {
        getURL();
        url.append("/");
        url.append(model);//应用名称
        return url.toString();
    }

    public String getURL() {
        url.setLength(0);
        url.append(BASE_PATH);//当前链接使用的协议、服务器地址、端口号
        return url.toString();
    }

}
