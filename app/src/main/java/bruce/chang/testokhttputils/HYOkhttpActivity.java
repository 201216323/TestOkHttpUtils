package bruce.chang.testokhttputils;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by: BruceChang
 * Date on : 2016/12/21.
 * Time on: 17:07
 * Progect_Name:TestOkHttpUtils
 * Source Github：
 * Description:鸿洋大神okhttputils使用
 */
public class HYOkhttpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = HYOkhttpActivity.class.getSimpleName();
    Button btn_get, btn_post,btn_download_big_file;
    TextView tv_result;
    ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hyokhttp);
        btn_get = (Button) findViewById(R.id.btn_get);
        btn_post = (Button) findViewById(R.id.btn_post);
        tv_result = (TextView) findViewById(R.id.tv_result);
        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        btn_download_big_file = (Button) findViewById(R.id.btn_download_big_file);
        //设置点击事件
        btn_get.setOnClickListener(this);
        btn_post.setOnClickListener(this);
        btn_download_big_file.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get:
                tv_result.setText("");
                getDataByOkhttputils(btn_get);
                break;
            case R.id.btn_post:
                tv_result.setText("");
                postDataByOkhttputils(btn_post);
                break;
            case R.id.btn_download_big_file:
                downloadFile(btn_download_big_file);
                break;
            default:
                break;
        }
    }
    /**
     * 使用okhttputils的post请求
     * @param view
     */
    public void postDataByOkhttputils(View view) {
        String url = "http://www.zhiyun-tech.com/App/Rider-M/changelog-zh.txt";
        url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
        OkHttpUtils
                .post()
                .url(url)
                .id(100)
                .build()
                .execute(new MyStringCallback());
    }
    /**
     * 使用okhttputils的get请求
     * @param view
     */
    public void getDataByOkhttputils(View view) {
        String url = "http://www.zhiyun-tech.com/App/Rider-M/changelog-zh.txt";
        url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
        OkHttpUtils
                .get()
                .url(url)
                .id(100)
                .build()
                .execute(new MyStringCallback());
    }

    public void downloadFile(View view)
    {
        String url = "http://vfx.mtime.cn/Video/2016/12/14/mp4/161214234616222245.mp4";
        OkHttpUtils//
                .get()//
                .url(url)//
                .build()//
                //下载的文件路径   和  名称
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "okhttp-utils-test.mp4")//
                {

                    @Override
                    public void onBefore(Request request, int id)
                    {
                    }

                    @Override
                    public void inProgress(float progress, long total, int id)
                    {
                        mProgressBar.setProgress((int) (100 * progress));
                        //打印进度信息
                        Log.e(TAG, "inProgress :" + (int) (100 * progress));
                    }

                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        //打印出错的信息
                        Log.e(TAG, "onError :" + e.getMessage());
                    }

                    @Override
                    public void onResponse(File file, int id)
                    {
                        //打印下载文件的路径
                        Log.e(TAG, "onResponse :" + file.getAbsolutePath());
                    }
                });
    }


    public class MyStringCallback extends StringCallback {
        @Override
        public void onBefore(Request request, int id) {
            setTitle("loading...");
        }

        @Override
        public void onAfter(int id) {
            setTitle("Sample-okHttp");
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            e.printStackTrace();
            tv_result.setText("onError:" + e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            Log.e(TAG, "onResponse：complete");
            tv_result.setText("onResponse:" + response);

            switch (id) {
                case 100:
                    Toast.makeText(HYOkhttpActivity.this, "http", Toast.LENGTH_SHORT).show();
                    break;
                case 101:
                    Toast.makeText(HYOkhttpActivity.this, "https", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void inProgress(float progress, long total, int id) {
            Log.e(TAG, "inProgress:" + progress);
            mProgressBar.setProgress((int) (100 * progress));
        }
    }

}
