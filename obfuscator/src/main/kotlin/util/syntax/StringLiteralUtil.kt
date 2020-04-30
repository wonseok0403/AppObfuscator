package util.syntax

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.expr.*
import com.github.javaparser.ast.stmt.BlockStmt
import com.github.javaparser.ast.stmt.ExpressionStmt

class StringLiteralUtil(override var cu: CompilationUnit) : BaseUtil<StringLiteralExpr>(StringLiteralExpr::class.java) {
    fun replace(method: String) {
        get().forEachIndexed { i, it ->
            val method = MethodCallExpr(method).apply {
                addArgument(stringify(i.toString()))
                addArgument(stringify(""))
            }
            it.replace(method)
        }
    }

    private fun stringify(value: String): String = "\"$value\""

    override fun isNameSame(node: StringLiteralExpr, comp: String): Boolean {
        return node.value.toString() == comp
    }
}