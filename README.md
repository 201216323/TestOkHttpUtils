# 1：OKHttp介绍
## 1.1简介
OKHttp是一款高效的HTTP客户端，支持连接同一地址的链接共享同一个socket，通过连接池来减小响应延迟，还有透明的GZIP压缩，请求缓存等优势，其核心主要有**路由**、**连接协议**、**拦截器**、**代理**、**安全性认证**、**连接池**以及**网络适配**，拦截器主要是++指添加，移除或者转换请求或者回应头部信息++

这个库也是square开源的一个网络请求库(okhttp内部依赖okio)。现在已被Google使用在Android源码上了，可见其强大。

关于网络请求库，现在应该还有很多人在使用**android-async-http**。他内部使用的是HttpClient，但是Google在6.0版本里面删除了HttpClient相关API，可见这个库现在有点过时了。

OkHttp最低支持Android2.3，最低支持java1.7.

对于square这个公司还有许多优秀的开源项目，例如**Retrofit**、**picasso**等，这些随后都会详细的说明。

## 1.2下载地址

https://github.com/square/okhttp

## 1.3OKHttp主要功能

1. 联网请求文本数据
2. 大文件下载
3. 大文件上传
4. 请求图片

# 2：原生OKHttp的Get和Post请求小案例
## 2.1使用前配置
### 2.1.1 参照网址：
http://square.github.io/okhttp/ 里面有对okhttp使用的描述，下面是里面主要的get和set方法。

### 2.1.2 get a rul:

```
OkHttpClient client = new OkHttpClient();

String run(String url) throws IOException {
  Request request = new Request.Builder()
      .url(url)
      .build();

  Response response = client.newCall(request).execute();
  return response.body().string();
}
```
### 2.1.3 post to a server

```
public static final MediaType JSON
    = MediaType.parse("application/json; charset=utf-8");

OkHttpClient client = new OkHttpClient();

String post(String url, String json) throws IOException {
  RequestBody body = RequestBody.create(JSON, json);
  Request request = new Request.Builder()
      .url(url)
      .post(body)
      .build();
  Response response = client.newCall(request).execute();
  return response.body().string();
}
```
### 2.1.4 最新的jar包

项目中要想使用OkHttp，有两种方法。

1. 导入OkHttp的jar包，因其内部依赖Okio，所以还需要导入okio的jar包，

2. 可以直接在项目中添加gradle配置，compile 'com.squareup.okhttp3:okhttp:(insert latest version)'
3. 小编写此文章时候最新的OkHttp版本是okhttp-3.5.0，最新的okiio版本是okio-1.11.0。
4. 将下载好的两个jar包导入到自己的项目中使用

## 2.2Get请求
### 2.2.1布局
在项目布局activity_main.xml中添加发送get请求和post请求的按钮，并添加显示请求结果的textview，这里省略布局。。。最重要的别往了添加网络请求权限。

```
<uses-permission android:name="android.permission.INTERNET"/>
```

### 2.2.2主程序

在MainActivity.java中添加相应按钮的点击事件，在点击get按钮执行get请求，将请求成功的数据显示在textview上。

> 从2.1.2中可以看到，首先需要new一个OkHttpClient供访问网络使用。

```
OkHttpClient client = new OkHttpClient();
```


> get请求核心方法


```
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
```
> 点击get请求按钮调用下面方法来得到请求成功返回的json数据，因为网络的访问是一个耗时的操作，这个需要单独开一个线程，最后通过Handle机制来加载请求成功的结果。


```
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
```
> Handler得到数据加载显示


```
Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET:
                    tv_result.setText(msg.obj.toString());
                    break;
            }
        }
    };
```
> get请求结果

