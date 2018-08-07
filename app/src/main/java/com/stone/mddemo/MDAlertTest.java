package com.stone.mddemo;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.widget.Toast;

import com.stone.mdlib.MDAlert;

/**
 * Created By: sqq
 * Created Time: 8/7/18 3:03 PM.
 */
public class MDAlertTest {
    public static void showSingle(Context ctx, String msg) {
        new MDAlert(ctx, msg).show();
    }

    public static void showSelect(final Context ctx, String msg) {
        MDAlert.Companion.setDefaultPositiveColor(Color.RED);
        new MDAlert(ctx, msg).setCancel()
                .setPositiveListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ctx, "click confirm", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }
}
