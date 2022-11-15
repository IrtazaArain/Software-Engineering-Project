package com.example.taxmanagmentinformationsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {


    ImageView pass_visible;
    TextView Forget, SignUp;
    EditText Email, Password;
    Button LoginBtn;

    AlertDialog.Builder builder;

    public static String PREFS_NAME = "MyPrefsFile";

    String server_url = "http://192.168.46.124/project/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //ClickableText
        SignUp = findViewById(R.id.sign_up);
        Forget = findViewById(R.id.help_login);

        //EditText
        Email = findViewById(R.id.Email_Et);
        Password = findViewById(R.id.Password_Et);
        pass_visible = findViewById(R.id.show_pass_btn);

        //Buttons
        LoginBtn = findViewById(R.id.Login_Btn);

        //Function
        pass_visible.setOnClickListener(this::ShowHidePass);
        SignUp.setOnClickListener(view -> openActivity());
        Forget.setOnClickListener(view -> Forget_Pass());
        LoginBtn.setOnClickListener(view -> Login_Func());


        builder = new AlertDialog.Builder(LoginActivity.this);

    }

    public void Login_Func(){

        final String email = Email.getText().toString().trim();
        final String password = Password.getText().toString().trim();

        if(!email.isEmpty() && !password.isEmpty()) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    builder.setTitle("Server Response");
                    builder.setMessage("Response :"+response);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Email.setText("");
                            Password.setText("");

                            if(response.equals("Signing in")){

                                SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME,0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putBoolean("hasLoggedIn", true);
                                editor.apply();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();
                            }
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            }

                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this,"some error occurred .....", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map <String,String> Params = new HashMap<String, String>();
                    Params.put("email",email);
                    Params.put("password",password);
                    return Params;

                }
            };
            Mysingleton.getInstance(LoginActivity.this).addTorequestque(stringRequest);
        }
        else{
            Toast.makeText(LoginActivity.this, "fields can't be empty", Toast.LENGTH_SHORT).show();
        }

    }

    public void openActivity(){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
    public void Forget_Pass(){
        Intent intent = new Intent(this, UpdateActivity.class);
        startActivity(intent);
        finish();
    }

    public void ShowHidePass(View view) {
        if(view.getId()==R.id.show_pass_btn){
            if(Password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_visibility);
                //Show Password
                Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_visibility_off);
                //Hide Password
                Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            Password.setSelection(Password.getText().length());
        }
    }
}