package com.example.project2_ecommerce_bibek_karki;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;
    private CartInteractionListener listener;

    public CartAdapter(Context context, List<CartItem> cartItemList, CartInteractionListener listener) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        holder.textProductName.setText(cartItem.getProduct().getName());
        holder.textProductPrice.setText(cartItem.getProduct().getPrice());
//        holder.textQuantity.setText("Quantity: " + cartItem.getQuantity());
        holder.editTextQuantity.setText(String.valueOf(cartItem.getQuantity()));

        // Plus Button Click Listener
        holder.btnPlus.setOnClickListener(v -> {
            int quantity = cartItem.getQuantity();
            quantity++;
            cartItem.setQuantity(quantity);
            holder.editTextQuantity.setText(String.valueOf(quantity));

            // Update the total price and notify the activity
            updateTotalPrice();

            // Save the updated cart to SharedPreferences
            saveCartItemsToSharedPreferences();
        });

        // Minus Button Click Listener
        holder.btnMinus.setOnClickListener(v -> {
            int quantity = cartItem.getQuantity();
            if (quantity > 1) {
                quantity--;
                cartItem.setQuantity(quantity);
                holder.editTextQuantity.setText(String.valueOf(quantity));

                // Update the total price and notify the activity
                updateTotalPrice();

                // Save the updated cart to SharedPreferences
                saveCartItemsToSharedPreferences();
            } else {
                // Remove the item from the cart when quantity becomes 0
                removeCartItem(position);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the position of the item that was clicked
                int position = holder.getAdapterPosition();

                // Ensure the position is valid
                if (position != RecyclerView.NO_POSITION) {
                    // Get the cart item at the clicked position
                    CartItem cartItem = cartItemList.get(position);

                    // Remove the item from the list
                    cartItemList.remove(position);
                    cartItemList.remove(cartItem);
                    listener.onItemRemoved(cartItem);
                    saveCartItemsToSharedPreferences();

                    // Notify the adapter of the item removal
                    notifyItemRemoved(position);

                    // notify for item range changed
                    notifyItemRangeChanged(position, cartItemList.size());

                    // Notify the listener that an item has been removed
                    if (listener != null) {
                        listener.onItemRemoved(cartItem);
                    }

                    // Display a toast message
                    Toast.makeText(context, "Removed: " + cartItem.getProduct().getName(), Toast.LENGTH_SHORT).show();
                    if (context instanceof CartActivity) {
                        ((CartActivity) context).calculateTotalPrice();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView textProductName, textProductPrice, textQuantity;
        EditText editTextQuantity;
        Button btnDelete;
        ImageButton btnPlus,btnMinus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            textProductName = itemView.findViewById(R.id.text_product_name);
            textProductPrice = itemView.findViewById(R.id.text_product_price);
            editTextQuantity = itemView.findViewById(R.id.edit_text_quantity);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnPlus = itemView.findViewById(R.id.btn_plus);
            btnMinus = itemView.findViewById(R.id.btn_minus);
        }
    }

    public interface CartInteractionListener {
        void onItemRemoved(CartItem cartItem);

    }

    private void removeCartItem(int position) {
        CartItem cartItem = cartItemList.get(position);
        cartItemList.remove(position);

        // Notify the adapter of the item removal
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cartItemList.size());

        // Notify the listener that an item has been removed
        if (listener != null) {
            listener.onItemRemoved(cartItem);
        }

        // Display a toast message
        Toast.makeText(context, "Removed: " + cartItem.getProduct().getName(), Toast.LENGTH_SHORT).show();

        // Update the total price and notify the activity
        updateTotalPrice();

        // Save the updated cart to SharedPreferences
        saveCartItemsToSharedPreferences();
    }

    private void updateTotalPrice() {
        if (context instanceof CartActivity) {
            ((CartActivity) context).calculateTotalPrice();
        }
    }
    private void saveCartItemsToSharedPreferences() {
        if (context instanceof CartActivity) {
            Log.e("fondo", "GOing to cart activity for updating ");
            ((CartActivity) context).saveCartItemsToSharedPreferences();
        }
    }
}
