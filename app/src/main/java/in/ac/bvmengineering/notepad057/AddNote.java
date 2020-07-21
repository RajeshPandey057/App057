package in.ac.bvmengineering.notepad057;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class AddNote extends AppCompatActivity {
    Toolbar toolbar;
    EditText noteTitle, noteDetail;
    Calendar c;
    String todayDate;
    String currentTime;
    Button call, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitleTextColor((getResources().getColor(R.color.white)));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        noteTitle = findViewById(R.id.noteTitle);
        noteDetail = findViewById(R.id.noteDetail);
        call = findViewById(R.id.callButton);
        message = findViewById(R.id.smsButton);

        FloatingActionButton buttonPickContact = findViewById(R.id.pickContact);
        buttonPickContact.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, 1);


            }});
            call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Calling dialer Via Implicit Intent" + noteTitle.getText().toString(), Toast.LENGTH_SHORT).show();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + noteTitle.getText().toString()));
                if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(v.getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(callIntent);


            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"SMS Sent to "+noteTitle.getText().toString(),Toast.LENGTH_SHORT).show();
                SmsManager.getDefault().sendTextMessage(noteTitle.getText().toString(), null, noteDetail.getText().toString(), null, null);
            }
        });

        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        c = Calendar.getInstance();
        todayDate = c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH)+1)+ "/" + c.get(Calendar.DAY_OF_MONTH);
        currentTime = pad(c.get(Calendar.HOUR))+ ":" + pad(c.get(Calendar.MINUTE));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                Uri contactData = data.getData();
                assert contactData != null;
                Cursor cursor =  getContentResolver().query(contactData, null, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                String number =       cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

                //contactName.setText(name);
                noteTitle.setText(number);
                cursor.close();
                //contactEmail.setText(email);
            }
        }
    }

    private String pad(int i) {
        if(i < 10) return "0" + i;
        else return String.valueOf(i);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.delete) {
            Toast.makeText(this,"Note Deleted",Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        if(item.getItemId() == R.id.save) {
            Note note = new Note(noteTitle.getText().toString(),noteDetail.getText().toString(),todayDate,currentTime);
            NoteDatabase db = new NoteDatabase(this);
            db.addNote(note);
            Toast.makeText(this,"Note Saved",Toast.LENGTH_SHORT).show();
            goToMain();

        }
        return super.onOptionsItemSelected(item);
    }

    private void goToMain() {
        Intent n = new Intent(this,MainActivity.class);
        startActivity(n);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
