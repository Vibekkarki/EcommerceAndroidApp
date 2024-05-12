package com.example.project2_ecommerce_bibek_karki;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartInteractionListener {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private TextView textTotalPrice;
    private Button btnCheckout;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recycler_view_cart);
        textTotalPrice = findViewById(R.id.text_total_price);
        btnCheckout = findViewById(R.id.btn_checkout);
        sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE);

        cartItemList = getCartItemsFromSharedPreferences();
        if (cartItemList == null) {
            cartItemList = new ArrayList<>();
            Log.e("karki", "Product object is null when adding to gii");
        } else {
            // If there are existing items, notify the adapter of data change
//            cartAdapter.notifyDataSetChanged();
            Log.e("dungooo", "Product object is not null when adding to gii"+ cartItemList);
        }
        Log.e("rumba", "completed step at cart activity ");
        cartAdapter = new CartAdapter(this, cartItemList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cartAdapter);

        calculateTotalPrice();

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Proceed to CheckoutActivity
                Toast.makeText(CartActivity.this, "Proceeding to Checkout", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                startActivity(intent);
                clearCart();
            }
        });

        Button btnContinueShopping = findViewById(R.id.btn_continue_shopping);
        btnContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCartItemsToSharedPreferences();
                finish();
            }
        });
    }
    private void clearCart() {
        // Clear the cartItemList
        cartItemList.clear();

        // Notify the adapter of the data change
        cartAdapter.notifyDataSetChanged();

        // Clear cart from SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("cartItems");
        editor.apply();

        // Recalculate the total price
        calculateTotalPrice();

    }

    private List<CartItem> getCartItemsFromSharedPreferences() {
        String json = sharedPreferences.getString("cartItems", null);
        if (json == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<CartItem>>() {}.getType();
        return gson.fromJson(json, type);
    }

    protected void saveCartItemsToSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(cartItemList);
        editor.putString("cartItems", json);
        editor.apply();
    }

    void calculateTotalPrice() {
        double totalPrice = 0;
        if (cartItemList != null) {
            for (CartItem cartItem : cartItemList) {
                Product product = cartItem.getProduct();
                if (product != null) {
                    // Remove the currency symbol before parsing
                    String priceString = product.getPrice().replace("$", "");
                    double productPrice = Double.parseDouble(priceString);
                    totalPrice += productPrice * cartItem.getQuantity();
                }
            }
        }
        textTotalPrice.setText(String.format("Total Price: $%.2f", totalPrice));
    }


    @Override
    protected void onStop() {
        super.onStop();
        saveCartItemsToSharedPreferences();
    }

    public void addToCart(Product product) {
        CartItem cartItem = new CartItem(product, 1);
        if (cartItemList == null) {
            cartItemList = new ArrayList<>();
        }
        cartItemList.add(cartItem);
        cartAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Added to Cart: " + product.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemRemoved(CartItem cartItem) {
        // Remove the item from the list
        cartItemList.remove(cartItem);

        // Notify the adapter of the data change
        cartAdapter.notifyDataSetChanged();

        // Recalculate the total price
        calculateTotalPrice();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
