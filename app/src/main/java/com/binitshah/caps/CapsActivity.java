package com.binitshah.caps;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CapsActivity extends AppCompatActivity {
    ArrayList<Cap> caps = new ArrayList<>();
    //final String TAG = "CAPSACTIVITY";
    //public final static long MILLIS_PER_DAY = 24 * 60 * 60 * 1000L;
    //private DatabaseReference mDatabase;
    RecyclerView capsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        int distance = intent.getIntExtra("distance", 5);
        int price = intent.getIntExtra("price", 19);
        final boolean outdoor = intent.getBooleanExtra("outdoor", false);

        capsRecyclerView = (RecyclerView) findViewById(R.id.caps_recyclerview);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        capsRecyclerView.setLayoutManager(horizontalLayoutManagaer);

        //caps.add(new Cap(10.0,10.0,10.0,"Lorem Ipsum","Est ec yuf volunti rand tum.",3.5,"Time",true));
        //caps.add(new Cap(10.0,10.0,10.0,"Lorem Ipsum","Est ec yuf volunti rand tum.",3.5,"Time",true));
        //caps.add(new Cap(10.0,10.0,10.0,"Lorem Ipsum","Est ec yuf volunti rand tum.",3.5,"Time",true));
        //caps.add(new Cap(10.0,10.0,10.0,"Lorem Ipsum","Est ec yuf volunti rand tum.",3.5,"Time",true));
        //caps.add(new Cap(10.0,10.0,10.0,"Lorem Ipsum","Est ec yuf volunti rand tum.",3.5,"Time",true));
        //caps.add(new Cap(10.0,10.0,10.0,"Lorem Ipsum","Est ec yuf volunti rand tum.",3.5,"Time",true));
        //caps.add(new Cap(10.0,10.0,10.0,"Lorem Ipsum","Est ec yuf volunti rand tum.",3.5,"Time",true));
        //CapAdapter adapter = new CapAdapter(caps);
        //capsRecyclerView.setAdapter(adapter);


        /*FirebaseDatabase.getInstance().getReference()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Cap cap = child.getValue(Cap.class);
                            Log.d("CAP", "CAP OBJECT: " + cap.getTitle());
                            try {
                                DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
                                Date dateMade = format.parse(cap.getTime());
                                Date currentDate = new Date();
                                boolean moreThanDay = Math.abs(dateMade.getTime() - currentDate.getTime()) > MILLIS_PER_DAY;
                                if(!moreThanDay){
                                    if(cap.isOutdoor() == outdoor) {
                                        caps.add(cap);
                                    }
                                }
                            }
                            catch (Exception e){
                                Log.e(TAG, "Issue with parsing the date: " + e);
                                caps.add(cap);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.toString());
                    }
                });*/


        //mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart(){
        super.onStart();

        /*FirebaseRecyclerAdapter<Cap, CapViewHolder> adapter = new FirebaseRecyclerAdapter<Cap, CapViewHolder>(
                Cap.class,
                R.layout.cap_card,
                CapViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(CapViewHolder viewHolder, Cap model, int position) {
                viewHolder.title.setText(model.getTitle());
                viewHolder.description.setText(model.getDescription());
            }
        };
        capsRecyclerView.setAdapter(adapter);*/

    }

    /*public class CapViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public ImageView star1;
        public ImageView star2;
        public ImageView star3;
        public ImageView star4;
        public ImageView star5;

        public CapViewHolder(View view) {
            super(view);

            title = (TextView) findViewById(R.id.cap_title);
            description = (TextView) findViewById(R.id.cap_description);
            star1 = (ImageView) findViewById(R.id.star1);
            star2 = (ImageView) findViewById(R.id.star2);
            star3 = (ImageView) findViewById(R.id.star3);
            star4 = (ImageView) findViewById(R.id.star4);
            star5 = (ImageView) findViewById(R.id.star5);
        }
    }*/

    /*public class CapAdapter extends RecyclerView.Adapter<CapAdapter.CapViewHolder> {

        private List<Cap> caplist;

        public class CapViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public TextView description;
            public ImageView star1;
            public ImageView star2;
            public ImageView star3;
            public ImageView star4;
            public ImageView star5;

            public CapViewHolder(View view) {
                super(view);

                title = (TextView) findViewById(R.id.cap_title);
                description = (TextView) findViewById(R.id.cap_description);
                star1 = (ImageView) findViewById(R.id.star1);
                star2 = (ImageView) findViewById(R.id.star2);
                star3 = (ImageView) findViewById(R.id.star3);
                star4 = (ImageView) findViewById(R.id.star4);
                star5 = (ImageView) findViewById(R.id.star5);
            }
        }


        public CapAdapter(ArrayList<Cap> horizontalList) {
            this.caplist = horizontalList;
        }

        @Override
        public CapViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cap_card, parent, false);

            return new CapViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final CapViewHolder holder, final int position) {
            holder.title.setText(caplist.get(position).getTitle());
            holder.description.setText(caplist.get(position).getDescription());
        }

        @Override
        public int getItemCount() {
            return caplist.size();
        }
    }*/
}
