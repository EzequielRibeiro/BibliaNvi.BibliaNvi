package com.projeto.biblianvi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.Layout;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.TextViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.projeto.biblianvi.biblianvi.R;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class MainActivity extends AppCompatActivity {


    static public String PACKAGENAME;
    static public String DATABASENAME;
    static public String VERSIONAPP;
    static public String MESSAGE_KEY = "msg";
    static private SharedPreferences sharedPrefDataBasePatch;
    static private SharedPreferences.Editor editor;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] menuTitulos;
    private BibliaBancoDadosHelper bibliaHelp;
    private Button button_sermon, buttonClock, button_biblia, button_dicionario, button_pesquisar;
    private ProgressDialog progressDialog;
    private Intent intent;
    private ListView listView;
    private AdView mAdView;
    private int REQUEST_STORAGE = 200;
    private TextView textViewAssuntoVers;
    private TextView textViewVersDia;
    private TextView textViewDeveloper, textViewDailyVerse;
    private FirebaseAnalytics mFirebaseAnalytics;
    private TextView text_qualificar;
    private LinearLayout layout_qualificar;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private Toolbar toolbar;
    private ReviewManager reviewManager;
    private com.google.android.play.core.tasks.Task<ReviewInfo> request;
    private com.google.android.play.core.tasks.Task<Void> flow;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);
        getSharedPreferences("brilhoAtual", Activity.MODE_PRIVATE).edit().putInt("brilhoAtualValor", Lista_Biblia.getScreenBrightness(getApplicationContext())).commit();
        PACKAGENAME = getPackageName();
        sharedPrefDataBasePatch = getSharedPreferences("DataBase", Context.MODE_PRIVATE);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config);


        mTitle = mDrawerTitle = getTitle();
        menuTitulos = getResources().getStringArray(R.array.menu_array);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);
        textViewDeveloper = findViewById(R.id.textViewDeveloper);
        textViewDeveloper.setTextColor(getResources().getColor(R.color.dark));
        textViewDeveloper.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textViewDeveloper.setText("");
        // set a custom shadow that overlays the activity_fragment content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, menuTitulos));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            VERSIONAPP = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException exception) {
            exception.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(exception);
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
            }

            public void onDrawerOpened(View drawerView) {
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        bibliaHelp = new BibliaBancoDadosHelper(this);

        listView = findViewById(R.id.listView);

        buttonClock = findViewById(R.id.buttonClock);

        textViewAssuntoVers = findViewById(R.id.textViewAssuntoVers);
        textViewAssuntoVers.setText("");
        textViewVersDia = findViewById(R.id.textViewVersDia);

        layout_qualificar = findViewById(R.id.layout_qualificar);
        button_sermon = findViewById(R.id.buttonSermon);
        button_biblia = findViewById(R.id.button_biblia);
        button_dicionario = findViewById(R.id.button_dicionario);
        button_pesquisar = findViewById(R.id.button_pesquisar);
        text_qualificar = findViewById(R.id.text_qualificar);
        text_qualificar.setText(getString(R.string.gostou_do_nosso_app));
        textViewDailyVerse = findViewById(R.id.textViewDailyVerse);
        textViewDailyVerse.setGravity(Gravity.CENTER);


        button_sermon.setText(getString(R.string.devocional));
        button_sermon.setMaxLines(1);
        button_sermon.setBackground(getDrawable(R.drawable.button_sermon_custom));
        TextViewCompat.setAutoSizeTextTypeWithDefaults(button_sermon, TextView.AUTO_SIZE_TEXT_TYPE_NONE);
        button_biblia.setText(getString(R.string.biblia));
        button_biblia.setMaxLines(1);
        button_biblia.setBackground(getDrawable(R.drawable.button_biblia_custom));
        TextViewCompat.setAutoSizeTextTypeWithDefaults(button_biblia, TextView.AUTO_SIZE_TEXT_TYPE_NONE);
        button_dicionario.setText(getString(R.string.dicionario));
        button_dicionario.setMaxLines(1);
        button_dicionario.setBackground(getDrawable(R.drawable.button_dicionario_custom));
        TextViewCompat.setAutoSizeTextTypeWithDefaults(button_dicionario, TextView.AUTO_SIZE_TEXT_TYPE_NONE);
        button_pesquisar.setText(getString(R.string.pesquisar));
        button_pesquisar.setMaxLines(1);
        button_pesquisar.setBackground(getDrawable(R.drawable.button_search_custom));

        textViewVersDia.setBackground(getDrawable(R.drawable.background_borders));

        TextViewCompat.setAutoSizeTextTypeWithDefaults(button_pesquisar, TextView.AUTO_SIZE_TEXT_TYPE_NONE);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(textViewDailyVerse, TextView.AUTO_SIZE_TEXT_TYPE_NONE);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(textViewAssuntoVers, TextView.AUTO_SIZE_TEXT_TYPE_NONE);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(textViewVersDia, TextView.AUTO_SIZE_TEXT_TYPE_NONE);

        text_qualificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                rateApp();
            }
        });

        layout_qualificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                rateApp();
            }
        });

        button_sermon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable(getApplicationContext())) {
                    intent = new Intent(MainActivity.this, Sermoes.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplication(), "Sem conexão", Toast.LENGTH_LONG).show();
                }
            }
        });
        /*
        textViewVersDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bibliaHelp != null) {
                    //  bibliaHelp.versDoDiaText(textViewAssuntoVers, textViewVersDia,false);
                }
            }
        });
        */

        buttonClock.setBackgroundResource(R.mipmap.alarm_clock);
        buttonClock.setText("");
        buttonClock.setPadding(0, 0, 5, 0);
        buttonClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterarHoraAlarme();
            }
        });


        button_biblia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isDataBaseDownload(getApplicationContext())) {
                    Intent i = new Intent();
                    i.setClass(MainActivity.this, MainActivityFragment.class);
                    i.putExtra("Biblia", "biblia");
                    startActivity(i);
                } else {
                    downloadDataBaseBible();
                }
            }
        });


        button_dicionario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                opcaoDicionario(getApplicationContext());

            }
        });

        button_pesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isDataBaseDownload(getApplicationContext())) {
                    startActivity(new Intent(MainActivity.this, Activity_busca_avancada.class));
                } else {
                    downloadDataBaseBible();
                    Log.e("button", "else");
                }
            }
        });

        // MobileAds.initialize(this, getString(R.string.ADMOB_APP_ID));
        mAdView = findViewById(R.id.adView);
        mAdView.setVisibility(View.GONE);
        final AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // MobileAds.setRequestConfiguration(new RequestConfiguration.Builder().setTestDeviceIds(Collections.singletonList("4CCDC45D57519669CA4C587B6E896BE8")).build());
                mAdView.loadAd(adRequest);
            }
        }, 500);

        mAdView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.layout_container);
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(R.id.layout_escolha_livro, ConstraintSet.BOTTOM, R.id.layout_qualificar, ConstraintSet.TOP, 0);
                    constraintSet.applyTo(constraintLayout);

                }


                // AdRequest.ERROR_CODE_NO_FILL)
                Log.i("admob", String.valueOf(errorCode));
                Bundle bundle = new Bundle();
                bundle.putString("ERRORCODE", String.valueOf(errorCode));
                bundle.putString("COUNTRY", getResources().getConfiguration().locale.getDisplayCountry());
                mFirebaseAnalytics.logEvent("ADMOB", bundle);


            }


        });


        Log.e("Banco:", Boolean.toString(isDataBaseDownload(getApplicationContext())));

        editor = sharedPrefDataBasePatch.edit();
        editor.putString("language", Locale.getDefault().getLanguage());
        editor.commit();


        SharedPreferences settings = getSharedPreferences("seekbar", Activity.MODE_PRIVATE);
        editor = settings.edit();
        editor.putInt("brilhoAtual", (Lista_Biblia.getScreenBrightness(getApplicationContext())));
        editor.commit();

        if (!isDataBaseDownload(getApplicationContext())) {
            downloadDataBaseBible();
        }

        int rated = getSharedPreferences("rated", MODE_PRIVATE).getInt("time", 1);
        getSharedPreferences("rated", MODE_PRIVATE).edit().putInt("time", rated + 1).commit();

        if (rated == 3) {
            if (isNetworkAvailable(this))
                showRequestRateApp();
            else getSharedPreferences("rated", MODE_PRIVATE).edit().putInt("time", 0).commit();
        } else if (rated == 50) {
            getSharedPreferences("rated", MODE_PRIVATE).edit().putInt("time", 0).commit();
        }

    }

    private void showRequestRateApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Feedback");
        builder.setMessage(getString(R.string.gostou_do_nosso_app));
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                rateApp();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getSharedPreferences("rated", MODE_PRIVATE).edit().putInt("time", 0).commit();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void rateApp() {
        reviewManager = ReviewManagerFactory.create(this);
        //    reviewManager = new FakeReviewManager(this);
        request = reviewManager.requestReviewFlow();

        request.addOnCompleteListener(new com.google.android.play.core.tasks.OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(com.google.android.play.core.tasks.Task<ReviewInfo> task) {
                if (task.isSuccessful()) {
                    ReviewInfo reviewInfo = task.getResult();
                    com.google.android.play.core.tasks.Task<Void> flow = reviewManager.launchReviewFlow(MainActivity.this, reviewInfo);
                    flow.addOnCompleteListener(new com.google.android.play.core.tasks.OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(com.google.android.play.core.tasks.Task<Void> task) {
                            //  Toast.makeText(MainActivity.this,"Thank you !",Toast. LENGTH_SHORT).show();
                        }
                    });
                } else {
                    getSharedPreferences("rated", MODE_PRIVATE).edit().putInt("time", 0).commit();
                }
            }
        });

    }

    static public void openNoticias(Context applicationContext) {

        Intent intent = new Intent(applicationContext, ActivityBrowser.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);

        if (isNetworkAvailable(applicationContext)) {

            switch (Locale.getDefault().getLanguage()) {

                case "pt":
                    intent.putExtra("url", applicationContext.getString(R.string.url_noticias));
                    applicationContext.startActivity(intent);
                    break;
                case "es":
                    intent.putExtra("url", "https://www.bibliatodo.com/NoticiasCristianas");
                    applicationContext.startActivity(intent);
                    break;
                default:
                    intent.putExtra("url", "https://www.christianitytoday.com/ct/topics/a/assemblies-of-god");
                    applicationContext.startActivity(intent);
                    break;

            }


        } else
            Toast.makeText(applicationContext, R.string.sem_conexao, Toast.LENGTH_LONG).show();


    }

    static public boolean isDataBaseDownload(Context context) {

        File folderStorage;
        String folderDest = "Android/data/" + PACKAGENAME + "/databases/";
        editor = context.getSharedPreferences("DataBase", Context.MODE_PRIVATE).edit();

        switch (Locale.getDefault().getLanguage()) {

            case "pt":
                folderDest = folderDest + DownloadTask.Utils.DATABASE_NAME_PT;
                DATABASENAME = DownloadTask.Utils.DATABASE_NAME_PT;
                break;
            case "es":
                folderDest = folderDest + DownloadTask.Utils.DATABASE_NAME_ES;
                DATABASENAME = DownloadTask.Utils.DATABASE_NAME_ES;
                break;
            case "ru":
                folderDest = folderDest + DownloadTask.Utils.DATABASE_NAME_RU;
                DATABASENAME = DownloadTask.Utils.DATABASE_NAME_RU;
                break;
            case "zh":
                folderDest = folderDest + DownloadTask.Utils.DATABASE_NAME_ZH;
                DATABASENAME = DownloadTask.Utils.DATABASE_NAME_ZH;
                break;
            case "hi":
                folderDest = folderDest + DownloadTask.Utils.DATABASE_NAME_HI;
                DATABASENAME = DownloadTask.Utils.DATABASE_NAME_HI;
                break;
            default:
                folderDest = folderDest + DownloadTask.Utils.DATABASE_NAME_EN;
                DATABASENAME = DownloadTask.Utils.DATABASE_NAME_EN;
                break;
        }

        //Get File if SD card is present
        if (new DownloadTask.CheckForSDCard().isSDCardPresent()) {

            folderStorage = new File(
                    Environment.getExternalStorageDirectory() + "/"
                            + folderDest);

            //If File is not present create directory
            if (folderStorage.exists()) {
                editor.putString("dataBasePatch", folderStorage.getAbsolutePath());
                editor.commit();
                return true;
            } else {
                return false;
            }

        } else {

            folderStorage = new File(
                    Environment.getDataDirectory() + "/"
                            + folderDest);

            if (folderStorage.exists()) {
                editor.putString("dataBasePatch", folderStorage.getAbsolutePath());
                editor.commit();
                return true;
            } else {
                return false;
            }
        }

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    static public void opcaoDicionario(Context context) {

        Intent intent = new Intent(context, ActivityBrowser.class);

        switch (Locale.getDefault().getLanguage()) {

            case "pt":
                if (isDataBaseDownload(context)) {
                    context.startActivity(new Intent(context, DicionarioActivity.class)
                            .setFlags(FLAG_ACTIVITY_NEW_TASK));
                }
                break;
            case "es":
                intent.putExtra("url", "https://dle.rae.es/biblia");
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            default:
                intent.putExtra("url", "https://www.kingjamesbibleonline.org/Free-Bible-Dictionary.php");
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
        }


    }


    @SuppressLint("WrongConstant")
    private void runDownloadFromDownloadTask() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ProgressBar progressBar = new ProgressBar(MainActivity.this, null, android.R.attr.progressBarStyleHorizontal);
            progressBar.setIndeterminate(false);
            progressBar.setMax(100);
            progressBar.setScaleY(5);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setLayoutParams(params);

            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView textView = new TextView(getApplicationContext());
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(16);
            textView.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
            params.setMargins(20, 0, 20, 20);
            textView.setLayoutParams(params);

            String smg = "<font color='red'>" + getString(R.string.app_name) + "</font>";
            smg = smg.concat("<br>" + getString(R.string.finished_install));
            textView.setText(Html.fromHtml(smg, Html.FROM_HTML_MODE_LEGACY));

            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setBackgroundColor(Color.BLACK);
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            linearLayout.setLayoutParams(params);
            linearLayout.addView(textView);
            linearLayout.addView(progressBar);
            FrameLayout frameLayout = findViewById(R.id.frame_layout_man);
            frameLayout.addView(linearLayout);
            if (isNetworkAvailable(this)) {
                new DownloadTask(getApplicationContext(), progressBar, frameLayout, linearLayout, sharedPrefDataBasePatch);
            } else {
                Toast.makeText(getApplicationContext(), R.string.sem_conexao, Toast.LENGTH_LONG).show();
            }

        } else {
            progressDialog = new ProgressDialog(MainActivity.this, R.style.ProgressBarStyle);
            progressDialog.setTitle(R.string.app_name);
            progressDialog.setMessage(getString(R.string.finished_install));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgressNumberFormat(null);
            progressDialog.setCancelable(false);
            progressDialog.setMax(100);
            progressDialog.show();
            if (isNetworkAvailable(this)) {
                new DownloadTask(getApplicationContext(), progressDialog, sharedPrefDataBasePatch);
            } else {
                Toast.makeText(getApplicationContext(), R.string.sem_conexao, Toast.LENGTH_LONG).show();
            }

        }
    }

    private void downloadDataBaseBible() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NOTIFICATION_POLICY) != PackageManager.PERMISSION_GRANTED) {

                //Show an explanation to the user *asynchronously*
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.msg_permission)
                        .setTitle(R.string.title_permission);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NOTIFICATION_POLICY}, REQUEST_STORAGE);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                builder.show();

            } else {
                runDownloadFromDownloadTask();
                Log.e("downs", "else");
            }

        } else {

            runDownloadFromDownloadTask();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {

            if (requestCode == REQUEST_STORAGE) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!isDataBaseDownload(getApplicationContext())) {
                        if (isNetworkAvailable(this)) {
                            runDownloadFromDownloadTask();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.not_internet_avaliable, Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_NOTIFICATION_POLICY)) {

                    }
                }
            }

        } catch (ArrayIndexOutOfBoundsException exception) {
            exception.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(exception);

        }

    }

    public void compartilharVers(View v) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textViewVersDia.getText().toString().concat('\n' + getString(R.string.app_name)));
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.compartilhar)));


    }

    public boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.projeto.biblianvi.ServiceNotification".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private boolean checarAlarmeExiste() {

      /*  boolean alarmUp = (PendingIntent.getBroadcast(MainActivity.this, 121312131,
                new Intent("com.projeto.biblianvi.VersiculoDiario"),
                PendingIntent.FLAG_NO_CREATE) != null);*/
        Intent tempIntent = new Intent(MainActivity.this, VersiculoDiario.class);
        tempIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        boolean alarmUp = (PendingIntent.getBroadcast(MainActivity.this, 101015, tempIntent, PendingIntent.FLAG_NO_CREATE) != null);


        if (alarmUp)
            Log.e("alarme ", "ativado");
        else {
            Log.e("alarme ", "desativado");
        }
        return alarmUp;

    }

    private void cancelarAgendarAlarmeVersiculo() {


        Intent intent = new Intent("com.projeto.biblianvi.VersiculoDiario");
        AlarmManager alarmManager =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent =
                PendingIntent.getService(MainActivity.this, 101015, intent,
                        PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }


    }

    private void agendarAlarmeVersiculo() {

        SharedPreferences settings = getSharedPreferences("alarme", Activity.MODE_PRIVATE);

        if (!settings.contains("hora") || !settings.contains("minuto")) {
            editor = getSharedPreferences("alarme", Activity.MODE_PRIVATE).edit();
            editor.putString("hora", "10");
            editor.putString("minuto", "30");
            editor.commit();
        }

        Intent it = new Intent(this, VersiculoDiario.class);
        PendingIntent p = PendingIntent.getBroadcast(MainActivity.this, 101015, it, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        int h = Integer.parseInt(settings.getString("hora", "10"));
        int m = Integer.parseInt(settings.getString("minuto", "30"));

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY, h);
        c.set(Calendar.MINUTE, m);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, p);

    }

    private void alterarHoraAlarme() {


        SharedPreferences settings = getSharedPreferences("alarme", Activity.MODE_PRIVATE);
        final SharedPreferences.Editor editor = settings.edit();

        String h = settings.getString("hora", "10");
        String m = settings.getString("minuto", "30");

        AlterarAlarm alterarAlarm;

        TextView title = new TextView(this);
        title.setText(R.string.confirmar_alarme_aviso);
        title.setTextColor(getResources().getColor(R.color.white));
        title.setPadding(5, 5, 5, 5);
        title.setGravity(View.TEXT_ALIGNMENT_CENTER);
        // title.setTextColor(getResources().getColor(R.color.greenBG));
        title.setTextSize(18);

        final TextView horaText = new TextView(this);
        horaText.setTextColor(getResources().getColor(R.color.blue));
        // horaText.setBackgroundColor(getResources().getColor(R.color.white));
        horaText.setText(h);
        horaText.setTextSize(22);

        //altura comprimento
        LinearLayout layoutTextHora = new LinearLayout(this);
        layoutTextHora.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layoutTextHora.setGravity(Gravity.CENTER);
        layoutTextHora.addView(horaText);

        final TextView minText = new TextView(this);
        minText.setTextColor(getResources().getColor(R.color.blue));
        // horaText.setBackgroundColor(getResources().getColor(R.color.white));
        minText.setText(m);
        minText.setTextSize(22);

        alterarAlarm = new AlterarAlarm(horaText, minText);

        LinearLayout layoutTextMin = new LinearLayout(this);
        layoutTextMin.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layoutTextMin.setGravity(Gravity.CENTER);
        layoutTextMin.addView(minText);

        Button horaMaisButton = new Button(this);
        horaMaisButton.setTag("horaMaisButton");
        horaMaisButton.setBackgroundResource(R.mipmap.img_mais);
        horaMaisButton.setLayoutParams(new LinearLayout.LayoutParams(50, 40));
        horaMaisButton.setOnClickListener(alterarAlarm);

        Button horaMenosButton = new Button(this);
        horaMenosButton.setTag("horaMenosButton");
        horaMenosButton.setLayoutParams(new LinearLayout.LayoutParams(50, 40));
        horaMenosButton.setBackgroundResource(R.mipmap.img_menos);
        horaMenosButton.setOnClickListener(alterarAlarm);

        Button minMaisButton = new Button(this);
        minMaisButton.setTag("minMaisButton");
        minMaisButton.setLayoutParams(new LinearLayout.LayoutParams(50, 40));
        minMaisButton.setBackgroundResource(R.mipmap.img_mais);
        minMaisButton.setOnClickListener(alterarAlarm);

        Button minMenosButton = new Button(this);
        minMenosButton.setTag("minMenosButton");
        minMenosButton.setLayoutParams(new LinearLayout.LayoutParams(50, 40));
        minMenosButton.setBackgroundResource(R.mipmap.img_menos);
        minMenosButton.setOnClickListener(alterarAlarm);

        LinearLayout horaLayout = new LinearLayout(this);
        horaLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 15, 0);

        TextView txtHora = new TextView(this);
        txtHora.setText(R.string.hora);
        txtHora.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        txtHora.setTextColor(getResources().getColor(R.color.white));

        horaLayout.setLayoutParams(params);
        horaLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        horaLayout.addView(txtHora);
        horaLayout.addView(horaMaisButton);
        horaLayout.addView(layoutTextHora);
        horaLayout.addView(horaMenosButton);


        LinearLayout minLayout = new LinearLayout(this);
        minLayout.setOrientation(LinearLayout.VERTICAL);
        minLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        minLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView txtMin = new TextView(this);
        txtMin.setText(R.string.minuto);
        txtMin.setTextColor(getResources().getColor(R.color.white));
        txtMin.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        minLayout.addView(txtMin);
        minLayout.addView(minMaisButton);
        minLayout.addView(layoutTextMin);
        minLayout.addView(minMenosButton);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 30, 15, 30);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.HORIZONTAL);
        content.setGravity(Gravity.CENTER);
        content.setBackgroundColor(getResources().getColor(R.color.dark));
        content.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        horaLayout.setLayoutParams(layoutParams);
        minLayout.setLayoutParams(layoutParams);
        content.addView(horaLayout);
        content.addView(minLayout);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);

        alertDialogBuilder.setView(content);
        alertDialogBuilder.setCustomTitle(title);


        // set dialog message
        alertDialogBuilder.setPositiveButton(R.string.redefinir, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                editor.putString("hora", horaText.getText().toString());
                editor.putString("minuto", minText.getText().toString());
                editor.commit();

                if (checarAlarmeExiste()) {
                    cancelarAgendarAlarmeVersiculo();
                }
                agendarAlarmeVersiculo();

                Toast.makeText(MainActivity.this, getString(R.string.hora_redefinida)
                                + horaText.getText().toString() + ":"
                                + minText.getText().toString() + "h"
                        , Toast.LENGTH_LONG).show();

            }
        });

        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();


    }

    private void versiculoDoDia() throws ParseException {

        SharedPreferences settings;
        settings = getSharedPreferences("versDiaPreference", Activity.MODE_PRIVATE);
        textViewAssuntoVers.setText(settings.getString("assunto", getString(R.string.peace)));
        textViewAssuntoVers.setMinLines(2);
        textViewAssuntoVers.setTextColor(Color.BLACK);
        //disabling textViewAssuntoVers
        ViewGroup.LayoutParams layoutparams;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            layoutparams = (ConstraintLayout.LayoutParams) textViewAssuntoVers.getLayoutParams();

        } else {

            layoutparams = (LinearLayout.LayoutParams) textViewAssuntoVers.getLayoutParams();

        }
        layoutparams.height = 0;
        textViewAssuntoVers.setLayoutParams(layoutparams);
        //disabling
        textViewVersDia.setText(Html.fromHtml("<font color='yellow'>" + settings.getString("assunto", getString(R.string.peace)) + "</font><br>" + settings.getString("versDia", getString(R.string.versiculo_text))
                + "<br>(" + settings.getString("livroNome", getString(R.string.book_name)) + " " +
                settings.getString("capVersDia", getString(R.string.capitulo_number)) + ":"
                + settings.getString("verVersDia", getString(R.string.versiculo_number)) + ")"));

    }

    protected void onStop() {
        super.onStop();
    }

    protected void onStart() {
        super.onStart();

        mFirebaseRemoteConfig.fetch(3600)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (!mFirebaseRemoteConfig.getString(MESSAGE_KEY).equals("not")) {

                                textViewDailyVerse.setTextColor(getResources().getColor(R.color.red));
                                textViewDailyVerse.setMovementMethod(LinkMovementMethod.getInstance());
                                textViewDailyVerse.setText(Html.fromHtml(mFirebaseRemoteConfig.getString(MESSAGE_KEY)));

                            }
                            mFirebaseRemoteConfig.activateFetched();
                            Log.e("RemoteConig: ", "valor: " + mFirebaseRemoteConfig.getString(MESSAGE_KEY));
                        } else {

                            Log.e("RemoteConig: ", Boolean.toString(task.isSuccessful()));

                        }

                    }
                });


    }

    protected void onPostResume() {
        super.onPostResume();
    }

    protected void onResume() {
        super.onResume();

        try {
            if (isDataBaseDownload(getApplicationContext())) {
                versiculoDoDia();
                textViewDeveloper.setText(getString(R.string.total_lido) + " " +
                        String.format("%.2f", GraficoGeral.quantVersosLidos(getApplicationContext())) + "%");

                if (!checarAlarmeExiste())
                    agendarAlarmeVersiculo();

            }
        } catch (ParseException exception) {
            exception.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(exception);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    public void onBackPressed() {
        super.onBackPressed();
        return;
    }

    protected void onPause() {

        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_websearch:
                // create intent to perform web search for this planet
                // Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                // intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
                // catch event that there's no activity to handle intent

                if (isDataBaseDownload(getApplicationContext())) {
                    Intent intent1 = new Intent();
                    intent1.setClass(getApplication(), Activity_busca_avancada.class);
                    startActivity(intent1);
                } else {
                    downloadDataBaseBible();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    private void selectItem(int position) {
        // update the activity_fragment content by replacing fragments
        Fragment fragment = new MenuLateralTeste.PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(MenuLateralTeste.PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        // fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        // setTitle(menuTitulos[position]);
        switch (position) {
            case 0:
                if (isDataBaseDownload(getApplicationContext())) {
                    intent = new Intent(MainActivity.this, Activity_favorito.class);
                    startActivity(intent);
                } else {
                    downloadDataBaseBible();
                }
                break;
            case 1:
                if (isDataBaseDownload(getApplicationContext())) {
                    intent = new Intent(MainActivity.this, ActivityAnotacao.class);
                    startActivity(intent);
                } else {
                    downloadDataBaseBible();
                }
                break;
            case 2:
                opcaoDicionario(getApplicationContext());
                break;
            case 3:
                if (isNetworkAvailable(this)) {
                    intent = new Intent(MainActivity.this, Sermoes.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplication(), "Sem conexão", Toast.LENGTH_LONG).show();
                }
                break;
            case 4:
                if (isDataBaseDownload(getApplicationContext())) {
                    intent = new Intent(MainActivity.this, GraficoGeral.class);
                    startActivity(intent);
                } else {
                    downloadDataBaseBible();
                }
                break;
            case 5:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case 6:
                intent = new Intent(MainActivity.this, ActivityPoliticaPrivacidade.class);
                startActivity(intent);
                break;
            case 7:
                mostrarAviso();
                break;
            default:
                break;
        }
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void mostrarAviso() {

        TextView title = new TextView(this);
        title.setText("Informação");
        title.setPadding(5, 5, 5, 5);
        title.setGravity(View.TEXT_ALIGNMENT_CENTER);
        // title.setTextColor(getResources().getColor(R.color.greenBG));
        title.setTextSize(18);

        BibliaBancoDadosHelper db = new BibliaBancoDadosHelper(getApplicationContext());

        String t;
        TextView msg = new TextView(this);
        msg.setTextColor(getResources().getColor(R.color.white));

        String version = "1.0";
        try {
            version = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException exception) {
            exception.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(exception);
        }

        t = getString(R.string.aviso).replace("@app_version@", version);
        t = t.replace("@bible_version@", db.getBibleVersion());
        msg.setText(t);
        msg.setPadding(10, 10, 10, 10);
        msg.setGravity(View.TEXT_ALIGNMENT_CENTER);
        msg.setTextSize(18);


        ScrollView scrollView = new ScrollView(getApplicationContext());
        scrollView.setBackgroundColor(getResources().getColor(R.color.dark));
        scrollView.addView(msg);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);

        alertDialogBuilder.setView(scrollView);
        alertDialogBuilder.setCustomTitle(title);

        // set dialog message
        alertDialogBuilder.setPositiveButton(R.string.fechar_about, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String planet = getResources().getStringArray(R.array.menu_array)[i];

            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
                    "drawable", getActivity().getPackageName());
            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
            getActivity().setTitle(planet);
            return rootView;
        }
    }

    private class AlterarAlarm implements View.OnClickListener {

        TextView textViewhora, textViewMin;
        int hora, min;

        public AlterarAlarm(TextView horaView, TextView minView) {
            textViewhora = horaView;
            textViewMin = minView;
            hora = Integer.parseInt(horaView.getText().toString());
            min = Integer.parseInt(minView.getText().toString());
        }

        @Override
        public void onClick(View v) {


            if (v.getTag().toString().equals("horaMaisButton")) {

                if (hora <= 22) {
                    ++hora;
                    setHora(hora);
                } else {
                    hora = 0;
                    setHora(hora);
                }

            } else if (v.getTag().toString().equals("horaMenosButton")) {


                if (hora >= 1) {
                    --hora;
                    setHora(hora);
                } else {
                    hora = 23;
                    setHora(hora);
                }
            } else if (v.getTag().toString().equals("minMaisButton")) {

                if (min <= 58) {
                    ++min;
                    setMin(min);
                } else {
                    min = 0;
                    setMin(min);
                }
            } else if (v.getTag().toString().equals("minMenosButton")) {

                if (min >= 1) {
                    --min;
                    setMin(min);
                } else {

                    min = 59;
                    setMin(min);
                }

            }

        }

        private void setHora(int h) {

            if (h < 10)
                textViewhora.setText("0" + h);
            else
                textViewhora.setText(Integer.toString(h));

        }

        private void setMin(int m) {


            if (m < 10)
                textViewMin.setText("0" + m);
            else
                textViewMin.setText(Integer.toString(m));


        }


    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

}
