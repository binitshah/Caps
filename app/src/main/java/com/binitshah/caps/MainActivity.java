package com.binitshah.caps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    ImageView expandButton;
    ImageView compressButton;
    LinearLayout filters;
    SeekBar distanceSeekBar;
    SeekBar priceSeekBar;
    Switch inoutdoorSwitch;
    int price = 19;
    int distance = 5;
    boolean outdoor = false;
    TextView distanceTextView;
    TextView priceTextView;
    Button randomButton;
    Button submitButton;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("LOGGEDIN", MODE_PRIVATE);
        boolean loggedin = prefs.getBoolean("log", false);
        if(!loggedin){
            Intent intent = new Intent(this, SigninActivity.class);
            startActivity(intent);
            finish();
        }

        String url = prefs.getString("url", "http://www.umcmission.org/Images/UserUploadedImages/158/profile%20generic.jpg");
        new DownloadImageTask((CircleImageView) findViewById(R.id.profile_image))
                .execute(url);

        expandButton = (ImageView) findViewById(R.id.expand_filters);
        compressButton = (ImageView) findViewById(R.id.compress_filters);
        filters = (LinearLayout) findViewById(R.id.filters);
        filters.setVisibility(View.GONE);
        distanceSeekBar = (SeekBar) findViewById(R.id.distance_seekbar);
        priceSeekBar = (SeekBar) findViewById(R.id.price_seekbar);
        inoutdoorSwitch = (Switch) findViewById(R.id.inoutdoor_switch);
        distanceTextView = (TextView) findViewById(R.id.distance_title);
        priceTextView = (TextView) findViewById(R.id.price_title);
        randomButton = (Button) findViewById(R.id.random_button);
        submitButton = (Button) findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                Cap capexample = new Cap(10.1,12.2,12.5,"aazi","Description",4.5,"2016/05/22 2:59:48",false);
                Cap capexample3 = new Cap(10.1,12.2,12.5,"welcome","Description",4.5,"2016/05/22 2:59:48",false);
                Cap capexample2 = new Cap(10.1,12.2,12.5,"aazssdasdi","thign",4.5,"2014/08/06 15:59:48",true);
                mDatabase.child("caps").setValue(capexample);
                mDatabase.child("cap").child("caps2").setValue(capexample2);
                mDatabase.child("cap").child("caps3").setValue(capexample2);
                mDatabase.child("cap").child("caps34").setValue(capexample3);
                mDatabase.push();
            }
        });

        distanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String distancePrefix = getResources().getString(R.string.distanceprefix);
                String distancePostfix = getResources().getString(R.string.distancepostfix);
                String message = distancePrefix + " " + progress + " "  + distancePostfix;
                distanceTextView.setText(message);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        priceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String pricePrefix= getResources().getString(R.string.priceprefix);
                String message = pricePrefix + progress;
                priceTextView.setText(message);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filters.setVisibility(View.VISIBLE);
                expandButton.setVisibility(View.GONE);
            }
        });

        compressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filters.setVisibility(View.GONE);
                expandButton.setVisibility(View.VISIBLE);
            }
        });

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price = priceSeekBar.getProgress();
                distance = distanceSeekBar.getProgress();
                outdoor = inoutdoorSwitch.isChecked();
                Intent intent = new Intent(getApplicationContext(), CapsActivity.class);
                intent.putExtra("price", price);
                intent.putExtra("distance", distance);
                intent.putExtra("outdoor", outdoor);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        CircleImageView bmImage;

        public DownloadImageTask(CircleImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
