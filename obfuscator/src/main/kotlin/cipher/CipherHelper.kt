package cipher

import java.math.BigInteger
import java.security.MessageDigest

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
}
