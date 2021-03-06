package io.kotlintest.specs

import io.kotlintest.AbstractSpec
import io.kotlintest.TestCase
import io.kotlintest.TestContainer
import io.kotlintest.TestContext
import io.kotlintest.lineNumber

/**
 * Example:
 *
 * "some test" {
 *   "with context" {
 *      should("do something") {
 *        // test here
 *      }
 *    }
 *  }
 *
 *  or
 *
 *  should("do something") {
 *    // test here
 *  }
 */
abstract class AbstractShouldSpec(body: AbstractShouldSpec.() -> Unit = {}) : AbstractSpec() {

  init {
    body()
  }

  final override fun isInstancePerTest(): Boolean = false

  fun should(name: String, test: TestContext.() -> Unit): TestCase {
    val tc = TestCase(rootDescription().append("should $name"), this@AbstractShouldSpec, test, lineNumber(), defaultTestCaseConfig)
    addRootScope(tc)
    return tc
  }

  operator fun String.invoke(init: ShouldContext.() -> Unit) =
      addRootScope(TestContainer(rootDescription().append(this), this@AbstractShouldSpec::class, { ShouldContext(it).init() }))

  inner class ShouldContext(val context: TestContext) {

    operator fun String.invoke(init: ShouldContext.() -> Unit) =
        context.executeScope(TestContainer(context.currentScope().description().append(this), this@AbstractShouldSpec::class, { ShouldContext(it).init() }))

    fun should(name: String, test: TestContext.() -> Unit): TestCase {
      val tc = TestCase(context.currentScope().description().append("should $name"), this@AbstractShouldSpec, test, lineNumber(), defaultTestCaseConfig)
      context.executeScope(tc)
      return tc
    }
  }


}