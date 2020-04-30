package modifier.encryptor

import cached.CACHED_APPLICATION
import cached.FUNCTION_ENCRPTION
import cached.FUNCTION_LOAD_JSON
import cached.classify
import modifier.AbstractModifier
import util.file.CompilationUtil.saveCompilationUnit
import util.json.JsonFactory
import util.syntax.ClassUtil
import util.syntax.MethodDeclUtil
import util.syntax.StringLiteralUtil
import java.io.File

class Encryptor private constructor() : AbstractModifier() {
    companion object {
        fun build(targetAppName: String): Encryptor {
            return Encryptor().apply { this.targetAppName = targetAppName }
        }
    }

    private class MakeJson(targetAppName: String) : AbstractModifier() {
        init {
            this.targetAppName = targetAppName
        }

        private fun makeJson(file: File) {
            val cu = parse(file)
            StringLiteralUtil(cu).also { util ->
                val jsonObject = JsonFactory.build(mainFolder)

                util.get().map { it.value.toString() }.forEach {
                    jsonObject.add(it)
                }
                jsonObject.save()
            }
        }

        override fun run() {
            javaFiles.forEach {
                makeJson(it)
            }
        }
    }

    @Deprecated("Replaced by InjectApplication")
    private class InjectMethodDecl(targetAppName: String) : AbstractModifier() {
        init {
            this.targetAppName = targetAppName
        }

        private fun injectCode(file: File) {
            val cu = parse(file)
            ClassUtil(cu).also { util ->
                util.get().forEach {
                    val name = it.name.toString()
                    val condition = util.getExtends(name)?.contains("AppCompatActivity") ?: false
                    if (condition) {
                        util.addMethod(null, classify(FUNCTION_ENCRPTION))
                        util.addMethod(null, classify(FUNCTION_LOAD_JSON))
                    }
                }
            }
            saveCompilationUnit(file, cu)
        }

        override fun run() {
            javaFiles.forEach {
                injectCode(it)
            }
        }
    }

    @Deprecated("Replaced by InjectApplication")
    private class InjectMethodCall(targetAppName: String) : AbstractModifier() {
        init {
            this.targetAppName = targetAppName
        }

        private fun injectCode(file: File) {
            val cu = parse(file)
            ClassUtil(cu).also { util ->
                util.get().forEach {
                    val name = it.name.toString()
                    val condition = util.getExtends(name)?.contains("Application") ?: false
                    if (condition) {
                        MethodDeclUtil(cu).also { util ->
                            util.get("onCreate").also {
                                util.injectMethod("onCreate", "encryption")
                            }
                        }
                    }
                }
            }
            saveCompilationUnit(file, cu)
        }

        override fun run() {
            javaFiles.forEach {
                injectCode(it)
            }
        }
    }

    private class ReplaceLiteral(targetAppName: String) : AbstractModifier() {
        init {
            this.targetAppName = targetAppName
        }

        private fun replace(file: File) {
            val cu = parse(file)
            StringLiteralUtil(cu).replace("MyApplication.pref.get")
            saveCompilationUnit(file, cu)
        }
        override fun run() {
            javaFiles.forEach {
                replace(it)
            }
        }
    }

    private class InjectApplication(targetAppName: String) : AbstractModifier() {
        init {
            this.targetAppName = targetAppName
        }

        private fun inject() {
            val cu = parse(CACHED_APPLICATION)
            saveCompilationUnit("$codeFolder/MyApplication.java", cu)
        }

        override fun run() {
            inject()
        }
    }

    override fun run() {
        /**
         * string literal json 생성 (o)
         * application class추가 -> xml 추가
         * 첫 실행 activity에 encryption() 추가 (o)
         * 첫 실행 activity onCreate()에서 encryption 호출 (o)
         * string literal -> securePref.get() 치환 (o)
         */
        MakeJson(targetAppName).run()
        ReplaceLiteral(targetAppName).run()
        InjectApplication(targetAppName).run()
    }
}