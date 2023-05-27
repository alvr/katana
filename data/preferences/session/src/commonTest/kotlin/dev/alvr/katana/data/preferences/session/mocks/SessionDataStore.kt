package dev.alvr.katana.data.preferences.session.mocks

import androidx.datastore.core.DataStore
import dev.alvr.katana.data.preferences.session.models.Session

internal sealed interface SessionDataStore : DataStore<Session>
