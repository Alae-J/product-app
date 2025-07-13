package com.example.productapp.controller

import com.example.productapp.model.Product
import com.example.productapp.service.ProductService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

@Controller
class ProductController(private val productService: ProductService) {
    private val objectMapper = jacksonObjectMapper()

    private fun convertJsonToCommaSeparated(variantJson: String?): String {
        if (variantJson.isNullOrBlank() || variantJson == "null") return ""
        return try {
            val variants: List<Map<String, Any>> = objectMapper.readValue(variantJson)
            variants.mapNotNull { it["title"] as? String }.joinToString(", ")
        } catch (e: Exception) {
            ""
        }
    }

    private fun convertCommaSeparatedToJson(variantText: String?): String? {
        if (variantText.isNullOrBlank()) return null
        val variants = variantText.split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .map { mapOf("title" to it) }
        return if (variants.isEmpty()) null else objectMapper.writeValueAsString(variants)
    }

    @GetMapping("/")
    fun home(model: Model): String {
        model.addAttribute("products", emptyList<Product>())
        return "index"
    }

    @GetMapping("/products")
    fun getProducts(@RequestParam(required = false) title: String?, model: Model): String {
        val products = if (title.isNullOrBlank()) {
            productService.findAll()
        } else {
            productService.findByTitleContainingIgnoreCase(title)
        }
        model.addAttribute("products", products)
        return "fragments/products-table :: table"
    }

    @GetMapping("/products/search")
    fun searchPage(model: Model): String {
        model.addAttribute("products", productService.findAll())
        return "search"
    }

    @GetMapping("/fragments/productForm")
    fun getProductForm(): String {
        return "fragments/productForm :: form"
    }

    @GetMapping("/fragments/editProductForm/{id}")
    fun getEditProductForm(@PathVariable id: Long, model: Model): String {
        val product = productService.findById(id) ?: return "fragments/empty :: empty"
        model.addAttribute("product", product)
        model.addAttribute("variantText", convertJsonToCommaSeparated(product.variant))
        return "fragments/editProductForm :: form"
    }

    @PostMapping("/products")
    fun addProduct(@RequestParam title: String, @RequestParam price: BigDecimal, @RequestParam(required = false) variantText: String?, model: Model): String {
        val variant = convertCommaSeparatedToJson(variantText)
        productService.createProduct(title, price, variant)
        model.addAttribute("products", productService.findAll())
        return "fragments/products-table :: table"
    }

    @PostMapping("/products/{id}")
    fun updateProduct(@PathVariable id: Long, @RequestParam title: String, @RequestParam price: BigDecimal, @RequestParam(required = false) variantText: String?, @RequestParam(defaultValue = "false") isFavorite: Boolean, model: Model): String {
        val variant = convertCommaSeparatedToJson(variantText)
        productService.updateProduct(id, title, price, variant, isFavorite)
        model.addAttribute("products", productService.findAll())
        return "fragments/products-table :: table"
    }

    @DeleteMapping("/products/{id}")
    fun deleteProduct(@PathVariable id: Long, model: Model): String {
        productService.deleteById(id)
        model.addAttribute("products", productService.findAll())
        return "fragments/products-table :: table"
    }

    @PostMapping("/products/{id}/favorite")
    fun toggleFavorite(@PathVariable id: Long, model: Model): String {
        productService.toggleFavorite(id)
        model.addAttribute("products", productService.findAll())
        return "fragments/products-table :: table"
    }
}
