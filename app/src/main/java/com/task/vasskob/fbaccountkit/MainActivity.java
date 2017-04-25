package com.task.vasskob.fbaccountkit;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Switch;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.ButtonType;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;
import com.facebook.accountkit.ui.TextPosition;
import com.facebook.accountkit.ui.UIManager;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int FRAMEWORK_REQUEST_CODE = 1;
    private int nextPermissionsRequestCode = 2;

    private final Map<Integer, OnCompleteListener> permissionsListeners = new HashMap<>();

    private interface OnCompleteListener {
        void onComplete();
    }

    @Bind(R.id.ui_switch)
    Switch uiSwitch;

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_phone_login)
    public void onPhoneLoginClick() {
        onLogin(LoginType.PHONE);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_email_login)
    public void onEmailLoginClick() {
        onLogin(LoginType.EMAIL);
    }

    private void onLogin(LoginType loginType) {
        final Intent intent = new Intent(this, AccountKitActivity.class);

        final AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder
                = createAccountKitConfiguration(loginType);

        final AccountKitConfiguration configuration = configurationBuilder.build();
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configuration);
        OnCompleteListener completeListener = new OnCompleteListener() {
            @Override
            public void onComplete() {
                startActivityForResult(intent, FRAMEWORK_REQUEST_CODE);
            }
        };
        switch (loginType) {
            case EMAIL:
                final OnCompleteListener getAccountsCompleteListener = completeListener;
                completeListener = new OnCompleteListener() {
                    @Override
                    public void onComplete() {
                        requestPermissions(
                                Manifest.permission.GET_ACCOUNTS,
                                R.string.permissions_get_accounts_title,
                                R.string.permissions_get_accounts_message,
                                getAccountsCompleteListener);
                    }
                };
                break;
            case PHONE:
                if (configuration.isReceiveSMSEnabled()) {
                    final OnCompleteListener receiveSMSCompleteListener = completeListener;
                    completeListener = new OnCompleteListener() {
                        @Override
                        public void onComplete() {
                            requestPermissions(
                                    Manifest.permission.RECEIVE_SMS,
                                    R.string.permissions_receive_sms_title,
                                    R.string.permissions_receive_sms_message,
                                    receiveSMSCompleteListener);
                        }
                    };
                }
                if (configuration.isReadPhoneStateEnabled()) {
                    final OnCompleteListener readPhoneStateCompleteListener = completeListener;
                    completeListener = new OnCompleteListener() {
                        @Override
                        public void onComplete() {
                            requestPermissions(
                                    Manifest.permission.READ_PHONE_STATE,
                                    R.string.permissions_read_phone_state_title,
                                    R.string.permissions_read_phone_state_message,
                                    readPhoneStateCompleteListener);
                        }
                    };
                }
                break;
        }
        completeListener.onComplete();

    }

    private AccountKitConfiguration.AccountKitConfigurationBuilder createAccountKitConfiguration(LoginType loginType) {
        int color = ContextCompat.getColor(this, R.color.colorAccent);

        AccountKitConfiguration.AccountKitConfigurationBuilder configBuilder
                = new AccountKitConfiguration.AccountKitConfigurationBuilder(loginType, AccountKitActivity.ResponseType.TOKEN);
        UIManager uiManager;
        if (uiSwitch.isChecked()) {
            ButtonType entryBtn = ButtonType.CONTINUE;
            ButtonType confirmBtn = ButtonType.LOG_IN;
            TextPosition texPosition = TextPosition.BELOW_BODY;
            int selectedThemeId = R.style.AppLoginTheme_Custom;

            uiManager = new MyAdvancedUIManager(
                    entryBtn,
                    confirmBtn,
                    texPosition,
                    selectedThemeId);

        } else {
            uiManager = new SkinManager(
                    SkinManager.Skin.CONTEMPORARY,
                    color, R.drawable.anonymous,
                    SkinManager.Tint.BLACK,
                    0.55d);
            configBuilder.setUIManager(uiManager);
        }
        configBuilder.setUIManager(uiManager);

        return configBuilder;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (AccountKit.getCurrentAccessToken() != null) {
            startActivity(new Intent(this, InfoActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != FRAMEWORK_REQUEST_CODE) {
            return;
        }

        final String toastMessage;
        final AccountKitLoginResult loginResult = AccountKit.loginResultWithIntent(data);
        if (loginResult == null || loginResult.wasCancelled()) {
            toastMessage = "Login Cancelled";
        } else if (loginResult.getError() != null) {
            toastMessage = loginResult.getError().getErrorType().getMessage();
            final Intent intent = new Intent(this, ErrorActivity.class);
            intent.putExtra(ErrorActivity.HELLO_TOKEN_ACTIVITY_ERROR_EXTRA, loginResult.getError());

            startActivity(intent);
        } else {
            final AccessToken accessToken = loginResult.getAccessToken();
            final long tokenRefreshIntervalInSeconds =
                    loginResult.getTokenRefreshIntervalInSeconds();
            if (accessToken != null) {
                toastMessage = "Success:" + accessToken.getAccountId()
                        + tokenRefreshIntervalInSeconds;
                startActivity(new Intent(this, InfoActivity.class));
            } else {
                toastMessage = "Unknown response type";
            }
        }

        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }

    private void requestPermissions(final String permission, final int rationaleTitleResourceId,
                                    final int rationaleMessageResourceId, final OnCompleteListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (listener != null) {
                listener.onComplete();
            }
            return;
        }

        checkRequestPermissions(
                permission,
                rationaleTitleResourceId,
                rationaleMessageResourceId,
                listener);
    }

    @TargetApi(23)
    private void checkRequestPermissions(final String permission, final int rationaleTitleResourceId,
                                         final int rationaleMessageResourceId, final OnCompleteListener listener) {
        if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            if (listener != null) {
                listener.onComplete();
            }
            return;
        }

        final int requestCode = nextPermissionsRequestCode++;
        permissionsListeners.put(requestCode, listener);

        if (shouldShowRequestPermissionRationale(permission)) {
            new AlertDialog.Builder(this)
                    .setTitle(rationaleTitleResourceId)
                    .setMessage(rationaleMessageResourceId)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            requestPermissions(new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            // ignore and clean up the listener
                            permissionsListeners.remove(requestCode);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            requestPermissions(new String[]{permission}, requestCode);
        }
    }

    @TargetApi(23)
    @SuppressWarnings("unused")
    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           final @NonNull String permissions[],
                                           final @NonNull int[] grantResults) {
        final OnCompleteListener permissionsListener = permissionsListeners.remove(requestCode);
        if (permissionsListener != null
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionsListener.onComplete();
        }
    }

}
