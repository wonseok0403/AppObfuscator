package util

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.VariableDeclarator

object VariableUtil {
    fun getVariable(cu: CompilationUnit): List<VariableDeclarator> {
        val list = mutableListOf<VariableDeclarator>()
        for (type in cu.types) {
            for (iField in type.fields) {
                for (value in iField.variables) {
                    list.add(value)
                }
            }
        }
        return list
    }

    fun modifyVariable(cu: CompilationUnit, varName: String, changeValue: String) {
        for (type in cu.types) {
            for (iField in type.fields) {
                for (value in iField.variables) {
                    if (value.name.identifier == varName) {
                        value.setInitializer(changeValue)
                    }
                }
            }
        }
    }

}