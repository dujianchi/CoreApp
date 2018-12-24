package cn.dujc.coreapp.widget;

import android.content.Context;
import android.widget.PopupWindow;

public class PopCreator {
    private int contentviewid;
    private int width;
    private int height;
    private boolean fouse;
    private boolean outsidecancel;
    private int animstyle;
    private Context context;

    public PopCreator setContext(Context context){
        this.context = context;
        return this;
    }

    public PopCreator setContentView(int contentviewid){
        this.contentviewid = contentviewid;
        return this;
    }

    public PopCreator setwidth(int width){
        this.width = width;
        return this;
    }
    public PopCreator setheight(int height){
        this.height = height;
        return this;
    }

    public PopCreator setFouse(boolean fouse){
        this.fouse = fouse;
        return this;
    }

    public PopCreator setOutSideCancel(boolean outsidecancel){
        this.outsidecancel = outsidecancel;
        return this;
    }
    public PopCreator setAnimationStyle(int animstyle){
        this.animstyle = animstyle;
        return this;
    }

    public PopupWindow builder(){
        final PopupWindow window = new PopupWindow(context);
        //window.setAnimationStyle();
        return window;
    }
}
