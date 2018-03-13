package com.dissertation.toaok.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dissertation.toaok.activity.BBMSPagerActivity;
import com.dissertation.toaok.activity.R;
import com.dissertation.toaok.base.BBMSBaseFragment;
import com.dissertation.toaok.model.Account;
import com.dissertation.toaok.utils.BBMS;
import com.dissertation.toaok.utils.BBMSSharedPreferencesUtil;
import com.dissertation.toaok.utils.BBMSUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TOAOK on 2017/9/13.
 */

public class BBMSLoginFragment extends BBMSBaseFragment implements BBMS.Callback {

    private static final String ARG_IS_SLIDING = "is_sliding";

    private boolean isDisplayPassword = false;


    private Account mAccount;
    private EditText mEditTextAccount;
    private EditText mEditTextPassword;
    private Button mButtonSubmit;
    private TextView mEditTextDisplay;
    private BBMSSharedPreferencesUtil mSharedPreferencesUtil;

    public static BBMSLoginFragment newInstance() {
        return newInstance(false);
    }

    public static BBMSLoginFragment newInstance(boolean isSliding) {
        BBMSLoginFragment fragment = new BBMSLoginFragment();
        Log.i("status", "BBMSLoginFragment hsa bean created!");
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_SLIDING, isSliding);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccount = new Account();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mEditTextAccount = v.findViewById(R.id.account);

        mEditTextPassword = v.findViewById(R.id.password);


        mEditTextAccount.addTextChangedListener(textWatcher);
        mEditTextPassword.addTextChangedListener(textWatcher);

        mEditTextDisplay = v.findViewById(R.id.display);
        mEditTextDisplay.setText("\ue908");
        mEditTextDisplay.setTypeface(BBMSUtils.getTypeface(getContext(), "fonts/icomoon.ttf"));
        mEditTextDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDisplayPassword) {
                    mEditTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mEditTextDisplay.setText("\ue90a");
                    isDisplayPassword = true;
                } else {
                    mEditTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mEditTextDisplay.setText("\ue908");
                    isDisplayPassword = false;
                }
            }
        });

        mButtonSubmit = v.findViewById(R.id.submit);
        changButtonStatus(mButtonSubmit);
        mSharedPreferencesUtil = BBMSSharedPreferencesUtil.getInstance();

        Account account = mSharedPreferencesUtil.getAccount();
        if (account != null) {
            Log.i("sharedPreference", account.toString());
            Log.i("sharedPreference", mSharedPreferencesUtil.getLoginStatus() ? "true" : "false");
        } else {
            Log.i("sharedPreference", "");
            Log.i("sharedPreference", mSharedPreferencesUtil.getLoginStatus() ? "true" : "false");
        }

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAccount.setAccount(mEditTextAccount.getText().toString());
                mAccount.setPassword(mEditTextPassword.getText().toString());
                BBMS.with(getContext()).send("AccountServlet", "validation", mAccount, BBMSLoginFragment.this, true);
            }
        });
        //初始化SharePreferenceUtil
        return v;
    }

    @Override
    public void callback(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                result(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            BBMSUtils.showMessage(getContext(), "未请求到数据！！");
        }
    }

    private void result(String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        if (jsonObject.optBoolean("isexist")) {
            mAccount.setAccountId(jsonObject.optJSONObject("account").getInt("accountId"));
            mSharedPreferencesUtil.setAccount(mAccount);
            getActivity().finish();
            startActivity(BBMSPagerActivity.newIntent(getContext()));
        } else {
            BBMSUtils.showMessage(getContext(), "你输入的账号或密码有误！！");
        }
    }

    private boolean isEmpty() {
        boolean accountResult = mAccount == null || mEditTextAccount.getText().toString().equals("");
        boolean passwordResult = mEditTextPassword == null || mEditTextPassword.getText().toString().equals("");

        if (!accountResult && !passwordResult) {
            return false;
        } else {
            return true;
        }
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            changButtonStatus(mButtonSubmit);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            changButtonStatus(mButtonSubmit);
        }

        @Override
        public void afterTextChanged(Editable s) {
            changButtonStatus(mButtonSubmit);
        }

    };

    private void changButtonStatus(Button button) {
        if (!isEmpty()) {
            button.setEnabled(true);
            button.setAlpha(1.0f);
        } else {
            button.setEnabled(false);
            button.setAlpha(0.6f);
        }
    }

    private boolean getArgument(){
        return getArguments().getBoolean(ARG_IS_SLIDING);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!BBMSUtils.checkUserIsLogin() &&getArgument()) {
            startActivity(BBMSPagerActivity.newIntent(getContext()));
        }
    }
}
