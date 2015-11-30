package com.cmpe277.skibuddy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by anusha on 11/25/15.
 */
public class SignedInUi extends Activity {

    private ImageView i;
    private TextView n;
    private TextView e;
    URL u;
    private Button skiDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signedin_ui);
        n = (TextView) findViewById(R.id.nameid);
        e = (TextView) findViewById(R.id.emailid);
        i = (ImageView)findViewById(R.id.imageView);
        skiDetails = (Button) findViewById(R.id.skiSession);
        skiDetails.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewSkiDetails();
            }
        });




        if (getIntent().getExtras() != null ) {
            String name = getIntent().getStringExtra("name");
            String email = getIntent().getStringExtra("email");
            String url = getIntent().getStringExtra("url");
            n.setText(name);
            e.setText(email);
            try {
                u = new URL(url);
            } catch (MalformedURLException e) {

            }
            new LoadProfileImage(i, u).execute();
        }
    }

    public void startMapView(View view) {
        Log.d("Hello", "Hello");
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

    }

    private void viewSkiDetails()
    {
        //Intent myIntent = new Intent(MainActivity.this, SkiDetailListActivity.class);
        //myIntent.putExtra("key", value); //Optional parameters
        //MainActivity.this.startActivity(myIntent);
        Intent intent = new Intent(this, SkiDetailListActivity.class);
        intent.putExtra("userID",MainActivity.getUserAcctId());
        Toast.makeText(this, "Button Clicked ", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.signinmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView profileimage;
        URL imageURL;
        Bitmap bitmap;
        public LoadProfileImage(ImageView profileimage, URL url) {
            this.profileimage = profileimage;
            this.imageURL = url;
        }
        protected Bitmap doInBackground(String... urls) {

            try{
                bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
            }catch (IOException e){

            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            profileimage.setImageBitmap(result);
        }
    }


}
