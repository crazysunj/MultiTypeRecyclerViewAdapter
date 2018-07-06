package com.crazysunj.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * author: sunjian
 * created on: 2017/8/13 下午4:22
 * description:
 */

public class FooterItemFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_footer, container, false);
    }

    public static FooterItemFragment newInstance() {
        return new FooterItemFragment();
    }
}
