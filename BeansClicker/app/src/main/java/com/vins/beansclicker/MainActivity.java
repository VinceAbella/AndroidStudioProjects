package com.vins.beansclicker;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button upgrdBeansBtn, upgrdClickBtn;
    private TextView numCount, upgrdBeansTxt, upgrdClickTxt, popUpText;
    private ImageButton imageBeansBtn;
    private int numberCount = 0;
    private int upgradeCount = 0;
    private int clickIncrement = 1;
    private int upgradeBeans = 250;
    private int upgradeClick = 5;
    private Handler handler;
    private Runnable runnable;
    private final long INTERVAL = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        numCount = findViewById(R.id.numCount);
        upgrdBeansTxt = findViewById(R.id.upgrdBeansTxt);
        upgrdClickTxt = findViewById(R.id.upgrdClickTxt);
        upgrdBeansBtn = findViewById(R.id.upgrdBeansBtn);
        upgrdClickBtn = findViewById(R.id.upgrdClickBtn);
        imageBeansBtn = findViewById(R.id.imageBeansBtn);
        popUpText = findViewById(R.id.popUpText);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        handler = new Handler(Looper.getMainLooper());
        startAutoUpdate();

        upgrdBeansBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberCount >= upgradeBeans) {
                    numberCount -= upgradeBeans;
                    upgradeCount++;
                    clickIncrement += 5;
                    upgrdBeansBtn.setEnabled(false);
                    upgradeBeans += 5000;

                    numCount.setText(String.valueOf(numberCount));
                    upgrdBeansTxt.setText(String.valueOf(upgradeBeans));

                    switch (upgradeCount) {
                        case 1:
                            clickIncrement += 50;
                            imageBeansBtn.setImageResource(R.drawable.green_beans);
                            break;
                        case 2:
                            clickIncrement += 100;
                            imageBeansBtn.setImageResource(R.drawable.str_beans);
                            break;
                        case 3:
                            clickIncrement += 250;
                            imageBeansBtn.setImageResource(R.drawable.baked_beans);
                            break;
                        case 4:
                            clickIncrement += 500;
                            imageBeansBtn.setImageResource(R.drawable.pork_and_beans);
                            break;
                    }
                }
            }
        });

        imageBeansBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    numberCount += clickIncrement;
                    numCount.setText(String.valueOf(numberCount));
                    showPopUp(event.getX(), event.getY());
                    return true;
                }
                return false;
            }
        });

        upgrdClickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberCount >= upgradeClick) {
                    numberCount -= upgradeClick;
                    int clickadd = 0;
                    clickadd++;
                    clickIncrement++;
                    upgradeClick += upgradeClick;
                    upgrdClickTxt.setText(String.valueOf(upgradeClick));
                    upgrdClickBtn.setEnabled(numberCount >= upgradeClick);
                    clickIncrement += clickadd;

                    numCount.setText(String.valueOf(numberCount));
                }
            }
        });
    }

    private void startAutoUpdate() {
        runnable = new Runnable() {
            @Override
            public void run() {
                numCount.setText(String.valueOf(numberCount));
                upgrdBeansBtn.setEnabled(numberCount >= upgradeBeans && upgradeCount < 4);
                upgrdClickBtn.setEnabled(numberCount >= upgradeClick);
                handler.postDelayed(this, INTERVAL);
            }
        };
        handler.post(runnable);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startAutoUpdate();
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    private void showPopUp(float x, float y) {
        // Update the pop-up text with the click increment
        popUpText.setText("+" + clickIncrement);

        // Get the location of the touch event on the screen
        int[] screenLocation = new int[2];
        imageBeansBtn.getLocationOnScreen(screenLocation);

        // Calculate the absolute position for the pop-up
        float absoluteX = screenLocation[0] + x - (popUpText.getWidth() / 2);
        float absoluteY = screenLocation[1] + y - (popUpText.getHeight() / 2);

        // Position the pop-up correctly
        popUpText.setX(absoluteX);
        popUpText.setY(absoluteY);

        // Make the pop-up visible and start animations
        popUpText.setVisibility(View.VISIBLE);
        popUpText.setAlpha(1.0f);

        // Animate the pop-up moving upwards
        ObjectAnimator animator = ObjectAnimator.ofFloat(popUpText, "translationY", absoluteY, absoluteY - 100f);
        animator.setDuration(2000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                popUpText.setVisibility(View.GONE);
            }
        });

        animator.start();
        popUpText.animate().alpha(0.0f).setDuration(1000).start();
    }




}
