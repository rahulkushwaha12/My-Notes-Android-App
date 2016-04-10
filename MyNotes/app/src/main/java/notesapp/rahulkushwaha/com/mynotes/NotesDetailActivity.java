 package notesapp.rahulkushwaha.com.mynotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import data.DatabaseHandler;

 public class NotesDetailActivity extends AppCompatActivity {
    private TextView title, date, content;
     private Button deleteBtn;
     private Button editBtn;
     private Button shareBtn;
    private  String ftitle;
     private String fcontent;
     private Button newBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_detail);
        title = (TextView)findViewById(R.id.detailsTitle);
        date = (TextView)findViewById(R.id.dateDetailsText);
        content = (TextView)findViewById(R.id.detailsTextView);
        deleteBtn = (Button)findViewById(R.id.deleteButton);
        editBtn = (Button)findViewById(R.id.editButton);
        shareBtn = (Button)findViewById(R.id.shareButton);
        newBtn = (Button)findViewById(R.id.newButton);
        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotesDetailActivity.this,MainActivity.class));
            }
        });
        final Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            ftitle = extras.getString("title");
            final String fdate = extras.getString("date");
            fcontent = extras.getString("content");
            title.setText(extras.getString("title"));
            date.setText("Created: "+extras.getString("date"));
            content.setText(" \" "+extras.getString("content")+ " \" ");
            final int id = extras.getInt("id");
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler dba = new DatabaseHandler(getApplicationContext());
                    dba.deleteNote(id);
                    Toast.makeText(getApplicationContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NotesDetailActivity.this, DisplayNotesActivity.class));
                }
            });

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(NotesDetailActivity.this,EditDetailActivity.class);
                    i.putExtra("content",fcontent);
                    i.putExtra("id",id);
                    i.putExtra("title",ftitle);
                    i.putExtra("date",fdate);
                    startActivity(i);
                }
            });
        }

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = fcontent;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, ftitle);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
    }
}
