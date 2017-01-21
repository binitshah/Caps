package com.binitshah.caps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends Activity {

    int price = 19;
    int distance = 5;
    boolean outdoor = true;
    boolean indoor = true;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        SharedPreferences prefs = getSharedPreferences("CAPS", MODE_PRIVATE);

        boolean firstTimeBoot = prefs.getBoolean("firsttimeboot", true);
        if(firstTimeBoot){
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }

        boolean loggedin = prefs.getBoolean("login", false);
        if(loggedin){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            new DownloadImageTask((CircleImageView) findViewById(R.id.profile_image))
                    .execute(user.getPhotoUrl().toString());
        }

        submitButton = (Button) findViewById(R.id.submit_button);
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
        String urldisplay;

        public DownloadImageTask(CircleImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            urldisplay = urls[0];
            Bitmap mIcon11 = null;
            if(!urldisplay.equals("")) {
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("CAPSDEBUG", e.getMessage());
                    e.printStackTrace();
                }
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if(!urldisplay.equals("")) {
                bmImage.setImageBitmap(result);
            }
        }
    }
}
