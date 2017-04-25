package com.task.vasskob.fbaccountkit;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountPreferences;
import com.facebook.accountkit.PhoneNumber;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class InfoActivity extends Activity {

    public static final String TAG = InfoActivity.class.getSimpleName();

    @Bind(R.id.user_id)
    TextView tvUserId;

    @Bind(R.id.user_phone)
    TextView tvUserPhone;

    @Bind(R.id.user_email)
    TextView tvUserEmail;

    @Bind(R.id.user_nick_name)
    TextView nickNameSetting;

    @SuppressWarnings("unused")
    @OnClick(R.id.log_out_button)
    public void onLogoutClick() {
        AccountKit.logOut();
        finish();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infro_activity);
        ButterKnife.bind(this);

        // Load all preferences for this account:
        AccountKit.getAccountPreferences().loadPreferences(new PrefsLoadListener());

        // Set a preference:
        AccountKit.getAccountPreferences().setPreference("nickname", "VAsskob", new PrefSetListener());

        // Load a specific preference by key
        AccountKit.getAccountPreferences().loadPreference("nickname", new SinglePrefLoadListener());


        // Delete a preference:
        AccountKit.getAccountPreferences().deletePreference("keyForDelete ", new PrefDeleteListener());


    }

    @Override
    protected void onResume() {
        super.onResume();

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(Account account) {
                tvUserId.setText(account.getId());

                PhoneNumber number = account.getPhoneNumber();
                String phoneNumberWarn = getResources().getString(R.string.phone_number_warn);
                tvUserPhone.setText(number == null ? phoneNumberWarn : number.toString());

                String email = account.getEmail();
                String emailWarn = getResources().getString(R.string.email_warn);
                tvUserEmail.setText(email == null ? emailWarn : email);

            }

            @Override
            public void onError(AccountKitError accountKitError) {
                Toast.makeText(InfoActivity.this, R.string.error_toast, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private class PrefsLoadListener implements AccountPreferences.OnLoadPreferencesListener {
        @Override
        public void onLoadPreferences(@Nullable Map<String, String> map, @Nullable AccountKitError accountKitError) {
            if (accountKitError != null) {
                Log.e(TAG, "onLoadPreference accountKitError" + accountKitError);
                return;
            }
            if (map != null) {
                for (Map.Entry<String, String> e : map.entrySet()) {
                    Log.d(TAG, " key =" + e.getKey() + " value =" + e.getValue());
                }
            }
        }
    }

    private class SinglePrefLoadListener implements AccountPreferences.OnLoadPreferenceListener {
        @Override
        public void onLoadPreference(String s, @Nullable String s1, @Nullable AccountKitError accountKitError) {
            if (accountKitError != null) {
                Log.e(TAG, "onLoadPreference accountKitError" + accountKitError);
                return;
            }
            nickNameSetting.setText(s1);
        }
    }

    private class PrefSetListener implements AccountPreferences.OnSetPreferenceListener {
        @Override
        public void onSetPreference(String s, String s1, @Nullable AccountKitError accountKitError) {
            if (accountKitError != null) {
                Log.e(TAG, "onSetPreference accountKitError" + accountKitError);
                return;
            }
            Log.d(TAG, "onSetPreference key= " + s + " value=" + s1);
        }
    }

    private class PrefDeleteListener implements AccountPreferences.OnDeletePreferenceListener {

        @Override
        public void onDeletePreference(String key, @Nullable AccountKitError accountKitError) {
            if (accountKitError != null) {
                Log.e(TAG, "onDeletePreference accountKitError" + accountKitError);
                return;
            }
            Toast.makeText(InfoActivity.this, R.string.settings_deleted + key, Toast.LENGTH_SHORT).show();
        }
    }
}
