package util.syntax

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration

class ClassUtil(override var cu: CompilationUnit) : BaseUtil<ClassOrInterfaceDeclaration>(ClassOrInterfaceDeclaration::class.java){
    fun setName(name: String?, value: String) {
        looper(name) { it.setName(value) }
    }

    override fun isNameSame(node: ClassOrInterfaceDeclaration, comp: String): Boolean {
        return node.name.toString() == comp
    }
}