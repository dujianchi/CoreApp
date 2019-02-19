package cn.dujc.core.ui.func;

import android.widget.PopupWindow;

import cn.dujc.core.ui.BaseDialog;
import cn.dujc.core.ui.BaseDialogFragment;

public interface IDialog {
    void _dismiss();

    boolean _dismissible();

    public static class BaseDialogImpl implements IDialog {
        private final BaseDialog mDialog;

        public BaseDialogImpl(BaseDialog dialog) {
            mDialog = dialog;
        }

        @Override
        public void _dismiss() {
            if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
        }

        @Override
        public boolean _dismissible() {
            return mDialog != null && mDialog.isCancelable();
        }
    }

    public static class PopupWindowImpl implements IDialog {
        private final PopupWindow mWindow;

        public PopupWindowImpl(PopupWindow window) {
            mWindow = window;
        }

        @Override
        public void _dismiss() {
            if (mWindow != null && mWindow.isShowing()) mWindow.dismiss();
        }

        @Override
        public boolean _dismissible() {
            return mWindow != null && mWindow.isOutsideTouchable();
        }
    }

    public static class DialogFragmentImpl implements IDialog {
        private final BaseDialogFragment mDialogFragment;

        public DialogFragmentImpl(BaseDialogFragment dialogFragment) {
            mDialogFragment = dialogFragment;
        }

        @Override
        public void _dismiss() {
            if (mDialogFragment != null && mDialogFragment.isShowing()) mDialogFragment.dismiss();
        }

        @Override
        public boolean _dismissible() {
            return mDialogFragment != null && mDialogFragment.isCanceledOnTouchOutside();
        }
    }
}
