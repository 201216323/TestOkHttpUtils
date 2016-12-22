package bruce.chang.testokhttputils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator
 * Date:2016/12/21
 * Time:22:29
 * Author:BruceChang
 * Function:使用okhttputils在列表中请求图片
 */
public class HYOKhttpListActivity extends AppCompatActivity {

    private static final String TAG = HYOKhttpListActivity.class.getName();
    ListView mListView;
    ProgressBar mProgressBar;
    TextView tv_nodata;
    String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
    HYOkHttpListAdapter mHYOkHttpListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hyokhttplist);
        initView();
        postDataByOkhttputils();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.mListView);
        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        tv_nodata = (TextView) findViewById(R.id.tv_nodata);
    }


    /**
     * 使用okhttputils的post请求
     */
    public void postDataByOkhttputils() {
        String cacheValue = CacheUtils.getString(HYOKhttpListActivity.this, url);
        if (!TextUtils.isEmpty(cacheValue)) {
            loadData(cacheValue);
        }
        OkHttpUtils
                .post()
                .url(url)
                .id(100)
                .build()
                .execute(new MyStringCallback());

    }

    public class MyStringCallback extends StringCallback {
        @Override
        public void onBefore(Request request, int id) {
            setTitle("loading...");
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAfter(int id) {
            setTitle("Sample-okHttp");
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            e.printStackTrace();
            tv_nodata.setVisibility(View.VISIBLE);
        }

        @Override
        public void onResponse(String response, int id) {
            Log.e(TAG, "onResponse：complete");
            tv_nodata.setVisibility(View.GONE);

            switch (id) {
                case 100:
                    Toast.makeText(HYOKhttpListActivity.this, "http", Toast.LENGTH_SHORT).show();
                    break;
                case 101:
                    Toast.makeText(HYOKhttpListActivity.this, "https", Toast.LENGTH_SHORT).show();
                    break;
            }
            if (response != null) {

                CacheUtils.putString(HYOKhttpListActivity.this, url, response);
                loadData(response);

            }
        }

        @Override
        public void inProgress(float progress, long total, int id) {
            Log.e(TAG, "inProgress:" + progress);
            mProgressBar.setProgress((int) (100 * progress));
        }
    }

    /**
     * 加载数据
     * @param response
     */
    private void loadData(String response) {
        //得到解析后的收据
        DataBean dataBean = parseData(response);
        List<DataBean.ItemData> datas = dataBean.getTrailers();
        if (datas != null && datas.size() > 0) {
            //有数据
            mHYOkHttpListAdapter = new HYOkHttpListAdapter(HYOKhttpListActivity.this, datas);
            mListView.setAdapter(mHYOkHttpListAdapter);
            tv_nodata.setVisibility(View.GONE);
            //显示适配器
        } else {
            ////没有数据
            tv_nodata.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 解析数据
     *
     * @param response
     */
    private DataBean parseData(String response) {
        DataBean dataBean = new DataBean();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.optJSONArray("trailers");
            if (jsonArray != null && jsonArray.length() > 0) {
                List<DataBean.ItemData> trailers = new ArrayList<>();
                dataBean.setTrailers(trailers);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObjectItem = (JSONObject) jsonArray.get(i);

                    if (jsonObjectItem != null) {

                        DataBean.ItemData mediaItem = new DataBean.ItemData();

                        String movieName = jsonObjectItem.optString("movieName");//name
                        mediaItem.setMovieName(movieName);

                        String videoTitle = jsonObjectItem.optString("videoTitle");//desc
                        mediaItem.setVideoTitle(videoTitle);

                        String imageUrl = jsonObjectItem.optString("coverImg");//imageUrl
                        mediaItem.setCoverImg(imageUrl);

                        String hightUrl = jsonObjectItem.optString("hightUrl");//data
                        mediaItem.setHightUrl(hightUrl);

                        //把数据添加到集合
                        trailers.add(mediaItem);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataBean;
    }
}
