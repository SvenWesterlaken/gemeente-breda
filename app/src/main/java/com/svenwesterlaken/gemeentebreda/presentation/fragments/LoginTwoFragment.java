package com.svenwesterlaken.gemeentebreda.presentation.fragments;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.svenwesterlaken.gemeentebreda.R;
import com.svenwesterlaken.gemeentebreda.data.database.DatabaseHandler;
import com.svenwesterlaken.gemeentebreda.domain.User;
import com.svenwesterlaken.gemeentebreda.presentation.activities.ReportActivity;

/**
 * Created by Sven Westerlaken on 12-5-2017.
 */

public class LoginTwoFragment extends Fragment {
    private TextInputLayout inputLayoutUsername;
    private TextInputLayout inputLayoutEmail;
    private TextInputLayout inputLayoutPhone;
    private EditText email;
    private EditText username;
    private EditText phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        inputLayoutUsername = (TextInputLayout) rootView.findViewById(R.id.login_TIL_username);
        username = (EditText) rootView.findViewById(R.id.login_ET_username);

        inputLayoutEmail = (TextInputLayout) rootView.findViewById(R.id.login_TIL_email);
        email = (EditText) rootView.findViewById(R.id.login_ET_email);

        inputLayoutPhone = (TextInputLayout) rootView.findViewById(R.id.login_TIL_phone);
        phone = (EditText) rootView.findViewById(R.id.login_ET_phone);

        username.addTextChangedListener(new LoginTextWatcher(username));
        email.addTextChangedListener(new LoginTextWatcher(email));
        phone.addTextChangedListener(new LoginTextWatcher(phone));

        Button submitBTN = (Button) rootView.findViewById(R.id.login_btn_submit);
        submitBTN.setOnClickListener(new RegisterClickListener());

        return rootView;
    }

    private boolean validateName() {
        String str = username.getText().toString().trim();

        if (str.isEmpty() || !str.contains(" ")) {
            inputLayoutUsername.setError(getString(R.string.login_error_name));
            requestFocus(username);
            return false;
        } else {
            inputLayoutUsername.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String str = email.getText().toString().trim();

        if (str.isEmpty() || !isValidEmail(str)) {
            inputLayoutEmail.setError(getString(R.string.login_error_email));
            requestFocus(email);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validatePhone() {
        String regex = "(^\\+[0-9]{2}|^\\+[0-9]{2}\\(0\\)|^\\(\\+[0-9]{2}\\)\\(0\\)|^00[0-9]{2}|^0)([0-9]{9}$|[0-9\\-\\s]{9}$)";
        String str = phone.getText().toString().trim();

        if (!str.isEmpty() && !str.matches(regex)) {
                inputLayoutPhone.setError(getString(R.string.login_error_phone));
                requestFocus(phone);
                return false;
        }

        inputLayoutPhone.setErrorEnabled(false);
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class RegisterClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (!validateName()) { return; }
            if (!validateEmail()) { return; }
            if (!validatePhone()) { return; }

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("pref_name", username.getText().toString());
            editor.putString("pref_email", email.getText().toString());

            if (!TextUtils.isEmpty(phone.getText())) {
                editor.putString("pref_phone", phone.getText().toString());
            }

            editor.apply();


            DatabaseHandler  handler = new DatabaseHandler(getContext());
            User user = new User();
            user.setName(preferences.getString("pref_name", ""));
            user.setEmailaddress(preferences.getString("pref_email", ""));
            user.setMobileNumber(preferences.getString("pref_phone", ""));
            user.setUserID(1);
            handler.addUser( user);

            Intent i = new Intent(getActivity().getApplicationContext(), ReportActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            getActivity().finish();
           
        }
    }

    private class LoginTextWatcher implements TextWatcher {

        private View view;

        private LoginTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //No need for this
        }
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //No need for this
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.login_ET_username:
                    validateName();
                    break;
                case R.id.login_ET_email:
                    validateEmail();
                    break;
                case R.id.login_ET_phone:
                    validatePhone();
                    break;
                default:
                    break;
            }
        }
    }

}
