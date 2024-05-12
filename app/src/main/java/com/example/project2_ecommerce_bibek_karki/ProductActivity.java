package com.example.project2_ecommerce_bibek_karki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import android.content.SharedPreferences;

public class ProductActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        recyclerView = findViewById(R.id.recycler_view_products);
        recyclerView.setHasFixedSize(true);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape Mode
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            // Portrait Mode
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE);

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(productAdapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Fetch products from Firestore
        fetchProducts();
        Button btnViewCart = findViewById(R.id.btn_view_cart);
        btnViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
        // Initialize the Cart instance
        Cart cart = Cart.getInstance();
        cart.setProductAdapter(productAdapter); // Set the cartAdapter
        cart.setContext(this);
    }

    private void fetchProducts() {
        db.collection("Product")
                .orderBy("name", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Product product = document.toObject(Product.class);
                        if (product != null) {
                            productList.add(product);
                        }
                    }
                    productAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProductActivity.this, "Error fetching products: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void addToCarts(Product product, int quantity) {
        // Check if the cart is null (first time adding a product)
        if (Cart.getInstance().getCartItems() == null) {
            List<CartItem> cartItems = new ArrayList<>();
            cartItems.add(new CartItem(product, quantity)); // Add the product with quantity 1
            Cart.getInstance().setCartItems(cartItems); // Set the cart items
        } else {
            List<CartItem> cartItems = Cart.getInstance().getCartItems();
            boolean productFound = false;
            // Iterate through existing items in the cart
            for (CartItem item : cartItems) {
                if (item != null && item.getProduct() != null && product != null && product.getId() != null) {
                    if (item.getProduct().getId() != null && item.getProduct().getId().equals(product.getId())) {
                        // Product already exists in the cart, increment the quantity
                        item.incrementQuantity();
                        productFound = true;
                        break;
                    }
                }
            }
            // If product is not already in the cart, add it as a new item
            if (!productFound) {
                cartItems.add(new CartItem(product, 1));
            }
            Cart.getInstance().setCartItems(cartItems); // Update the cart items
        }
        productAdapter.notifyDataSetChanged();
        saveCartItemsToSharedPreferences();
        // Display a toast message confirming the addition
        Toast.makeText(this, "Added to Cart: " + product.getName(), Toast.LENGTH_SHORT).show();
    }

    private void saveCartItemsToSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Cart.getInstance().getCartItems());
        editor.putString("cartItems", json);
        editor.apply();
    }

}