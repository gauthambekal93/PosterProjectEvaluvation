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
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddEntity extends AppCompatActivity {
EditText addUser;
EditText addTeam;
EditText addQuestion;

Button addUserButton;
Button addTeamButton;
Button addQuestionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entity);
addUser =(EditText)findViewById(R.id.addUser);
addTeam =(EditText)findViewById(R.id.addTeam);
addQuestion=(EditText)findViewById(R.id.addQuestion);

addUserButton=(Button)findViewById(R.id.addUserButton);
addTeamButton=(Button)findViewById(R.id.addTeamButton);
addQuestionButton=(Button)findViewById(R.id.addQuestionButton);

addUserButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        addUser();
    }
});

addTeamButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        addTeam();
    }
});

addQuestionButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        addQuestion();
    }
});

    }

    public void addUser()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                apiURL.Url+"createUser",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(),
                                    jsonObject.getString("message"),Toast.LENGTH_SHORT).show();

                            new CountDownTimer(3000, 1000) {
                                public void onFinish() {
                                    // When timer is finished
                                    try {
                                        sendEmail("USER TOKEN",jsonObject.getString("message"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                public void onTick(long millisUntilFinished) {
                                    // millisUntilFinished    The amount of time until finished.
                                }
                            }.start();

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(),"Unsucessfully Registration",Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params =new HashMap<>();
                params.put("userid",addUser.getText().toString());
                params.put("message","Login successful");
                return  params;
            }
        };
        HandleApiRequests.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void addTeam()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                apiURL.Url+"createTeam",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(),
                                    jsonObject.getString("message"),Toast.LENGTH_SHORT).show();

                            new CountDownTimer(3000, 1000) {
                                public void onFinish() {
                                    // When timer is finished
                                    try {
                                        sendEmail("TEAM TOKEN",jsonObject.getString("message"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                public void onTick(long millisUntilFinished) {
                                    // millisUntilFinished    The amount of time until finished.
                                }
                            }.start();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(),"Unsucessfully Registration",Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params =new HashMap<>();
                params.put("teamid",addTeam.getText().toString());
                params.put("message","Login successful");
                return  params;
            }
        };
        HandleApiRequests.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void addQuestion()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                apiURL.Url+"createQuestion",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(),
                                    jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(),"Unsucessfully Registration",Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params =new HashMap<>();
                params.put("question",addQuestion.getText().toString());
                return  params;
            }
        };
        HandleApiRequests.getInstance(this).addToRequestQueue(stringRequest);
    }
    public void sendEmail(String emailSubject,String emailText)
    {

        String emailList[]= {"gauthambekal93@gmail.com"};
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL,emailList);
        intent.putExtra(Intent.EXTRA_SUBJECT,emailSubject);
        intent.putExtra(Intent.EXTRA_TEXT,emailText);
        startActivity(Intent.createChooser(intent,"Choice email APP"));
        Toast.makeText(getApplicationContext(),"Email Sent",Toast.LENGTH_SHORT).show();
    }
}

