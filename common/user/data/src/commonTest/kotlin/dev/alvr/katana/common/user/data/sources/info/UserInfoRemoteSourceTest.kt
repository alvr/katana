package dev.alvr.katana.common.user.data.sources.info

import app.cash.turbine.test
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.annotations.ApolloExperimental
import com.apollographql.apollo.testing.MapTestNetworkTransport
import com.apollographql.apollo.testing.registerTestNetworkError
import com.apollographql.apollo.testing.registerTestResponse
import dev.alvr.katana.common.user.data.UserInfoQuery
import dev.alvr.katana.common.user.domain.failures.UserFailure
import dev.alvr.katana.common.user.domain.models.UserInfo
import dev.alvr.katana.core.remote.type.buildUser
import dev.alvr.katana.core.remote.type.buildUserAvatar
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import io.kotest.core.spec.style.FreeSpec

@OptIn(ApolloExperimental::class)
internal class UserInfoRemoteSourceTest : FreeSpec() {
    private val client = ApolloClient.Builder().networkTransport(MapTestNetworkTransport()).build()
    private val source: UserInfoRemoteSource = UserInfoRemoteSourceImpl(client)

    init {
        "observing the user info" - {
            "the server returns no data" {
                client.registerTestResponse(UserInfoQuery())
                source.userInfo.test {
                    awaitItem().shouldBeLeft(UserFailure.GettingUserInfo)
                    cancelAndIgnoreRemainingEvents()
                }
            }

            validUserInfoData.map { (query, userInfo) ->
                "the server returns valid user info ($query)" {
                    client.registerTestResponse(UserInfoQuery(), query)
                    source.userInfo.test {
                        awaitItem().shouldBeRight(userInfo)
                        cancelAndIgnoreRemainingEvents()
                    }
                }
            }

            "there is a problem with the server" {
                client.registerTestNetworkError(UserInfoQuery())
                source.userInfo.test {
                    awaitItem().shouldBeLeft(UserFailure.GettingUserInfo)
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    private companion object {
        const val USER_NAME = "alvr"
        const val AVATAR = "https://s4.anilist.co/file/anilistcdn/user/avatar/large/b37384-xJE9aA4X20Yr.png"
        const val BANNER = "https://s4.anilist.co/file/anilistcdn/user/banner/37384-jtds8dpQIGVG.jpg"

        val validUserInfoData = listOf(
            UserInfoQuery.Data {
                this["Viewer"] = buildUser {
                    name = USER_NAME
                    avatar = buildUserAvatar { large = AVATAR }
                    bannerImage = BANNER
                }
            } to UserInfo(username = USER_NAME, avatar = AVATAR, banner = BANNER),
            UserInfoQuery.Data {
                this["Viewer"] = buildUser {
                    name = USER_NAME
                    avatar = buildUserAvatar { large = AVATAR }
                    bannerImage = null
                }
            } to UserInfo(username = USER_NAME, avatar = AVATAR, banner = null),
        )
    }
}
