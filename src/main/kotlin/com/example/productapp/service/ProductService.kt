package com.example.productapp.service

import com.example.productapp.model.Product
import java.math.BigDecimal

interface ProductService {
    fun findAll(): List<Product>
    fun findByTitleContainingIgnoreCase(title: String): List<Product>
    fun findById(id: Long): Product?
    fun save(product: Product): Product
    fun createProduct(title: String, price: BigDecimal, variant: String?): Product
    fun updateProduct(id: Long, title: String, price: BigDecimal, variant: String?, isFavorite: Boolean): Product
    fun deleteById(id: Long)
    fun toggleFavorite(id: Long): Product?
}