package notesapp.rahulkushwaha.com.mynotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import data.DatabaseHandler;
import model.MyNotes;

public class DisplayNotesActivity extends AppCompatActivity {
    private DatabaseHandler dba;
    private ArrayList<MyNotes> dbNotes = new ArrayList<>();
    private NoteAdapter noteAdapter;
    private ListView listView;
    private Button newBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notes);
        listView =(ListView)findViewById(R.id.list);
        refreshData();
        newBtn = (Button)findViewById(R.id.newButton);
        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DisplayNotesActivity.this,MainActivity.class));
            }
        });
    }

    private void refreshData() {
        dbNotes.clear();
        dba = new DatabaseHandler(getApplicationContext());
        ArrayList<MyNotes> notesFromDB = dba.getNotes();
        for (int i=0; i<notesFromDB.size(); i++){
            String title = notesFromDB.get(i).getTitle();
            String content =  notesFromDB.get(i).getContent();
            String dateText = notesFromDB.get(i).getRecordDate();
            int mId  = notesFromDB.get(i).getItemId();

            MyNotes myNotes = new MyNotes();
            myNotes.setTitle(title);
            myNotes.setContent(content);
            myNotes.setRecordDate(dateText);
            myNotes.setItemId(mId);
            dbNotes.add(myNotes);
        }
        dba.close();
        //setup adapter
        noteAdapter = new NoteAdapter(DisplayNotesActivity.this, R.layout.note_row, dbNotes);
        listView.setAdapter(noteAdapter);
        noteAdapter.notifyDataSetChanged();
    }

    public class NoteAdapter extends ArrayAdapter<MyNotes> {
        Activity activity;
        int layoutResource;
        MyNotes notes;
        ArrayList<MyNotes> mData = new ArrayList<>();
        public NoteAdapter(Activity act, int resource, ArrayList<MyNotes> data) {
            super(act, resource, data);
            activity = act;
            layoutResource = resource;
            mData = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public MyNotes getItem(int position) {
            return mData.get(position);
        }

        @Override
        public int getPosition(MyNotes item) {
            return super.getPosition(item);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            View row = convertView;
            ViewHolder holder = null;
            if (row == null || (row.getTag())==null){
                LayoutInflater inflater = LayoutInflater.from(activity);
                row = inflater.inflate(layoutResource,null);
                holder = new ViewHolder();
                holder.mTitle = (TextView) row.findViewById(R.id.name);
                holder.mDate = (TextView) row.findViewById(R.id.dateText);
                row.setTag(holder);
            }else{
                holder = (ViewHolder)row.getTag();
            }
            holder.myNotes = getItem(position);
            holder.mTitle.setText(holder.myNotes.getTitle());
            holder.mDate.setText(holder.myNotes.getRecordDate());
            final ViewHolder finalHolder = holder;
            holder.mTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = finalHolder.myNotes.getContent().toString();
                    String dateText = finalHolder.myNotes.getRecordDate().toString();
                    String title = finalHolder.myNotes.getTitle().toString();
                    int mId = finalHolder.myNotes.getItemId();
                    Intent i = new Intent(DisplayNotesActivity.this, NotesDetailActivity.class);
                    i.putExtra("content",text);
                    i.putExtra("date",dateText);
                    i.putExtra("title",title);
                    i.putExtra("id",mId);
                    startActivity(i);
                }
            });
            return row;
        }
        class ViewHolder{
            MyNotes myNotes;
            TextView mTitle;
            TextView mId;
            TextView mContent;
            TextView mDate;
        }
    }


}
