package study1.kimjy.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import study1.kimjy.R;
import study1.kimjy.utils.Utils;

public class CommonDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = CommonDialog.class.getSimpleName();
    private static boolean mChecked = false;
    public boolean mainExit = false;
    private Context mContext;
    private Button cancel, ok, complete;
    private TextView title, message;
    private ListView listView;
    private CheckBox checkBox;
    private ImageButton mCloseBtn;

    private View.OnClickListener listenerCancel, listenerOk, listenerClose;


    public CommonDialog(Context context) {
        super(context, R.style.popup_update);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_common);
        getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, android.view.WindowManager.LayoutParams.MATCH_PARENT);
        initLayout();
    }

    private void initLayout() {
        cancel = (Button) findViewById(R.id.cancel);
        ok = (Button) findViewById(R.id.ok);
        complete = (Button) findViewById(R.id.complete);
        title = (TextView) findViewById(R.id.title);
        message = (TextView) findViewById(R.id.message);
//        listView = (ListView) findViewById(R.id.listview);
        checkBox = (CheckBox) findViewById(R.id.popup_checkbox);
        mCloseBtn = (ImageButton) findViewById(R.id.ib_close);
        mCloseBtn.setOnClickListener(this);
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
        complete.setOnClickListener(this);
    }

    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
    }

    public void setCloseButton(boolean isvisible) {
        if(!isvisible) {
            mCloseBtn.setVisibility(View.GONE);
        }
    }

    public void setNegativeButton(int resId, View.OnClickListener listener) {
        cancel.setText(resId);
        cancel.setVisibility(View.VISIBLE);
        listenerCancel = listener;
    }

    public void setNegativeButton(String text, View.OnClickListener listener) {
        cancel.setText(text);
        cancel.setVisibility(View.VISIBLE);
        listenerCancel = listener;
    }

    public void setPositiveButton(int resId, View.OnClickListener listener) {
        ok.setText(resId);
        ok.setVisibility(View.VISIBLE);
        listenerOk = listener;
    }

    public void setPositiveButton(String text, View.OnClickListener listener) {
        ok.setText(text);
        ok.setVisibility(View.VISIBLE);
        listenerOk = listener;
    }

    public void setCloseListener(View.OnClickListener listener) {
        listenerClose = listener;
    }

    public void setCompleteButton(int resId, View.OnClickListener listener) {
        complete.setText(resId);
        complete.setVisibility(View.VISIBLE);
        listenerCancel = listener;
    }

    public void setCompleteButton(String text, View.OnClickListener listener) {
        complete.setText(text);
        complete.setVisibility(View.VISIBLE);
        listenerCancel = listener;
    }

    public void setTitle(int resId) {
        title.setText(resId);
        title.setVisibility(View.VISIBLE);
    }

    public void setTitle(String text) {
        title.setText(text);
        title.setVisibility(View.VISIBLE);
    }

    public void setMessage(int resId) {
        message.setText(resId);
        message.setVisibility(View.VISIBLE);
    }

    public void setMessage(String text) {
        message.setText(text);
        message.setVisibility(View.VISIBLE);
    }

    public Boolean getCheckBox() {
        return checkBox.isChecked();
    }

    public void setCheckBox(Boolean checked) {
        checkBox.setVisibility(View.VISIBLE);
        mChecked = checked;
    }

    @Override
    public void onBackPressed() {
        if (mainExit) {
            Utils.killProcess(mContext);
        } else {
            super.setCancelable(true);
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (v == cancel) {
            if (listenerCancel != null)
                listenerCancel.onClick(cancel);
        } else if (v == ok) {
            if (listenerOk != null)
                listenerOk.onClick(ok);
        } else if (v == complete) {
            if (listenerOk != null)
                listenerOk.onClick(ok);
        } else if (v == mCloseBtn) {
            if (listenerClose != null)
                listenerClose.onClick(v);
        }

    }

}
