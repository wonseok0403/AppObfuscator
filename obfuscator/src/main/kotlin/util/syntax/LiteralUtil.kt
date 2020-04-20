package util.syntax

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.expr.Expression
import com.github.javaparser.ast.expr.LiteralExpr

class LiteralUtil(override var cu: CompilationUnit) : BaseUtil<LiteralExpr>(LiteralExpr::class.java) {
    fun encrypt() {
        //암호화
        looper(null) {
            val t = StaticJavaParser.parseExpression<Expression>(it.toString())
            it.replace(t)
        }
    }

    override fun isNameSame(node: LiteralExpr, comp: String): Boolean {
        return true
    }
}