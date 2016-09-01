package com.paradoxie.bicycle;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.paradoxie.bicycle.HideIME.HideIMEUtil;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mEditText;
    private String mStringBefore, mStringAfter;
    private Button mButton_expalin, mButton_translate, mButton_url;
    private MaterialSpinner spinner;
    private TransManager mTransManager = new TransManager();
    private Handler mHandler = new Handler();
    private static Boolean isExit = false;
    //百度生成短网址请求地址
    private static final String CREATE_SHORT_URL = "http://dwz.cn/create.php";
    //百度还原短网址请求地址
    private static final String QUERY_SHORT_URL = "http://dwz.cn/query.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HideIMEUtil.wrap(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mEditText = (EditText) findViewById(R.id.et_code);
        mButton_expalin = (Button) findViewById(R.id.btn_explain);
        mButton_expalin.setOnClickListener(this);
        mButton_translate = (Button) findViewById(R.id.btn_translate);
        mButton_translate.setOnClickListener(this);
        mButton_url = (Button) findViewById(R.id.btn_url);
        mButton_url.setOnClickListener(this);

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
            case R.id.share:
//                Toast.makeText(this, "share", Toast.LENGTH_LONG).show();
                share(this, "分享项目地址", "https://github.com/fangx/ZhiHuMVP");
                break;
            case R.id.about:
                Toast.makeText(this, "about", Toast.LENGTH_LONG).show();
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
//                        if (Utils.isIChina(mStringAfter))
                            mStringBefore = mTransManager.ToCode(TransManager.iChina, mStringAfter);
//                        else
//                            Snackbar.make(view, R.string.sourse_error, Snackbar.LENGTH_SHORT)
//                                    .setAction("Action", null).show();
                    } else if (spinner.getText().equals("Base64")) {
//                        if (Utils.isBase64(mStringAfter))
                            mStringBefore = TransManager.base64Decode2String(TransManager.base64Decode(mStringAfter));
//                        else
//                            Snackbar.make(view, R.string.sourse_error, Snackbar.LENGTH_SHORT)
//                                    .setAction("Action", null).show();
                    } else if (spinner.getText().equals("Morse码")) {
//                        if (Utils.isMorse(mStringAfter))
                            mStringBefore = mTransManager.ToCode(TransManager.morseCode, mStringAfter);
//                        else
//                            Snackbar.make(view, R.string.sourse_error, Snackbar.LENGTH_SHORT)
//                                    .setAction("Action", null).show();
                    }
                    mEditText.setText(mStringBefore);
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
                if (mEditText.getText().toString().contains("www.")||mEditText.getText().toString().contains("http"))
                    new AT().execute(mEditText.getText().toString());
                else
                    Snackbar.make(view, R.string.error_net, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                break;
            default:
                break;
        }
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
                    } else {
                        myToast("短链接生成失败");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }

    }


    //分享
    public void share(Context context, String Title, String Url) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        //noinspection deprecation
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_TEXT,Title + " " + Url + " 分享自知乎网");
        context.startActivity(Intent.createChooser(share, "分享到"));
    }
    //Toast信息类
    private void myToast(final String str) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setEditText(final String str) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
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
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
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
}
