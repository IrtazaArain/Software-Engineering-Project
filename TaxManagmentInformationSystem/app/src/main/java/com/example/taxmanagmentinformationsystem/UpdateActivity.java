package com.example.taxmanagmentinformationsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class UpdateActivity extends AppCompatActivity {

    ImageView pass_visible;
    EditText Email, Password;

    Button Back, Submit_Btn;
    AlertDialog.Builder builder;

    String server_url = "http://192.168.1.105/project/Forget_Password.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Back = findViewById(R.id.Back_Btn);
        Password = findViewById(R.id.Password_Et);
        pass_visible = findViewById(R.id.show_pass_btn);
        Submit_Btn = findViewById(R.id.btn_submit);
        Email = findViewById(R.id.Email_Et);

        pass_visible.setOnClickListener(this::ShowHidePass);
        Back.setOnClickListener(view -> Forget_Pass());
        Submit_Btn.setOnClickListener(view -> Login_Func());

        builder = new AlertDialog.Builder(UpdateActivity.this);
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

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            }

                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(UpdateActivity.this,"some error occurred .....", Toast.LENGTH_SHORT).show();
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
            Mysingleton.getInstance(UpdateActivity.this).addTorequestque(stringRequest);
        }
        else{
            Toast.makeText(UpdateActivity.this, "fields can't be empty", Toast.LENGTH_SHORT).show();
        }

    }

    public void Forget_Pass(){
        Intent intent = new Intent(this, LoginActivity.class);
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