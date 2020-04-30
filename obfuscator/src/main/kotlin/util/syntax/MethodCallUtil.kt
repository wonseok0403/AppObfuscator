package util.syntax

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.stmt.BlockStmt

class MethodCallUtil(override var cu: CompilationUnit) : BaseUtil<MethodCallExpr>(MethodCallExpr::class.java) {
    fun setName(name: String?, value: String) {
        looper(name) { it.setName(value) }
    }

    override fun isNameSame(node: MethodCallExpr, comp: String): Boolean {
        return node.name.toString() == comp
    }
}