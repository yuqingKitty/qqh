package com.leuters.qqh.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.leuters.qqh.R;
import com.leuters.qqh.utils.ScreenUtil;

public class LoginInputView extends RelativeLayout implements View.OnClickListener {
    private View rootView;
    private EditText edtRegisterPhone;
    private ImageView ivClear;

    private View driver1;

    private String hintText;
    private int hintColor;

    private Drawable drawableLeft;
    private Drawable drawableTure;
    private Drawable drawableFalse;
    private float drawPadding;
    private boolean isShowDriver;

    private int curcorCOlor;
    private float paddingTop;
    private boolean isEmptyContent = true;
    private boolean isDefaultIcon;
    private TextWatcher mTextwatcher;


    public LoginInputView(Context context) {
        super(context, null);
    }

    public LoginInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            rootView = LayoutInflater.from(context).inflate(R.layout.login_input_view, null);
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.loinInput);
            hintText = a.getString(R.styleable.loinInput_loginInputHint);
            hintColor = a.getColor(R.styleable.loinInput_loginInputHintcolor, Color.parseColor("#C6C9CC"));
            drawableLeft = a.getDrawable(R.styleable.loinInput_loginInputDrawable);
            drawableTure = a.getDrawable(R.styleable.loinInput_loginInputDrawableTure);
            drawableFalse = a.getDrawable(R.styleable.loinInput_loginInputDrawableFalse);
            drawPadding = a.getDimension(R.styleable.loinInput_loginInputDrawPadding, ScreenUtil.dp2px(context, 5));
            isShowDriver = a.getBoolean(R.styleable.loinInput_loginInputisDriver, true);
            isDefaultIcon = a.getBoolean(R.styleable.loinInput_loginInputIconDefault, false);
            paddingTop = a.getDimension(R.styleable.loinInput_login_padding_top,ScreenUtil.dp2px(context, 5));
            a.recycle();
            addView(rootView);
            initView();
            initData();
        }
    }

    private void initData() {
        if (!TextUtils.isEmpty(hintText)) {
            edtRegisterPhone.setHint(hintText);
        }
        edtRegisterPhone.setHintTextColor(hintColor);
        edtRegisterPhone.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
        edtRegisterPhone.setCompoundDrawablePadding(new Float(drawPadding).intValue());
        if (isShowDriver) {
            driver1.setVisibility(View.VISIBLE);
        } else {
            driver1.setVisibility(View.GONE);
        }
        edtRegisterPhone.setPadding(0,new Float(paddingTop).intValue(),0,new Float(paddingTop).intValue());
        ivClear.setVisibility(View.GONE);
        ivClear.setOnClickListener(this);
        mTextwatcher=new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (edtRegisterPhone.getInputType() == InputType.TYPE_CLASS_NUMBER) {
//                    if (s == null || s.length() == 0) return;
//                    StringBuilder sb = new StringBuilder();
//                    for (int i = 0; i < s.length(); i++) {
//                        if (i != 3 && i != 8 && s.charAt(i) == ' ') {
//                            continue;
//                        } else {
//                            sb.append(s.charAt(i));
//                            if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
//                                sb.insert(sb.length() - 1, ' ');
//                            }
//                        }
//                    }
//                    if (!sb.toString().equals(s.toString())) {
//                        int index = start + 1;
//                        if (sb.charAt(start) == ' ') {
//                            if (before == 0) {
//                                index++;
//                            } else {
//                                index--;
//                            }
//                        } else {
//                            if (before == 1) {
//                                index--;
//                            }
//                        }
//                        edtRegisterPhone.setText(sb.toString());
//                        edtRegisterPhone.setSelection(index);
//                    }
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (hasInputContent()) {
                    if (edtRegisterPhone.isFocused()) {
                        ivClear.setVisibility(View.VISIBLE);
                    }
                    isEmptyContent = false;   // 不为空
                } else {
                    isEmptyContent = true;    // 为空
                    ivClear.setVisibility(View.GONE);
                }
                if(isDefaultIcon){
                    if(!TextUtils.isEmpty(edtRegisterPhone.getText())){
                        edtRegisterPhone.setCompoundDrawablesWithIntrinsicBounds(drawableTure,null,null,null);
                    }else {
                        edtRegisterPhone.setCompoundDrawablesWithIntrinsicBounds(drawableFalse,null,null,null);
                    }
                    if(edtRegisterPhone.isFocused())
                        edtRegisterPhone.setCompoundDrawablesWithIntrinsicBounds(drawableTure,null,null,null);
                }

                if (loginInputCallback != null) {
                    loginInputCallback.isEditTextEmpty(LoginInputView.this, isEmptyContent);
                }
            }
        };
        edtRegisterPhone.addTextChangedListener(mTextwatcher);
        edtRegisterPhone.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    ivClear.setVisibility(View.GONE);
                    if(isDefaultIcon){
                        if (hasInputContent()) {
                            edtRegisterPhone.setCompoundDrawablesWithIntrinsicBounds(drawableTure,null,null,null);
                        }else {
                            edtRegisterPhone.setCompoundDrawablesWithIntrinsicBounds(drawableFalse,null,null,null);
                        }
                    }
                } else {
                    if (hasInputContent()) {
                        ivClear.setVisibility(View.VISIBLE);
                        edtRegisterPhone.setSelection(edtRegisterPhone.getText().length());
                    }
                    edtRegisterPhone.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    if(isDefaultIcon)
                        edtRegisterPhone.setCompoundDrawablesWithIntrinsicBounds(drawableTure,null,null,null);
                }
                /*if(isDefaultIcon){
                    if(hasFocus){
                        edtRegisterPhone.setCompoundDrawablesWithIntrinsicBounds(drawableTure,null,null,null);
                    }else {
                        edtRegisterPhone.setCompoundDrawablesWithIntrinsicBounds(drawableFalse,null,null,null);
                    }
                }*/
            }
        });
    }

    private void initView() {
        edtRegisterPhone = (EditText) findViewById(R.id.edtRegisterPhone);
        ivClear = (ImageView) findViewById(R.id.ivClearInput);
        driver1 = findViewById(R.id.driver1);
    }


    public LoginInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivClearInput:
                if (loginInputCallback != null) {
                    loginInputCallback.isClearEditRegisterPhone(LoginInputView.this);
                }
                break;
        }
    }

    public void clearText() {
        edtRegisterPhone.setText("");
        ivClear.setVisibility(View.GONE);
    }

    /**
     * remove textwatcher
     */
    public void removeTextwachter(){
        if(mTextwatcher==null)
            return;
        edtRegisterPhone.removeTextChangedListener(mTextwatcher);
    }
    public interface LoginInputCallback {
        public void isEditTextEmpty(LoginInputView view, boolean isContentEmpty);

        public void isClearEditRegisterPhone(LoginInputView view);
    }

    private LoginInputCallback loginInputCallback;

    public void setOnLoginInputListener(LoginInputCallback loginInputListener) {
        this.loginInputCallback = loginInputListener;
    }


    public String getEditRegisterPhone() {
        return edtRegisterPhone.getText().toString().trim().replaceAll(" ", "");
    }

    public boolean hasInputContent() {
        return edtRegisterPhone != null && edtRegisterPhone.getText().length() > 0;
    }

    public void setEditRegisterPhone(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        edtRegisterPhone.setText(text);
    }

    public EditText getEdtRegisterPhone() {
        return edtRegisterPhone;
    }

    public void setEdtRegisterPhone(EditText edtRegisterPhone) {
        this.edtRegisterPhone = edtRegisterPhone;
    }

    public void setIvClearVisible() {
        ivClear.setVisibility(View.GONE);
    }

    public ImageView getIvClear() {
        return ivClear;
    }

    public void setMaxLength(int maxLength) {
        edtRegisterPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    public void setEditPadding(int left, int top, int right, int bottom) {
        edtRegisterPhone.setPadding(left, top, right, bottom);
    }


    @Override
    public void setEnabled(boolean enabled) {
        if(!enabled){
            ivClear.setEnabled(enabled);
            edtRegisterPhone.setEnabled(enabled);
            edtRegisterPhone.clearFocus();
            ivClear.setVisibility(View.GONE);
        }
        super.setEnabled(enabled);
    }
}
