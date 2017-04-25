package com.task.vasskob.fbaccountkit;

import android.app.Fragment;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.ui.BaseUIManager;
import com.facebook.accountkit.ui.ButtonType;
import com.facebook.accountkit.ui.LoginFlowState;
import com.facebook.accountkit.ui.TextPosition;


class MyAdvancedUIManager extends BaseUIManager {

    private static final String TAG = MyAdvancedUIManager.class.getSimpleName();
    private final ButtonType confirmButton;
    private final ButtonType entryButton;
    private final TextPosition textPosition;


    MyAdvancedUIManager(
            final ButtonType entryButton,
            final ButtonType confirmButton,
            final TextPosition textPosition,
            final int themeResourceId) {
        super(themeResourceId);
        this.entryButton = entryButton;
        this.confirmButton = confirmButton;
        this.textPosition = textPosition;
    }

    private MyAdvancedUIManager(final Parcel source) {
        super(source);
        String s = source.readString();
        final ButtonType confirmButton = s == null ? null : ButtonType.valueOf(s);
        s = source.readString();
        final ButtonType entryButton = s == null ? null : ButtonType.valueOf(s);
        s = source.readString();
        final TextPosition textPosition = s == null ? null : TextPosition.valueOf(s);
        this.confirmButton = confirmButton;
        this.entryButton = entryButton;
        this.textPosition = textPosition;
    }

    @Override
    @Nullable
    public Fragment getHeaderFragment(final LoginFlowState state) {
        final String prefix;
        final int height;
        switch (state) {
            case PHONE_NUMBER_INPUT:
                prefix = "Custom Header Phone Input ";
                height = 70;
                break;
            case EMAIL_INPUT:
                prefix = "Custom Header  EMAIL Input ";
                height = 50;
                break;
            case EMAIL_VERIFY:
                prefix = "Custom Header EMAIL verify ";
                height = 50;
                break;
            case SENDING_CODE:
                prefix = "Custom Header sending code";
                height = 50;
                break;
            case SENT_CODE:
                prefix = "Custom Header senct code";
                height = 50;
                break;
            case CODE_INPUT:
                prefix = "Custom Header code input ";
                height = 50;
                break;
            case VERIFYING_CODE:
                prefix = "Custom Header verifying code ";
                height = 50;
                break;
            case VERIFIED:
                prefix = "Custom Header verified";
                height = 50;
                break;
            case ACCOUNT_VERIFIED:
                prefix = "Custom Header account verified ";
                height = 50;
                break;
            case CONFIRM_ACCOUNT_VERIFIED:
                prefix = "Custom Header EMAIL confirm account ver ";
                height = 50;
                break;
            case CONFIRM_INSTANT_VERIFICATION_LOGIN:
                prefix = "Custom Header confirm instant ver ";
                height = 50;
                break;
            case ERROR:
                Log.e(TAG, "getHeaderFragment ERROR state");
            default:
                return null;
        }
        return PlaceholderFragment.create(height, prefix);
    }

    @Nullable
    @Override
    public Fragment getFooterFragment(LoginFlowState state) {
        final String prefix;
        final int height;
        switch (state) {
            case PHONE_NUMBER_INPUT:
                prefix = "Custom Footer Phone Input ";
                height = 70;
                break;
            case EMAIL_INPUT:
                prefix = "Custom Footer  EMAIL Input ";
                height = 50;
                break;
            case EMAIL_VERIFY:
                prefix = "Custom Footer EMAIL verify ";
                height = 50;
                break;
            case SENDING_CODE:
                prefix = "Custom Footer sending code";
                height = 50;
                break;
            case SENT_CODE:
                prefix = "Custom Footer senct code";
                height = 50;
                break;
            case CODE_INPUT:
                prefix = "Custom Footer code input ";
                height = 50;
                break;
            case VERIFYING_CODE:
                prefix = "Custom Footer verifying code ";
                height = 50;
                break;
            case VERIFIED:
                prefix = "Custom Footer verified";
                height = 50;
                break;
            case ACCOUNT_VERIFIED:
                prefix = "Custom Footer account verified ";
                height = 50;
                break;
            case CONFIRM_ACCOUNT_VERIFIED:
                prefix = "Custom Footer EMAIL confirm account ver ";
                height = 50;
                break;
            case CONFIRM_INSTANT_VERIFICATION_LOGIN:
                prefix = "Custom Footer confirm instant ver ";
                height = 50;
                break;
            case ERROR:
                Log.e(TAG, "getHeaderFragment ERROR state");
            default:
                return null;
        }
        return PlaceholderFragment.create(height, prefix);
    }

    @Nullable
    @Override
    public Fragment getBodyFragment(LoginFlowState state) {
        final String prefix;
        final int height;
        switch (state) {
            case PHONE_NUMBER_INPUT:
                prefix = "Custom Body Phone Input ";
                height = 70;
                break;
            case EMAIL_INPUT:
                prefix = "Custom Body  EMAIL Input ";
                height = 50;
                break;
            case EMAIL_VERIFY:
                prefix = "Custom Body EMAIL verify ";
                height = 50;
                break;
            case SENDING_CODE:
                prefix = "Custom Body sending code";
                height = 50;
                break;
            case SENT_CODE:
                prefix = "Custom Body senct code";
                height = 50;
                break;
            case CODE_INPUT:
                prefix = "Custom Body code input ";
                height = 50;
                break;
            case VERIFYING_CODE:
                prefix = "Custom Body verifying code ";
                height = 50;
                break;
            case VERIFIED:
                prefix = "Custom Body verified";
                height = 50;
                break;
            case ACCOUNT_VERIFIED:
                prefix = "Custom Body account verified ";
                height = 50;
                break;
            case CONFIRM_ACCOUNT_VERIFIED:
                prefix = "Custom Body EMAIL confirm account ver ";
                height = 50;
                break;
            case CONFIRM_INSTANT_VERIFICATION_LOGIN:
                prefix = "Custom Body confirm instant ver ";
                height = 50;
                break;
            case ERROR:
                Log.e(TAG, "getHeaderFragment ERROR state");
            default:
                return null;
        }
        return PlaceholderFragment.create(height, prefix);
    }

    public
    @Nullable
    ButtonType getButtonType(final LoginFlowState state) {
        switch (state) {
            case PHONE_NUMBER_INPUT:
                return entryButton;
            case EMAIL_INPUT:
                return entryButton;
            case CODE_INPUT:
                return confirmButton;
            default:
                return null;
        }
    }

    @Override
    public void onError(final AccountKitError error) {
        // handle error
        Log.e(TAG, "onError" + error);
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(confirmButton != null ? confirmButton.name() : null);
        dest.writeString(entryButton != null ? entryButton.name() : null);
        dest.writeString(textPosition != null ? textPosition.name() : null);
    }

    public static final Creator<MyAdvancedUIManager> CREATOR
            = new Creator<MyAdvancedUIManager>() {
        @Override
        public MyAdvancedUIManager createFromParcel(final Parcel source) {
            return new MyAdvancedUIManager(source);
        }

        @Override
        public MyAdvancedUIManager[] newArray(final int size) {
            return new MyAdvancedUIManager[size];
        }
    };
}

