package util.syntax

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.VariableDeclarator
import com.github.javaparser.ast.expr.AssignExpr

//int a = 5;
class VariableUtil(override var cu: CompilationUnit) : BaseUtil<VariableDeclarator>(VariableDeclarator::class.java){

    fun setValue(name: String?, value: String) {
        looper(name) { it.setInitializer(value) }
    }

    fun setName(name: String?, value: String) {
        looper(name) { it.setName(value) }
    }

    override fun isNameSame(node: VariableDeclarator, comp: String): Boolean {
        return node.name.toString() == comp
    }
}