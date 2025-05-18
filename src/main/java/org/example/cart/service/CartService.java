package org.example.cart.service;

import org.example.cart.model.Product;
import org.example.cart.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartService {
    
    @Autowired
    private FileCartService fileCartService;

    @Autowired
    private FileProductService fileProductService;

    // Cart Items methods
    public List<CartItem> getAllCartItems() {
        return fileCartService.getAllCartItems();
    }

    public CartItem addToCart(Long productId, int quantity) {
        return fileCartService.addToCart(productId, quantity);
    }

    public void removeFromCart(Long cartItemId) {
        fileCartService.removeFromCart(cartItemId);
    }

    public CartItem updateCartItem(CartItem cartItem) {
        fileCartService.updateCartItem(cartItem);
        return cartItem;
    }

    public CartItem getCartItem(Long id) {
        return fileCartService.getCartItem(id);
    }

    // Product methods
    public List<Product> getAllProducts() {
        return fileProductService.getAllProducts();
    }

    public void addProduct(Product product) {
        fileProductService.addProduct(product);
    }

    public Product getProduct(Long id) {
        return fileProductService.getProduct(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void updateProduct(Product product) {
        fileProductService.updateProduct(product);
    }

    public void deleteProduct(Long id) {
        fileProductService.deleteProduct(id);
    }
}