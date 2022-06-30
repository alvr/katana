package dev.alvr.katana.data.preferences.session.serializers

import dev.alvr.katana.data.preferences.base.serializers.PreferencesSerializer
import dev.alvr.katana.data.preferences.session.models.Session
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer

@ExperimentalSerializationApi
internal object SessionSerializer : PreferencesSerializer<Session> {
    override val defaultValue: Session = Session()
    override val serializer: KSerializer<Session> = Session.serializer()
}
