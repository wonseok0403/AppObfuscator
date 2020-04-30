package util.syntax

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.expr.FieldAccessExpr

class FieldAccessUtil(override var cu: CompilationUnit) :  BaseUtil<FieldAccessExpr>(FieldAccessExpr::class.java) {
    fun setName(name: String?, value: String) {
        looper(name) { it.setName(value) }
    }

    override fun isNameSame(node: FieldAccessExpr, comp: String): Boolean {
        return node.name.toString() == comp
    }
}