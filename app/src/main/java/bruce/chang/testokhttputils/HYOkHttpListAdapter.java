package bruce.chang.testokhttputils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator
 * Date:2016/12/21
 * Time:22:47
 * Author:BruceChang
 * Function:
 */

public class HYOkHttpListAdapter extends BaseAdapter {

    private Context context;
    private List<DataBean.ItemData> itemDatas;

    public HYOkHttpListAdapter(Context context, List<DataBean.ItemData> itemDatas) {
        this.context = context;
        this.itemDatas = itemDatas;
    }

    @Override
    public int getCount() {
        return itemDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return itemDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if (view==null){
            view = LayoutInflater.from(context).inflate(R.layout.activity_hy_adapter_item,null);
            viewHolder = new ViewHolder();
            viewHolder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        DataBean.ItemData itemData = itemDatas.get(i);
        viewHolder.tv_name.setText(itemData.getMovieName());
        viewHolder.tv_desc.setText(itemData.getVideoTitle());
        //在列表中使用Okhttputils来请求图片
        final ViewHolder finalViewHolder = viewHolder;
        OkHttpUtils
                .get()//
                .url(itemData.getCoverImg())//
                .tag(this)//
                .build()//
                .connTimeOut(20000)
                .readTimeOut(20000)
                .writeTimeOut(20000)
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(Bitmap bitmap, int id) {
                        Log.e("TAG", "onResponse：complete");
                        finalViewHolder.iv_icon.setImageBitmap(bitmap);
                    }
                });
        return view;
    }

    class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_desc;
    }
}
