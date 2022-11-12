package com.loadapp.load.dialog;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

/**
 * @author xu.wang
 * @date 2019/12/16 11:53
 * @desc
 */
public class BaseDialog extends Dialog {
    public BaseDialog(@NonNull Context context) {
        super(context);
        initialize();
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initialize();
    }

    private void initialize() {
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private Application.ActivityLifecycleCallbacks callback = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
//            Log.e("BaseDialog","on activity stopped ...");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (getContext() instanceof Activity){
                Activity activity1 = (Activity) getContext();
                if (activity1 == activity && isShowing()) {
                    dismiss();
                }
            }

        }
    };
}
