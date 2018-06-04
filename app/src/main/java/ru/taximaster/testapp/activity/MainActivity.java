package ru.taximaster.testapp.activity;

import android.app.ProgressDialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.taximaster.testapp.R;
import ru.taximaster.testapp.adapter.ImageAdapter;
import ru.taximaster.testapp.services.PostApi;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PAGE_COUNT = 4;
    public static final int PPP = 20;
    public int page = 1;
    public static final String TAG = "mylogs";
    public Button button;
    public EditText editText;
    public static ProgressDialog pd;
    public static BitmapDrawable[] bitmaps;
    public RecyclerView recyclerView;
    private ImageAdapter mAdapter;
    ArrayList<BitmapDrawable> bitmap = new ArrayList<BitmapDrawable>();
    Handler updater;
    /*
     * ключ для доступа к api выдается после регистрации в качестве разработчика на сервисе
     * https://www.flickr.com/services/apps/create/noncommercial/?
     */
    private final String KEY = "b7b0101ea9d61c6d61a4bb9467c59295"; //api key
    private final String secret = "7cbb874c6f2d57df"; //secret

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        pd = new ProgressDialog(this);
        pd.setTitle("Понимаем...");
        pd.setMessage("Подождите еще немножко...");
        pd.setCancelable(false);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                onButtonClick();
                break;
            default:
                break;
        }
    }

    public void onButtonClick() {
        button.setClickable(false);
        Log.d("mylogs", "salom");
        pd.show();
        String query = editText.getText().toString();
        String[] questRaw = query.split(" ");
        String quest = questRaw[0];
        for (int i = 1; i < questRaw.length; i++) {
            quest = quest + "+" + questRaw[i];
        }

        String reqURL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key="
                + KEY + "&sort=relevance&content_type=1&per_page="
                + (PPP * page) + "&page=1&media=photos&format=json&nojsoncallback=1&text='"
                + quest + "'";
        Log.d("mylogs", "" + reqURL);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.flickr.com/services/rest/").addConverterFactory(GsonConverterFactory.create(gson)).build();
        final PostApi postApi = retrofit.create(PostApi.class);
        Call<JsonObject> getthelist = postApi.getList(reqURL);
        getthelist.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                ArrayList<String> list = new ArrayList<String>();
                Log.d("mylogs", "" + response.code());
                Log.d("mylogs", "" + response.body().toString());
                JsonArray photo = null;

                try {
                    photo = response.body().getAsJsonObject("photos").getAsJsonArray("photo");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < photo.size(); i++) {
                        JsonElement d = photo.get(i);
                        JsonObject p = (JsonObject) d;
                        String farm = p.get("farm").getAsString();
                        String server = p.get("server").getAsString();
                        String id = p.get("id").getAsString();
                        String secret = p.get("secret").getAsString();
                        String url = "http://farm" + farm + ".static.flickr.com/"
                                + server + "/" + id + "_" + secret + ".jpg";
                        Log.d("mylogs", "" + url);
                        list.add(url);

                }
                mAdapter = new ImageAdapter(list, MainActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                pd.cancel();
                button.setClickable(true);
            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                pd.cancel();
                Log.d("mylogs", "not found");
                Log.d("mylogs", t.toString());
            }
        });
    }
}