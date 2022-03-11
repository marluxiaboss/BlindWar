package ch.epfl.sdp.blindwar

import android.media.Image

class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    var screenName: String,
    var profilePicture: Image?,
    var userStatistics: AppStatistics
)