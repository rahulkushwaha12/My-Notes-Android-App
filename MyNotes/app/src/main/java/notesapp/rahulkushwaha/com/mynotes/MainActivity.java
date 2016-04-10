package notesapp.rahulkushwaha.com.mynotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import data.DatabaseHandler;
import model.MyNotes;

public class MainActivity extends AppCompatActivity {
    private EditText title;
    private EditText content;
    private Button saveButton;
    private DatabaseHandler dba;
    private Button allNotesBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dba = new DatabaseHandler(MainActivity.this);

        title = (EditText)findViewById(R.id.title);
        content = (EditText)findViewById(R.id.notes);
        saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Title field cannot be empty!",Toast.LENGTH_LONG).show();
                }
                else if(content.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Content field cannot be empty!",Toast.LENGTH_LONG).show();
                }
                else{
                    saveToDB();
                }

            }
        });
        allNotesBtn = (Button)findViewById(R.id.allNotesButton);
        allNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,DisplayNotesActivity.class));
            }
        });
    }

    private void saveToDB() {
        MyNotes note = new MyNotes();
         note.setTitle(title.getText().toString().trim());
        note.setContent(content.getText().toString().trim());
        dba.addNotes(note);
        dba.close();

        //clear
        title.setText("");
        content.setText("");
       Intent i = new Intent(MainActivity.this,DisplayNotesActivity.class );
        startActivity(i);

    }
}
