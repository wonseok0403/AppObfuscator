package util.syntax

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.Parameter

class ParameterUtil(override var cu: CompilationUnit) : BaseUtil<Parameter>(Parameter::class.java) {

    fun setName(name: String?, value: String) {
        looper(name) { it.setName(value) }
    }

    override fun isNameSame(node: Parameter, comp: String): Boolean {
        return node.name.toString() == comp
    }
}