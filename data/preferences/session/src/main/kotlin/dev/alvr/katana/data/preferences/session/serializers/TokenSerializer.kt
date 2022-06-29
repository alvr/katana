package dev.alvr.katana.data.preferences.session.serializers

import dev.alvr.katana.data.preferences.base.PreferencesSerializer
import dev.alvr.katana.data.preferences.session.models.Token
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer

@ExperimentalSerializationApi
internal object TokenSerializer : PreferencesSerializer<Token> {
    override val defaultValue: Token = Token()
    override val serializer: KSerializer<Token> = Token.serializer()
}
