package com.shinmusic.Fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.shinmusic.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_Dianhac extends Fragment {
    View view;
    CircleImageView circleImageView;
    public  ObjectAnimator objectAnimator;
    Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dianhac,container,false);
        circleImageView = view.findViewById(R.id.circleimagedianhac);
        objectAnimator = ObjectAnimator.ofFloat(circleImageView,"rotation",0f,360f);
        objectAnimator.setDuration(100000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
        return view;

    }

    public void Playnhac(String hinhanh) {
        if (hinhanh != null){
        Picasso.with(context).load(hinhanh).into(circleImageView);
        }
    }


}
