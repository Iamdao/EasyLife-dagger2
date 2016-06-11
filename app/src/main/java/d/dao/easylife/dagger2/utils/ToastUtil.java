package d.dao.easylife.dagger2.utils;

import android.content.Context;
import android.widget.Toast;

import javax.inject.Inject;

import d.dao.easylife.dagger2.scopes.ActivityScope;

/**
 * 管理toast的类，整个app用该类来显示toast
 * Created by niuxiaowei on 16/3/22.
 */
@ActivityScope
public class ToastUtil {

    private Context mContext;

    @Inject
    public ToastUtil(Context context){
        this.mContext = context;
    }

    public void showToast(String message){
        Toast.makeText(mContext,message,Toast.LENGTH_LONG).show();
    }

}
