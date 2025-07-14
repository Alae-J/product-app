package com.example.productapp.service

import com.example.productapp.model.Product
import com.example.productapp.repository.ProductRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun findAll(): List<Product> {
        return productRepository.findAllByOrderByTitleAsc()
    }

    fun findAllSorted(sortBy: String, sortDir: String): List<Product> {
        val sort = createSort(sortBy, sortDir)
        return productRepository.findAll(sort)
    }

    fun findByTitleContainingIgnoreCase(title: String): List<Product> {
        return productRepository.findByTitleContainingIgnoreCaseOrderByTitleAsc(title)
    }

    fun findByTitleContainingIgnoreCaseSorted(title: String, sortBy: String, sortDir: String): List<Product> {
        val sort = createSort(sortBy, sortDir)
        return productRepository.findByTitleContainingIgnoreCase(title, sort)
    }

    private fun createSort(sortBy: String, sortDir: String): Sort {
        val direction = if (sortDir.lowercase() == "desc") Sort.Direction.DESC else Sort.Direction.ASC
        val property = when (sortBy.lowercase()) {
            "price" -> "price"
            "title" -> "title"
            else -> "title"
        }
        return Sort.by(direction, property)
    }

    fun findById(id: Long): Product? {
        return productRepository.findById(id).orElse(null)
    }

    fun save(product: Product): Product {
        return productRepository.save(product)
    }

    fun createProduct(title: String, price: BigDecimal, variant: String?): Product {
        val newProduct = Product(title = title, price = price, variant = variant)
        return productRepository.save(newProduct)
    }

    fun updateProduct(id: Long, title: String, price: BigDecimal, variant: String?, isFavorite: Boolean): Product {
        val product = Product(id = id, title = title, price = price, variant = variant, isFavorite = isFavorite)
        return productRepository.save(product)
    }

    fun deleteById(id: Long) {
        productRepository.deleteById(id)
    }

    fun toggleFavorite(id: Long): Product? {
        val product = productRepository.findById(id).orElse(null)
        if (product != null) {
            val updatedProduct = product.copy(isFavorite = !product.isFavorite)
            return productRepository.save(updatedProduct)
        }
        return null
    }
}