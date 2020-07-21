package in.ac.bvmengineering.notepad057;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        int SPALSH_TIME_OUT = 2000;
        Toast.makeText(this,"Explicit Intent Called",Toast.LENGTH_SHORT);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent (HomeActivity.this,MainActivity.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPALSH_TIME_OUT);

    }
}
