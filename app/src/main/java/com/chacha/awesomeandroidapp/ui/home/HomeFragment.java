package com.chacha.awesomeandroidapp.ui.home;

import android.os.Bundle;
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

import com.chacha.awesomeandroidapp.R;
import com.chacha.awesomeandroidapp.adapter.GridViewAdapter;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private final int[] imgList = {
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
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initGridView(View root) {
        final GridViewAdapter adapter = new GridViewAdapter(getContext(), imgList);
        final GridView gridView = root.findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
    }
}