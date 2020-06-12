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
import java.io.*
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


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
        @Throws(Exception::class)
        fun readFile(filePath: String): ByteArray {
            val file = File(filePath)
            val fileContents = file.readBytes()
            val inputBuffer = BufferedInputStream(
                FileInputStream(file)
            )

            inputBuffer.read(fileContents)
            inputBuffer.close()

            return fileContents
        }
        @Throws(Exception::class)
        fun saveFile(fileData: ByteArray, path: String) {
            val file = File(path)
            val bos = BufferedOutputStream(FileOutputStream(file, false))
            bos.write(fileData)
            bos.flush()
            bos.close()
        }

        @Throws(Exception::class)
        fun generateSecretKey(): SecretKey? {
            val secureRandom = SecureRandom()
            val keyGenerator = KeyGenerator.getInstance("AES")
            //generate a key with secure random
            keyGenerator?.init(128, secureRandom)
            return keyGenerator?.generateKey()
        }

        @Throws(Exception::class)
        fun encryptImgs(yourKey: SecretKey, fileData: ByteArray): ByteArray {
            val data = yourKey.getEncoded()
            println(data.toString())
            val skeySpec = SecretKeySpec(data, 0, data.size, "AES")
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.ENCRYPT_MODE,  yourKey)
            return cipher.doFinal(fileData)
        }

        override fun run() {
            javaFiles.forEach {
                makeJson(it)
            }

            imgFiles.forEach{
                //encryptImages
                val secretKey = generateSecretKey()
                val encryptedImg = secretKey?.let { it1 -> encryptImgs(it1, readFile(it.path)) }

                if (encryptedImg != null) {
                    val newFile =
                        File(it.getParent(), "encrypted-img.jpg")
                    saveFile(encryptedImg, newFile.path)
                }
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