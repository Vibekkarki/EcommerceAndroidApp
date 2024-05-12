package com.example.project2_ecommerce_bibek_karki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CheckoutActivity extends AppCompatActivity {

    private EditText editTextFirstName, editTextLastName, editTextAddress, editTextEmail, editTextPhone;
    private RadioGroup radioGroupPayment;
    private RadioButton radioButtonCreditCard, radioButtonDebitCard, radioButtonPaymentPortal;
    private Button btnSubmitOrder;

    // Views for Credit Card Payment Form
    private View layoutCreditCard;
    private CreditCardEditText editTextCreditCardNumber;
    private EditText editTextCreditCardExpiry, editTextCreditCardCVV;

    // Views for Debit Card Payment Form
    private View layoutDebitCard;
    private CreditCardEditText editTextDebitCardNumber;
    private EditText editTextDebitCardExpiry, editTextDebitCardCVV;

    // Views for Payment Portal Form
    private View layoutPaymentPortal;
    private EditText editTextPaymentPortalUsername, editTextPaymentPortalPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Initialize Views
        editTextFirstName = findViewById(R.id.edit_text_first_name);
        editTextLastName = findViewById(R.id.edit_text_last_name);
        editTextAddress = findViewById(R.id.edit_text_address);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPhone = findViewById(R.id.edit_text_phone);
        radioGroupPayment = findViewById(R.id.radio_group_payment);
        radioButtonCreditCard = findViewById(R.id.radio_credit_card);
        radioButtonDebitCard = findViewById(R.id.radio_debit_card);
        radioButtonPaymentPortal = findViewById(R.id.radio_payment_portal);
        btnSubmitOrder = findViewById(R.id.btn_submit_order);

        // Initialize Payment Forms
        layoutCreditCard = findViewById(R.id.layout_credit_card);
        editTextCreditCardNumber = findViewById(R.id.edit_text_credit_card_number);
        editTextCreditCardExpiry = findViewById(R.id.edit_text_credit_card_expiry);
        editTextCreditCardCVV = findViewById(R.id.edit_text_credit_card_cvv);

        layoutDebitCard = findViewById(R.id.layout_debit_card);
        editTextDebitCardNumber = findViewById(R.id.edit_text_debit_card_number);
        editTextDebitCardExpiry = findViewById(R.id.edit_text_debit_card_expiry);
        editTextDebitCardCVV = findViewById(R.id.edit_text_debit_card_cvv);

        layoutPaymentPortal = findViewById(R.id.layout_payment_portal);
        editTextPaymentPortalUsername = findViewById(R.id.edit_text_payment_portal_username);
        editTextPaymentPortalPassword = findViewById(R.id.edit_text_payment_portal_password);

        // Initially Hide Payment Forms
        layoutCreditCard.setVisibility(View.GONE);
        layoutDebitCard.setVisibility(View.GONE);
        layoutPaymentPortal.setVisibility(View.GONE);

        // RadioGroup Listener for Payment Method Selection
        radioGroupPayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_credit_card) {
                    showCreditCardForm();
                } else if (checkedId == R.id.radio_debit_card) {
                    showDebitCardForm();
                } else if (checkedId == R.id.radio_payment_portal) {
                    showPaymentPortalForm();
                }
            }
        });

        // Submit the Order Button Click Listener
        btnSubmitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input values
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String address = editTextAddress.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String phone = editTextPhone.getText().toString().trim();
                String paymentMethod = getSelectedPaymentMethod();

                // Validate input
                if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || email.isEmpty() || phone.isEmpty() || paymentMethod.isEmpty()) {
                    Toast.makeText(CheckoutActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Validate email address
                if (!isValidEmail(email)) {
                    Toast.makeText(CheckoutActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidPhoneNumber(phone)) {
                    Toast.makeText(CheckoutActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                switch (paymentMethod) {
                    case "Credit Card":
                        if (!isValidCreditCard()) {
                            Toast.makeText(CheckoutActivity.this, "Please enter a valid credit card information", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        break;
                    case "Debit Card":
                        if (!isValidDebitCard()) {
                            Toast.makeText(CheckoutActivity.this, "Please enter a valid debit card information", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        break;
                    case "Payment Portal":
                        if (!isValidPaymentPortal()) {
                            Toast.makeText(CheckoutActivity.this, "Please enter valid payment portal information", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        break;
                }

                String paymentInfo = "";
                switch (paymentMethod) {
                    case "Credit Card":
                        paymentInfo = "Credit Card Number: " + editTextCreditCardNumber.getCreditCardNumber() +
                                "\nExpiry Date: " + editTextCreditCardExpiry.getText().toString().trim() +
                                "\nCVV: " + editTextCreditCardCVV.getText().toString().trim();
                        break;
                    case "Debit Card":
                        paymentInfo = "Debit Card Number: " + editTextDebitCardNumber.getCreditCardNumber() +
                                "\nExpiry Date: " + editTextDebitCardExpiry.getText().toString().trim() +
                                "\nCVV: " + editTextDebitCardCVV.getText().toString().trim();
                        break;
                    case "Payment Portal":
                        paymentInfo = "Username: " + editTextPaymentPortalUsername.getText().toString().trim() +
                                "\nPassword: " + editTextPaymentPortalPassword.getText().toString().trim();
                        break;
                }

                String message = "Order submitted!\nFirst Name: " + firstName +
                        "\nLast Name: " + lastName +
                        "\nAddress: " + address +
                        "\nEmail: " + email +
                        "\nPhone: " + phone +
                        "\nPayment Method: " + paymentMethod +
                        "\nPayment Info:\n" + paymentInfo;
                Toast.makeText(CheckoutActivity.this, message, Toast.LENGTH_LONG).show();
//                 cartItemList.clear();
//                 cartAdapter.notifyDataSetChanged();
                Intent intent = new Intent(CheckoutActivity.this, ThankYouActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private String getSelectedPaymentMethod() {
        int selectedId = radioGroupPayment.getCheckedRadioButtonId();
        if (selectedId == radioButtonCreditCard.getId()) {
            return "Credit Card";
        } else if (selectedId == radioButtonDebitCard.getId()) {
            return "Debit Card";
        } else if (selectedId == radioButtonPaymentPortal.getId()) {
            return "Payment Portal";
        }
        return "";
    }

    private void showCreditCardForm() {
        layoutCreditCard.setVisibility(View.VISIBLE);
        layoutDebitCard.setVisibility(View.GONE);
        layoutPaymentPortal.setVisibility(View.GONE);
    }

    private void showDebitCardForm() {
        layoutCreditCard.setVisibility(View.GONE);
        layoutDebitCard.setVisibility(View.VISIBLE);
        layoutPaymentPortal.setVisibility(View.GONE);
    }

    private void showPaymentPortalForm() {
        layoutCreditCard.setVisibility(View.GONE);
        layoutDebitCard.setVisibility(View.GONE);
        layoutPaymentPortal.setVisibility(View.VISIBLE);
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidCreditCard() {
        String creditCardNumber = editTextCreditCardNumber.getCreditCardNumber();
        return creditCardNumber.length() == 16;
    }

    private boolean isValidDebitCard() {
        String debitCardNumber = editTextDebitCardNumber.getCreditCardNumber();
        return debitCardNumber.length() == 16;
    }

    private boolean isValidPaymentPortal() {
        String username = editTextPaymentPortalUsername.getText().toString().trim();
        String password = editTextPaymentPortalPassword.getText().toString().trim();
        return !username.isEmpty() && !password.isEmpty();
    }

    private boolean isValidPhoneNumber(String phone) {
        // Check if phone number is not empty
        if (phone.isEmpty()) {
            return false;
        }

        // Check if phone number contains only digits
        if (!phone.matches("\\d+")) {
            return false;
        }

        // Check if phone number is of a specific length
        if (phone.length() != 10) {
            return false;
        }

        return true;
    }
}
