package ru.taximaster.testapp.services;

import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Httphelper {

    /*public Context context;
    public Httphelper(Context context) {
        this.context = context;
    }*/

    public void setImage(final ImageView imageView, final String name, final ProgressBar progressBar) {
        if(!name.equals("")) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.flickr.com/services/rest/").build();
            final PostApi postApi = retrofit.create(PostApi.class);
            Log.d("mylogs", name);
            retrofit2.Call<ResponseBody> getimage = postApi.getImage(name);
            getimage.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                    imageView.setImageBitmap(BitmapFactory.decodeStream(response.body().byteStream()));
                    progressBar.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                    Log.d("mylogs", "unable to load the image of " + name);
                }
            });
        }
    }
}
