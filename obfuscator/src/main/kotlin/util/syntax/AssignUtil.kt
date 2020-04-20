package util.syntax

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.expr.AssignExpr
import com.github.javaparser.ast.expr.Expression

class AssignUtil(override var cu: CompilationUnit) : BaseUtil<AssignExpr>(AssignExpr::class.java) {

    fun modifyAssignName(assignName: String?, changeValue: String) {
        val t = StaticJavaParser.parseExpression<Expression>(changeValue)
        looper(assignName) { it.target = t }
    }

    fun modifyAssignValue(assignName: String?, changeValue: String) {
        val t = StaticJavaParser.parseExpression<Expression>(changeValue)
        looper(assignName) { it.value = t }
    }

    override fun isNameSame(node: AssignExpr, comp: String): Boolean {
        return node.target.toString() == comp
    }
}