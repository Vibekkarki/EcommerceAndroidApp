package com.example.project2_ecommerce_bibek_karki;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

public class Cart {
    private static Cart instance;
    private List<CartItem> cartItems;
    private CartAdapter cartAdapter;
    private Context context;
    private ProductAdapter productAdapter;
    private Cart() {
        cartItems = new ArrayList<>();
    }

    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    // Setter for CartAdapter
    public void setProductAdapter(ProductAdapter productAdapter) {
        this.productAdapter = productAdapter;
    }

    // Setter for Context
    public void setContext(Context context) {
        this.context = context;
    }
    public void addToCart(Product product, int quantity) {
        // Check if the product is already in the cart
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        else{
            Log.e("seesam", "cart items has some objects");}
//        List<CartItem> cartItems = Cart.getInstance().getCartItems();
        boolean productFound = false;
        for (CartItem item : cartItems) {
            if(item.getProduct().getId() != null){Log.e("seesam", "yes items ho hoh hoo");}
            if(product.getId() != null){Log.e("seesam", "yes products ho hoh hoo");}
            if (item != null && item.getProduct() != null && product != null && product.getId() != null) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                productFound = true;
                break;
            }
            }
        }
        if (!productFound) {
            cartItems.add(new CartItem(product, quantity));
        }

        // Notify the adapter of data change
        if (productAdapter != null) {
            productAdapter.notifyDataSetChanged();
            Log.e("seesam", "notify completed");
        }

        // Optionally, save the cart items to SharedPreferences
        if (context != null) {
            saveCartItemsToSharedPreferences(context);
            Log.e("seesam", "notify completed 2");
        }
    }

    public void removeFromCart(CartItem cartItem) {
        cartItems.remove(cartItem);
    }

    public void clearCart() {
        if (cartItems != null) {
            cartItems.clear();
        }
    }

    private void saveCartItemsToSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(cartItems);
        editor.putString("cartItems", json);
        editor.apply();
    }
}