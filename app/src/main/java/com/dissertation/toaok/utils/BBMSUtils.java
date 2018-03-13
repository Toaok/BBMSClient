package com.dissertation.toaok.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.dissertation.toaok.model.Account;
import com.dissertation.toaok.model.BookInfo;
import com.dissertation.toaok.model.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TOAOK on 2017/9/13.
 */

public class BBMSUtils {

    public static final int TIME_OUT = 0;
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";


    public static int dip2px(float dipValue) {
        DisplayMetrics metrics = new DisplayMetrics();
        metrics.setToDefaults();
        ;
        float scale = metrics.density;
        return (int) (dipValue * scale + 0.5f);

    }

    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    //网络请求
    public static String HttpGet(String path) {
        try {
            return HttpGet(new URL(path));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String HttpGet(URL url) {

        HttpURLConnection connection = null;
        String response = "";
        try {
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            connection = (HttpURLConnection) url.openConnection();

            //设置连接参数
            connection.setReadTimeout(TIME_OUT);
            connection.setConnectTimeout(TIME_OUT);
            connection.setDoInput(true);
            connection.setRequestMethod(METHOD_GET);

            //设置请求消息头
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-ALive");
            connection.setRequestProperty("charset", "utf-8");

            switch (connection.getResponseCode()) {

                case HttpURLConnection.HTTP_NOT_FOUND:
                    Thread.sleep(TIME_OUT);
                    break;
                case HttpURLConnection.HTTP_OK:
                    InputStream inputStream = null;
                    BufferedReader reader = null;
                    try {
                        inputStream = connection.getInputStream();

                        StringBuffer buffer = new StringBuffer();

                        reader = new BufferedReader(new InputStreamReader(inputStream));
                        String readLine = null;

                        while ((readLine = reader.readLine()) != null) {
                            buffer.append(readLine);
                        }
                        response = buffer.toString();
                    } finally {
                        if (reader != null) ;
                        {
                            reader.close();
                        }
                        connection.disconnect();
                    }
                    break;
                case HttpURLConnection.HTTP_MOVED_TEMP:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String HttpPost(URL url, String post) {
        HttpURLConnection connection = null;
        String request = null;
        try {
            connection = (HttpURLConnection) url.openConnection();

            //设置连接参数
            connection.setConnectTimeout(TIME_OUT);
            connection.setReadTimeout(TIME_OUT);
            connection.setRequestMethod(METHOD_POST);
            //设置请求消息头
            connection.setRequestProperty("Content-Type", "binary/octet-stream");
            connection.setRequestProperty("charset", "utf-8");
            //发送POS请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.connect();

            if (post != null || post.equals("")) {
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(post.getBytes("utf-8"));
                outputStream.flush();
                outputStream.close();
            }

            InputStream inputStream = null;
            BufferedReader reader = null;
            try {
                inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer buffer = new StringBuffer();
                String readLine = null;

                while ((readLine = reader.readLine()) != null) {
                    buffer.append(readLine);
                }
                request = buffer.toString();
                connection.disconnect();
            } finally {
                if (reader != null)
                    reader.close();
                if (inputStream != null)
                    inputStream.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }

    //将Json数据转换为BookInfo对象
    public static BookInfo json2Object(JSONObject jsonObject) {
        BookInfo bookInfo = new BookInfo();
        bookInfo.setBookIsbn(jsonObject.optString("bookIsbn"));
        bookInfo.setBookName(jsonObject.optString("bookName"));
        bookInfo.setBookAuthor(jsonObject.optString("bookAuthor"));
        bookInfo.setBookPublish(jsonObject.optString("bookPublish"));
        bookInfo.setBookPrice(jsonObject.optString("bookPrice"));
        bookInfo.setBookCnum(String.valueOf(jsonObject.optInt("bookCnum")));
        bookInfo.setBookSnum(String.valueOf(jsonObject.optInt("bookSnum")));
        bookInfo.setBookClassify(jsonObject.optString("bookClassify"));
        bookInfo.setBookSummary(jsonObject.optString("bookSummary"));
        bookInfo.setBookCover(jsonObject.optString("bookCover"));
        return bookInfo;
    }

    //将JSONArray数据转换为List<BookInfo>对象
    public static ArrayList<BookInfo> json2Object(JSONArray jsonArray) throws JSONException {
        ArrayList list = new ArrayList();
        int length = jsonArray.length();
        for (int i = 0; i < length; ++i) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            list.add(json2Object(jsonObject));
        }
        return list;
    }

    //将json数据转换为List<Subscribe>对象
    public static List<BookInfo> json2Object(String json) throws JSONException {
        return json2Object(new JSONArray(json));
    }

    //获取富文本

    public static SpannableStringBuilder setFontColorSpannableStringBuilder(String string, int startIndex, int color) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append(string);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);
        spannableStringBuilder.setSpan(foregroundColorSpan, startIndex, spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableStringBuilder;
    }

    public static SpannableStringBuilder setFontColorSpannableStringBuilder(String string, String startStr, int color) {
        int startIndex = string.indexOf(startStr);
        return setFontColorSpannableStringBuilder(string, startIndex, color);
    }

    //通过序列化对象，将对象序列化成base64编码的文本.
    public static Object str2Obj(String str) {
        if (str == null) {
            return null;
        }

        Object obj = null;
        try {
            byte[] bytes = Base64.decode(str.getBytes(), Base64.DEFAULT);
            ByteArrayInputStream baio = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(baio);
            obj = ois.readObject();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return obj;

    }

    //通过序列化对象，将序列化文本解码成对象.
    public static String obj2Str(Object obj) {
        if (obj == null) {
            return "";
        }
        String str = null;
        try {
            //实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //将得到的字符数据装载到ObjectOutputStream
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            //writeObject方法负责写入特定类的对象的状态，以便相应的readObject可以还原它
            oos.writeObject(obj);
            //用Base64.encode将字节文件转换成Base64编码，并以String形式保存
            str = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }


    //生成订单
    public static Subscribe createSubscribe(BookInfo bookInfo) {
        Subscribe subscribe = null;
        Account account = BBMSSharedPreferencesUtil.getInstance().getAccount();
        if (account != null) {
            subscribe = new Subscribe();
            subscribe.setAccountId(account.getAccountId());
            subscribe.setBookInfo(bookInfo);
        }
        return subscribe;
    }


    //获取订单的价格
    public static String getTotalPrice(List<BookInfo> subscribes) {

        double totalPrice = 0;
        for (BookInfo bookInfo : subscribes) {
            totalPrice += getData(bookInfo.getBookPrice());
        }
        return formatPrice(totalPrice);
    }

    //格式化价格
    public static String formatPrice(double price) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(price);
    }

    //从字符串中提取数据
    public static double getData(String value) {

        String data = "";
        if (value != null && !"".equals(value)) {
            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) >= 48 && value.charAt(i) <= 57 || value.charAt(i) == 46) {
                    data += value.charAt(i);
                }
            }
        }
        if (!TextUtils.isEmpty(data)) {
            return Double.parseDouble(data);
        } else {
            return 0;
        }

    }

    //检查用户是否登录
    public static boolean checkUserIsLogin() {
        BBMSSharedPreferencesUtil sharedPreferencesUtil = BBMSSharedPreferencesUtil.getInstance();
        return sharedPreferencesUtil.getLoginStatus();
    }

    //获取Typeface
    public static Typeface getTypeface(Context context, String filepath) {
        return Typeface.createFromAsset(context.getAssets(), filepath);
    }

    //选中
    public static ColorStateList newSelector(int normal, int pressed) {
        int[] colors = new int[]{pressed, pressed, normal};
        int[][] states = new int[3][];

        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{-android.R.attr.state_enabled};
        states[2] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    public static StateListDrawable newSelector(int colorNormal, int colorPressed, int radius) {
        StateListDrawable bg = new StateListDrawable();

        GradientDrawable normal = new GradientDrawable();
        normal.setShape(GradientDrawable.RECTANGLE);

        normal.setCornerRadii(new float[]{radius, radius, radius, radius, radius, radius, radius, radius});
        normal.setColor(colorNormal);


        GradientDrawable pressed = new GradientDrawable();
        pressed.setShape(GradientDrawable.RECTANGLE);

        pressed.setCornerRadii(new float[]{radius, radius, radius, radius, radius, radius, radius, radius});
        pressed.setColor(colorPressed);
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        bg.addState(new int[]{-android.R.attr.state_enabled}, pressed);
        bg.addState(new int[]{}, normal);
        return bg;
    }


    //画图
    public static GradientDrawable newDrawable(int color, int border, int borderCode, int radius) {
        return newDrawable(color, border, borderCode, radius, 0);
    }

    public static GradientDrawable newDrawable(int color, int border, int borderCode, int radius, int padding) {
        GradientDrawable normal = new GradientDrawable();
        normal.setShape(GradientDrawable.RECTANGLE);
        if (radius > 0)
            normal.setCornerRadii(new float[]{radius, radius, radius, radius, radius, radius, radius, radius});
        if (color != 0)
            normal.setColor(color);
        if (border > 0)
            normal.setStroke(border, borderCode);
        if (padding > 0)
            normal.getPadding(new Rect(padding, padding, padding, padding));
        return normal;
    }

    //将Object数组解析成Map对象
    public static Map parseMap(Object... objects) {
        Map map = new HashMap();
        int l = (objects.length / 2) * 2;
        for (int i = 0; i < l; i += 2) {
            map.put(objects[i], objects[i + 1]);
        }
        return map;
    }

    //推算时间
    public static Date calulateData(int afterDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, afterDay);
        return calendar.getTime();
    }
}
