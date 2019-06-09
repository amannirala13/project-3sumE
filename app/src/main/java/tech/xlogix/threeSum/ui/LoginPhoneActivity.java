package tech.xlogix.threeSum.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import tech.xlogix.threeSum.R;
import tech.xlogix.threeSum.ui.auth.FirstTimeUser;

public class LoginPhoneActivity extends AppCompatActivity {

    private static final String TAG = "PhoneAuthActivity";
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;
    EditText otp;
    TextView phoneNum;
    Button done, edit, resend;
    LinearLayout phoneview;
    LinearLayout otpview;
    LinearLayout progressview;
    FirebaseAuth mAuth;
    private Button getotp;
    private EditText phone;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private ViewGroup mPhoneNumberViews;
    private ViewGroup mSignedInViews;
    private TextView mStatusText;
    private TextView mDetailText;
    private EditText mPhoneNumberField;
    private EditText mVerificationField;
    private Button mStartButton;
    private Button mVerifyButton;
    private Button mResendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);


        getotp = findViewById(R.id.getotp);
        phoneview = findViewById(R.id.phoneview);
        otpview = findViewById(R.id.otpview);
        progressview = findViewById(R.id.progressview);
        phone = findViewById(R.id.phone);
        done = findViewById(R.id.done);
        resend = findViewById(R.id.resend);
        edit = findViewById(R.id.edit);
        otp = findViewById(R.id.otp);
        phoneNum = findViewById(R.id.number);


        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                Toast.makeText(getApplicationContext(), "Verified", Toast.LENGTH_SHORT).show();
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
//                updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                Toast.makeText(getApplicationContext(), "Verification Failed", Toast.LENGTH_SHORT).show();
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    phone.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(getApplicationContext(), "Code Sent", Toast.LENGTH_SHORT).show();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                progressview.setVisibility(View.GONE);
                otpview.setVisibility(View.VISIBLE);
                // [START_EXCLUDE]
                // Update UI
//                updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!otp.getText().toString().isEmpty()) {

                    String code = otp.getText().toString();
                    verifyPhoneNumberWithCode(mVerificationId, code);
                }
            }


        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = phone.getText().toString();

                resendVerificationCode(phoneNumber, mResendToken);


            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                otpview.setVisibility(View.GONE);
                progressview.setVisibility(View.VISIBLE);
                progressview.setVisibility(View.GONE);
                phoneview.setVisibility(View.VISIBLE);
            }
        });


        // [END phone_auth_callbacks]
        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            String phone;

                if (!phone.getText().toString().isEmpty()) {

                    String phoneNumber = phone.getText().toString();

                    startPhoneNumberVerification("+91 " + phoneNumber);
                    phoneview.setVisibility(View.GONE);
                    phoneNum.setText("+91 " + phoneNumber);
                    progressview.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter Phone Number", Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    private void startPhoneNumberVerification(String phoneNumber) {


        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

//        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {

    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");


                            FirebaseUser user = task.getResult().getUser();

                            if (user.getEmail() == null) {

                                Intent intent = new Intent(getApplicationContext(), FirstTimeUser.class);
                                startActivity(intent);
                                finish();


                            } else {
                                Intent intent = new Intent(getApplicationContext(), PreMainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                otp.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
//                            updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }

}
