package arf.com.masterfinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import org.w3c.dom.Text;

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




    private ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "YzGhBkn9wzY9f1dxuatZGMIlzSmFVknx5n7zWm37", "v6lIaY8OjtneQLo1OrHfdXCBxynL9XBVUvXOPRwP");
        ParseFacebookUtils.initialize(getApplicationContext());


        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


/*        ParseUser user = new ParseUser();
        user.setUsername("my name");
        user.setPassword("my pass");
        user.setEmail("email@example.com");

// other fields can be set just like with ParseObject
        user.put("phone", "650-555-0000");

        user.signUpInBackground(new SignUpCallback() {


            @Override
            public void done(com.parse.ParseException e) {
                if (e == null){
                    Log.d("","");
                }
                else{
                    Log.d("","");
                }
            }
        });*/
        currentUser = ParseUser.getCurrentUser();

        if(currentUser == null){
            ParseLoginBuilder builder = new ParseLoginBuilder(this);
            startActivityForResult(builder.build(), LOGIN_REQUEST);
        }
        else{

            txtDearUser.setText(getResources().getString(R.string.dear_user) +" " + currentUser.getString("name"));
            txtMessage.setText(getResources().getString(R.string.txt_message, getResources().getString(R.string.app_owner)));



        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}
