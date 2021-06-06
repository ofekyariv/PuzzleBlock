package com.example.puzzleBlock.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.puzzleBlock.R;
import com.example.puzzleBlock.common.GameConfig;
import com.example.puzzleBlock.common.ResourceManager;


public class InformDialog extends Dialog {

    private final TextView mTextViewMessage;

    public InformDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.getAttributes().windowAnimations = R.style.AnimationDialog;
        }
        setContentView(R.layout.dialog_inform);
        mTextViewMessage = findViewById(R.id.textViewMessage);
        findViewById(R.id.textViewOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GameConfig.getInstance().getSoundAvailable()) {
                    ResourceManager.getInstance().playButtonClickSound();
                }
                InformDialog.this.dismiss();
            }
        });
    }

    public void show(String message) {
        mTextViewMessage.setText(message);
        super.show();
    }
}
