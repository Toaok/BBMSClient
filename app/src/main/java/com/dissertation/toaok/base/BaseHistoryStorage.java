package com.dissertation.toaok.base;

import com.dissertation.toaok.model.SearchHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TOAOK on 2017/10/30.
 */

public abstract class BaseHistoryStorage {

    //保存历史记录
    public abstract void save(String searchString);

    //删除历史记录
    public abstract void remove(String key);

    //清空历史记录
    public abstract void clear();

    //生成键
    public abstract String generateKey();

    //返回排序好的历史记录
    public abstract List<SearchHistory> getHistory();

}
