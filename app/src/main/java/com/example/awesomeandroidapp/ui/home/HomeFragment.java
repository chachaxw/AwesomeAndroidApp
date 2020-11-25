package com.example.awesomeandroidapp.ui.home;

import android.Manifest;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;

import com.example.awesomeandroidapp.R;
import com.sch.share.WXShareMultiImageHelper;
import com.example.awesomeandroidapp.adapter.GridViewAdapter;
import com.tbruyelle.rxpermissions3.RxPermissions;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RxPermissions rxPermissions = new RxPermissions(this);
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
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
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
            rxPermissions
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                            shareToFriend((List<Bitmap> bitmapList) -> {
                                Log.d(TAG, "分享数据" + bitmapList);
                            });
                        } else {
                            Log.d(TAG, "未开启文件写入权限！");
                        }
                    });
        }
    }

    @Override
    public void onDestroy() {
        WXShareMultiImageHelper.clearTmpFile(requireContext());
        super.onDestroy();
    }

    private void initGridView(View root) {
        final GridViewAdapter adapter = new GridViewAdapter(getContext(), imgList);
        final GridView gridView = root.findViewById(R.id.gridView);
        gridView.setNumColumns(3);
        gridView.setAdapter(adapter);
    }

    /** Called when the user taps the button */
    private void shareToFriend(final OnLoadImageEndCallback callback) {
        Log.d(TAG, "分享给好友！");
        new Thread(new Task("Thread-1", callback)).start();
    }

    private class Task implements Runnable {
        private String threadName;
        private OnLoadImageEndCallback onLoadImageEnd;

        Task(String name, OnLoadImageEndCallback callback) {
            threadName = name;
            onLoadImageEnd = callback;
            Log.d(TAG, "线程名称" + threadName);
        }

        @Override
        public void run() {
            try {
                Log.d(TAG, "多线程处理运行开始" + threadName);
                List<Bitmap> bitmapList = new ArrayList<>();

                for (int item : imgList) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), item);
                    bitmapList.add(bitmap);
                }

                onLoadImageEnd.onEnd(bitmapList);
                Log.d(TAG, "多线程处理运行结束" + threadName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private interface OnLoadImageEndCallback {
        void onEnd(List<Bitmap> bitmapList);
    }
}