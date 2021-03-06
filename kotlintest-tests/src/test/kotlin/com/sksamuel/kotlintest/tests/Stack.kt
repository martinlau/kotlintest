package com.sksamuel.kotlintest.tests

interface Stack<T> {
    fun push(t: T): Unit
    fun pop(): T
    fun peek(): T
    fun size(): Int
    fun isEmpty(): Boolean
}