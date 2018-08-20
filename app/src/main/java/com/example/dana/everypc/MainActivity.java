package com.example.dana.everypc;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.example.dana.everypc.R;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        //
        tv_result = (TextView) findViewById(R.id.tv_result);
        tv_result.setMovementMethod(new ScrollingMovementMethod()); //스크롤기능 추가하기

        MyAsyncTask mProcessTask = new MyAsyncTask();
        mProcessTask.execute();
    }

    //AsyncTask 생성 - 모든 네트워크 로직을 여기서 작성해 준다.
    public class MyAsyncTask extends AsyncTask<String, Void, Getter[]> {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        //OkHttp 객체생성
        OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("\t로딩중...");
            //show dialog
            progressDialog.show();
        }

        @Override
        protected Getter[] doInBackground(String... params) {

            //파라미터를 더해 주거나 authentication header를 추가할 수 있다.
            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://api.danawa.com/api/search/product/info?key=bc716b5130a42e5aa7b0534e25821e51&keyword=CPU&mediatype=json&charset=utf8").newBuilder();
            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try {
                Response response = client.newCall(request).execute();

                //여기서 해야 하나???
                //gson을 이용해서 json을 자바 객체로 변환한다.
                Gson gson = new GsonBuilder().create();
                JsonParser parser = new JsonParser();
                //제공되는 오픈API데이터에서 어떤 항목을 가여올지 설정해야 하는데.... 음~
                JsonElement rootObject = parser.parse(response.body().charStream()).getAsJsonObject().get("productList"); //원하는 항목(?)까지 찾아 들어가야 한다.
                Getter[] posts = gson.fromJson(rootObject, Getter[].class);

                return posts;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Getter[] result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            //요청결과를 여기서 처리한다. 화면에 출력하기등...

            //아니면 여기서 해야 하나? JSON 오브젝트로 변환하기
            if(result.length > 0){
                for (Getter post: result){
//                    Log.d(TAG, String.valueOf(post.getRank()));//랭킹
//                    Log.d(TAG, post.getMovieNm());//영화제목 출력
//                    Log.d(TAG, post.getOpenDt());//개봉일 출력
                    tv_result.append(post.getProd_name()+"\n");
                    tv_result.append(post.getMaker()+"\n");
                    tv_result.append(String.valueOf(post.getMin_price())+"\n");
                    tv_result.append(String.valueOf(post.getAvr_price())+"\n");
                    tv_result.append(post.getDanawaUrl()+"\n");
                }
            }
        }
    }


}// MainActivity class