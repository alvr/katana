package dev.alvr.katana.data.remote.user.sources.info

import app.cash.turbine.test
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.testing.MapTestNetworkTransport
import com.apollographql.apollo3.testing.registerTestNetworkError
import com.apollographql.apollo3.testing.registerTestResponse
import dev.alvr.katana.common.core.empty
import dev.alvr.katana.common.tests.shouldBeLeft
import dev.alvr.katana.common.tests.shouldBeRight
import dev.alvr.katana.data.remote.base.type.buildUser
import dev.alvr.katana.data.remote.base.type.buildUserAvatar
import dev.alvr.katana.data.remote.user.UserInfoQuery
import dev.alvr.katana.domain.user.failures.UserFailure
import dev.alvr.katana.domain.user.models.UserInfo
import io.kotest.core.spec.style.FreeSpec
import kotlin.time.Duration.Companion.seconds

@OptIn(ApolloExperimental::class)
internal class UserInfoRemoteSourceTest : FreeSpec() {
    private val client = ApolloClient.Builder().networkTransport(MapTestNetworkTransport()).build()
    private val source: UserInfoRemoteSource = UserInfoRemoteSourceImpl(client)

    init {
        "observing the user info" - {
            "the server returns no data" {
                client.registerTestResponse(UserInfoQuery())
                source.userInfo.test(5.seconds) {
                    awaitItem().shouldBeRight(userInfoNoData)
                    cancelAndIgnoreRemainingEvents()
                }
            }

            "the server returns an empty user" {
                val query = UserInfoQuery.Data { this["user"] = null }
                client.registerTestResponse(UserInfoQuery(), query)
                source.userInfo.test(5.seconds) {
                    awaitItem().shouldBeRight(userInfoNoData)
                    cancelAndIgnoreRemainingEvents()
                }
            }

            validUserInfoData.map { (query, userInfo) ->
                "the server returns valid user info ($query)" {
                    client.registerTestResponse(UserInfoQuery(), query)
                    source.userInfo.test(5.seconds) {
                        awaitItem().shouldBeRight(userInfo)
                        cancelAndIgnoreRemainingEvents()
                    }
                }
            }

            "there is a problem with the server" {
                client.registerTestNetworkError(UserInfoQuery())
                source.userInfo.test(5.seconds) {
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

        val userInfoNoData = UserInfo(
            username = String.empty,
            avatar = String.empty,
            banner = String.empty,
        )

        val validUserInfoData = listOf(
            UserInfoQuery.Data {
                this["user"] = buildUser {
                    this["name"] = USER_NAME
                    this["avatar"] = buildUserAvatar { this["medium"] = AVATAR }
                    this["bannerImage"] = BANNER
                }
            } to UserInfo(username = USER_NAME, avatar = AVATAR, banner = BANNER),
            UserInfoQuery.Data {
                this["user"] = buildUser {
                    this["name"] = USER_NAME
                    this["avatar"] = buildUserAvatar { this["medium"] = null }
                    this["bannerImage"] = BANNER
                }
            } to UserInfo(username = USER_NAME, avatar = String.empty, banner = BANNER),
            UserInfoQuery.Data {
                this["user"] = buildUser {
                    this["name"] = USER_NAME
                    this["avatar"] = null
                    this["bannerImage"] = BANNER
                }
            } to UserInfo(username = USER_NAME, avatar = String.empty, banner = BANNER),
        )
    }
}
