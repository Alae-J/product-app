package com.example.productapp.model

import java.math.BigDecimal

data class Product(
    val id: Long? = null,
    val title: String,
    val price: BigDecimal?,
    val variant: String? = null
)
