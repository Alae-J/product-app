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
class ProductImportService(private val productRepository: ProductRepository) {
    private val client = RestTemplate()

    @Scheduled(initialDelay = 0, fixedDelay = Long.MAX_VALUE)
    fun importProducts() {
        val jsonString = client.getForObject("https://famme.no/products.json", String::class.java) ?: return

        // Clear existing products
        productRepository.deleteAll()

        // Parse as JSONObject and extract "products" array
        val root = JSONObject(jsonString)
        val products = root.getJSONArray("products")

        // Import exactly 50 products (or fewer if less available - unecessary here but good addition)
        val productsToImport = minOf(50, products.length())
        
        for (i in 0 until productsToImport) {
            val prod = products.getJSONObject(i)

            val title = prod.getString("title")
            val allVariants = prod.optJSONArray("variants") ?: JSONArray()
            
            // Limit to 3 variants maximum
            // val limitedVariants = JSONArray()
            // val maxVariants = minOf(3, allVariants.length())
            // for (j in 0 until maxVariants) {
            //     limitedVariants.put(allVariants.getJSONObject(j))
            // }

            val firstPrice = allVariants.optJSONObject(0)?.optBigDecimal("price")

            val product = Product(
                  title = title,
                  price = firstPrice,
                variant = allVariants.toString()
            )

            productRepository.save(product)
        }

        println("✅ Imported $productsToImport products (max 50 limit maintained).")
    }

    private fun JSONObject.optBigDecimal(field: String): BigDecimal? =
        if (has(field)) BigDecimal(getDouble(field)) else null
}
