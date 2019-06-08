package tech.xlogix.threeSum.ui.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tech.xlogix.threeSum.R;
import tech.xlogix.threeSum.model.User;
import tech.xlogix.threeSum.ui.LoginPhoneActivity;
import tech.xlogix.threeSum.ui.MainActivity;


public class FirstTimeUser extends AppCompatActivity {

    EditText fullname, email, dateofbirth;
    FirebaseAuth auth;
    FirebaseUser mFirebaseUser;
    LinearLayout progressview, mainview;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_user);
        auth = FirebaseAuth.getInstance();
        mFirebaseUser = auth.getCurrentUser();
        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        done = findViewById(R.id.done);
        progressview = findViewById(R.id.progressview);
        mainview = findViewById(R.id.mainview);
        hideProgress();

        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user == null) {
                    startActivity(new Intent(FirstTimeUser.this, LoginPhoneActivity.class));
                    finish();
                }

            }
        };

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                boolean error = false;

                if (fullname.getText().toString().isEmpty()) {
                    error = true;
                    fullname.setError("Full Name is Required");
                }

                if (email.getText().toString().isEmpty()) {
                    error = true;
                    email.setError("Full Name is Required");
                }


                if (dateofbirth.getText().toString().isEmpty()) {
                    error = true;
                    dateofbirth.setError("Full Name is Required");
                }

                if (!error) {

                    String FullName = fullname.getText().toString();
                    final String Email = email.getText().toString();

                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                    String date = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(new Date());
                    String profilepic = mFirebaseUser.getPhotoUrl().toString();
                    User users = new User(FullName, Email, mFirebaseUser.getPhoneNumber(), "Student", "NA", date, profilepic);


                    mDatabase.child(mFirebaseUser.getUid()).setValue(users);
                    final UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(FullName)
                            .setPhotoUri(Uri.parse(profilepic))

                            .build();
                    mFirebaseUser.updateEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mFirebaseUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("FirstTimeUser.java", "User profile updated.");
                                                startActivity(new Intent(FirstTimeUser.this, MainActivity.class));
                                                finish();
                                            }
                                        }
                                    });

                        }
                    });


                } else {
                    hideProgress();
                }
            }
        });

    }

    public void showProgress() {
        mainview.setVisibility(View.GONE);
        progressview.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progressview.setVisibility(View.GONE);
        mainview.setVisibility(View.VISIBLE);
    }

}
