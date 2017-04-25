package com.task.vasskob.fbaccountkit;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.PhoneNumber;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class InfoActivity extends Activity {

    @Bind(R.id.user_id)
    TextView tvUserId;

    @Bind(R.id.user_phone)
    TextView tvUserPhone;

    @Bind(R.id.user_email)
    TextView tvUserEmail;

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
}
