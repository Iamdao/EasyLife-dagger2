package d.dao.easylife.dagger2.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by dao on 5/23/16.
 * intent跳转管理
 */
public class NavigationManager {


    public void gotoActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    public void gotoActivityWithExtraString(Context context, Class<?> cls, String flag, String value) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(flag, value);
        context.startActivity(intent);
    }

    public void gotoActivity(Context context, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(context, cls);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
