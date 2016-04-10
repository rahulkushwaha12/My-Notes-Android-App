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

public class EditDetailActivity extends AppCompatActivity {
    private EditText edittitle;
    private EditText editcontent;
    private Button updateBtn;
    private DatabaseHandler dba;
    private int id;
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detail);
        edittitle = (EditText)findViewById(R.id.edittitle);
        editcontent = (EditText)findViewById(R.id.editnotes);
        updateBtn = (Button)findViewById(R.id.editsaveButton);
        dba = new DatabaseHandler(EditDetailActivity.this);
        final Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            edittitle.setText(extras.getString("title"));
            editcontent.setText(extras.getString("content"));
             id = extras.getInt("id");
            date = extras.getString("date");
        }
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edittitle.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Title field cannot be empty!",Toast.LENGTH_LONG).show();
                }
                else if(editcontent.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Content field cannot be empty!",Toast.LENGTH_LONG).show();
                }
                else{
                    saveToDB(id,date);
                }

            }
        });
    }
    private void saveToDB(int id, String date) {
        MyNotes note = new MyNotes();
        note.setTitle(edittitle.getText().toString().trim());
        note.setContent(editcontent.getText().toString().trim());
        //note.setRecordDate(date);
        //dba.addNotes(note);
        //update notes
        dba.updateNotes(note,id);
        dba.close();
        Intent i = new Intent(EditDetailActivity.this,DisplayNotesActivity.class );
        Toast.makeText(getApplicationContext(), "Note Updated", Toast.LENGTH_SHORT).show();
        startActivity(i);

    }
}
