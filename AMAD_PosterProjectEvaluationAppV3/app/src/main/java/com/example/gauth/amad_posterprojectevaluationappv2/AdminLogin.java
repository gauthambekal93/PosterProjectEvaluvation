package com.example.gauth.amad_posterprojectevaluationappv2;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminLogin extends AppCompatActivity {
EditText adminUsername;
EditText adminPassword;
Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        adminUsername =(EditText)findViewById(R.id.adminUsername);
        adminPassword =(EditText)findViewById(R.id.adminPassword);
        submit =(Button)findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAdmin();
            }
        });
    }

    public  void loginAdmin()
    {
        Toast.makeText(getApplicationContext(),"LOGIN CLICKED",Toast.LENGTH_SHORT).show();
        String url =apiURL.Url+"loginAdmin?"
                + "adminid="+adminUsername.getText()+"&"
                +"adminpass="+adminPassword.getText();
//We need to add the jwt token in the authorization header

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
//SUCCESSFUL RESPONSE
                        try {

                            new CountDownTimer(1000, 1000) {
                                public void onFinish() {
                                    // When timer is finished
                                    try {
                                        if(response.getString("message").equals("Login successful"))
                                        {
                                            Toast.makeText(getApplicationContext(),"LOGIN IS SUCCESSFUL ",Toast.LENGTH_SHORT).show();
                                            adminUsername.setText("");
                                            adminPassword.setText("");
                                            startActivity(new Intent(getBaseContext(), AddEntity.class));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                public void onTick(long millisUntilFinished) {
                                    // millisUntilFinished    The amount of time until finished.
                                }
                            }.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"ADMIN NOT LOGGED IN",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//UNSUCCESSFUL RESPONSE
                Toast.makeText(getApplicationContext(),"ERROR IS!! "+error.toString(),Toast.LENGTH_SHORT).show();

            }
        })
        {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        HandleApiRequests.getInstance(this).addToRequestQueue(request);
    }

}
