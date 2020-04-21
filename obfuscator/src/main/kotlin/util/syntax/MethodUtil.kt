package util.syntax

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.stmt.BlockStmt
import java.lang.reflect.Method

//void temp(){}
class MethodUtil(override var cu: CompilationUnit) : BaseUtil<MethodDeclaration>(MethodDeclaration::class.java) {
    fun setType(methodName: String?, changeValue: String) {
        looper(methodName) { it.setType(changeValue) }
    }

    fun setName(methodName: String?, changeValue: String) {
        looper(methodName) { it.setName(changeValue) }
    }

    fun setBody(methodName: String?, blockStmt: BlockStmt) {
        looper(methodName) { it.setBody(blockStmt) }
    }

    override fun isNameSame(node: MethodDeclaration, comp: String): Boolean {
        return node.name.toString() == comp
    }
}