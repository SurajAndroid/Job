package utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dtos.CandidateDTO;
import dtos.CompanyDTO;
import dtos.FilterDTO;
import dtos.GetFillterDTO;
import dtos.JobRollDTO;
import dtos.MembershipDTO;
import dtos.NotificationDTO;
import dtos.PostDTO;


//import org.apache.http.entity.mime.MultipartEntity;
@SuppressWarnings("deprecation")
public class WebserviceHelper extends AsyncTask<Void, Void, String[]> {

    private RequestReceiver mContext;
    @SuppressWarnings("unused")
    private String method = null;
    private Map<String, String> paramMap = new HashMap<String, String>();
    private String errorMessage;
    private boolean error_flag = false;
    ProgressDialog mProgressDialog;

    public static int action;

    ProgressDialog dialog;
    Activity mcont;

    public WebserviceHelper() {
    }

    public WebserviceHelper(RequestReceiver context, Activity mcontext) {
        mContext = context;
        mcont = mcontext;
        dialog = new ProgressDialog(mcontext);
    }

    WebserviceHelper(RequestReceiver context, String setMethod) {
        mContext = context;
        method = setMethod;
    }

    private void clearErrors() {
        this.errorMessage = null;
        this.error_flag = false;
    }

    public void setMethod(String m) {
        method = m;
    }

    public void addParam(String key, String value) {
        paramMap.put(key, value);
    }

