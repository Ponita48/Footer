package com.coin.footer.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.coin.footer.R;
import com.coin.footer.services.APIService;

import com.coin.footer.dao.DefaultMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText editUname;
    private EditText editPass;
    private EditText editFull;
    private EditText editNick;
    private EditText editEmail;
    private RadioGroup radioGroup;
    private ProgressBar loading;
    private ScrollView registerForm;

    private String uname;
    private String password;
    private String fullName;
    private String nickname;
    private String gender;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        editUname = (EditText) findViewById(R.id.usernameReg);
        editPass = (EditText) findViewById(R.id.pwdReg);
        editFull = (EditText) findViewById(R.id.fullReg);
        editNick = (EditText) findViewById(R.id.nickReg);
        editEmail = (EditText) findViewById(R.id.emailReg);
        radioGroup = (RadioGroup) findViewById(R.id.radioGender);
        loading = (ProgressBar) findViewById(R.id.loadingReg);
        registerForm = (ScrollView) findViewById(R.id.layoutRegister);
        radioGroup.check(R.id.radioMale);


        Button btn = (Button) findViewById(R.id.registerButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRegister();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getText() {
        uname = editUname.getText().toString();
        password = editPass.getText().toString();
        fullName = editFull.getText().toString();
        nickname = editNick.getText().toString();
        email = editEmail.getText().toString();
        if (radioGroup.getCheckedRadioButtonId() == R.id.radioMale) {
            gender = "Pria";
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioFemale) {
            gender = "Wanita";
        } else {
            gender = "Other";
        }
    }

    public void postRegister() {
        if (checkError()) {
            showProgress(true);
            Call<DefaultMessage> call = APIService.services.postRegister(uname, fullName, nickname,
                    gender, email, password);
            call.enqueue(new Callback<DefaultMessage>() {
                @Override
                public void onResponse(Call<DefaultMessage> call, Response<DefaultMessage> response) {
                    if (response.isSuccessful()) {
                        DefaultMessage body = response.body();
                        Toast.makeText(RegisterActivity.this, "titit", Toast.LENGTH_SHORT).show();
                        if (body.isStatus()) {
                            Toast.makeText(RegisterActivity.this,
                                    "Anda telah terdaftar!, silahkan cek email anda",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Connection Timed Out",
                                    Toast.LENGTH_SHORT).show();
                        }
                        showProgress(false);
                    }
                }

                @Override
                public void onFailure(Call<DefaultMessage> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    showProgress(false);
                }
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            registerForm.setVisibility(show ? View.GONE : View.VISIBLE);
            registerForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    registerForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            loading.setVisibility(show ? View.VISIBLE : View.GONE);
            loading.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loading.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            loading.setVisibility(show ? View.VISIBLE : View.GONE);
            registerForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public boolean checkError() {
        editUname.setError(null);
        editPass.setError(null);
        editFull.setError(null);
        editNick.setError(null);
        editEmail.setError(null);

        getText();

        // Store values at the time of the login attempt.

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            editPass.setError(getString(R.string.error_invalid_password));
            focusView = editPass;
            cancel = true;
        }

        // Check for a valid username
        if (TextUtils.isEmpty(uname)) {
            editUname.setError(getString(R.string.error_field_required));
            focusView = editUname;
            cancel = true;
        } else if (!isFieldValid(uname)) {
            editUname.setError(getString(R.string.error_invalid_uname));
            focusView = editUname;
            cancel = true;
        }
        // Check for a valid full name
        if (TextUtils.isEmpty(fullName)) {
            editFull.setError(getString(R.string.error_field_required));
            focusView = editFull;
            cancel = true;
        } else if (!isFieldValid(fullName)) {
            editFull.setError(getString(R.string.error_invalid_uname));
            focusView = editFull;
            cancel = true;
        }
        // Check for a valid nick name
        if (TextUtils.isEmpty(nickname)) {
            editNick.setError(getString(R.string.error_field_required));
            focusView = editNick;
            cancel = true;
        } else if (!isFieldValid(nickname)) {
            editNick.setError(getString(R.string.error_invalid_uname));
            focusView = editNick;
            cancel = true;
        }

        // Check for a valid email
        if (TextUtils.isEmpty(email)) {
            editEmail.setError(getString(R.string.error_field_required));
            focusView = editEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            editEmail.setError(getString(R.string.error_invalid_uname));
            focusView = editEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public boolean isPasswordValid(String password) {
        return password.length() > 6;
    }

    public boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    public boolean isFieldValid(String field) {
        return field.length() > 4;
    }

}
