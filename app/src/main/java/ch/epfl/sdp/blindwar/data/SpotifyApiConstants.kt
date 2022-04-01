package ch.epfl.sdp.blindwar.data

import android.util.Base64
import ch.epfl.sdp.blindwar.BuildConfig

object SpotifyApiConstants {
    /** Credentials constants **/
    private const val CLIENT_ID = ""
    private const val CLIENT_SECRET = ""
    const val AUTH_TYPE = ""

    fun credentialsEncoding(): String {
        return "Basic ${
            Base64.encodeToString(
                "$CLIENT_ID:$CLIENT_SECRET".toByteArray(Charsets.UTF_8),
                Base64.NO_WRAP
            )
        }"
    }

    fun tokenParameter(responseToken: SpotifyToken): String {
        return "Bearer ${responseToken.access_token}"
    }

    /** Headers constants **/
    const val AUTH = "Authorization"
    const val ACCEPT = "Accept: application/json"
    const val CONTENT_TYPE = "Content-Type: application/json"
    const val URL_ENCODED_FORM = "Content-Type: application/x-www-form-urlencoded"

    /** URL path constants **/
    const val SPOTIFY_META_END_POINT = "https://api.spotify.com/v1/"
    const val SPOTIFY_AUTH_END_POINT = "https://accounts.spotify.com/"
    const val API_PATH = "api/token/"
    const val ARTIST_PATH = "artists/{artist_id}"
    //const val TRACK_PATH = "tracks/{track_id}"

    /** Query and field constants **/
    const val ARTIST_ID = "artist_id"
    const val GRANT_TYPE = "grant_type"
    //const val TRACK_ID = "track_id"
    //const val LIMIT = "limit"
}