package com.github.gserej.warthundersqbhelper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    EditText mEditTagText;
    EditText mEditIPText;

    String tagName;
    String ipAddress;


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mEditTagText = findViewById(R.id.editSquadronName);
        mEditTagText.setEnabled(true);


        mEditIPText = findViewById(R.id.editIPAddress);
        mEditIPText.setEnabled(true);

        TextView link = findViewById(R.id.linkView);
        String linkText = "<a href='https://www.groovypost.com/howto/microsoft/windows-7/find-your-local-ip-address-windows-7-cmd/'>" +
                "Click here to find out how to find your local IP address</a>.";
        link.setText(Html.fromHtml(linkText));
        link.setMovementMethod(LinkMovementMethod.getInstance());


        SharedPreferences pref = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        tagName = pref.getString("squadronTag", "");
        ipAddress = pref.getString("ipAddress", "");

        if (tagName.equals("")) {
            mEditTagText.setText("ACEpl");
            editor.remove("squadronTag");
            editor.putString("squadronTag", "ACEpl");
            editor.apply();
        } else {
            mEditTagText.setText(tagName);
        }


        if (ipAddress.equals("")) {
            mEditIPText.setText("192.168.1.1");
            editor.remove("ipAddress");
            editor.putString("ipAddress", "192.168.1.1");
            editor.apply();
        } else {
            mEditIPText.setText(ipAddress);
        }


        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start,
                                       int end, Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart) +
                            source.subSequence(start, end) +
                            destTxt.substring(dend);
                    if (!resultingTxt.matches("^\\d{1,3}(\\." +
                            "(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i = 0; i < splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }
        };
        mEditIPText.setFilters(filters);

        mEditTagText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (v.getId() == mEditTagText.getId()) {
                    mEditTagText.setCursorVisible(true);
                }
            }
        });

        mEditTagText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                editor.remove("squadronTag");
                editor.putString("squadronTag", mEditTagText.getText().toString());
                editor.apply();
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(mEditTagText.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

        mEditTagText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    editor.remove("squadronTag");
                    editor.putString("squadronTag", mEditTagText.getText().toString());
                    editor.apply();
                }
            }
        });

        mEditIPText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    mEditIPText.setCursorVisible(false);
                    editor.remove("ipAddress");
                    editor.putString("ipAddress", mEditIPText.getText().toString());
                    editor.apply();
                    hideKeyboard(v);
                    mEditIPText.clearFocus();

                    return true;
                }
                return false;
            }
        });

        mEditIPText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (v.getId() == mEditIPText.getId()) {
                    mEditIPText.setCursorVisible(true);
                }
            }
        });

        mEditIPText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                editor.remove("ipAddress");
                editor.putString("ipAddress", mEditIPText.getText().toString());
                editor.apply();
                hideKeyboard(v);
                mEditIPText.clearFocus();
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(mEditIPText.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

                return false;
            }
        });


        mEditIPText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    editor.remove("ipAddress");
                    editor.putString("ipAddress", mEditIPText.getText().toString());
                    editor.apply();
                }
            }
        });

    }

}