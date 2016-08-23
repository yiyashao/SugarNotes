package com.techosoft.idea.sugarnote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVOSCloud;
import com.techosoft.idea.sugarnote.helper.CloudAgent;
import com.techosoft.idea.sugarnote.helper.MyConst;
import com.techosoft.idea.sugarnote.helper.MyHelper;
import com.techosoft.idea.sugarnote.model.User;

public class LoginActivity extends AppCompatActivity {


    //setup UI
    EditText etUsername;
    EditText etPassword;
    Button btnLogin;


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
                //TODO login / register user
                User currentUsr = new User(etUsername.getText().toString(), etPassword.getText().toString());

                //register first
                if(isNewUser(currentUsr.username)){
                    cloudConn.addNewUser(currentUsr);
                }

                //save this user as logged in
                mHelper.setSettingsBool(MyConst.KEY_LOGIN, true);

                //goto record list activity
                goToActivity(ListActivity.class);
                killActivity();
            }
        });

        //if user already logged in, go on
        if(mHelper.isLogin()){
            //go to list activity
            goToActivity(ListActivity.class);
            mHelper.displayToast("already login");
            killActivity();
        }
    }

    private boolean isNewUser(String username) {
        return true;
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
