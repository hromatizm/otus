package spring.jwt

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*

@Component
class JwtPublicKeyHolder(
    @Value("\${jwt.public-key}")
    private val publicKeyStr: String
) {

    private val base64Decoder = Base64.getDecoder()
    private val keyBytes: ByteArray = base64Decoder.decode(publicKeyStr)
    private val kyeSpec = X509EncodedKeySpec(keyBytes)
    private val keyFactory = KeyFactory.getInstance("RSA")

    val key = keyFactory.generatePublic(kyeSpec) as RSAPublicKey
}

