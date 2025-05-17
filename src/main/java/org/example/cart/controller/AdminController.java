package org.example.cart.controller;

import org.example.cart.model.Product;
import org.example.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CartService cartService;

    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", cartService.getAllProducts());
        model.addAttribute("newProduct", new Product());
        return "admin/products";
    }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute Product product) {
        cartService.addProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = cartService.getProduct(id);
        model.addAttribute("product", product);
        return "admin/edit-product";
    }

    @PostMapping("/products/update/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product) {
        product.setId(id);
        cartService.updateProduct(product);
        return "redirect:/admin/products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        cartService.deleteProduct(id);
        return "redirect:/admin/products";
    }
} 