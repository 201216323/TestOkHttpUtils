package bruce.chang.testokhttputils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int GET = 1;
    private static final int POST = 2;
    Button btn_get, btn_post;
    TextView tv_result;
    OkHttpClient client = new OkHttpClient();


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET:
                    tv_result.setText(msg.obj.toString());
                    break;
                case POST:
                    tv_result.setText(msg.obj.toString());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_get = (Button) findViewById(R.id.btn_get);
        btn_post = (Button) findViewById(R.id.btn_post);
        tv_result = (TextView) findViewById(R.id.tv_result);
        //设置点击事件
        btn_get.setOnClickListener(this);
        btn_post.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get:
                tv_result.setText("");
                getDataFromGet();
                break;
            case R.id.btn_post:
                tv_result.setText("");
                getDataFromPost();
                break;
            default:
                break;
        }
    }

    private void getDataFromPost() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    String result = okPost("http://api.m.mtime.cn/PageSubArea/TrailerList.api", "");
                    Message msg = new Message();
                    msg.what = POST;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private void getDataFromGet() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    String result = okGet("http://api.m.mtime.cn/PageSubArea/TrailerList.api");
                    Message msg = new Message();
                    msg.what = GET;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    /**
     * get请求，要在子线程运行
     *
     * @param url 请求的地址
     * @return
     * @throws IOException
     */
    String okGet(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    /**
     * post请求，要在子线程运行
     *
     * @param url  请求链接
     * @param json 参数
     * @return
     * @throws IOException
     */
    String okPost(String url, String json) throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
