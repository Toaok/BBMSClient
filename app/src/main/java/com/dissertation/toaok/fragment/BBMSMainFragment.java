package com.dissertation.toaok.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dissertation.toaok.activity.BBMSSearchResultActivity;
import com.dissertation.toaok.activity.R;
import com.dissertation.toaok.utils.BBMSUtils;
import com.google.zxing.android.CaptureActivity;
import com.google.zxing.bean.ZxingConfig;
import com.google.zxing.common.Constant;

/**
 * Created by TOAOK on 2017/9/17.
 */

public class BBMSMainFragment extends Fragment {

    private static final String ARG_SEARCH_STATUS = "search_status";


    private static final String[] APP_PERMISSION_GROUP = new String[]{Manifest.permission.CAMERA};
    private final int REQUEST_CODE_SCAN = 0xb1;
    private final int PERMISSION_REQUEST_CODE = 0x2710;
    private boolean isGeanted = false;//该应用是否拥有权限

    private TextView mSearchInput;
    private TextView mScanner;
    private ImageView mWeiXin;
    private ImageView mWeiBo;

    public static BBMSMainFragment newInstance() {
        return newInstance(false);
    }

    public static BBMSMainFragment newInstance(boolean isSearch) {
        BBMSMainFragment fragment = new BBMSMainFragment();
        Log.i("status", "BBMSMainFragment hsa bean created!");
        Bundle args = new Bundle();
        args.putSerializable(ARG_SEARCH_STATUS, isSearch);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        mSearchInput = v.findViewById(R.id.search_input);
        mScanner = v.findViewById(R.id.scanner);
        mWeiXin = v.findViewById(R.id.weixin);
        mWeiBo = v.findViewById(R.id.weibo);


        mSearchInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BBMSSearchResultActivity.newIntent(getContext(), true));
            }
        });

        mScanner.setText("\ue904");
        mScanner.setTypeface(BBMSUtils.getTypeface(getContext(), "fonts/icomoon.ttf"));
        mScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //检查是否拥有权限
                isGeanted = checkGroupPermission(APP_PERMISSION_GROUP);
                //如果拥有权限跳转到扫描码界面
                if (isGeanted) {
                    startScanner();
                } else {
                    //请求权限
                    BBMSMainFragment.this.requestPermissions(APP_PERMISSION_GROUP, PERMISSION_REQUEST_CODE);
                }
            }
        });

        return v;
    }


    //检查是否拥有权限
    private boolean checkGroupPermission(String[] permissions) {

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    //申请权限启动结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            isGeanted = true;
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isGeanted = false;
                    break;
                }
            }
        }
        if (isGeanted) {
            //如果拥有权限跳转到扫描码界面
            startScanner();
        } else if (!isGeanted) {
            //解释需要该权限的原因，并引导用户去应用权限管理中手动启动权限
            openAppDetails();
        }

    }

    //打开APP管理详情页面
    private void openAppDetails() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("该功能需要用到相机功能，请到 “应用信息 -> 权限” 中授予！");
        builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    //启动扫描器（跳转到扫描码界面）
    private void startScanner() {
        Intent intent = CaptureActivity.newIntent(getContext());
                                /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
                                * 也可以不传这个参数
                                * 不传的话  默认都为默认不震动  其他都为true
                                * */
        ZxingConfig config = new ZxingConfig();
        config.setPlayBeep(true);
        config.setShake(true);
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //我们需要的结果返回
        if (requestCode == REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK) {

            //result就是二维码扫描的结果。
            String result = data.getStringExtra(Constant.CODED_CONTENT);

            startActivity(BBMSSearchResultActivity.newIntent(getContext(), result, false));
        }
    }
}
