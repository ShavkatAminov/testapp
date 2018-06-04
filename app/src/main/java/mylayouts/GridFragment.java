package mylayouts;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.Random;

import ru.taximaster.testapp.activity.MainActivity;
import ru.taximaster.testapp.R;

import static ru.taximaster.testapp.activity.MainActivity.TAG;

public class GridFragment extends Fragment {
    int bkgColor;
    int page;
    final static String PAGE = "page";
    public MyAdapter adapter = new MyAdapter();
    @SuppressLint("HandlerLeak")
    public Handler updater = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
        }
    };

    public static GridFragment getNewInstance(int page) {
        GridFragment gf = new GridFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE, page);
        gf.setArguments(args);
        return gf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Random rnd = new Random();
        bkgColor = Color.argb(40, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        page = getArguments().getInt(PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_fragment, null);
        view.setBackgroundColor(bkgColor);
        ((GridView) view.findViewById(R.id.grid)).setAdapter(adapter);
        return view;
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return MainActivity.PPP;
        }

        @Override
        public Object getItem(int i) {
            return MainActivity.bitmaps[i + MainActivity.PPP * page];
        }

        @Override
        public long getItemId(int i) {
            return i + MainActivity.PPP * page;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            int ind = i + MainActivity.PPP * page;
            ImageView v;
            if (view != null) {
                v = (ImageView) view;
            } else {
                v = new ImageView(getActivity());
            }
            if (MainActivity.bitmaps != null) {
                try {
                    BitmapDrawable[] bs = MainActivity.bitmaps;
                    BitmapDrawable b = bs[ind];
                    v.setImageDrawable(b);
                } catch (Exception e) {
                    Log.e(TAG, "Ошибка показа изображения");
                }
            } else {
                v.setImageResource(R.drawable.iv_ico);
            }
            return v;
        }
    }
}
