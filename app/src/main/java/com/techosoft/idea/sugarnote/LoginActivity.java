package com.techosoft.idea.sugarnote;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.techosoft.idea.sugarnote.helper.CloudAgent;
import com.techosoft.idea.sugarnote.helper.MyConst;
import com.techosoft.idea.sugarnote.helper.MyHelper;
import com.techosoft.idea.sugarnote.model.User;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {


    //setup UI
    EditText etUsername;
    EditText etPassword;
    Button btnLogin;
    private ProgressDialog progress;


    private MyHelper mHelper;
    private CloudAgent cloudConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //init helpers
        mHelper = new MyHelper(this);
        AVOSCloud.initialize(this, MyConst.CLOUD_KEY_01, MyConst.CLOUD_KEY_02); //initilize the cloud service
        cloudConn = new CloudAgent(this);

        //initialize UI
        etUsername = (EditText)findViewById(R.id.etUsrName);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btnLogin = (Button)findViewById(R.id.btnLoginRegister);
        //handle the progress loading image
        progress=new ProgressDialog(this);
        progress.setMessage("loading data");

        //setup the tool bar
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.my_actionbar);
        View customView = getSupportActionBar().getCustomView();
        Toolbar parent =(Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0,0);

        //testing purpose, set login = false
        mHelper.setSettingsBool(MyConst.KEY_LOGIN, false);


        //setup listener
        etUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etUsername.setHint(null); //make username hint disappear
            }
        });
        etPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etPassword.setHint(null); //make password hint disappear
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //read this user informatioin
                User currentUsr = new User(etUsername.getText().toString(), etPassword.getText().toString());
                //check if empty input OR wrong format
                if(isValidEntries()){
                    //check user, start the process
                    checkNewUser(currentUsr);
                }
            }
        });

        //if user already logged in, go on
        if(mHelper.isLogin()){
            //go to list activity
            goToActivity(ListActivity.class);
           // mHelper.displayToast("user is already logged in");
            killActivity();
        }else{
            //mHelper.displayToast("user not logged in" );
        }
    }


    //helper for check the user name rules
    private boolean isValidEntries() {
        boolean valid = true;
        if(etUsername.getText().toString().matches("") ||
                etPassword.getText().toString().matches("")){
            mHelper.displayToast("sorry, username / password can not be empty");
            etPassword.setHint(MyConst.HINT_PASSWORD);
            etUsername.setHint(MyConst.HINT_USERNAME);
            valid = false;
        }
        return valid;
    }

    /**
     * STEP 1, check if this is a new user OR old user, register the new user and login the old user
     * @param currentUser
     */
    private void checkNewUser(final User currentUser) {
        progress.show();
        final String username = currentUser.username;
        Log.d(MyConst.LOG_TAG, "check if user is already in database for user: " + username);
        AVQuery<AVObject> query = new AVQuery<>(MyConst.TABLE_USER);
        query.whereEqualTo(MyConst.USER_NAME, username); //add this line to get records only for this user
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> resultList, AVException e) {
                //loop through the result if returned value
                if (resultList.size() > 0) {
                    mHelper.logInfo("user already registered, " + username);
                    loginUser(currentUser);
                }else{
                    mHelper.logInfo("new user, register start" + username);
                   addNewUser(currentUser);
                }
            }
        });
    }

    /***
     * STEP 2, add new user to database, NOTE: it doesn not check identical username here, it was checked elsewhere
     * go to last step is user added correctly
     * @param user
     */
    private void addNewUser(final User user){
        final AVObject avoUser = new AVObject(MyConst.TABLE_USER);
        //avoUser.put(mConst.USER_ID, user.userId);
        avoUser.put(MyConst.USER_NAME, user.username);
        avoUser.put(MyConst.USER_PASSWORD, user.password);
        avoUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    mHelper.logInfo( "new user saved with objId: " + avoUser.getObjectId());
                    // save the new user's id as this app's user ID
                    user.objId = avoUser.getObjectId();
                    mHelper.processLoginFor(user); //save USER_ID and USER_NAME to cache
                    goNext(); //finish all business on this page, go on
                } else {
                    Log.d(MyConst.LOG_TAG, "failed add new user");
                }
            }
        });
    }

    /**
     * STEP 3,  login this user, check if password is valid
     * @param currentUser
     */
    private void loginUser(final User currentUser) {
        //setup the query
        AVQuery<AVObject> usernameQuery = new AVQuery<>(MyConst.TABLE_USER);
        usernameQuery.whereEqualTo(MyConst.USER_NAME, currentUser.username);
        AVQuery<AVObject> passwordQuery = new AVQuery<>(MyConst.TABLE_USER);
        passwordQuery.whereEqualTo(MyConst.USER_PASSWORD, currentUser.password);
        AVQuery<AVObject> query = AVQuery.and(Arrays.asList(usernameQuery, passwordQuery)); //AND 2 queries
        //run the query
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> resultList, AVException e) {
                //loop through the result if returned value
                if (resultList.size() > 0) {
                    currentUser.objId = resultList.get(0).getObjectId();
                    mHelper.processLoginFor(currentUser);
                    goNext();
                }else{
                    mHelper.logInfo("username password DOESN'T match for " + currentUser.username);
                    askForRetry();
                }
            }
        });

    }

    /**
     * STEP 4, ask user to try again
     */
    private void askForRetry() {
        mHelper.displayToast("user already exsisted, but username and password does not match, please try again");
        etPassword.setHint(MyConst.HINT_PASSWORD);
        etUsername.setHint(MyConst.HINT_USERNAME);
        progress.hide();
    }

    /**
     * LAST STEP: go to next activity
     */
    private void goNext(){
        mHelper.logInfo("going to record list, user logged in");
        progress.hide();
        progress.dismiss();
        goToActivity(ListActivity.class);
        finish();
    }

    //inner helpers
    private void goToActivity(Class destinationClass) {
        Intent intent = new Intent(this, destinationClass);
        startActivity(intent);
    }
    void killActivity(){
        finish();
    }

}
