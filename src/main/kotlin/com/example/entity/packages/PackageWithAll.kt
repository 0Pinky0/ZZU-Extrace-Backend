package com.example.entity.packages

import com.example.entity.express.Express
import com.example.entity.node.Node

data class PackageWithAll(
    val packages: Package,
    val expresses: List<Express>,
    val src: Node,
    val dst: Node
)