    @Override
    protected void onPreExecute() {
        this.clearErrors();

        dialog.setMessage("Please Wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected String[] doInBackground(Void... params) {

        Log.e("in background", "");
        Log.d("d  in background", "");
        // Create a newhome HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        JSONObject jsonObj = new JSONObject();
        HttpResponse response1 = null;
        HttpPost httppost = null;
        HttpGet httpGet = null;
        JSONObject jsonData = new JSONObject();
        switch (action) {

            case Constant.EMPLOYER_RAGISTRATION:
                String[] emloyer = new String[3];
                httppost = new HttpPost(Constant.EMPLOYER_RAGISTRATION_URL);
                try {
                    try {

                        jsonData.accumulate("company_name", Constant.COMPANY_NAME);
                        jsonData.accumulate("contact_person", Constant.CONTACTPERSON);
                        jsonData.accumulate("email", Constant.EMAIL);
                        jsonData.accumulate("password", Constant.PASSWORD);
                        jsonData.accumulate("phone", Constant.PHONE_NUMBER);
                        jsonData.accumulate("current_requirment", Constant.GENDER);
                        jsonData.accumulate("experience", Constant.EXPERIENCE);
                        jsonData.accumulate("skill", Constant.SKILLES);
                        jsonData.accumulate("location", Constant.LOCATION);
                        jsonData.accumulate("discription", Constant.DISCRIPTION);
                        jsonData.accumulate("job_type", Constant.JOB_TYPE);
                        jsonData.accumulate("specilaization", Constant.SPECILIZATION);
                        jsonData.accumulate("job_role", Constant.JOBROLL);
                        jsonData.accumulate("num_of_requirment", Constant.NO_OF_REQUIRMENT);
//                        jsonData.accumulate("device_token", Constant.TOKEN);

                        Log.e("", "URL " + Constant.EMPLOYER_RAGISTRATION_URL);
                        Log.e("Json : ", "" + jsonData.toString(5));
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            if (response1 != null) {
                                Log.e("", "responce");
                                jsonData.has("success");
                            } else {
                                Log.e("", "Null responce");
                            }
                            response1.getStatusLine().getStatusCode();
                            StatusLine statusLine = response1.getStatusLine();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);

                            emloyer[0] = object.getString("success");
                            emloyer[1] = object.getString("message");

                            try {

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            emloyer[0] = object.getString("success");
                            emloyer[1] = object.getString("message");
                        }
                        break;
                    }
                    return emloyer;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constant.CONDIDATE_RAGISTRATION:
                String[] regParm = new String[3];
                httppost = new HttpPost(Constant.CONDIDATE_RAGISTRATION_URL);
                Log.e("", "Constant.SIGNUP_URL : " + Constant.CONDIDATE_RAGISTRATION_URL);
                try {
                    MultipartEntity entity = new MultipartEntity();
                    try {
                        Log.e("", "ImagePathe : " + Constant.DOCUMENT);
                        File file = new File(Constant.DOCUMENT);
                        FileBody bin = new FileBody(file);
                        entity.addPart("resume", bin);
                    } catch (Exception e) {
                        Log.v("Exception in Image", "" + e);
                    }

                    /* entity.addPart("user_type", new StringBody(Constant.USER_TYPE));
                    entity.addPart("device_token", new StringBody(Constant.TOKEN));
                    entity.addPart("company_name", new StringBody(Constant.COMPANY_NAME));*/

                    entity.addPart("name", new StringBody(Constant.NAME));
                    entity.addPart("user_name", new StringBody(Constant.USER_NAME));
                    entity.addPart("email", new StringBody(Constant.EMAIL));
                    entity.addPart("password", new StringBody(Constant.PASSWORD));
                    entity.addPart("phone", new StringBody(Constant.PHONE_NUMBER));

                    entity.addPart("job_type", new StringBody(Constant.JOB_TYPE));
                    entity.addPart("specilaization", new StringBody(Constant.SPECILIZATION));
                    entity.addPart("job_role", new StringBody(Constant.JOBROLL));
                    entity.addPart("gender", new StringBody(Constant.GENDER));
                    entity.addPart("location", new StringBody(Constant.LOCATION));

                    httppost.setEntity(entity);

                    try {
                        response1 = httpclient.execute(httppost);
                        Log.d("myapp", "response " + response1.getEntity());
                        Log.e("myapp", "response.. statau.." + response1.getStatusLine().getStatusCode());
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    InputStreamReader inputStreamReader = new InputStreamReader(
                            inputStream);
                    BufferedReader bufferedReader = new BufferedReader(
                            inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String bufferedStrChunk = null;
                    String encodeRes = "";
                    JSONObject jsondata = null;
                    JSONObject object = null;
                    JSONObject jsonObject = null;
                    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                        stringBuilder.append(bufferedStrChunk);
                        encodeRes = stringBuilder.toString();
                        //                        break;
                    }

                    try {
                        object = new JSONObject(encodeRes);
                        Log.d("", "jsonObj responce... " + object);
                        regParm[0] = object.getString("success");
                        regParm[1] = object.getString("message");
                        jsondata = object.getJSONObject("data");
                        Constant.RESUME = jsondata.getString("resume");
                        Constant.USER_ID = jsondata.getString("user_id");

                    } catch (JSONException e) {
                        e.printStackTrace();
                        regParm[0] = object.getString("success");
                        regParm[1] = object.getString("message");
                    }

                    return regParm;

                } catch (Exception e) {
                    // TODO: handle exception
                }

                break;


            case Constant.LOGIN:
                String[] login = new String[3];
                httppost = new HttpPost(Constant.LOGIN_URL);
                try {
                    try {

                        jsonData.accumulate("email", Constant.EMAIL);
                        jsonData.accumulate("password", Constant.PASSWORD);
                        jsonData.accumulate("fbid", Constant.FB_ID);
                        jsonData.accumulate("gmail_id", Constant.GOOGLE_ID);
                        jsonData.accumulate("device_token", Constant.TOKEN);

                        Log.e("", "URL " + Constant.LOGIN_URL);
                        Log.e("Json : ", "" + jsonData.toString(5));
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            if (response1 != null) {
                                Log.e("", "responce");
                                jsonData.has("success");
                            } else {
                                Log.e("", "Null responce");
                            }
                            response1.getStatusLine().getStatusCode();
                            StatusLine statusLine = response1.getStatusLine();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);

                            login[0] = object.getString("success");
                            login[1] = object.getString("message");

                            try {
                                JSONObject data = object.getJSONObject("data");

                                try {
                                    Log.e("", "Candidate");
                                    Constant.USER_ID = data.getString("user_id");
                                    Constant.USER_NAME = data.getString("candidate_name");
                                    Constant.USER_IMAGE = data.getString("candidate_image");
                                    Constant.EMAIL = data.getString("email");
                                    Constant.PHONE_NUMBER = data.getString("phone");
                                    Constant.LOCATION = data.getString("location");
                                    Constant.USER_TYPE = data.getString("user_type");
                                } catch (Exception e) {
                                    Log.e("", "Company");

                                    Constant.USER_ID = data.getString("user_id");
                                    Constant.COMPANY_NAME = data.getString("company_name");
                                    Constant.USER_IMAGE = data.getString("company_image");
                                    Constant.EMAIL = data.getString("email");
                                    Constant.PHONE_NUMBER = data.getString("phone");
                                    Constant.LOCATION = data.getString("location");
                                    Constant.USER_TYPE = data.getString("user_type");

                                    Constant.OUT_OF_DOWNLOAD = data.getString("out of download");
                                    Constant.NOOF_DOWNLOAD = data.getString("no.of download");
                                    Constant.OUT_OF_POST = data.getString("out_of_post_job");
                                    Constant.NO_OF_POST = data.getString("posted_job");

                                    e.printStackTrace();
                                }

                                try {
                                    Log.e("", "FB IN");
                                    Constant.USER_ID = data.getString("user_id");
                                    Constant.EMAIL = data.getString("email");
                                    Constant.USER_NAME = data.getString("candidate_name");
                                    Constant.USER_IMAGE = data.getString("candidate_image");
                                    Constant.PHONE_NUMBER = data.getString("phone");
                                    Constant.LOCATION = data.getString("location");
                                    Constant.USER_TYPE = data.getString("user_type");
                                } catch (Exception e) {

                                    Constant.USER_ID = data.getString("user_id");
                                    Constant.EMAIL = data.getString("email");
                                    Constant.USER_NAME = data.getString("candidate_name");
                                    Constant.USER_IMAGE = data.getString("candidate_image");
                                    Constant.PHONE_NUMBER = data.getString("phone");
                                    Constant.LOCATION = data.getString("location");
                                    Constant.USER_TYPE = data.getString("user_type");
                                    e.printStackTrace();
                                }


                            } catch (Exception e) {
                                e.printStackTrace();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            login[0] = object.getString("success");
                            login[1] = object.getString("message");
                        }
                        break;
                    }
                    return login;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constant.LOGOUT:
                String[] logout = new String[3];
                httppost = new HttpPost(Constant.LOGOUT_URL);
                try {
                    try {

                        jsonData.accumulate("email", Constant.EMAIL);

                        Log.e("", "URL " + Constant.LOGOUT_URL);
                        Log.e("Json : ", "" + jsonData.toString(5));
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            if (response1 != null) {
                                Log.e("", "responce");
                                jsonData.has("success");
                            } else {
                                Log.e("", "Null responce");
                            }
                            response1.getStatusLine().getStatusCode();
                            StatusLine statusLine = response1.getStatusLine();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);

                            logout[0] = object.getString("success");
                            logout[1] = object.getString("message");

                            try {

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            logout[0] = object.getString("success");
                            logout[1] = object.getString("message");
                        }
                        break;
                    }
                    return logout;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constant.FORGOTPASSWORD:
                String[] forgotpassword = new String[3];
                httppost = new HttpPost(Constant.FORGOT_URL);
                try {
                    try {

                        jsonData.accumulate("email", Constant.EMAIL);

                        Log.e("", "URL " + Constant.FORGOT_URL);
                        Log.e("Json : ", "" + jsonData.toString(5));
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            if (response1 != null) {
                                Log.e("", "responce");
                                jsonData.has("success");
                            } else {
                                Log.e("", "Null responce");
                            }
                            response1.getStatusLine().getStatusCode();
                            StatusLine statusLine = response1.getStatusLine();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);

                            forgotpassword[0] = "010" + object.getString("success");
                            forgotpassword[1] = object.getString("message");
                            try {
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            forgotpassword[0] = object.getString("success");
                            forgotpassword[1] = object.getString("message");
                        }
                        break;
                    }
                    return forgotpassword;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            case Constant.CHANGEPASSWORD:
                String[] changepassword = new String[3];
                httppost = new HttpPost(Constant.CHANGE_PASSWORD_URL);
                try {
                    try {

                        jsonData.accumulate("email", Constant.EMAIL);
                        jsonData.accumulate("old_password", Constant.OLDPASSWORD);
                        jsonData.accumulate("new_password", Constant.PASSWORD);

                        Log.e("", "URL " + Constant.FORGOT_URL);
                        Log.e("Json : ", "" + jsonData.toString(5));
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            if (response1 != null) {
                                Log.e("", "responce");
                                jsonData.has("success");
                            } else {
                                Log.e("", "Null responce");
                            }
                            response1.getStatusLine().getStatusCode();
                            StatusLine statusLine = response1.getStatusLine();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);

                            changepassword[0] = "10" + object.getString("success");
                            changepassword[1] = object.getString("message");

                            try {

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            changepassword[0] = object.getString("success");
                            changepassword[1] = object.getString("message");
                        }
                        break;
                    }
                    return changepassword;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constant.SOCIALLOGIN:
                String[] social_login = new String[3];
                httppost = new HttpPost(Constant.SOCIAL_LOGIN_URL);
                try {
                    try {

                        jsonData.accumulate("fbid", Constant.FB_ID);
                        jsonData.accumulate("email", Constant.EMAIL);
                        jsonData.accumulate("gmail_id", Constant.GOOGLE_ID);

                        Log.e("", "URL " + Constant.SOCIAL_LOGIN_URL);
                        Log.e("Json : ", "" + jsonData.toString(5));
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            if (response1 != null) {
                                Log.e("", "responce");
                                jsonData.has("success");
                            } else {
                                Log.e("", "Null responce");
                            }
                            response1.getStatusLine().getStatusCode();
                            StatusLine statusLine = response1.getStatusLine();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);

                            social_login[0] = object.getString("success");
                            social_login[1] = object.getString("message");
                            try {
                                JSONObject data = object.getJSONObject("data");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            social_login[0] = object.getString("success");
                            social_login[1] = object.getString("message");
                        }
                        break;
                    }
                    return social_login;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constant.GET_COMPANY_PROFILE:
                String[] getprofile = new String[3];
                httppost = new HttpPost(Constant.GET_PROFILE_URL);
                try {
                    try {
                        jsonData.accumulate("email", Constant.EMAIL);
                        Log.e("", "URL " + Constant.GET_PROFILE_URL);
                        Log.e("Json : ", "" + jsonData.toString(1));
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            if (response1 != null) {
                                Log.e("", "responce");
                                jsonData.has("success");
                            } else {
                                Log.e("", "Null responce");
                            }
                            response1.getStatusLine().getStatusCode();
                            StatusLine statusLine = response1.getStatusLine();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;
                    Global.companylist.clear();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            getprofile[0] = "0" + object.getString("success");
                            getprofile[1] = object.getString("message");
                            try {
                                JSONObject object1 = object.getJSONObject("data");
                                CompanyDTO companyDTO = new CompanyDTO();

                                companyDTO.setEmployer_id(object1.getString("employer_id"));
                                companyDTO.setCompany_name(object1.getString("company_name"));
                                companyDTO.setContact_person(object1.getString("contact_person"));
                                companyDTO.setEmail(object1.getString("email"));
                                companyDTO.setPhone(object1.getString("phone"));
                                companyDTO.setCurrent_requirment(object1.getString("current_requirment"));
                                companyDTO.setExperience(object1.getString("experience"));
                                companyDTO.setSkill(object1.getString("skill"));
                                companyDTO.setJob_role(object1.getString("job_role"));
                                companyDTO.setLocation(object1.getString("location"));
                                companyDTO.setAddress(object1.getString("address"));
                                companyDTO.setExp_date(object1.getString("exp_date"));
                                companyDTO.setEmppackage(object1.getString("package"));
                                companyDTO.setAmount(object1.getString("amount"));
                                companyDTO.setUser_type(object1.getString("user_type"));

                                Global.companylist.add(companyDTO);
                                Log.e("", "List Size : " + Global.companylist.size());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            getprofile[0] = object.getString("success");
                            getprofile[1] = object.getString("message");
                        }
                        break;
                    }
                    return getprofile;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constant.GET_CANDIDATE_PROFILE:
                String[] getcandidateprofile = new String[3];
                httppost = new HttpPost(Constant.GET_PROFILE_URL);
                try {
                    try {

                        jsonData.accumulate("email", Constant.EMAIL);

                        Log.e("", "URL " + Constant.GET_PROFILE_URL);
                        Log.e("Json : ", "" + jsonData.toString(1));
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            if (response1 != null) {
                                Log.e("", "responce");
                                jsonData.has("success");
                            } else {
                                Log.e("", "Null responce");
                            }
                            response1.getStatusLine().getStatusCode();
                            StatusLine statusLine = response1.getStatusLine();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;
                    Global.candidatelist.clear();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            getcandidateprofile[0] = "00" + object.getString("success");
                            getcandidateprofile[1] = object.getString("message");
                            try {
                                JSONObject object1 = object.getJSONObject("data");
                                CandidateDTO candidateDTO = new CandidateDTO();

                                candidateDTO.setName(object1.getString("name"));
                                candidateDTO.setPhone(object1.getString("phone"));
                                candidateDTO.setGender(object1.getString("gender"));
                                candidateDTO.setLocation(object1.getString("location"));
                                candidateDTO.setJobType(object1.getString("job_type"));
                                candidateDTO.setJobRole(object1.getString("job_role"));
                                candidateDTO.setSpecialization(object1.getString("specilaization"));
                                candidateDTO.setEmail(object1.getString("email"));
                                candidateDTO.setUser_type(object1.getString("user_type"));

                                Global.candidatelist.add(candidateDTO);
                                Log.e("", "Size of list : " + Global.candidatelist.size());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            getcandidateprofile[0] = object.getString("success");
                            getcandidateprofile[1] = object.getString("message");
                        }
                        break;
                    }
                    return getcandidateprofile;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constant.UPDATE_CANDIDATE_PROFILE:

                String[] updatecandidate = new String[3];
                httppost = new HttpPost(Constant.UPDATE_CANDIDATE_PROFILE_URL);

                try {
                    try {
                        jsonData.accumulate("name", Constant.USER_NAME);
                        jsonData.accumulate("phone", Constant.PHONE_NUMBER);
                        jsonData.accumulate("gender", "");
                        jsonData.accumulate("location", Constant.LOCATION);
                        jsonData.accumulate("experience", Constant.EXPERIENCE);
                        jsonData.accumulate("skill", Constant.SKILLES);
                        jsonData.accumulate("strength", Constant.STRENGHT);
                        jsonData.accumulate("expected_salary", Constant.EXP_SALARY);
                        jsonData.accumulate("address", Constant.ADDRESS);
                        jsonData.accumulate("prefered_location", "");
                        jsonData.accumulate("objective", Constant.OBJECTIVE);
                        jsonData.accumulate("brief_description", Constant.BRIEFDESCRIPTION);
                        jsonData.accumulate("email", Constant.EMAIL);

                        Log.e("", "URL " + Constant.UPDATE_CANDIDATE_PROFILE_URL);
                        Log.e("Json : ", "" + jsonData.toString(12));
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            if (response1 != null) {
                                Log.e("", "responce");
                                jsonData.has("success");
                            } else {
                                Log.e("", "Null responce");
                            }
                            response1.getStatusLine().getStatusCode();
                            StatusLine statusLine = response1.getStatusLine();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;
                    Global.candidatelist.clear();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            updatecandidate[0] = object.getString("success");
                            updatecandidate[1] = object.getString("message");
                            try {
                                JSONObject object1 = object.getJSONObject("data");
                                CandidateDTO candidateDTO = new CandidateDTO();

                                candidateDTO.setName(object1.getString("name"));
                                candidateDTO.setPhone(object1.getString("phone"));
                                candidateDTO.setGender(object1.getString("gender"));
                                candidateDTO.setLocation(object1.getString("location"));
                                candidateDTO.setExperience(object1.getString("experience"));
                                candidateDTO.setSkill(object1.getString("skill"));
                                candidateDTO.setStrength(object1.getString("strength"));
                                candidateDTO.setExpected_salary(object1.getString("expected_salary"));
                                candidateDTO.setAddress(object1.getString("address"));
                                candidateDTO.setPrefered_location(object1.getString("prefered_location"));
                                candidateDTO.setObjective(object1.getString("objective"));
                                candidateDTO.setBrief_description(object1.getString("brief_description"));
                                candidateDTO.setEmail(object1.getString("email"));
                                candidateDTO.setUser_type(object1.getString("user_type"));

                                Global.candidatelist.add(candidateDTO);
                                Log.e("", "Size of list : " + Global.candidatelist.size());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            updatecandidate[0] = object.getString("success");
                            updatecandidate[1] = object.getString("message");
                        }
                        break;
                    }
                    return updatecandidate;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constant.UPDATE_EMPLOYER_PROFILE:
                String[] updateemployer = new String[3];
                httppost = new HttpPost(Constant.UPDATE_EMPLOYER_PROFILE_URL);
                try {
                    try {
                        jsonData.accumulate("company_name", Constant.COMPANY_NAME);
                        jsonData.accumulate("contact_person", Constant.CONTACTPERSON);
                        jsonData.accumulate("phone", Constant.PHONE_NUMBER);
                        jsonData.accumulate("current_requirment", Constant.CURRENT_REQUIRMENT);
                        jsonData.accumulate("experience", Constant.EXPERIENCE);
                        jsonData.accumulate("skill", Constant.SKILLES);
                        jsonData.accumulate("job_role", Constant.JOBROLL);
                        jsonData.accumulate("location", Constant.LOCATION);
                        jsonData.accumulate("address", Constant.ADDRESS);
                        jsonData.accumulate("email", Constant.EMAIL);

                        Log.e("", "URL " + Constant.UPDATE_EMPLOYER_PROFILE_URL);
                        Log.e("Json : ", "" + jsonData.toString(12));
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            if (response1 != null) {
                                Log.e("", "responce");
                                jsonData.has("success");
                            } else {
                                Log.e("", "Null responce");
                            }
                            response1.getStatusLine().getStatusCode();
                            StatusLine statusLine = response1.getStatusLine();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;
                    Global.companylist.clear();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            updateemployer[0] = object.getString("success");
                            updateemployer[1] = object.getString("message");
                            try {
                                JSONObject object1 = object.getJSONObject("data");
                                CompanyDTO companyDTO = new CompanyDTO();
                                companyDTO.setCompany_name(object1.getString("company_name"));
                                companyDTO.setContact_person(object1.getString("contact_person"));
                                companyDTO.setPhone(object1.getString("phone"));
                                companyDTO.setCurrent_requirment(object1.getString("current_requirment"));
                                companyDTO.setExperience(object1.getString("experience"));
                                companyDTO.setSkill(object1.getString("skill"));
                                companyDTO.setJob_role(object1.getString("job_role"));
                                companyDTO.setLocation(object1.getString("location"));
                                companyDTO.setAddress(object1.getString("address"));
                                companyDTO.setEmail(object1.getString("email"));
                                companyDTO.setUser_type(object1.getString("user_type"));

                                Global.companylist.add(companyDTO);
                                Log.e("", "Size of list : " + Global.companylist.size());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            updateemployer[0] = object.getString("success");
                            updateemployer[1] = object.getString("message");
                        }
                        break;
                    }
                    return updateemployer;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constant.UPDATE_EMPLOYER_PIC:

                String[] updateCompanyPic = new String[3];
                httppost = new HttpPost(Constant.UPDATE_EMPLOYER_PIC_URL);

                try {
                    try {

                        MultipartEntity multipartEntity = new MultipartEntity();
                        Log.e("", "URL " + Constant.UPDATE_EMPLOYER_PIC_URL);

                        try {
                            File imgfile = new File(Constant.USER_IMAGE);
                            FileBody bin = new FileBody(imgfile);
                            multipartEntity.addPart("profile_image", bin);
                        } catch (Exception e) {
                            Log.v("Exception in Image", "" + e);
                        }


                        multipartEntity.addPart("email", new StringBody(Constant.EMAIL));
                        Log.e("", "USER_IMAGE : " + Constant.EMAIL);


                        httppost.setEntity(multipartEntity);

                        try {
                            response1 = httpclient.execute(httppost);
                            Log.d("myapp", "response " + response1.getEntity());
                            Log.e("myapp", "response.. statau.." + response1.getStatusLine().getStatusCode());
                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        BufferedReader reader = new BufferedReader(new InputStreamReader(
                                response1.getEntity().getContent(), "UTF-8"));
                        String sResponse;
                        StringBuilder s = new StringBuilder();
                        while ((sResponse = reader.readLine()) != null) {
                            s = s.append(sResponse);
                        }
                        try {
                            JSONObject jobj = new JSONObject(s.toString());
                            Log.d("", "jsonObj responce... " + jobj);
                            updateCompanyPic[0] = "10" + jobj.getString("success");
                            updateCompanyPic[1] = jobj.getString("message");

                            try {
                                JSONObject dataObject = jobj.getJSONObject("data");
                                Constant.USER_IMAGE = dataObject.getString("profile_pic");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return updateCompanyPic;
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                break;

            case Constant.UPDATE_CADIDATE_PIC:

                String[] updateCandidatePic = new String[3];
                httppost = new HttpPost(Constant.UPDATE_CANDIDATE_PIC_URL);

                try {
                    try {

                        MultipartEntity multipartEntity = new MultipartEntity();
                        Log.e("", "URL " + Constant.UPDATE_CANDIDATE_PIC_URL);

                        try {
                            File imgfile = new File(Constant.USER_IMAGE);
                            FileBody bin = new FileBody(imgfile);
                            multipartEntity.addPart("profile_image", bin);
                        } catch (Exception e) {
                            Log.v("Exception in Image", "" + e);
                        }
                        multipartEntity.addPart("email", new StringBody(Constant.EMAIL));
                        Log.e("", "USER_IMAGE : " + Constant.EMAIL);


                        httppost.setEntity(multipartEntity);

                        try {
                            response1 = httpclient.execute(httppost);
                            Log.d("myapp", "response " + response1.getEntity());
                            Log.e("myapp", "response.. statau.." + response1.getStatusLine().getStatusCode());
                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        BufferedReader reader = new BufferedReader(new InputStreamReader(
                                response1.getEntity().getContent(), "UTF-8"));
                        String sResponse;
                        StringBuilder s = new StringBuilder();
                        while ((sResponse = reader.readLine()) != null) {
                            s = s.append(sResponse);
                        }
                        try {
                            JSONObject jobj = new JSONObject(s.toString());
                            Log.d("", "jsonObj responce... " + jobj);
                            updateCandidatePic[0] = "10" + jobj.getString("success");
                            updateCandidatePic[1] = jobj.getString("message");

                            try {
                                JSONObject dataObject = jobj.getJSONObject("data");
                                Constant.USER_IMAGE = dataObject.getString("profile_pic");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return updateCandidatePic;
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                break;

            case Constant.EMPLOYEETOP_TEN:
                String[] employeeTen = new String[3];
                httppost = new HttpPost(Constant.UPDATE_EMPLOYEE_TOP_TEN_URL);
                try {
                    try {

                        jsonData.accumulate("", "");

                        Log.e("", "URL " + Constant.UPDATE_EMPLOYEE_TOP_TEN_URL);
                        Log.e("Json : ", "" + jsonData.toString(1));
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            if (response1 != null) {
                                Log.e("", "responce");
                                jsonData.has("success");
                            } else {
                                Log.e("", "Null responce");
                            }
                            response1.getStatusLine().getStatusCode();
                            StatusLine statusLine = response1.getStatusLine();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;
                    Global.companylist.clear();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            employeeTen[0] = "0" + object.getString("success");
                            employeeTen[1] = object.getString("message");

                            try {
                                JSONArray jsonArray = object.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    object = jsonArray.getJSONObject(i);
                                    CompanyDTO companyDTO = new CompanyDTO();


                                    companyDTO.setEmployer_id(object.getString("employer_id"));
                                    companyDTO.setCompany_name(object.getString("company_name"));
                                    companyDTO.setContact_person(object.getString("contact_person"));
                                    companyDTO.setEmail(object.getString("email"));
                                    companyDTO.setPhone(object.getString("phone"));
                                    companyDTO.setExperience(object.getString("experience"));
                                    companyDTO.setLocation(object.getString("location"));
                                    companyDTO.setJobe_type(object.getString("job_type"));
                                    companyDTO.setSpecilization(object.getString("specilaization"));
                                    companyDTO.setJob_role(object.getString("job_role"));
                                    companyDTO.setNo_of_requirment(object.getString("num_of_requirment"));
                                    companyDTO.setAddress(object.getString("address"));
                                    companyDTO.setPosted_job(object.getString("job_posted_at"));
                                    companyDTO.setEmp_Image(object.getString("picture"));



                                    Global.companylist.add(companyDTO);
                                }

                                Log.e("", "List Size : " + Global.companylist.size());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            employeeTen[0] = object.getString("success");
                            employeeTen[1] = object.getString("message");
                        }
                        break;
                    }
                    return employeeTen;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            case Constant.CADIDATE_TOP_TEN:
                String[] cadidatetop = new String[3];
                httppost = new HttpPost(Constant.UPDATE_CANDIDATE_TOP_TEN_URL);
                try {
                    try {
                        jsonData.accumulate("", "");
                        Log.e("", "URL " + Constant.UPDATE_CANDIDATE_TOP_TEN_URL);
                        Log.e("Json : ", "" + jsonData.toString(1));
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            if (response1 != null) {
                                Log.e("", "responce");
                                jsonData.has("success");
                            } else {
                                Log.e("", "Null responce");
                            }
                            response1.getStatusLine().getStatusCode();
                            StatusLine statusLine = response1.getStatusLine();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;
                    Global.candidatelist.clear();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            cadidatetop[0] = "0" + object.getString("success");
                            cadidatetop[1] = object.getString("message");
                            try {
                                JSONArray array = object.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject jsonObject = array.getJSONObject(i);
                                    CandidateDTO candidateDTO = new CandidateDTO();
                                    candidateDTO.setUserId(jsonObject.getString("user_id"));
                                    candidateDTO.setName(jsonObject.getString("name"));
                                    candidateDTO.setEmail(jsonObject.getString("email"));
                                    candidateDTO.setPhone(jsonObject.getString("phone"));
                                    candidateDTO.setGender(jsonObject.getString("gender"));
                                    candidateDTO.setLocation(jsonObject.getString("location"));
                                    candidateDTO.setUserImage(jsonObject.getString("image"));
                                    candidateDTO.setUser_type(jsonObject.getString("user_type"));
                                    candidateDTO.setDesignation(jsonObject.getString("designation"));
                                    candidateDTO.setResume(jsonObject.getString("resume"));
                                    candidateDTO.setJobRole(jsonObject.getString("job_role"));
                                    candidateDTO.setJobType(jsonObject.getString("job_type"));
                                    candidateDTO.setSpecialization(jsonObject.getString("specilaization"));

                                    Global.candidatelist.add(candidateDTO);
                                }

                                Log.e("", "Candidate List" + Global.candidatelist.size());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            cadidatetop[0] = object.getString("success");
                            cadidatetop[1] = object.getString("message");
                        }
                        break;
                    }
                    return cadidatetop;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            case Constant.SEARCH_API:
                String[] searchapi = new String[3];
                httppost = new HttpPost(Constant.SEARCH_URL);
                try {
                    try {
                        jsonData.accumulate("skill", Constant.SKILLES);
                        jsonData.accumulate("location", Constant.LOCATION);

                        Log.e("", "URL " + Constant.SEARCH_URL);
                        Log.e("Json : ", "" + jsonData.toString(12));
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            if (response1 != null) {
                                Log.e("", "responce");
                                jsonData.has("success");
                            } else {
                                Log.e("", "Null responce");
                            }
                            response1.getStatusLine().getStatusCode();
                            StatusLine statusLine = response1.getStatusLine();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;
                    Global.searchcandidatelist.clear();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            searchapi[0] = "000" + object.getString("success");
                            searchapi[1] = object.getString("message");

                            try {
                                JSONArray array = object.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object1 = array.getJSONObject(i);

                                    JSONObject jsonObject = array.getJSONObject(i);
                                    CandidateDTO candidateDTO = new CandidateDTO();
                                    candidateDTO.setUserId(jsonObject.getString("user_id"));
                                    candidateDTO.setName(jsonObject.getString("name"));
                                    candidateDTO.setEmail(jsonObject.getString("email"));
                                    candidateDTO.setPhone(jsonObject.getString("phone"));
                                    candidateDTO.setGender(jsonObject.getString("gender"));
                                    candidateDTO.setLocation(jsonObject.getString("location"));
                                    candidateDTO.setUserImage(jsonObject.getString("image"));
                                    candidateDTO.setUser_type(jsonObject.getString("user_type"));
                                    candidateDTO.setDesignation(jsonObject.getString("designation"));
                                    candidateDTO.setResume(jsonObject.getString("resume"));
                                    candidateDTO.setJobRole(jsonObject.getString("job_role"));
                                    candidateDTO.setJobType(jsonObject.getString("job_type"));
                                    candidateDTO.setSpecialization(jsonObject.getString("specilaization"));

                                    Global.searchcandidatelist.add(candidateDTO);
                                }

                                Log.e("", "Size : " + Global.searchcandidatelist.size());

                            } catch (Exception e) {
                                e.printStackTrace();
                                searchapi[0] = "000" + object.getString("success");
                                searchapi[1] = object.getString("message");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            searchapi[0] = "000" + object.getString("success");
                            searchapi[1] = object.getString("message");
                        }
                        break;
                    }
                    return searchapi;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constant.COMPANY_API:
                String[] cmp_search = new String[3];
                httppost = new HttpPost(Constant.COMPANY_SEARCH_URL);
                try {
                    try {
                        jsonData.accumulate("skill", Constant.SKILLES);
                        jsonData.accumulate("location", Constant.LOCATION);

                        Log.e("", "URL " + Constant.COMPANY_SEARCH_URL);
                        Log.e("Json : ", "" + jsonData.toString(12));
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            if (response1 != null) {
                                Log.e("", "responce");
                                jsonData.has("success");
                            } else {
                                Log.e("", "Null responce");
                            }
                            response1.getStatusLine().getStatusCode();
                            StatusLine statusLine = response1.getStatusLine();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;
                    CompanyDTO companyDTO = null;
                    Global.companySearchlist.clear();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            cmp_search[0] = "000" + object.getString("success");
                            cmp_search[1] = object.getString("message");

                            try {
                                JSONArray array = object.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object1 = array.getJSONObject(i);
                                    companyDTO = new CompanyDTO();

                                    companyDTO.setEmployer_id(object1.getString("employer_id"));
                                    companyDTO.setCompany_name(object1.getString("company_name"));
                                    companyDTO.setContact_person(object1.getString("contact_person"));
                                    companyDTO.setEmail(object1.getString("email"));
                                    companyDTO.setPhone(object1.getString("phone"));
                                    companyDTO.setExperience(object1.getString("experience"));
                                    companyDTO.setLocation(object1.getString("location"));

                                    companyDTO.setJobe_type(object1.getString("job_type"));
                                    companyDTO.setSpecilization(object1.getString("specilaization"));
                                    companyDTO.setJob_role(object1.getString("job_role"));
                                    companyDTO.setNo_of_requirment(object1.getString("num_of_requirment"));
                                    companyDTO.setAddress(object1.getString("address"));
                                    companyDTO.setEmp_Image(object1.getString("picture"));
                                    companyDTO.setExp_date(object1.getString("exp_date"));
                                    companyDTO.setPosted_job(object1.getString("job_posted_at"));

                                    Global.companySearchlist.add(companyDTO);
                                }
                                Log.e("", "Lit Size  companySearchlist " + Global.companySearchlist.size());
                            } catch (Exception e) {
                                e.printStackTrace();
                                cmp_search[0] = "000" + object.getString("success");
                                cmp_search[1] = object.getString("message");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            cmp_search[0] = "000" + object.getString("success");
                            cmp_search[1] = object.getString("message");
                        }
                        break;
                    }
                    return cmp_search;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            case Constant.NOTIFICATION_API:
                String[] notification = new String[3];
                httppost = new HttpPost(Constant.NOTIFICATION_URL);
                try {
                    try {
                        jsonData.accumulate("user_id", Constant.USER_ID);
                        Log.e("", "URL " + Constant.NOTIFICATION_URL);
                        Log.e("Json : ", "" + jsonData.toString(12));
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            response1.getStatusLine().getStatusCode();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;
                    Global.notificationList.clear();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);
                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            notification[0] = object.getString("success");
                            notification[1] = object.getString("message");
                            try {
                                JSONArray array = object.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    NotificationDTO notificationDTO = new NotificationDTO();
                                    notificationDTO.setId(jsonObject.getString("id"));
                                    notificationDTO.setView_id(jsonObject.getString("view_id"));
                                    notificationDTO.setEmail(jsonObject.getString("email"));
                                    notificationDTO.setMessage(jsonObject.getString("msg"));
                                    notificationDTO.setDatetime(jsonObject.getString("datetime"));
                                    Global.notificationList.add(notificationDTO);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                notification[0] = object.getString("success");
                                notification[1] = object.getString("message");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            notification[0] = object.getString("success");
                            notification[1] = object.getString("message");
                        }
                        break;
                    }
                    return notification;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            case Constant.VIEW_PROFILE_API:
                String[] viewprofile = new String[3];
                httppost = new HttpPost(Constant.VIEW_PROFILE_URL);
                try {
                    try {

                        jsonData.accumulate("user_id", Constant.USER_ID);
                        jsonData.accumulate("company_id", Constant.COMPANY_ID);


                        Log.e("", "URL " + Constant.VIEW_PROFILE_URL);
                        Log.e("Json : ", "" + jsonData.toString());
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            response1.getStatusLine().getStatusCode();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            viewprofile[0] = "0" + object.getString("success");
                            viewprofile[1] = object.getString("message");
                            try {
                                JSONArray array = object.getJSONArray("data");

                            } catch (Exception e) {
                                e.printStackTrace();
                                viewprofile[0] = "0" + object.getString("success");
                                viewprofile[1] = object.getString("message");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            viewprofile[0] = "0" + object.getString("success");
                            viewprofile[1] = object.getString("message");
                        }
                        break;
                    }
                    return viewprofile;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            case Constant.GET_FILTER_API:
                String[] get_filter = new String[3];
                httppost = new HttpPost(Constant.GET_FILTER_URL);
                try {
                    try {
                        jsonData.accumulate("user_id", "");
                        Log.e("", "URL " + Constant.GET_FILTER_URL);
                        Log.e("Json : ", "" + jsonData.toString());
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            response1.getStatusLine().getStatusCode();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());
                        } catch (ClientProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;
                    Global.cityList.clear();
                    Global.industryList.clear();
                    Global.roleList.clear();
                    Global.educationList.clear();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            get_filter[0] = object.getString("success");
                            get_filter[1] = object.getString("message");
                            try {
                                JSONArray city = object.getJSONArray("city");
                                JSONArray role = object.getJSONArray("role");
                                JSONArray education = object.getJSONArray("industry");
                                JSONArray industry = object.getJSONArray("industry");

                                for (int i = 0; i < city.length(); i++) {
                                    JSONObject object1 = city.getJSONObject(i);
                                    FilterDTO filterDTO = new FilterDTO();
                                    filterDTO.setFilterString(object1.getString("city"));
                                    filterDTO.setFlage(false);
                                    Global.cityList.add(filterDTO);
                                }
                                for (int i = 0; i < role.length(); i++) {
                                    JSONObject object1 = role.getJSONObject(i);
                                    FilterDTO filterDTO = new FilterDTO();
                                    filterDTO.setFilterString(object1.getString("role"));
                                    filterDTO.setFlage(false);
                                    Global.roleList.add(filterDTO);
                                }

                                for (int i = 0; i < education.length(); i++) {
                                    JSONObject object1 = education.getJSONObject(i);
                                    FilterDTO filterDTO = new FilterDTO();
                                    filterDTO.setFilterString(object1.getString("industry"));
                                    filterDTO.setFlage(false);
                                    Global.educationList.add(filterDTO);
                                }

                                for (int i = 0; i < industry.length(); i++) {
                                    JSONObject object1 = industry.getJSONObject(i);
                                    FilterDTO filterDTO = new FilterDTO();
                                    filterDTO.setFilterString(object1.getString("industry"));
                                    filterDTO.setFlage(false);
                                    Global.industryList.add(filterDTO);
                                }

                                Log.e("", "City" + Global.cityList.size());
                                Log.e("", "Role" + Global.roleList.size());
                                Log.e("", "Education" + Global.educationList.size());
                                Log.e("", "Industry" + Global.industryList.size());


                            } catch (Exception e) {
                                e.printStackTrace();
                                get_filter[0] = object.getString("success");
                                get_filter[1] = object.getString("message");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            get_filter[0] = object.getString("success");
                            get_filter[1] = object.getString("message");
                        }
                        break;
                    }
                    return get_filter;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constant.GET_FILTER_DATA:
                String[] filter = new String[3];
                httppost = new HttpPost(Constant.GET_FILTER_DATA_URL);
                try {
                    try {

                        jsonData.accumulate("", "");

                        Log.e("", "URL " + Constant.GET_FILTER_DATA_URL);
                        Log.e("Json : ", "" + jsonData.toString(1));
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            if (response1 != null) {
                                Log.e("", "responce");
                                jsonData.has("success");
                            } else {
                                Log.e("", "Null responce");
                            }
                            response1.getStatusLine().getStatusCode();
                            StatusLine statusLine = response1.getStatusLine();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;
                    ArrayList<JobRollDTO> joblist;
                    Global.getFilterList.clear();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            filter[0] = "0" + object.getString("success");
                            filter[1] = object.getString("message");

                            JSONArray array = object.getJSONArray("branch");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object1 = array.getJSONObject(i);
                                GetFillterDTO fillterDTO = new GetFillterDTO();
                                fillterDTO.setBranchId(object1.getString("id"));
                                fillterDTO.setIndustry(object1.getString("industry"));

                                JSONArray array1 = object1.getJSONArray("job_role");
                                joblist = new ArrayList<>();

                                for (int j = 0; j < array1.length(); j++) {
                                    JSONObject data = array1.getJSONObject(j);
                                    JobRollDTO jobRollDTO = new JobRollDTO();
                                    jobRollDTO.setJobrollId(data.getString("id"));
                                    jobRollDTO.setFunId(data.getString("fun_id"));
                                    jobRollDTO.setTitle(data.getString("title"));
                                    joblist.add(jobRollDTO);
                                }
                                fillterDTO.setJobRolllist(joblist);
                                Global.getFilterList.add(fillterDTO);
                            }

                            Log.e("", "SIZE : " + Global.getFilterList.size());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            filter[0] = object.getString("success");
                            filter[1] = object.getString("message");
                        }
                        break;
                    }
                    return filter;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            case Constant.POST_JOB:
                String[] postJOb = new String[3];
                httppost = new HttpPost(Constant.POST_JOB_URL);
                try {
                    try {

                        jsonData.accumulate("emp_id", Constant.COMPANY_ID);
                        jsonData.accumulate("company_name", Constant.COMPANY_NAME);
                        jsonData.accumulate("job_role", Constant.JOBROLL);
                        jsonData.accumulate("skill", Constant.SKILLES);
                        jsonData.accumulate("experience", Constant.EXPERIENCE);
                        jsonData.accumulate("gender", Constant.GENDER);
                        jsonData.accumulate("job_type", Constant.JOB_TYPE);
                        jsonData.accumulate("specilaization", Constant.SPECILIZATION);
                        jsonData.accumulate("location", Constant.LOCATION);
                        jsonData.accumulate("discription", Constant.DISCRIPTION);
                        jsonData.accumulate("no_of_requirement", Constant.NO_OF_REQUIRMENT);


                        Log.e("", "URL " + Constant.POST_JOB_URL);
                        Log.e("Json : ", "" + jsonData.toString());
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            response1.getStatusLine().getStatusCode();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            postJOb[0] = "00" + object.getString("success");
                            postJOb[1] = object.getString("message");
                            JSONObject object1 = object.getJSONObject("data");
                            Constant.OUT_OF_POST = object1.getString("out_of_post_job");
                            Constant.NO_OF_POST = object1.getString("posted_job");



                        } catch (JSONException e) {
                            e.printStackTrace();
                            postJOb[0] = "00" + object.getString("success");
                            postJOb[1] = object.getString("message");
                        }
                        break;
                    }
                    return postJOb;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            case Constant.UPDATE_POST:
                String[] updatepost = new String[3];
                httppost = new HttpPost(Constant.UPDATE_POST_URL);
                try {
                    try {

                        jsonData.accumulate("job_id", Constant.JOB_ID);
                        jsonData.accumulate("company_name", Constant.COMPANY_NAME);
                        jsonData.accumulate("job_role", Constant.JOBROLL);
                        jsonData.accumulate("skill", Constant.SKILLES);
                        jsonData.accumulate("experience", Constant.EXPERIENCE);
                        jsonData.accumulate("gender", Constant.GENDER);
                        jsonData.accumulate("job_type", Constant.JOB_TYPE);
                        jsonData.accumulate("specilaization", Constant.SPECILIZATION);
                        jsonData.accumulate("location", Constant.LOCATION);
                        jsonData.accumulate("discription", Constant.DISCRIPTION);


                        Log.e("", "URL " + Constant.UPDATE_POST_URL);
                        Log.e("Json : ", "" + jsonData.toString());
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            response1.getStatusLine().getStatusCode();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            updatepost[0] = "00" + object.getString("success");
                            updatepost[1] = object.getString("message");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            updatepost[0] = "0" + object.getString("success");
                            updatepost[1] = object.getString("message");
                        }
                        break;
                    }
                    return updatepost;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constant.GET_ALL_POST:
                String[] getpost = new String[3];
                httppost = new HttpPost(Constant.GET_ALL_POST_URL);
                try {
                    try {

                        jsonData.accumulate("emp_id", Constant.COMPANY_ID);

                        Log.e("", "URL " + Constant.GET_ALL_POST_URL);
                        Log.e("Json : ", "" + jsonData.toString());
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            response1.getStatusLine().getStatusCode();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;
                    PostDTO postDTO = null;
                    Global.postJob_List.clear();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            getpost[0] = "0" + object.getString("success");
                            getpost[1] = object.getString("message");
                            try {
                                JSONArray array = object.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject data = array.getJSONObject(i);
                                    postDTO = new PostDTO();

                                    postDTO.setId(data.getString("id"));
                                    postDTO.setEmp_id(data.getString("emp_id"));
                                    postDTO.setEmployer_name(data.getString("employer_name"));
                                    postDTO.setImage(data.getString("image"));
                                    postDTO.setIndustry_type(data.getString("industry_type"));
                                    postDTO.setFunctional_area(data.getString("functional_area"));
                                    postDTO.setJob_role(data.getString("job_role"));
                                    postDTO.setNum_of_requirment(data.getString("num_of_requirment"));
                                    postDTO.setDesignation(data.getString("designation"));
                                    postDTO.setSkill(data.getString("skill"));
                                    postDTO.setExperience(data.getString("experience"));
                                    postDTO.setCountry(data.getString("country"));
                                    postDTO.setState(data.getString("state"));
                                    postDTO.setCity(data.getString("city"));
                                    postDTO.setDiscription(data.getString("discription"));
                                    postDTO.setCount(data.getString("count"));
                                    postDTO.setLast_date(data.getString("last_date"));
                                    postDTO.setExtand(data.getString("extand"));
                                    postDTO.setGender(data.getString("gender"));

                                    Global.postJob_List.add(postDTO);
                                }

                                Log.e("", "List SIze :" + Global.postJob_List.size());
                            } catch (Exception e) {
                                e.printStackTrace();
                                getpost[0] = "0" + object.getString("success");
                                getpost[1] = object.getString("message");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            getpost[0] = "0" + object.getString("success");
                            getpost[1] = object.getString("message");
                        }
                        break;
                    }
                    return getpost;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            case Constant.DELETE_POST:
                String[] deletepost = new String[3];
                httppost = new HttpPost(Constant.DELETE_POST_URL);
                try {
                    try {

                        jsonData.accumulate("job_id", Constant.JOB_ID);
                        jsonData.accumulate("emp_id", Constant.COMPANY_ID);

                        Log.e("", "URL " + Constant.DELETE_POST_URL);
                        Log.e("Json : ", "" + jsonData.toString());
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            response1.getStatusLine().getStatusCode();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;
                    PostDTO postDTO = null;
                    Global.postJob_List.clear();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            deletepost[0] = "0" + object.getString("success");
                            deletepost[1] = object.getString("message");
                            try {
                                JSONArray array = object.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject data = array.getJSONObject(i);
                                    postDTO = new PostDTO();

                                    postDTO.setId(data.getString("id"));
                                    postDTO.setEmp_id(data.getString("emp_id"));
                                    postDTO.setEmployer_name(data.getString("employer_name"));
                                    postDTO.setImage(data.getString("image"));
                                    postDTO.setIndustry_type(data.getString("industry_type"));
                                    postDTO.setFunctional_area(data.getString("functional_area"));
                                    postDTO.setJob_role(data.getString("job_role"));
                                    postDTO.setNum_of_requirment(data.getString("num_of_requirment"));
                                    postDTO.setDesignation(data.getString("designation"));
                                    postDTO.setSkill(data.getString("skill"));
                                    postDTO.setExperience(data.getString("experience"));
                                    postDTO.setCountry(data.getString("country"));
                                    postDTO.setState(data.getString("state"));
                                    postDTO.setCity(data.getString("city"));
                                    postDTO.setDiscription(data.getString("discription"));
                                    postDTO.setCount(data.getString("count"));
                                    postDTO.setLast_date(data.getString("last_date"));
                                    postDTO.setExtand(data.getString("extand"));
                                    postDTO.setGender(data.getString("gender"));

                                    Global.postJob_List.add(postDTO);
                                }

                                Log.e("", "List SIze :" + Global.postJob_List.size());
                            } catch (Exception e) {
                                e.printStackTrace();
                                deletepost[0] = "0" + object.getString("success");
                                deletepost[1] = object.getString("message");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            deletepost[0] = "0" + object.getString("success");
                            deletepost[1] = object.getString("message");
                        }
                        break;
                    }
                    return deletepost;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            case Constant.GET_JOB_LIST:
                String[] joblist = new String[3];
                httppost = new HttpPost(Constant.GET_ALL_JOBLIST_URL);
                try {
                    try {

                        jsonData.accumulate("", "");

                        Log.e("", "URL " + Constant.GET_FILTER_DATA_URL);
                        Log.e("Json : ", "" + jsonData.toString(1));
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            if (response1 != null) {
                                Log.e("", "responce");
                                jsonData.has("success");
                            } else {
                                Log.e("", "Null responce");
                            }
                            response1.getStatusLine().getStatusCode();
                            StatusLine statusLine = response1.getStatusLine();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;
                    Global.jobroll_List.clear();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            joblist[0] = "70" + object.getString("success");
                            joblist[1] = object.getString("message");
                            JSONArray array = object.getJSONArray("data");
                            Global.jobroll_List.add("Select Job Role");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jobroll = array.getJSONObject(i);
                                Global.jobroll_List.add(jobroll.getString("title"));
                            }
                            Log.e("", "Size : " + Global.jobroll_List.size());

                        } catch (JSONException e) {
                            e.printStackTrace();
                            joblist[0] = object.getString("success");
                            joblist[1] = object.getString("message");
                        }
                        break;
                    }
                    return joblist;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
//Sohel
            case Constant.GET_PACK_LIST:
                String[] getPack = new String[3];
                httppost = new HttpPost(Constant.GET_ALL_MEMBERSHIP_URL);
                try {
                    try {

//                        jsonData.accumulate("emp_id", Constant.COMPANY_ID);

                        Log.e("", "URL " + Constant.GET_ALL_MEMBERSHIP_URL);
                        Log.e("Json : ", "" + jsonData.toString());
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            response1.getStatusLine().getStatusCode();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;
                    MembershipDTO membershipDTO = null;
                    Global.membershipPack_List.clear();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            getPack[0] = "0" + object.getString("success");
                            getPack[1] = object.getString("message");
                            try {
                                JSONArray array = object.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject data = array.getJSONObject(i);
                                    membershipDTO = new MembershipDTO();

                                    membershipDTO.setId(data.getString("id"));
                                    membershipDTO.setPackage_name(data.getString("package_name"));
                                    membershipDTO.setCandidate_count(data.getString("candidate_count"));
                                    membershipDTO.setPost_job_count(data.getString("post_job_count"));
                                    membershipDTO.setDiscription(data.getString("discription"));
                                    membershipDTO.setPackage_price(data.getString("package_price"));
                                    membershipDTO.setValidFor(data.getString("valid_days"));
                                    membershipDTO.setCreated_at(data.getString("created_at"));

                                    Global.membershipPack_List.add(membershipDTO);
                                }

                                Log.e("", "List SIze :" + Global.membershipPack_List.size());
                            } catch (Exception e) {
                                e.printStackTrace();
                                getPack[0] = "0" + object.getString("success");
                                getPack[1] = object.getString("message");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            getPack[0] = "0" + object.getString("success");
                            getPack[1] = object.getString("message");
                        }
                        break;
                    }
                    return getPack;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constant.SELECT_PACK:
                String[] selectPack = new String[3];
                httppost = new HttpPost(Constant.SELECT_MEMBERSHIP_URL);
                try {
                    try {

                        jsonData.accumulate("employer_id", Constant.USER_ID);
                        jsonData.accumulate("package_name", Constant.SELECTED_PACK);

                        Log.e("", "URL " + Constant.SELECT_MEMBERSHIP_URL);
                        Log.e("Json : ", "" + jsonData.toString());
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            response1.getStatusLine().getStatusCode();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.e("", "jsonObj responce... " + object);
                            selectPack[0] = "00" + object.getString("success");
                            selectPack[1] = object.getString("message");

                            JSONObject data = object.getJSONObject("data");

                            Constant.SELECTED_PACK = data.getString("package_name");
                            SavedData.savePack(Constant.SELECTED_PACK);
                            Log.e("sohel   ", "selected pack -- " + Constant.SELECTED_PACK);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            selectPack[0] = "00" + object.getString("success");
                            selectPack[1] = object.getString("message");
                        }
                        break;
                    }
                    return selectPack;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case Constant.APPLY_FILTER:
                String[] apply_filter = new String[3];
                httppost = new HttpPost(Constant.APPLY_FILTTER_URL);
                try {
                    try {
                        JSONArray city = new JSONArray();
                        JSONArray role = new JSONArray();
                        JSONArray specilization = new JSONArray();
                        JSONArray gender = new JSONArray();
                        JSONArray experience = new JSONArray();

                        for(int i=0;i<Global.cityList.size();i++){
                            if(Global.cityList.get(i).isFlage()){
                                city.put(Global.cityList.get(i).getFilterString());
                            }
                        }
                        for(int i=0;i<Global.roleList.size();i++){
                            if(Global.roleList.get(i).isFlage()){
                                role.put(Global.roleList.get(i).getFilterString());
                            }
                        }
                        for(int i=0;i<Global.educationList.size();i++){
                            if(Global.educationList.get(i).isFlage()){
                                specilization.put(Global.educationList.get(i).getFilterString());
                            }
                        }

                        for(int i=0;i<Global.experienceList.size();i++){
                            if(Global.experienceList.get(i).isFlage()){
                                experience.put(Global.experienceList.get(i).getYear());
                            }
                        }

                        for(int i=0;i<Global.genderList.size();i++){
                            if(Global.genderList.get(i).isFlage()){
                                gender.put(Global.genderList.get(i).getGender());
                            }
                        }

                        jsonData.accumulate("location", city);
                        jsonData.accumulate("job_role", role);
                        jsonData.accumulate("specilization", specilization);
                        jsonData.accumulate("experience",experience );
                        jsonData.accumulate("gender", gender);

                        Log.e("", "URL " + Constant.APPLY_FILTTER_URL);
                        Log.e("Json : ", "" + jsonData.toString(12));
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            if (response1 != null) {
                                Log.e("", "responce");
                                jsonData.has("success");
                            } else {
                                Log.e("", "Null responce");
                            }
                            response1.getStatusLine().getStatusCode();
                            StatusLine statusLine = response1.getStatusLine();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;
                    CompanyDTO companyDTO = null;
                    Global.searchcandidatelist.clear();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            apply_filter[0] = "000" + object.getString("success");
                            apply_filter[1] = object.getString("message");

                            try {
                                JSONArray array = object.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {


                                    JSONObject jsonObject = array.getJSONObject(i);
                                    CandidateDTO candidateDTO = new CandidateDTO();

                                    candidateDTO.setUserId(jsonObject.getString("user_id"));
                                    candidateDTO.setName(jsonObject.getString("name"));
                                    candidateDTO.setEmail(jsonObject.getString("email"));
                                    candidateDTO.setPhone(jsonObject.getString("phone"));
                                    candidateDTO.setGender(jsonObject.getString("gender"));
                                    candidateDTO.setLocation(jsonObject.getString("location"));
                                    candidateDTO.setUserImage(jsonObject.getString("image"));
                                    candidateDTO.setUser_type(jsonObject.getString("user_type"));
                                    candidateDTO.setDesignation(jsonObject.getString("designation"));
                                    candidateDTO.setExperience(jsonObject.getString("experience"));
                                    candidateDTO.setJobRole(jsonObject.getString("job_role"));
                                    candidateDTO.setJobType(jsonObject.getString("job_type"));
                                    candidateDTO.setSpecialization(jsonObject.getString("specilaization"));

                                    Global.searchcandidatelist.add(candidateDTO);
                                }

                                Log.e("", "Size : " + Global.searchcandidatelist.size());

                            } catch (Exception e) {
                                e.printStackTrace();
                                apply_filter[0] = "000" + object.getString("success");
                                apply_filter[1] = object.getString("message");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            apply_filter[0] = "000" + object.getString("success");
                            apply_filter[1] = object.getString("message");
                        }
                        break;
                    }
                    return apply_filter;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            case Constant.APPLY_CUSTOMER_FILTER:
                String[] apply_customer_filter = new String[3];
                httppost = new HttpPost(Constant.APPLY_CUSTOMER_FILTTER_URL);
                try {
                    try {
                        JSONArray city = new JSONArray();
                        JSONArray role = new JSONArray();
                        JSONArray specilization = new JSONArray();
                        JSONArray gender = new JSONArray();
                        JSONArray experience = new JSONArray();

                        for(int i=0;i<Global.cityList.size();i++){
                            if(Global.cityList.get(i).isFlage()){
                                city.put(Global.cityList.get(i).getFilterString());
                            }
                        }
                        for(int i=0;i<Global.roleList.size();i++){
                            if(Global.roleList.get(i).isFlage()){
                                role.put(Global.roleList.get(i).getFilterString());
                            }
                        }
                        for(int i=0;i<Global.educationList.size();i++){
                            if(Global.educationList.get(i).isFlage()){
                                specilization.put(Global.educationList.get(i).getFilterString());
                            }
                        }

                        for(int i=0;i<Global.experienceList.size();i++){
                            if(Global.experienceList.get(i).isFlage()){
                                experience.put(Global.experienceList.get(i).getYear());
                            }
                        }

                        for(int i=0;i<Global.genderList.size();i++){
                            if(Global.genderList.get(i).isFlage()){
                                gender.put(Global.genderList.get(i).getGender());
                            }
                        }

                        jsonData.accumulate("location", city);
                        jsonData.accumulate("job_role", role);
                        jsonData.accumulate("specilization", specilization);
                        jsonData.accumulate("experience",experience );
                        jsonData.accumulate("gender", gender);

                        Log.e("", "URL " + Constant.APPLY_CUSTOMER_FILTTER_URL);
                        Log.e("Json : ", "" + jsonData.toString(12));
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            if (response1 != null) {
                                Log.e("", "responce");
                                jsonData.has("success");
                            } else {
                                Log.e("", "Null responce");
                            }
                            response1.getStatusLine().getStatusCode();
                            StatusLine statusLine = response1.getStatusLine();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;
                    CompanyDTO companyDTO = null;
                    Global.companylist.clear();
                    Global.candidatelist.clear();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.d("", "jsonObj responce... " + object);
                            apply_customer_filter[0] = "000" + object.getString("success");
                            apply_customer_filter[1] = object.getString("message");

                            try {
                                JSONArray array = object.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object1 = array.getJSONObject(i);
                                    companyDTO = new CompanyDTO();

                                    /*try{
                                        companyDTO.setEmployer_id(object1.getString("user_id"));
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }*/


                                    companyDTO.setEmployer_id(object1.getString("emp_id"));
                                    companyDTO.setCompany_name(object1.getString("company_name"));
                                    companyDTO.setEmail(object1.getString("email"));
                                    companyDTO.setPhone(object1.getString("phone"));
                                    companyDTO.setLocation(object1.getString("location"));
                                    companyDTO.setExperience(object1.getString("experience"));
                                    companyDTO.setEmp_Image(object1.getString("image"));
                                    companyDTO.setJobe_type(object1.getString("job_type"));
                                    companyDTO.setSpecilization(object1.getString("specilaization"));
                                    companyDTO.setJob_role(object1.getString("job_role"));




                                    Global.companylist.add(companyDTO);

                                    /*JSONObject jsonObject = array.getJSONObject(i);
                                    CandidateDTO candidateDTO = new CandidateDTO();
                                    candidateDTO.setUserId(jsonObject.getString("user_id"));
                                    candidateDTO.setName(jsonObject.getString("name"));
                                    candidateDTO.setEmail(jsonObject.getString("email"));
                                    candidateDTO.setPhone(jsonObject.getString("phone"));
                                    candidateDTO.setGender(jsonObject.getString("gender"));
                                    candidateDTO.setLocation(jsonObject.getString("location"));
                                    candidateDTO.setUserImage(jsonObject.getString("image"));
                                    candidateDTO.setUser_type(jsonObject.getString("user_type"));
                                    candidateDTO.setDesignation(jsonObject.getString("designation"));
                                    candidateDTO.setResume(jsonObject.getString("resume"));
                                    candidateDTO.setJobRole(jsonObject.getString("job_role"));
                                    candidateDTO.setJobType(jsonObject.getString("job_type"));
                                    candidateDTO.setSpecialization(jsonObject.getString("specilaization"));

                                    Global.candidatelist.add(candidateDTO);*/
                                }
                                Log.e("", "Lit Size  companySearchlist " + Global.companylist.size());
                            } catch (Exception e) {
                                e.printStackTrace();
                                apply_customer_filter[0] = "000" + object.getString("success");
                                apply_customer_filter[1] = object.getString("message");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            apply_customer_filter[0] = "000" + object.getString("success");
                            apply_customer_filter[1] = object.getString("message");
                        }
                        break;
                    }
                    return apply_customer_filter;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            case Constant.UPDATE_DWONLOAD:
                String[] update_download = new String[3];
                httppost = new HttpPost(Constant.UPDATE_DOWNLOAD_URL);
                try {
                    try {

                        jsonData.accumulate("employee_id", Constant.USER_ID);

                        Log.e("", "URL " + Constant.UPDATE_DOWNLOAD_URL);
                        Log.e("Json : ", "" + jsonData.toString());
                        StringEntity se = new StringEntity(jsonData.toString());
                        httppost.setEntity(se);
                        httppost.setHeader("Accept", "application/json");
                        httppost.setHeader("Content-type", "application/json");
                        try {
                            response1 = httpclient.execute(httppost);
                            response1.getStatusLine().getStatusCode();
                            Log.e("myapp", "response statau.." + response1.getStatusLine().getStatusCode());
                            Log.e("myapp", "response.. " + response1.getEntity());

                        } catch (ClientProtocolException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    InputStream inputStream = response1.getEntity().getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    String result = "";
                    JSONObject object = null;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                        result = sb.toString();
                        Log.e("", "encodeRes : " + result);

                        try {
                            object = new JSONObject(result);
                            Log.e("", "jsonObj responce... " + object);
                            update_download[0] = "00" + object.getString("success");
                            update_download[1] = object.getString("message");
                            JSONObject data = object.getJSONObject("data");
                            Constant.OUT_OF_DOWNLOAD = data.getString("out_of_download");
                            Constant.NOOF_DOWNLOAD = data.getString("download");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            update_download[0] = "00" + object.getString("success");
                            update_download[1] = object.getString("message");
                        }
                        break;
                    }
                    return update_download;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
        return null;
    }


    @Override
    protected void onPostExecute(String[] result) {

        if (dialog.isShowing()) {
            dialog.cancel();
        }
        try {
            ((RequestReceiver) mContext).requestFinished(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;

    }

    public boolean errors_occurred() {
        return this.error_flag;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    private void releaseListMemory() {

    }

    public void setAction(int action) {
        WebserviceHelper.action = action;
    }

}
