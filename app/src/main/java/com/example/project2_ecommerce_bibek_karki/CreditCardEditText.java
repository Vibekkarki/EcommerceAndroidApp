package com.example.project2_ecommerce_bibek_karki;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class CreditCardEditText extends AppCompatEditText {

    private static final int CREDIT_CARD_LENGTH = 16;

    public CreditCardEditText(Context context) {
        super(context);
        init();
    }

    public CreditCardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CreditCardEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        setFilters(new InputFilter[] { new InputFilter.LengthFilter(CREDIT_CARD_LENGTH) });
    }

    public String getCreditCardNumber() {
        return getText().toString().replaceAll("\\s+", "");
    }
}
