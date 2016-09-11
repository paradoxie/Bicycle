package com.paradoxie.bicycle.UI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.paradoxie.bicycle.R;
import com.paradoxie.bicycle.Util.HideIME.HideIMEUtil;
import com.paradoxie.bicycle.Util.TransManager;
import com.paradoxie.bicycle.Util.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEditText;
    private String mStringBefore, mStringAfter;
    private Button mButton_expalin, mButton_translate, mButton_url;
    private ProgressBar progressBar;
    private FrameLayout mFrameLayout;
    private MaterialSpinner spinner;
    private TransManager mTransManager = new TransManager();
    private Handler mHandler = new Handler();
    private static Boolean isExit = false;
    private int clickNum = 0;
    private String[] welcome_array;
    private String content;
    //百度生成短网址请求地址
    private static final String CREATE_SHORT_URL = "http://dwz.cn/create.php";
    //彩蛋1
    private static final String EGG_1 = "https://btso.pw/tags";
    private static final String EGG_2 = "http://dwz.cn/2wnhQ";


    //百度还原短网址请求地址private static final String QUERY_SHORT_URL = "http://dwz.cn/query.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HideIMEUtil.wrap(this);
        JPushInterface.init(getApplicationContext());
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBy10Click();
            }
        });
        setSupportActionBar(toolbar);
        mEditText = (EditText) findViewById(R.id.et_code);
        mEditText.setHint(getRandomWelcomeTips());

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            content = bundle.getString(JPushInterface.EXTRA_ALERT);
            mEditText.setText(content);
        }

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mEditText.getText().toString().equals("1024")) {
                    mEditText.setText("");//跳转打开绅士网
                    openEgg(EGG_2);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mFrameLayout = (FrameLayout) findViewById(R.id.main_framelayout);
        mFrameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                float startX = 0, offsetX;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        if (Math.abs(offsetX) > 100) {
                            mEditText.setText("");
                        }
                        break;
                }
                return true;
            }
        });
        mButton_expalin = (Button) findViewById(R.id.btn_explain);
        mButton_expalin.setOnClickListener(this);
        mButton_translate = (Button) findViewById(R.id.btn_translate);
        mButton_translate.setOnClickListener(this);
        mButton_url = (Button) findViewById(R.id.btn_url);
        mButton_url.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditText.getText().length() > 1) {
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(mEditText.getText());
                    Snackbar.make(view, R.string.copy, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(view, R.string.nothing_copy, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setItems(getString(R.string.iChina), getString(R.string.base_64), getString(R.string.morse_code));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.theme:
                item.setTitle(R.string.action_theme_2);
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                recreate();
                break;
            case R.id.share:
                Utils.share(this, "An exciting tool for old drivers!!项目地址:", "https://github.com/paradoxie/Bicycle");
                break;
            case R.id.about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
            case R.id.exit:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_explain://解码:密码-->明文
                mStringAfter = mEditText.getText().toString();
                if (!mStringAfter.isEmpty()) {
                    if (spinner.getText().equals("伏羲六十四卦")) {
                        if (Utils.isIChina(mStringAfter)) {
                            mStringBefore = mTransManager.ToCode(TransManager.iChina, mStringAfter);
                            mEditText.setText(mStringBefore);
                        } else {
                            Snackbar.make(view, R.string.sourse_error, Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        }
                    } else if (spinner.getText().equals("Base64")) {
                        if (Utils.isBase64(mStringAfter)) {
                            mStringBefore = TransManager.base64Decode2String(TransManager.base64Decode(mStringAfter));
                            mEditText.setText(mStringBefore);
                        } else {
                            Snackbar.make(view, R.string.sourse_error, Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        }
                    } else if (spinner.getText().equals("Morse码")) {
                        if (Utils.isMorse(mStringAfter)) {
                            mStringBefore = mTransManager.ToCode(TransManager.morseCode, mStringAfter);
                            mEditText.setText(mStringBefore);
                        } else {
                            Snackbar.make(view, R.string.sourse_error, Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        }
                    }

                } else {
                    Snackbar.make(view, R.string.empty_msg, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                break;
            case R.id.btn_translate://编码:明文-->密码
                mStringBefore = mEditText.getText().toString();
                if (Utils.isLink(mStringBefore)) {
                    if (spinner.getText().equals("伏羲六十四卦")) {
                        mStringAfter = mTransManager.codeTo(TransManager.iChina, mStringBefore);
                    } else if (spinner.getText().equals("Base64")) {
                        mStringAfter = TransManager.base64Encode2String(TransManager.base64Encode(mStringBefore));
                    } else if (spinner.getText().equals("Morse码")) {
                        mStringAfter = mTransManager.codeTo(TransManager.morseCode, mStringBefore);
                    }
                    mEditText.setText(mStringAfter);
                } else {
                    Snackbar.make(view, R.string.error_msg, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                break;

            case R.id.btn_url:
                if (mEditText.getText().toString().contains("w") ||
                        mEditText.getText().toString().contains("http")) {
                    //                if (Utils.isNet(mEditText.getText().toString())) {
                    if (progressBar.getVisibility() == View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    new AT().execute(mEditText.getText().toString());
                } else {
                    Snackbar.make(view, R.string.error_net, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                break;
            default:
                break;
        }
    }

    private String getRandomWelcomeTips() {
        String welcome_tip = null;
        welcome_array = this.getResources().getStringArray(R.array.tips);
        int index = (int) (Math.random() * (welcome_array.length - 1));
        welcome_tip = welcome_array[index];
        return welcome_tip;
    }

    //生成短链接
    class AT extends AsyncTask {
        @Override
        protected String doInBackground(Object[] objects) {

            try {
                HttpClient hc = new DefaultHttpClient();
                HttpPost hp = new HttpPost(CREATE_SHORT_URL);
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                nvps.add(new BasicNameValuePair("url", (String) objects[0]));
                hp.setEntity(new UrlEncodedFormEntity(nvps));
                HttpResponse hr = hc.execute(hp);
                String result = null;
                if (hr.getStatusLine().getStatusCode() == 200) {
                    result = EntityUtils.toString(hr.getEntity());
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    if (status.equals("0")) {
                        setEditText(jsonObject.getString("tinyurl"));
                        myToast("生成短链成功ಠ౪ಠ");
                    } else {
                        myToast("地址无效,短链接生成失败ಥ_ಥ");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }
    }

    //Toast信息类
    private void myToast(final String str) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (progressBar.getVisibility() == View.VISIBLE)
                    progressBar.setVisibility(View.GONE);
                Utils.showShort(MainActivity.this, str);
            }
        });
    }

    private void setEditText(final String str) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (progressBar.getVisibility() == View.VISIBLE)
                    progressBar.setVisibility(View.GONE);
                mEditText.setText(str);
            }
        });
    }


    @Override
    public void onBackPressed() {
        exitBy2Click();
    }

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true;
            // 准备退出
            //            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            Utils.showShort(this, "再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    private void openBy10Click() {
        Timer openTimer = null;
        if (clickNum < 10) {
            if (clickNum > 6) {
                Utils.showShort(this, "再按" + (10 - clickNum) + "次开启彩蛋");
            }
            clickNum++;
            openTimer = new Timer();
            openTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    clickNum = 0;
                }
            }, 5000);
        } else {
            Utils.showShort(this, "开启成功");
            openEgg(EGG_1);
            clickNum = 0;
        }
    }

    private void openEgg(String s) {
        final Uri uri = Uri.parse(s);
        final Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }
}
