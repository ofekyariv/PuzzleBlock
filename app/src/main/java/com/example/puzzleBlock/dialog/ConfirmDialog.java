package com.example.puzzleBlock.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.puzzleBlock.R;
import com.example.puzzleBlock.common.GameConfig;
import com.example.puzzleBlock.common.ResourceManager;


public class ConfirmDialog extends Dialog {

    private final TextView mTextViewMessage;
    private boolean isCallDismissCallback = true;

    public ConfirmDialog(@NonNull Context context, final ConfirmCallback confirmCallback) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (isCallDismissCallback) {
                    confirmCallback.cancel();
                }
                isCallDismissCallback = true;
            }
        });
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.getAttributes().windowAnimations = R.style.AnimationDialog;
        }
        setContentView(R.layout.dialog_confirm);
        mTextViewMessage = findViewById(R.id.textViewMessage);
        findViewById(R.id.textViewCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GameConfig.getInstance().getSoundAvailable()) {
                    ResourceManager.getInstance().playButtonClickSound();
                }
                confirmCallback.cancel();
                isCallDismissCallback = false;
                ConfirmDialog.this.dismiss();
            }
        });
        findViewById(R.id.textViewOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GameConfig.getInstance().getSoundAvailable()) {
                    ResourceManager.getInstance().playButtonClickSound();
                }
                confirmCallback.ok();
                isCallDismissCallback = false;
                ConfirmDialog.this.dismiss();
            }
        });
    }

    public void show(String message) {
        mTextViewMessage.setText(message);
        super.show();
    }
}
