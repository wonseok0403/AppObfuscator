package util.syntax

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.expr.NameExpr

class NameUtil(override var cu: CompilationUnit) : BaseUtil<NameExpr>(NameExpr::class.java){

    fun setName(name: String?, value: String){
        looper(name) {it.setName(value)}
    }

    override fun isNameSame(node: NameExpr, comp: String): Boolean {
        return node.name.toString() == comp
    }
}