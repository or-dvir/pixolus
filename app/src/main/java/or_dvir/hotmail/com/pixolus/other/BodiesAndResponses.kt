package or_dvir.hotmail.com.pixolus.other

import com.squareup.moshi.JsonClass
import or_dvir.hotmail.com.pixolus.model.Meter

class BodyAuth(val email: String, val password: String)
@JsonClass(generateAdapter = true)
class ResponseAuth(val token: String)

@JsonClass(generateAdapter = true)
class ResponseMeters(val next: String?, val results: List<Meter>?)