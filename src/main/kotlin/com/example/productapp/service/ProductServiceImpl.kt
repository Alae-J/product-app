package com.example.productapp.service

import com.example.productapp.model.Product
import com.example.productapp.repository.ProductRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ProductServiceImpl(private val productRepository: ProductRepository) : ProductService {

    override fun findAll(): List<Product> {
        return productRepository.findAllByOrderByTitleAsc()
    }

    override fun findByTitleContainingIgnoreCase(title: String): List<Product> {
        return productRepository.findByTitleContainingIgnoreCaseOrderByTitleAsc(title)
    }

    override fun findById(id: Long): Product? {
        return productRepository.findById(id).orElse(null)
    }

    override fun save(product: Product): Product {
        return productRepository.save(product)
    }

    override fun createProduct(title: String, price: BigDecimal, variant: String?): Product {
        val newProduct = Product(title = title, price = price, variant = variant)
        return productRepository.save(newProduct)
    }

    override fun updateProduct(id: Long, title: String, price: BigDecimal, variant: String?, isFavorite: Boolean): Product {
        val product = Product(id = id, title = title, price = price, variant = variant, isFavorite = isFavorite)
        return productRepository.save(product)
    }

    override fun deleteById(id: Long) {
        productRepository.deleteById(id)
    }

    override fun toggleFavorite(id: Long): Product? {
        val product = productRepository.findById(id).orElse(null)
        if (product != null) {
            val updatedProduct = product.copy(isFavorite = !product.isFavorite)
            return productRepository.save(updatedProduct)
        }
        return null
    }
}