package tech.xlogix.threeSum.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import tech.xlogix.threeSum.R;

public class PreMainActivity extends AppCompatActivity {

    ImageView cardView, cardView2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_main);

        cardView = findViewById(R.id.thumbnail_1);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PreMainActivity.this, CheckInActivity.class));
            }
        });

        cardView2 = findViewById(R.id.thumbnail_4);
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PreMainActivity.this, MainActivity.class));
            }
        });

    }
}
