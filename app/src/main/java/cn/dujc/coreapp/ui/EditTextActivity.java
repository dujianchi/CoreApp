package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.coreapp.R;

/**
 * @author du
 * date: 2019/3/13 6:03 PM
 */
public class EditTextActivity extends BaseActivity {

    @Override
    public int getViewId() {
        return R.layout.activity_edit_text;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean yes = "yes".equals(button.getText().toString());
                UnitStatus.getInstance().change(!yes);
                button.setText(yes ? "no" : "yes");
            }
        });
    }

    public static interface StatusChange {
        void enable(boolean yes);
    }

    public static final class UnitStatus {
        private final static UnitStatus INSTANCE = new UnitStatus();
        private final List<WeakReference<StatusChange>> mStatusChanges = new ArrayList<>();

        public static UnitStatus getInstance() {
            return INSTANCE;
        }

        public void register(StatusChange statusChange) {
            mStatusChanges.add(new WeakReference<>(statusChange));
        }

        public void unregister(StatusChange statusChange) {
            Iterator<WeakReference<StatusChange>> iterator = mStatusChanges.iterator();
            while (iterator.hasNext()) {
                final StatusChange next = iterator.next().get();
                if (next == null) {
                    iterator.remove();
                } else if (next == statusChange) {
                    iterator.remove();
                    break;
                }
            }
        }

        public void change(boolean enable) {
            Iterator<WeakReference<StatusChange>> iterator = mStatusChanges.iterator();
            while (iterator.hasNext()) {
                final StatusChange statusChange = iterator.next().get();
                if (statusChange == null) {
                    iterator.remove();
                } else {
                    statusChange.enable(enable);
                }
            }
        }
    }
}
