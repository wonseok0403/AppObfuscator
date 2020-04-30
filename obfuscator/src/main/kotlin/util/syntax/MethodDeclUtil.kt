package util.syntax

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.stmt.BlockStmt
import java.lang.Exception
import java.lang.reflect.Method

class MethodDeclUtil(override var cu: CompilationUnit) : BaseUtil<MethodDeclaration>(MethodDeclaration::class.java) {
    fun setType(name: String?, value: String) {
        looper(name) { it.setType(value) }
    }

    fun setName(name: String?, value: String) {
        looper(name) { it.setName(value) }
    }

    fun setBody(name: String?, blockStmt: BlockStmt) {
        looper(name) { it.setBody(blockStmt) }
    }

    fun injectMethod(name: String, method: String, parameter: List<String>? = null, index: Int = 0) {
        val method = MethodCallExpr(method)
        parameter?.forEach {
            method.addArgument(it)
        }

        looper(name) {
            it.findFirst(BlockStmt::class.java).get().apply {
                this.addStatement(index, method)
            }
        }
    }

    override fun isNameSame(node: MethodDeclaration, comp: String): Boolean {
        return node.name.toString() == comp
    }
}