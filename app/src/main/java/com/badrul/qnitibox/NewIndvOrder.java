package com.badrul.qnitibox;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Settings.Global;
import android.provider.Settings.SettingNotFoundException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewIndvOrder extends AppCompatActivity implements OnItemSelectedListener {

    public static final String NEW_CONFIRMORDER_URL = "https://qnitibox.qniti.com/apps/neworder.php";
    public static final String EMAIL_IDV_URL = "http://atsventures.com/mail/indmailer.php";

    public static final String KEY_MENUTYPE = "menuType";
    public static final String KEY_MENUDAY = "menuDay";
    public static final String KEY_ORDER_DATE = "orderDate";
    public static final String KEY_ORDER_TIME = "orderTime";
    public static final String KEY_CARDNUM = "userCard";
    public static final String KEY_PHONE = "phoneID";
    public static final String KEY_NAME = "nameID";
    public static final String KEY_MENUQTT = "userQtt";
    public static final String KEY_MENUSTATUS = "orderStatus";
    public static final String KEY_LOCATION = "puLocation";
    public static final String KEY_EMAIL = "emailID";
    public static final String KEY_USERID = "userID";
    public static final String KEY_FOODID = "foodID";

    double result;
    String userID;
    String nameID;
    String phoneID;
    String emailID;
    String matrixID;
    String menuType;
    String menuDay;
    String orderDate;
    String orderTime;
    // String emailID;
    String locat = "0";
    String myStatus = "Processing";
    //private PopupWindow pwindo;
    final Context context = this;
    int id = 0;
    DecimalFormat df;
    Button nextBtn2;
    ImageButton exit;
    ImageButton staffOrder;
    TextView menuTypeD;
    TextView menuDayD;
    EditText cardNum;
    EditText qttNum;
    Spinner sp;
    List<String> list;
    ArrayAdapter<String> adp;
    Boolean loggedIn;
    Double foodprice1;
    String foodID;
    String userLocation;
    double totalprice1;
    int maxQTT;
    String claimPromo;;
    RadioButton promoBtn;
    List<Promo> promoList;
    List<SaleTime> saletimeList;
    int promoID;
    String promoQTT = "0";
    String promoName;
    String promo;
    SharedPreferences sharedPreferences;
    int saletimeID;
    int saleStart;
    int saleEnd;
    int hour;
    String getsalestart;
    String getsaleend;
    SimpleDateFormat dateFormat1;
    String inasisID,foodimage;
    ProgressBar progressBar;
    RadioGroup radioGroup1;
    String paymentType = "none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_indv_order);

        df = new DecimalFormat("0.00");

        sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        menuType = sharedPreferences.getString(Config.MENU_TYPE, "Not Available");
        menuDay = sharedPreferences.getString(Config.MENU_DAY, "Not Available");
        orderDate = sharedPreferences.getString(Config.ORDER_DATE, "Not Available");
        orderTime = sharedPreferences.getString(Config.ORDER_TIME, "Not Available");
        userID = sharedPreferences.getString(Config.USER_ID2,"0");
        String foodprice = sharedPreferences.getString(Config.FOOD_PRICE,"0");
        foodprice1 = Double.valueOf(foodprice);
        nameID = sharedPreferences.getString(Config.NAME_ID2, "Not Available");
        phoneID = sharedPreferences.getString(Config.PHONE_ID2, "Not Available");
        emailID = sharedPreferences.getString(Config.EMAIL_ID2, "Not Available");
        matrixID = sharedPreferences.getString(Config.MATRIX_ID2, "Not Available");
        foodID = sharedPreferences.getString(Config.FOOD_ID, "Not Available");
        userLocation = sharedPreferences.getString(Config.LOCATION_ID2, "Not Available");
        inasisID = sharedPreferences.getString(Config.INASIS_ID,"Not Available");
        foodimage= sharedPreferences.getString(Config.FOOD_IMAGE, "Not Available");

        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
        //If we will get true
        if(loggedIn==false){
            //We will start the Profile Activity
            Intent intent = new Intent(NewIndvOrder.this, LoginPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        final ImageView foodDisplay = findViewById(R.id.imageFood);
        progressBar = findViewById(R.id.progress);
        radioGroup1 = findViewById(R.id.radioGroup);
        TextView showName = findViewById(R.id.nameText);
        TextView showPhone = findViewById(R.id.phoneNum);
        TextView showEmail = findViewById(R.id.emailIDtxt);
        TextView showMatrix = findViewById(R.id.matrixNum);
        TextView menuPrice = findViewById(R.id.menuPrice);
        promoBtn = findViewById(R.id.promo1);

        menuPrice.setText("RM "+foodprice);
        showName.setText(nameID);
        showEmail.setText(emailID);
        showMatrix.setText(matrixID);
        showPhone.setText(phoneID);

        nextBtn2 = findViewById(R.id.nextBtnStart);
        exit = findViewById(R.id.exit);
        // staffOrder =(ImageButton) findViewById(R.id.stafforder);
        menuTypeD = findViewById(R.id.menuTypeDisplay);
        menuDayD = findViewById(R.id.menuDayDisplay);
        cardNum = findViewById(R.id.cardNum);
        qttNum = findViewById(R.id.qttNum);
        sp = findViewById(R.id.spinner);
        sp.setOnItemSelectedListener(this);

        Calendar currTime = Calendar.getInstance();
        hour = currTime.get(Calendar.HOUR_OF_DAY);

        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        promoList = new ArrayList<>();
        saletimeList = new ArrayList<>();
        checkSaleDate();
        checkMaxQTT();


        //Display Food Image
        RequestOptions options = new RequestOptions().centerCrop().dontAnimate().placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher);
        Glide
                .with(NewIndvOrder.this)
                .load(foodimage).apply(options).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                foodDisplay.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                foodDisplay.setVisibility(View.VISIBLE);
                return false;
            }
        })
                .into(foodDisplay);




        //Payment Type

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){

                    case R.id.fpx:


                        paymentType = "fpx";
                        break;

                    case R.id.cod:

                        paymentType = "cod";
                        break;

                }
            }
        });




        // to convert Date to String, use format method of SimpleDateFormat
        // class.
        String strDate = dateFormat.format(dt);

        menuDayD.setText(orderDate);
        menuTypeD.setText(menuType);

        final RadioGroup locationType = findViewById(R.id.radioType);

        locationType.clearCheck();
        locationType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.inasis) {

                    sp.setVisibility(View.VISIBLE);

                            if(userLocation.equalsIgnoreCase("UUM")) {

                                list = new ArrayList<>();

                                if(inasisID.equalsIgnoreCase("14") || inasisID.equalsIgnoreCase("15")) {

                                list.add("Near Bank Rakyat Office (Time: 6 PM)");
                                list.add("Near SME Bank Office (Time: 6 PM)");

                                }else{

                                    list.add("VMALL (Time: 6 PM)");
                                    list.add("Near Petronas Office (Time: 6 PM)");

                                }
                                adp = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, list);
                                adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                sp.setAdapter(adp);
                            }

                            else{

                                list = new ArrayList<>();

                                list.add("Unimap1 (Time: 7 PM - 9 PM)");
                                list.add("Unimap2 (Time: 7 PM - 9 PM)");

                                adp = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, list);
                                adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                sp.setAdapter(adp);
                            }
                    }
            }
        });

        final RadioGroup promoType = findViewById(R.id.radioPromo);

        promoType.clearCheck();
        promoType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == R.id.promo1) {

                    promo = sharedPreferences.getString(Config.PROMO, "Not Available");

                        if (promo.equalsIgnoreCase("YES")){



                            promoBtn.setEnabled(false);
                            Toast.makeText(getApplicationContext(), "Sorry. You already claim this promotion",
                                    Toast.LENGTH_LONG).show();
                            promoType.clearCheck();

                        }else{

                            claimPromo ="YES";
                        }
                    }
                else if(i == R.id.normal1){

                            claimPromo = "NO";

                }

            }});


        nextBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String myCard = cardNum.getText().toString().trim();
                final String myQtt = qttNum.getText().toString().trim();

                if (myQtt.equalsIgnoreCase("")|| myQtt.equalsIgnoreCase("0")){

                    Toast.makeText(getApplicationContext(), "Please enter minimum 1 order",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    result = Double.parseDouble(myQtt);
                }

             if (locat.equalsIgnoreCase( "0")) {
                    Toast.makeText(getApplicationContext(), "Please select pick-up location",
                            Toast.LENGTH_LONG).show();
                } else if (result < 1) {
                    Toast.makeText(getApplicationContext(), "Please enter minimum 1 order",
                            Toast.LENGTH_LONG).show();
                }
                else if(promoType.getCheckedRadioButtonId()==-1)
                {
                    Toast.makeText(getApplicationContext(), "Please select promotion type", Toast.LENGTH_SHORT).show();
                }
                else if(locationType.getCheckedRadioButtonId()==-1)
                {
                    Toast.makeText(getApplicationContext(), "Please select pick-up point", Toast.LENGTH_SHORT).show();
                }
                else if (result > maxQTT && claimPromo.equalsIgnoreCase("NO")) {
                    Toast.makeText(getApplicationContext(),
                            "Cannot order more than "+ maxQTT +" unit. Maximum allowed per order reached",
                            Toast.LENGTH_LONG).show();
                }                else if (result > 1&& claimPromo.equalsIgnoreCase("YES")) {
                    Toast.makeText(getApplicationContext(),
                            "Cannot order more than 1 unit. Maximum allowed per promotion reached",
                            Toast.LENGTH_LONG).show();
                }


                else

                    try {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewIndvOrder.this);
                        alertDialogBuilder.setTitle(getString(R.string.txt_confirm));
                        alertDialogBuilder.setMessage(getString(R.string.txt_tnc));

                        final Dialog dialog = new Dialog(NewIndvOrder.this);

                        alertDialogBuilder.setPositiveButton(getString(R.string.btn_yes),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        dialog.setCanceledOnTouchOutside(true);

                                        final String newpromoID = sharedPreferences.getString(Config.PROMO_ID,"0");


                                        try {
                                            if ("none".equalsIgnoreCase(paymentType)){
                                                Toast.makeText(NewIndvOrder.this, "Choose Payment Type", Toast.LENGTH_LONG)
                                                        .show();

                                            }
                                            else if (Global.getInt(getContentResolver(), Global.AUTO_TIME) == 0) {

                                                Toast.makeText(getApplicationContext(),
                                                        "Please set Automatic Date & Time to ON in the Settings",
                                                        Toast.LENGTH_LONG).show();

                                                startActivityForResult(
                                                        new Intent(Settings.ACTION_DATE_SETTINGS), 0);
                                            } else if (Global.getInt(getContentResolver(),
                                                    Global.AUTO_TIME_ZONE) == 0) {

                                                Toast.makeText(getApplicationContext(),
                                                        "Please set Automatic Time Zone to ON in the Settings",
                                                        Toast.LENGTH_LONG).show();

                                                startActivityForResult(
                                                        new Intent(Settings.ACTION_DATE_SETTINGS), 0);
                                            }else {

                                                totalprice1 = foodprice1*result;

                                                if (claimPromo.equalsIgnoreCase("YES")) {

                                                    totalprice1 = 0;
                                                }


                                                if ("cod".equalsIgnoreCase(paymentType)){

                                                    final ProgressDialog loading = ProgressDialog.show(NewIndvOrder.this,"Please Wait","Contacting Server",false,false);


                                                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                                            NEW_CONFIRMORDER_URL, new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {

                                                            loading.dismiss();

                                                            if(response.contains("Successfully Order")){

                                                                Toast.makeText(NewIndvOrder.this, "Order success. Thank you", Toast.LENGTH_LONG)
                                                                        .show();

                                                                pushDeliverer();

                                                            }
                                                            else if(response.contains("Could not order")) {

                                                                Toast.makeText(NewIndvOrder.this, "Could not order. Please try again", Toast.LENGTH_LONG)
                                                                        .show();
                                                            }
                                                        }
                                                    }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            loading.dismiss();
                                                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                                                Toast.makeText(NewIndvOrder.this,"No internet . Please check your connection",
                                                                        Toast.LENGTH_LONG).show();
                                                            }
                                                            else{

                                                                Toast.makeText(NewIndvOrder.this, error.toString(), Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    }) {
                                                        @Override
                                                        protected Map<String, String> getParams() {
                                                            Map<String, String> params = new HashMap<String, String>();
                                                            params.put(KEY_MENUTYPE, menuType);
                                                            params.put(KEY_MENUDAY, menuDay);
                                                            params.put(KEY_ORDER_DATE, orderDate);
                                                            params.put(KEY_ORDER_TIME, orderTime);
                                                            params.put(KEY_CARDNUM, myCard);
                                                            params.put(KEY_MENUQTT, myQtt);
                                                            params.put(KEY_MENUSTATUS, myStatus);
                                                            params.put(KEY_LOCATION, locat);
                                                            params.put(KEY_USERID, userID);
                                                            params.put(KEY_FOODID, foodID);
                                                            params.put("totalPrice", df.format(totalprice1));
                                                            params.put("orderLocation", userLocation);
                                                            params.put("promoID", newpromoID);
                                                            params.put("claimpromo", claimPromo);
                                                            return params;
                                                        }

                                                    };

                                                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                            30000,
                                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                                    requestQueue.add(stringRequest);

                                                    emailer();
                                                    notifyMe();



                                                }else if ("fpx".equalsIgnoreCase(paymentType)){

                                                    if (totalprice1 <= 0){

                                                        Toast.makeText(NewIndvOrder.this,"Please choose COD for free items",
                                                                Toast.LENGTH_LONG).show();

                                                    }else {

                                                        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME,
                                                                Context.MODE_PRIVATE);

                                                        // Creating editor to store values to shared preferences
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                                        // Adding values to editor
                                                        editor.putString(Config.FOOD_PRICE_ALL, df.format(totalprice1));

                                                        // Saving values to editor
                                                        editor.commit();

                                                        Intent intent = new Intent(NewIndvOrder.this, WebViewPayment.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }

                                            }

                                        } catch (SettingNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                });

                        alertDialogBuilder.setNegativeButton(getString(R.string.btn_no),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        dialog.setCanceledOnTouchOutside(true);

                                    }
                                });
                        alertDialogBuilder.setOnCancelListener(
                                new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {

                                    }
                                }
                        );

                        //Showing the alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent i = new Intent(NewIndvOrder.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

        /*
         * staffOrder.setOnClickListener(new View.OnClickListener() { public
         * void onClick(View view) {
         *
         * Intent i = new Intent(IndvOrder.this, StaffOrder.class);
         * i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(i);
         * finish(); } });
         */

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        locat = parent.getSelectedItem().toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + locat, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        Toast.makeText(arg0.getContext(), "Please Select Your Pickup Location", Toast.LENGTH_LONG).show();

    }

    @TargetApi(24)
    public void notifyMe() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Order Notifications")
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Your order has been recorded \n\nPlease pay during delivery. Check your email for order details"))
                .setContentText("Your order has been recorded");

        Intent notifyIntent = new Intent(this, MainActivity.class);
        // Set the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Create the PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(notifyPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());

    }

    public void emailer() {

        final String myQtt = qttNum.getText().toString().trim();
        final ProgressDialog loading = ProgressDialog.show(NewIndvOrder.this,"Please Wait","Contacting Server",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EMAIL_IDV_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(NewIndvOrder.this,"No internet . Please check your connection",
                            Toast.LENGTH_LONG).show();
                }
                else{

                    Toast.makeText(NewIndvOrder.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_MENUTYPE, menuType);
                params.put(KEY_MENUDAY, menuDay);
                params.put(KEY_ORDER_DATE, orderDate);
                params.put(KEY_ORDER_TIME, orderTime);
                params.put(KEY_PHONE, phoneID);
                params.put(KEY_NAME, nameID);
                params.put(KEY_MENUQTT, myQtt);
                params.put(KEY_MENUSTATUS, myStatus);
                params.put(KEY_LOCATION, locat);
                params.put(KEY_EMAIL, emailID);
                params.put("totalPrice", df.format(totalprice1));
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void checkMaxQTT(){

        final ProgressDialog loading = ProgressDialog.show(this,"Please Wait","Contacting Server",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Config.URL_CHECKMAXQTT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                loading.dismiss();
                maxQTT = Integer.valueOf(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loading.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(NewIndvOrder.this,
                            "No internet. Please check your connection",
                            Toast.LENGTH_LONG).show();
                }else{

                    Toast.makeText(NewIndvOrder.this,
                            error.toString(),
                            Toast.LENGTH_LONG).show();
                }
            }
        })
                ;

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    public void checkPromo(){
        final ProgressDialog loading = ProgressDialog.show(this,"Please Wait","Contacting Server",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Config.URL_CHECKPROMOQTT+userLocation+"&foodID="+foodID, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject promo = array.getJSONObject(i);

                                //adding the product to product list
                                promoList.add(new Promo(
                                        promoID = promo.getInt("promoID"),
                                        promoQTT = promo.getString("promoQTT"),
                                        promoName = promo.getString("promoName")

                                ));

                            }

                            promoBtn.setText(promoName);

                            int promo_qtt = Integer.valueOf(promoQTT);

                            if(loggedIn==true){

                                if (promo_qtt > 5){

                                    promoBtn.setEnabled(true);

                                }else{

                                    Toast.makeText(NewIndvOrder.this,"No promotion for this menu. Please check in the future",
                                            Toast.LENGTH_LONG).show();
                                    promoBtn.setText("Offer Not Available");
                                    promoBtn.setEnabled(false);
                                }

                                checkPromoUser();
                            }


                            //add shared preference ID,nama,credit here
                            SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME,
                                    Context.MODE_PRIVATE);

                            // Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            // Adding values to editor

                            editor.putString(Config.PROMO_ID, String.valueOf(promoID));
                            editor.putString(Config.PROMO_QTT, promoQTT);

                            // Saving values to editor
                            editor.commit();

                            loading.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(NewIndvOrder.this,"No internet . Please check your connection",
                                    Toast.LENGTH_LONG).show();
                        }
                        else{

                            Toast.makeText(NewIndvOrder.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //adding our stringrequest to queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void checkPromoUser(){
        //Getting values from edit texts
        final ProgressDialog loading = ProgressDialog.show(this,"Please Wait","Contacting Server",false,false);
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CHECK_USERPROMO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loading.dismiss();
                        //If we are getting success from server
                        if(response.equalsIgnoreCase("Success")){
                            //Creating a shared preference
                            SharedPreferences sharedPreferences = NewIndvOrder.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor
                            editor.putString(Config.PROMO, "YES");

                            //Saving values to editor
                            editor.commit();


                        }else{
                            //If the server response is not success
                            //Displaying an error message on toast

                            SharedPreferences sharedPreferences = NewIndvOrder.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor
                            editor.putString(Config.PROMO, "NO");

                            //Saving values to editor
                            editor.commit();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        loading.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(NewIndvOrder.this,"No internet . Please check your connection",
                                    Toast.LENGTH_LONG).show();
                        }
                        else{

                            Toast.makeText(NewIndvOrder.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }){


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                //Adding parameters to request
                params.put("promoID", String.valueOf(promoID));
                params.put("userID", userID);

                //returning parameter
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void checkSaleDate(){
        final ProgressDialog loading = ProgressDialog.show(this,"Please Wait","Contacting Server",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Config.URL_CHECKSALEDATE+foodID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //converting the string to json array object
                    JSONArray array = new JSONArray(response);

                    //traversing through all the object
                    for (int i = 0; i < array.length(); i++) {

                        //getting product object from json array
                        JSONObject saletime = array.getJSONObject(i);

                        //adding the product to product list
                        saletimeList.add(new SaleTime(
                                saletimeID = saletime.getInt("saletimeID"),
                                saleStart = saletime.getInt("saleStart"),
                                saleEnd = saletime.getInt("saleEnd")

                        ));

                    }
                    if ( saleStart == saleEnd)
                    {
                        Toast.makeText(NewIndvOrder.this,"Available Soon. In the meantime, please order other menu",
                                Toast.LENGTH_LONG).show();

                        Intent i = new Intent(NewIndvOrder.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();

                    }
                    else if (!(hour >= saleStart && hour < saleEnd))
                    {
                       Toast.makeText(NewIndvOrder.this,"Order close now. You can start ordering from "+saleStart+":00 until "+saleEnd+":00",
                              Toast.LENGTH_LONG).show();

                        Intent i = new Intent(NewIndvOrder.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();

                    }else{

                        checkPromo();

                    }

                    loading.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(NewIndvOrder.this,"No internet . Please check your connection",
                                    Toast.LENGTH_LONG).show();
                        }
                        else{

                            Toast.makeText(NewIndvOrder.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //adding our stringrequest to queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void pushDeliverer() {

        final ProgressDialog loading = ProgressDialog.show(NewIndvOrder.this,"Please Wait","Contacting Server",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.PUSH_NOTI_DELIVERER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                            loading.dismiss();

                        Intent i = new Intent(NewIndvOrder.this, OrderPage.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(NewIndvOrder.this,"No internet . Please check your connection",
                                    Toast.LENGTH_LONG).show();
                        }
                        else{

                            Toast.makeText(NewIndvOrder.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //adding our stringrequest to queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
