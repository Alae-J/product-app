package com.example.productapp.service

import com.example.productapp.model.Product
import com.example.productapp.repository.ProductRepository
import org.json.JSONObject
import org.json.JSONArray
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal

@Service
class ProductImportService(
    private val productRepository: ProductRepository
) {
    private val client = RestTemplate()

    @Scheduled(initialDelay = 0, fixedDelay = Long.MAX_VALUE)
    fun importProducts() {
        val jsonString = client.getForObject("https://famme.no/products.json", String::class.java) ?: return

        // Parse as JSONObject and extract "products" array
        val root = JSONObject(jsonString)
        val products = root.getJSONArray("products")

        for (i in 0 until minOf(50, products.length())) {
            val prod = products.getJSONObject(i)

            val title = prod.getString("title")
            val variants = prod.optJSONArray("variants") ?: JSONArray()
            val firstPrice = variants
                .optJSONObject(0)
                ?.optBigDecimal("price")

            val product = Product(
                title = title,
                price = firstPrice,
                variant = variants.toString()
            )

            productRepository.save(product)
        }

        println("✅ Imported ${minOf(50, products.length())} products.")
    }

    private fun JSONObject.optBigDecimal(field: String): BigDecimal? =
        if (has(field)) BigDecimal(getDouble(field)) else null
}
