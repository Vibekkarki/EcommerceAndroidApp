package com.example.project2_ecommerce_bibek_karki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import android.widget.Button;

import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imageProductDetail;
    private TextView textProductName, textProductPrice, textProductDescription;
    private EditText editTextQuantity;
    private Button btnAddToCartDetail;

    private Button btnGoToCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Initialize Views
        imageProductDetail = findViewById(R.id.image_product_detail);
        textProductName = findViewById(R.id.text_product_name_detail);
        textProductPrice = findViewById(R.id.text_product_price_detail);
        textProductDescription = findViewById(R.id.text_product_description_detail);
        editTextQuantity = findViewById(R.id.edit_text_quantity);
        btnAddToCartDetail = findViewById(R.id.btn_add_to_cart_detail);
        btnGoToCart = findViewById(R.id.btn_go_to_cart);
        ImageButton btnPlus = findViewById(R.id.btn_plus);
        ImageButton btnMinus = findViewById(R.id.btn_minus);

        // Get product details from intent
        if (getIntent().hasExtra("product")) {
            Product product = getIntent().getParcelableExtra("product");

            // Set product details to views
//            imageProductDetail.setImageResource(product.getImageResourceId());
            Glide.with(this)
                    .load(product.getImageUrl())
                    .into(imageProductDetail);
            textProductName.setText(product.getName());
            textProductPrice.setText(product.getPrice());
            textProductDescription.setText(product.getDescription());

            // Add to Cart Button Click Listener
            btnAddToCartDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get quantity entered by the user
                    String quantityString = editTextQuantity.getText().toString().trim();
                    if (quantityString.isEmpty()) {
                        Toast.makeText(ProductDetailActivity.this, "Please enter quantity", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int quantity = Integer.parseInt(quantityString);
                    if (quantity <= 0) {
                        Toast.makeText(ProductDetailActivity.this, "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Here, you would add the product to the cart with the selected quantity
                    // For example: display a toast message
                    // Toast.makeText(ProductDetailActivity.this, "Added to Cart: " + product.getName() + " Quantity: " + quantity, Toast.LENGTH_SHORT).show();
                    addToCart(product, quantity);                }
            });

            // "Go to Cart" Button Click Listener
            btnGoToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open CartActivity
                    Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                    startActivity(intent);
                }
            });

            Button btnContinueShopping = findViewById(R.id.btn_continue_shopping_detail);
            btnContinueShopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Finish the CartActivity and go back to the ProductActivity
                    finish();
                }
            });

            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(editTextQuantity.getText().toString());
                    quantity++;
                    editTextQuantity.setText(String.valueOf(quantity));
                }
            });

            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(editTextQuantity.getText().toString());
                    if (quantity > 1) {
                        quantity--;
                        editTextQuantity.setText(String.valueOf(quantity));
                    }
                }
            });

        }
    }
    private void addToCart(Product product, int quantity) {
        Cart.getInstance().addToCart(product, quantity);
        // Add logic to add the product to the cart
        // For now, we'll display a toast message
        String message = "Added to Cart: " + product.getName() + " Quantity: " + quantity;
        Toast.makeText(ProductDetailActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}