![get请求结果](http://ww1.sinaimg.cn/mw690/b0d9a523jw1fayhjcu0ddg20bx0mlqv5.gif)

## 2.3Post请求

从2.1.3中我们可以看到post请求的方法，拷贝上面的代码到MainActivity.java中，去掉再get请求中已经添加的OkHttpClient client = new OkHttpClient()，然后像get方法一样，在新的线程中进行请求调用，结果同样通过Handle消息机制发出并在textview上加载服务器返回的json数据。

> post请求核心方法

```
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
```
> post按钮点击调用下面方法，okPost()方法的第二个参数是请求参数，这里没有参数，就传入空字符串，最后和get请求一样，利用Handle加载数据。

```
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
```
> post请求结果

![post请求结果](http://ww1.sinaimg.cn/mw690/b0d9a523jw1fayi3xb134g20bx0mlqv5.gif)

# 3第三方封装好的OKHttp库（1）————okhttputils

## 3.1okhttp-utils简介

okhttputils是鸿洋大神基于OkHttp3.3.1的一个封装，最新的版本是3.5.0，主要包含有：
- 一般的get请求
- 一般的post请求
- 基于Http的文件上传
- 文件下载
- 加载图片
- 支持请求回调，直接返回对象、对象集合
- 支持session的保持

可以参考他这篇文章的博客：http://blog.csdn.net/lmj623565791/article/details/47911083

## 3.2下载导入自己的工程

okhttputils的Github地址：https://github.com/hongyangAndroid/okhttputils

将下载好的文件解压后，以库工程的形式导入到 项目中，但会有一些错误。

## 3.3解决报错

### 3.3.1在自己项目的gradle文件中增加如下代码

```
allprojects {
    repositories {
        jcenter()
        maven { url "https://www.jitpack.io" }
    }
}
```
### 3.3.2把okhttp-utils集成到案例中

> 关联库


```
compile project(':okhttputils')
```
> 解决报错

```
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    testCompile 'junit:junit:4.12'
    compile 'com.android.support:appcompat-v7:23.3.0'
//    compile files('libs/okhttp-3.4.1.jar')
//    compile files('libs/okio-1.9.0.jar')
    compile project(':okhttputils')
}

```
重新运行程序，点击get或者post请求，哇哦，奔溃了，，百思不得其解，一怒之下，删掉了Jar包，然后运行、点击按钮得到了json数据，这一点需要特别注意。

## 3.4使用okhttp-utils请求文本

在MainActivity.java中使用原生的OkHttp的get和post方法，这里新建一个HYOkhttpActivity.java类，专门用来使用okhttputils中的方法
，布局都很简单，不多说明。

> get请求


```
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
```
MyStringCallback类是一个回调类，继承自okhttputils中的StringCallback类，在这里面处理请求网络前、请求网络后、请求错误、请求成功以及进度的回调，代码如下：
```
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
//            mProgressBar.setProgress((int) (100 * progress));
        }
    }
```

运行结果就是在tv_result上加上请求成功的json数据，就不写了。

> post请求

post请求和get请求一样，只需要把get请求中的get方法换成post方法即可，只是这里。

```
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
```
运行结果和get请求一样。

## 3.5使用okhttp-utils下载文件

> 下载文件主要方法，这里下载一个视频

```
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
```
> 进度回调的打印信息如下，下载的文件就在手机根目录，视频是可以播放的。


```
E/HYOkhttpActivity: inProgress :0

省略。。。。

E/HYOkhttpActivity: inProgress :100

E/HYOkhttpActivity: onResponse :/storage/emulated/0/okhttp-utils-test.mp4
```


## 3.6使用okhttp-utils上传文件

> 上传文件的时候调用下面的方法，其中mBaseUrl是自己搭建的本地服务器的地址，这里上传手机存储根目录下的一个code.jpg图片和123.txt图片。

```
/**
     * 使用okhttputils 上传多个或者单个文件
     *
     * @param view
     */
    public void multiFileUpload(View view) {
        String mBaseUrl = "http://192.168.7.167:8082/FileUpload/FileUploadServlet";
        File file = new File(Environment.getExternalStorageDirectory(), "code.jpg");
        File file2 = new File(Environment.getExternalStorageDirectory(), "123.txt");
        if (!file.exists() || !file2.exists()) {
            Toast.makeText(HYOkhttpActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("username", "bruceChang");
        params.put("password", "123");

        String url = mBaseUrl;
        OkHttpUtils.post()//
                .addFile("mFile", "server_code.jpg", file)//
//                .addFile("mFile", "123.txt", file2)//
                .url(url)
                .params(params)//
                .build()//
                .execute(new MyStringCallback());
    }
```
> 上传结果，从服务器端可以查看，已经将两个文件上传成功了。

![上传文件结果](http://ww3.sinaimg.cn/mw690/b0d9a523jw1fazcx0axcvj20bi07aq3y.jpg)

## 3.7使用okhttp-utils请求单张图片

> 调用方法，将请求的图片加载到iv_http这个ImageView上，从运行的结果来看，在请求单张图片的时候还是不错的，但在加载列表图片的时候就有问题了。

```
 /**
     * 使用okhttputils 请求单张图片
     *
     * @param view
     */
    public void getImage(View view) {
        tv_result.setText("");
        String url = "http://images.csdn.net/20150817/1.jpg";
        OkHttpUtils
                .get()//
                .url(url)//
                .tag(this)//
                .build()//
                .connTimeOut(20000)
                .readTimeOut(20000)
                .writeTimeOut(20000)
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        tv_result.setText("onError:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(Bitmap bitmap, int id) {
                        Log.e("TAG", "onResponse：complete");
                        iv_http.setImageBitmap(bitmap);
                    }
                });
    }
```
> 结果：

![使用okhttputils 请求单张图片](http://ww2.sinaimg.cn/mw690/b0d9a523jw1fays32vn29g209n0hi444.gif)

## 3.7使用okhttp-utils请求列表图片

> 既然要在列表中加载图片，就新建一个Activity，使用3.4中的post请求得到返回的数据，利用SharedPreferences存储起来，断网的时候从本地读取数据，调用loadData方法，加载数据。

```
   if (response != null) {

    CacheUtils.putString(HYOKhttpListActivity.this, url, response);
    loadData(response);

    }
```
> loadData加载数据，此方法中需要调用parseData方法来解析数据，这样做到不同功能的方法相互分离，方便调用，HYOkHttpListAdapter也是非常简单的，里面就调用3.6中的方法加载单张图片，最下面有源代码，不懂的可以查看。

```
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
```
> 当手机没有网络的时候，从本地读取存储的数据，这就需要稍微修改一下3.4中的post请求方法，但或许是OkHttp本身的原因还是okhttputils的原因，没网的时候图片是不能加载出来的。

```
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
```
> 有网的时候结果，可以看到，ListView上面的图片在加载的时候有跳跃，这也是存在的一个问题。

![image](http://ww4.sinaimg.cn/mw690/b0d9a523jw1faz89n9rd8g209n0hihdu.gif)

> 断开网络结果,图片没有加载出来

![image](http://ww4.sinaimg.cn/mw690/b0d9a523jw1faz8aggl3gg209n0hi7wh.gif)





源码地址：https://github.com/201216323/TestOkHttpUtils

欢迎访问201216323.tech来查看我的CSDN博客。

欢迎关注我的个人技术公众号,快速查看我的最新文章。

![我的公众号图片](http://img.blog.csdn.net/20161220174646569?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvY2NnXzIwMTIxNjMyMw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast "bruce常")
