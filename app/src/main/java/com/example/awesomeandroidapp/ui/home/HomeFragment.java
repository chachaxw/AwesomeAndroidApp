package com.example.awesomeandroidapp.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.Observer;

import com.example.awesomeandroidapp.R;
import com.sch.share.WXShareMultiImageHelper;
import com.example.awesomeandroidapp.adapter.GridViewAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private static final String TAG = HomeFragment.class.getSimpleName();
    private int[] imgList = {
        R.mipmap.img_1,
        R.mipmap.img_2,
        R.mipmap.img_3,
        R.mipmap.img_4,
        R.mipmap.img_5,
        R.mipmap.img_6,
        R.mipmap.img_7,
        R.mipmap.img_8,
        R.mipmap.img_9,
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Button shareButton = root.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new ButtonOnClickListener());
        initGridView(root);
        return root;
    }

    class ButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Toast toast = Toast.makeText(getContext(), "这是匿名内部类作为事件监听器类", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            shareToFriend();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WXShareMultiImageHelper.clearTmpFile(Objects.requireNonNull(getContext()));
    }

    private void initGridView(View root) {
        final GridViewAdapter adapter = new GridViewAdapter(getContext(), imgList);
        final GridView gridView = root.findViewById(R.id.gridView);
        gridView.setNumColumns(3);
        gridView.setAdapter(adapter);
    }

    /** Called when the user taps the button */
    private void shareToFriend() {
//        WXShareMultiImageHelper.shareToSession((getActivity(), );
        Log.d(TAG, "分享给好友！");
    }

    private void share() {
        new Thread(new Task("Thread-1")).start();
    }

    private class Task implements Runnable {
        private String threadName;

        Task(String name) {
            threadName = name;
            Log.d(TAG, "线程名称" + threadName);
        }

        @Override
        public void run() {
            try {
                Log.d(TAG, "多线程处理运行开始");
                ArrayList<Bitmap> bitmapList = new ArrayList<Bitmap>();

                for (int item : imgList) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), item);
                    bitmapList.add(bitmap);
                }

//                WXShareMultiImageHelper.shareToSession(getActivity(), bitmapList);
                Log.d(TAG, "多线程处理运行结束");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}