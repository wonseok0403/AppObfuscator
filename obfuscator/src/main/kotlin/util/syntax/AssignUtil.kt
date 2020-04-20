package util.syntax

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.expr.AssignExpr
import com.github.javaparser.ast.expr.Expression

class AssignUtil(private var cu: CompilationUnit) {
    fun getAssign(): List<AssignExpr> {
        val list = mutableListOf<AssignExpr>()
        looper(null){list.add(it)}
        return list
    }

    fun modifyAssignName(assignName: String?, changeValue: String) {
        val t = StaticJavaParser.parseExpression<Expression>(changeValue)
        looper(assignName) { it.target = t }
    }

    fun modifyAssignValue(assignName: String?, changeValue: String) {
        val t = StaticJavaParser.parseExpression<Expression>(changeValue)
        looper(assignName) { it.value = t }
    }

    private fun looper(assignName: String?, block: (AssignExpr) -> Unit) {
        cu.findAll(AssignExpr::class.java).filter {
            if (assignName.isNullOrEmpty()) {
                true
            } else {
                it.target.toString() == assignName
            }
        }.forEach {
            block(it)
        }
    }
}