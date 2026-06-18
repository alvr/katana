package dev.alvr.katana.common.media.data

import com.apollographql.apollo.api.Error
import dev.alvr.katana.core.remote.optional
import dev.alvr.katana.core.remote.type.MediaType
import io.kotest.property.Arb
import io.kotest.property.arbitrary.enum
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.orNull
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.arbitrary.string

internal val mediaListCollectionQueryMock =
    MediaListCollectionQuery(
        user = Arb.positiveInt().orNull().next().optional,
        type = Arb.enum<MediaType>().next(),
        status = null.optional,
    )

internal val apolloErrorMock = Error.Builder(Arb.string().next()).build()
