package com.cmpe277.skibuddy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class MainActivity extends FragmentActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "Google Sign In";
    private static final int signInRequestCode = 9001;
    private  GoogleSignInAccount acct;
    private GoogleSignInResult result;
    private ImageView profilepic;
    private TextView name;
    private TextView email;

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private static Boolean isFacebook = false;

    private static String googlename;
    private static String googleemail;
    private static String facebookname;
    private static String facebookemail;
    private static String googleuserid;
    private static String facebookuserid;

    public static String getUserEmail(){
        if(isFacebook){
            return facebookemail;
        }else{
            return googleemail;
        }
    }
    public static String getUserAcctId(){
        if(isFacebook){
            return facebookuserid;
        }else{
            return googleuserid;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FACEBOOK
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        //this.getActionBar().show();

        //GOOGLE
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .requestProfile()
                .requestScopes(Plus.SCOPE_PLUS_PROFILE)
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();

        profilepic = (ImageView) findViewById(R.id.profilepic);
        name = (TextView) findViewById(R.id.nameid);
        email = (TextView) findViewById(R.id.emailid);

        profilepic.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
        email.setVisibility(View.GONE);

        //FACEBOOK
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("user_likes", "user_status", "email", "public_profile"));
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        isFacebook = true;
                        String FacebookUserID = loginResult.getAccessToken().getUserId();
                        facebookuserid = FacebookUserID;
                        new LoadProfileImage2(profilepic, FacebookUserID).execute();
                        //String FacebookAuthToken = loginResult.getAccessToken().getToken();
                        //System.out.println("inside");
                        /* make the API call */
                        new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                "/me",
                                null,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                    /* handle the result */
                                        Log.v("LoginActivity", response.toString());
                                        try{
                                            response.getJSONObject().getString("name");
                                            facebookname= response.getJSONObject().getString("name");
                                            System.out.println("name=" + facebookname);
                                            name.setText(facebookname);
                                            name.setVisibility(View.VISIBLE);
                                        }catch (JSONException j){

                                        }
                                    }
                                }
                        ).executeAsync();

                        /*new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                "/"+loginResult.getAccessToken(),
                                null,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                    handle the result
                                        System.out.println(response.toString());
                                        Log.v("LoginActivity", response.toString());

                                    }

                                }).executeAsync();
                        */

                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.i("LoginActivityfinal", response.toString());
                                // Get facebook data from login
                                Bundle bFacebookData = getFacebookData(object);
                                System.out.println("emailfrombundle" + bFacebookData.getString("email"));
                                facebookemail = bFacebookData.getString("email");
                                email.setText(facebookemail);
                                email.setVisibility(View.VISIBLE);
                            }
                        }
                    );
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id, first_name, last_name, email, gender"); // Parámetros que pedimos a facebook
                    request.setParameters(parameters);
                    request.executeAsync();

                    updateUI(true);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

    }

    private Bundle getFacebookData(JSONObject object) {
        String id;
        Bundle bundle = null;
        try {
            bundle = new Bundle();
            id = object.getString("id");

            bundle.putString("idFacebook", id);

            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));

            return bundle;

        } catch (JSONException e){

        }
        return bundle;
    }


    /*public static Bitmap getFacebookProfilePicture(String userID) throws IOException {
        URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
        /////http://graph.facebook.com/"+10154290507339045+"/picture?type=large
        System.out.println("FACEBOOK"+imageURL);
        Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

        return bitmap;
    }

    // on createView method

    public void facebookdetails(JSONObject object){
        String id;
        String nameString;
        String emailString;
        try {
            id = object.getString("id");
            String picture = object.getJSONObject("picture").getJSONObject("data").getString("url");
            nameString = object.getString("name");
            emailString = object.getString("email");
            name.setText("Welcome: \n" + nameString);
            email.setText("Signed In as: \n" + emailString);
            name.setTextSize(18);
            email.setTextSize(18);
            try {
                URL imageURL = new URL(picture);
                System.out.println("FACEBOOK" + imageURL);
                Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                profilepic.setImageBitmap(bitmap);
            } catch (MalformedURLException e) {

            } catch (IOException i) {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
 */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == signInRequestCode) {
            result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        //if(isFacebook) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        //}
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            acct = result.getSignInAccount();
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, signInRequestCode);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]
    private void updateUI(boolean signedIn) {
        if (signedIn) {
            if(!isFacebook) {
                findViewById(R.id.login_button).setVisibility(View.GONE);
                name.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                findViewById(R.id.sign_in_button).setVisibility(View.GONE);
                String url = "https://www.googleapis.com/plus/v1/people/" + acct.getId() + "?fields=image&key=AIzaSyCkDrLVCePMsRyRg6JlNo1tBVX6wqygB8s";
                googleuserid = acct.getId();
                googleemail = acct.getEmail();
                googlename = acct.getDisplayName();
                name.setText("Welcome: \n" + acct.getDisplayName());
                email.setText("Signed In as: \n" + acct.getEmail());
                name.setTextSize(18);
                email.setTextSize(18);
                new LoadProfileImage(profilepic, url).execute();
                System.out.println(url);
                profilepic.setVisibility(View.VISIBLE);
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.homeImage);
                linearLayout.setBackgroundColor(Color.TRANSPARENT);
                Toast.makeText(this, "Logged in via Google: Welcome "+ googlename +", "+ googleemail, Toast.LENGTH_SHORT).show();
            }else{
                findViewById(R.id.sign_in_button).setVisibility(View.GONE);
                findViewById(R.id.login_button).setVisibility(View.GONE);
                profilepic.setVisibility(View.VISIBLE);
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.homeImage);
                linearLayout.setBackgroundColor(Color.TRANSPARENT);
                Toast.makeText(this, "Logged in via Facebook: Welcome " + facebookname + ", " + facebookemail, Toast.LENGTH_SHORT).show();
            }
        } else {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.login_button).setVisibility(View.VISIBLE);
            profilepic.setVisibility(View.GONE);
            name.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.homeImage);
            linearLayout.setBackground(getResources().getDrawable(R.drawable.androidwallpaper, null));
        }
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
        if (id == R.id.action_signout) {
            if (isFacebook){
                isFacebook= false;
                LoginManager.getInstance().logOut();
                updateUI(false);
            }
            else {
                signOut();
           }
            return true;
        }
        if(id == R.id.action_signoutanddisconnect){
            if (isFacebook){
                isFacebook= false;
                LoginManager.getInstance().logOut();
                updateUI(false);
            }else {
                revokeAccess();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
    ImageView profileimage;
    String inputurl;
    String url2;
    public LoadProfileImage(ImageView profileimage, String url) {
        this.profileimage = profileimage;
        this.inputurl = url;
    }

    protected Bitmap doInBackground(String... urls) {
        StringBuilder serverSB = new StringBuilder();
        HttpURLConnection urlConnection = null;
       // StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(inputurl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(10000);
            urlConnection.connect();
            //String responseMessage=urlConnection.getResponseMessage();
            //int HttpResult =urlConnection.getResponseCode();
            //System.out.println("CODE: "+HttpResult);
            //int len= urlConnection.getContentLength();
            //System.out.println(responseMessage);
            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK ) {
                Log.i("Google Profile Pic", "---Failed : HTTP error code : " + urlConnection.getResponseCode());
            }
            else {
                BufferedReader br = new BufferedReader(new InputStreamReader((urlConnection.getInputStream())));
                String serverOutput;
                System.out.println("Output from GET .... \n");
                //To ensure the body is not zero
                int count = 0;
                while ((serverOutput = br.readLine()) != null) {
                    count++;
                    System.out.println(serverOutput);
                    //serverSB.append(serverOutput + "\n");
                    serverSB.append(serverOutput);
                }
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(serverSB.toString());
                    JSONObject queryArray = jsonObject.getJSONObject("image");
                    url2 = queryArray.get("url").toString();
                    System.out.println( queryArray.get("url"));
                }catch(JSONException e)
                {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null)
            urlConnection.disconnect();
        }

        String urldisplay = url2;
        Bitmap b = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            b = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return b;
    }

    protected void onPostExecute(Bitmap result) {
        profileimage.setImageBitmap(result);
    }
}

class LoadProfileImage2 extends AsyncTask<String, Void, Bitmap> {
    ImageView profileimage;
    String inputId;
    String url2;
    URL imageURL;
    Bitmap bitmap;
    public LoadProfileImage2(ImageView profileimage, String inputId) {
        this.profileimage = profileimage;
        this.inputId = inputId;
    }

    protected Bitmap doInBackground(String... urls) {

        try{
            imageURL = new URL("https://graph.facebook.com/" + inputId + "/picture?type=large");
        }catch (MalformedURLException e)
        {

        }
        //http://graph.facebook.com/"+10154290507339045+"/picture?type=large
        System.out.println("FACEBOOK"+imageURL);
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
