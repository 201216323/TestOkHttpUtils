package bruce.chang.testokhttputils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator
 * Date:2016/12/21
 * Time:23:01
 * Author:BruceChang
 * Function:
 */

public class CacheUtils {

    /**
     * 保持数据
     * @param context
     * @param key
     * @param values
     */
    public static  void putString(Context context, String key, String values){
        SharedPreferences sharedPreferences = context.getSharedPreferences("hyokhttp",Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key,values).commit();
    }

    /**
     * 得到缓存的数据
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("hyokhttp",Context.MODE_PRIVATE);
        return  sharedPreferences.getString(key,"");
    }
}
