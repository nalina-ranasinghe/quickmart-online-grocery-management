package com.quickmart.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    private final CartQueue cartQueue;

    @Autowired
    public CartController(CartQueue cartQueue) {
        this.cartQueue = cartQueue;
    }

    @GetMapping
    public String showCart(Model model) {
        model.addAttribute("cartItems", cartQueue.getAllItems());
        model.addAttribute("totalPrice", cartQueue.getTotalPrice());
        model.addAttribute("totalItems", cartQueue.getTotalItems());
        return "cart";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(@RequestBody CartItem item) {
        cartQueue.addItem(item);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "totalItems", cartQueue.getTotalItems(),
            "totalPrice", cartQueue.getTotalPrice()
        ));
    }

    @PostMapping("/remove/{itemId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeFromCart(@PathVariable Long itemId) {
        cartQueue.removeItem(itemId);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "totalItems", cartQueue.getTotalItems(),
            "totalPrice", cartQueue.getTotalPrice()
        ));
    }

    @PostMapping("/update/{itemId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateQuantity(
            @PathVariable Long itemId,
            @RequestParam int quantity) {
        cartQueue.updateQuantity(itemId, quantity);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "totalItems", cartQueue.getTotalItems(),
            "totalPrice", cartQueue.getTotalPrice()
        ));
    }

    @GetMapping("/items")
    @ResponseBody
    public ResponseEntity<List<CartItem>> getCartItems() {
        return ResponseEntity.ok(cartQueue.getAllItems());
    }

    @PostMapping("/clear")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> clearCart() {
        cartQueue.clear();
        return ResponseEntity.ok(Map.of(
            "success", true,
            "totalItems", 0,
            "totalPrice", 0.0
        ));
    }
} 