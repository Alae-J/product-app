package com.example.productapp.repository

import com.example.productapp.model.Product
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findAllByOrderByTitleAsc(): List<Product>
    fun findByTitleContainingIgnoreCaseOrderByTitleAsc(title: String): List<Product>
    fun findByTitleContainingIgnoreCase(title: String, sort: Sort): List<Product>
}
