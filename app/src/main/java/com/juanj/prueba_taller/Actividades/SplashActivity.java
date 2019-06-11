package com.juanj.prueba_taller.Actividades;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.juanj.prueba_taller.MainActivity;
import com.juanj.prueba_taller.R;

public class SplashActivity extends AppCompatActivity {

    private TextView ivLogo, ivSubtitle;
    ImageView ivSplash;
    Animation smalltobig, fleft, fhelper;
    ImageView ivImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        smalltobig= AnimationUtils.loadAnimation(this,R.anim.smalltobig);
        fleft= AnimationUtils.loadAnimation(this,R.anim.fleft);
        fhelper= AnimationUtils.loadAnimation(this,R.anim.fhelper);

        Typeface logox=Typeface.createFromAsset(getAssets(),"fonts/Fredoka.ttf");
        Typeface mlight=Typeface.createFromAsset(getAssets(),"fonts/MontserratLight.ttf");

        ivLogo=findViewById(R.id.ivLogo);
        ivSubtitle=findViewById(R.id.ivSubtitle);
        ivSplash=findViewById(R.id.ivSplash);
        ivImagen=findViewById(R.id.ivImagen);

        ivLogo.setTypeface(logox);
        ivSubtitle.setTypeface(mlight);

        ivSplash.startAnimation(smalltobig);

        ivLogo.setTranslationX(400);
        ivSubtitle.setTranslationX(400);
        ivImagen.setTranslationX(400);

        ivLogo.setAlpha(0);
        ivSubtitle.setAlpha(0);


        ivLogo.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        ivSubtitle.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        ivImagen.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this, MainActivity.class);

                startActivity(intent);
                overridePendingTransition(R.anim.fleft, R.anim.fhelper);
                finish();
            }
        },3000);


    }
}
