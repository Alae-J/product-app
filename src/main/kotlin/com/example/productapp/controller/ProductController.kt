package com.example.productapp.controller

import com.example.productapp.model.Product
import com.example.productapp.repository.ProductRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@Controller
class ProductController(
    private val productRepository: ProductRepository
) {

    @GetMapping("/")
    fun home(model: Model): String {
        val products = productRepository.findAll()
        model.addAttribute("products", products)
        return "index"
    }

    @GetMapping("/products")
    fun getProducts(model: Model): String {
        model.addAttribute("products", productRepository.findAll())
        return "fragments/products-table :: table"
    }

    @PostMapping("/products")
    fun addProduct(
        @RequestParam title: String,
        @RequestParam price: BigDecimal,
        @RequestParam(required = false) variant: String?
    ): String {
        val newProduct = Product(title = title, price = price, variant = variant)
        productRepository.save(newProduct)

        return "fragments/products-table :: table"
    }
}
