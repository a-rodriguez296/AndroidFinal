package arf.com.masterfinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FunctionCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.ui.ParseLoginBuilder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Shows the user profile. This simple activity can function regardless of whether the user
 * is currently logged in.
 */
public class MainActivity extends Activity {
    private static final int LOGIN_REQUEST = 0;

    @Bind(R.id.txt_dear_user)
    TextView txtDearUser;

    @Bind(R.id.txt_message)
    TextView txtMessage;

    @Bind(R.id.btn_log_out)
    Button btnLogout;

    @Bind(R.id.img_profile)
    ImageView imgProfile;

    private ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "YzGhBkn9wzY9f1dxuatZGMIlzSmFVknx5n7zWm37", "v6lIaY8OjtneQLo1OrHfdXCBxynL9XBVUvXOPRwP");
        ParseFacebookUtils.initialize(getApplicationContext());


        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        currentUser = ParseUser.getCurrentUser();


        if(currentUser == null){
            ParseLoginBuilder builder = new ParseLoginBuilder(this);
            startActivityForResult(builder.build(), LOGIN_REQUEST);
        }
        else{
            updateUI();
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {

                        ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
                        startActivityForResult(builder.build(), LOGIN_REQUEST);

                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
        currentUser = ParseUser.getCurrentUser();
        ParseCloud.callFunctionInBackground("facebookInfo", new HashMap<String, String>(), new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                String userID = ((HashMap<String, String>) object).get("id");
                String imageUrl = String.format("https://graph.facebook.com/%s/picture?type=normal", userID);


                currentUser.put("name", ((HashMap<String, String>) object).get("name"));
                currentUser.put("imgUrl", imageUrl);

                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        updateUI();
                        ;
                    }
                });

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void updateUI(){
        txtDearUser.setText(getResources().getString(R.string.dear_user) + " " + currentUser.getString("name"));
        txtMessage.setText(getResources().getString(R.string.txt_message, getResources().getString(R.string.app_owner)));

        String imageUrl = (String) currentUser.get("imgUrl");
        Picasso.with(getApplicationContext()).load(imageUrl).into(imgProfile);
    }
}
