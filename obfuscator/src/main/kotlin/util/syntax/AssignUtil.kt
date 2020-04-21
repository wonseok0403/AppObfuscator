package util.syntax

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.expr.AssignExpr
import com.github.javaparser.ast.expr.Expression

class AssignUtil(override var cu: CompilationUnit) : BaseUtil<AssignExpr>(AssignExpr::class.java) {

    fun setName(name: String?, value: String) {
        val t = StaticJavaParser.parseExpression<Expression>(value)
        looper(name) { it.target = t }
    }

    fun setValue(name: String?, value: String) {
        val t = StaticJavaParser.parseExpression<Expression>(value)
        looper(name) { it.value = t }
    }

    override fun isNameSame(node: AssignExpr, comp: String): Boolean {
        return node.target.toString() == comp
    }
}