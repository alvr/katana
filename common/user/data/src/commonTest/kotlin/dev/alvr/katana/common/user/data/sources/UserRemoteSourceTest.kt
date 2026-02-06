package dev.alvr.katana.common.user.data.sources

import app.cash.turbine.test
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.annotations.ApolloExperimental
import com.apollographql.apollo.exception.JsonDataException
import com.apollographql.apollo.testing.MapTestNetworkTransport
import com.apollographql.apollo.testing.registerTestNetworkError
import com.apollographql.apollo.testing.registerTestResponse
import dev.alvr.katana.common.user.data.UserIdQuery
import dev.alvr.katana.common.user.data.UserInfoQuery
import dev.alvr.katana.common.user.domain.failures.UserFailure
import dev.alvr.katana.common.user.domain.models.UserId
import dev.alvr.katana.common.user.domain.models.UserInfo
import dev.alvr.katana.core.domain.failures.Failure
import dev.alvr.katana.core.remote.type.buildUser
import dev.alvr.katana.core.remote.type.buildUserAvatar
import dev.alvr.katana.core.tests.shouldBeLeft
import dev.alvr.katana.core.tests.shouldBeRight
import io.kotest.assertions.throwables.shouldThrowExactlyUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.test.TestCase

@OptIn(ApolloExperimental::class)
internal class UserRemoteSourceTest : FreeSpec() {
    private val client = ApolloClient.Builder().networkTransport(MapTestNetworkTransport()).build()

    private lateinit var source: UserRemoteSource

    init {
        "userId" -
            {
                "getting the userId" -
                    {
                        "the server returns no data" {
                            client.registerTestResponse(UserIdQuery())
                            source.getUserId().shouldBeLeft(Failure.Unknown)
                        }

                        "the server returns an empty userId" {
                            shouldThrowExactlyUnit<JsonDataException> {
                                val query = UserIdQuery.Data { this["Viewer"] = null }
                                client.registerTestResponse(UserIdQuery(), query)
                                source.getUserId()
                            }
                        }

                        "the server returns a valid id" {
                            val query = UserIdQuery.Data { this["Viewer"] = buildUser { id = 37_384 } }
                            client.registerTestResponse(UserIdQuery(), query)
                            source.getUserId().shouldBeRight(UserId(37_384))
                        }
                    }

                "saving" -
                    {
                        "is successful" {
                            val query = UserIdQuery.Data { this["viewer"] = buildUser { id = 37_384 } }
                            client.registerTestResponse(UserIdQuery(), query)
                            source.saveUserId().shouldBeRight()
                        }

                        "is error" {
                            client.registerTestNetworkError(UserIdQuery())
                            source.saveUserId().shouldBeLeft()
                        }
                    }
            }

        "userInfo" -
            {
                "observing the user info" -
                    {
                        "the server returns no data" {
                            client.registerTestResponse(UserInfoQuery())
                            source.userInfo.test {
                                awaitItem().shouldBeLeft(UserFailure.GettingUserInfo)
                                awaitComplete()
                            }
                        }

                        validUserInfoData.map { (query, userInfo) ->
                            "the server returns valid user info ($query)" {
                                client.registerTestResponse(UserInfoQuery(), query)
                                source.userInfo.test {
                                    awaitItem().shouldBeRight(userInfo)
                                    awaitComplete()
                                }
                            }
                        }

                        "there is a problem with the server" {
                            client.registerTestNetworkError(UserInfoQuery())
                            source.userInfo.test {
                                awaitItem().shouldBeLeft(UserFailure.GettingUserInfo)
                                awaitComplete()
                            }
                        }
                    }
            }
    }

    override suspend fun beforeEach(testCase: TestCase) {
        source = UserRemoteSourceImpl(client)
    }
}

private const val USER_NAME = "alvr"
private const val AVATAR = "https://s4.anilist.co/file/anilistcdn/user/avatar/large/b37384-xJE9aA4X20Yr.png"
private const val BANNER = "https://s4.anilist.co/file/anilistcdn/user/banner/37384-jtds8dpQIGVG.jpg"

private val validUserInfoData =
    listOf(
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
