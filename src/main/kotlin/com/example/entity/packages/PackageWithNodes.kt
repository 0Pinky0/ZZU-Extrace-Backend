package com.example.entity.packages

import com.example.entity.node.Node

data class PackageWithNodes(
    val packages: Package,
    val src: Node,
    val dst: Node
)