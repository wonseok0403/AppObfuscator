package util.syntax

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.VariableDeclarator
import com.github.javaparser.ast.expr.AssignExpr

class VariableUtil(override var cu: CompilationUnit) : BaseUtil<VariableDeclarator>(VariableDeclarator::class.java){

    fun modifyValue(varName: String, changeValue: String) {
        looper(varName) { it.setInitializer(changeValue) }
    }

    fun modifyName(varName: String, changeValue: String) {
        looper(varName) { it.setName(changeValue) }
    }

    override fun isNameSame(node: VariableDeclarator, comp: String): Boolean {
        return node.name.toString() == comp
    }
}