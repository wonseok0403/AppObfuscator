package util.cipher

import sun.security.krb5.internal.ktab.KeyTabConstants.keySize
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


object CipherHelper{
    //WITH_DROP_INT_PREFIX
    fun SHA256(input: String): String? {
        var toReturn: String? = null
        try {
            val digest = MessageDigest.getInstance("SHA-256")
            digest.reset()
            digest.update(input.toByteArray(charset("utf8")))
            toReturn = String.format("%064x", BigInteger(1, digest.digest()))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        for(i in toReturn?.indices!!){
            if(!toReturn[i].isDigit()){
                toReturn = toReturn.substring(i) + toReturn.substring(0,i)
                break
            }
        }
        return toReturn
    }

    @Throws(Exception::class)
    fun encryptImg(raw: ByteArray, clear: ByteArray): ByteArray? {
        val skeySpec = SecretKeySpec(raw, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
        return cipher.doFinal(clear)
    }

    @Throws(Exception::class)
    fun decryptImg(
        raw: ByteArray,
        encrypted: ByteArray
    ): ByteArray? {
        val skeySpec = SecretKeySpec(raw, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, skeySpec)
        return cipher.doFinal(encrypted)
    }
}
