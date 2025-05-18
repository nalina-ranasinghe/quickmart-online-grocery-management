package org.example.cart.controller;

import org.example.cart.model.CartItem;
import org.example.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public String viewCart(Model model) {
        // Get cart items
        var cartItems = cartService.getAllCartItems();
        model.addAttribute("cartItems", cartItems);

        // Calculate totals
        double subtotal = cartItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();
        
        double shipping = cartItems.isEmpty() ? 0.0 : 2.99;
        double tax = subtotal * 0.05;
        double total = subtotal + shipping + tax;

        // Add values to model
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("shipping", shipping);
        model.addAttribute("tax", tax);
        model.addAttribute("total", total);

        return "cart";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, @RequestParam int quantity) {
        cartService.addToCart(productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/update/{itemId}")
    public String updateCartItem(@PathVariable Long itemId, @RequestParam int quantity) {
        if (quantity > 0 && quantity <= 10) {
            CartItem item = cartService.getCartItem(itemId);
            if (item != null) {
                item.setQuantity(quantity);
                cartService.updateCartItem(item);
            }
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove/{itemId}")
    public String removeFromCart(@PathVariable Long itemId) {
        cartService.removeFromCart(itemId);
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout() {
        // TODO: Implement checkout logic
        return "redirect:/checkout";
    }
}
