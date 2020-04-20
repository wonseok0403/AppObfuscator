package util.syntax

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.expr.AssignExpr

abstract class BaseUtil<T : Node>(private val clazz : Class<T>) {
    abstract var cu: CompilationUnit

    fun get(): List<T> {
        val list = mutableListOf<T>()
        looper(null) { list.add(it) }
        return list
    }

    abstract fun isNameSame(node: T, comp:String): Boolean

    protected fun looper(name: String?, block: (T) -> Unit) {
        cu.findAll(clazz).filter {
            if (name.isNullOrEmpty()) {
                true
            } else {
                isNameSame(it, name)
            }
        }.forEach {
            block(it)
        }
    }
}