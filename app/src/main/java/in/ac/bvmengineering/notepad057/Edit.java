package in.ac.bvmengineering.notepad057;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class Edit extends AppCompatActivity {
    Toolbar toolbar;
    EditText noteTitle, noteDetail;
    Calendar c;
    String todayDate;
    String currentTime;
    NoteDatabase db;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        toolbar =  findViewById(R.id.toolBar);
        toolbar.setTitleTextColor((getResources().getColor(R.color.white)));
        setSupportActionBar(toolbar);
        Intent i = getIntent();
        Long id = i.getLongExtra("ID",0);
        db = new NoteDatabase(this);
        note = db.getNote(id);

        getSupportActionBar().setTitle(note.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        noteTitle = findViewById(R.id.noteTitle);
        noteDetail = findViewById(R.id.noteDetail);
        noteTitle.setText(note.getTitle());
        noteDetail.setText(note.getContent());
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
            note.setTitle(noteTitle.getText().toString());
            note.setContent(noteDetail.getText().toString());
            int id = db.editNote(note);
            if (id==note.getID()){
                Toast.makeText(this,"Note Updated",Toast.LENGTH_SHORT).show();
            }else{
                {
                    Toast.makeText(this,"Note not Updated",Toast.LENGTH_SHORT).show();
                }
            }
            Intent i = new Intent(getApplicationContext(),Detail.class);
            i.putExtra("ID",note.getID());
            startActivity(i);

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