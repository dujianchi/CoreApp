package cn.dujc.core.bridge;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

/**
 * 将数据获取等逻辑放在这里
 * 这样的好处就是，我可以将某个功能抽出来，别的地方需要这样的功能时，我只要重新new一下实现方法就可以了
 * Created by lucky on 2017/11/8.
 */
public interface IModel {

    @MainThread
    void onCallback(@NonNull Arg arg);

}
