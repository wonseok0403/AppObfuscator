package util.syntax

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.stmt.BlockStmt

class MethodUtil(private var cu : CompilationUnit) {
    fun getMethod(): List<MethodDeclaration> {
        val list = mutableListOf<MethodDeclaration>()
        looper(null){list.add(it)}
        return list
    }

    fun modifyMethodType(methodName: String?, changeValue: String) {
        looper(methodName) { it.setType(changeValue) }
    }

    fun modifyMethodName(methodName: String?, changeValue: String) {
        looper(methodName) { it.setName(changeValue) }
    }

    fun setMethodBody(methodName: String?, blockStmt: BlockStmt) {
        looper(methodName) { it.setBody(blockStmt) }
    }

    private fun looper(methodName: String?, block: (MethodDeclaration) -> Unit) {
        cu.findAll(MethodDeclaration::class.java).filter {
            if (methodName.isNullOrEmpty()) {
                true
            } else {
                it.name.toString() == methodName
            }
        }.forEach {
            block(it)
        }
    }
}