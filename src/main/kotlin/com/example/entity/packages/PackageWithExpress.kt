package com.example.entity.packages

import com.example.entity.express.Express

data class PackageWithExpress(
    val packages: Package,
    val expresses: List<Express>,
)