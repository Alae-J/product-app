package com.example.productapp.repository

import com.example.productapp.model.Product
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository

@Repository
class ProductRepository(
    private val jdbcClient: JdbcClient
) {
    fun findAll(): List<Product> =
        jdbcClient.sql("SELECT * FROM products")
            .query { rs, _ ->
                Product(
                    id = rs.getLong("id"),
                    title = rs.getString("title"),
                    price = rs.getBigDecimal("price"),
                    variant = rs.getString("variant")
                )
            }.list()

    fun save(product: Product) {
        jdbcClient.sql(
            "INSERT INTO products (title, price, variant) VALUES (?, ?, ?::jsonb)"
        ).params(
            listOf(product.title, product.price, product.variant)
        ).update()
    }
}
