package com.fiky.lofo_app.screens.profile

data class ProfileState(
    val name: String = "Julian Sterling",
    val email: String = "julian.sterling@concierge.com",
    val profileImage: String = "https://lh3.googleusercontent.com/aida-public/AB6AXuBy2yIrjW7yLjUMUQ6V_YoKgzND1cfDtRuLbcdOg4-6BRuTlsenOaxjsVZrqHXrFMuELpI7nONONHl4_wwy8VGS0jSmcIUCpaDeZb7gsKvjjsOUX0DHfRWvDUSAGcFqUBkHQxbKXzoAWgWsd2nZd0xkQg10wf5I3FBI8tdYqUyAPQ0T8we-iCW36lesT__1GdPmMT80_RmS3xfdEUsy4bsnQhv2Z9IVjy9vn3wZ0ZWKlnAk5RyHsrRR_oklFPhc8MZAlW82BzRudDY",
    val memberStatus: String = "Premier Member",
    val itemsRecovered: Int = 12,
    val reportedItems: List<ReportedItem> = listOf(
        ReportedItem(
            id = "1",
            name = "Tan Leather Briefcase",
            location = "Grand Central Terminal",
            timeAgo = "2h ago",
            image = "https://lh3.googleusercontent.com/aida-public/AB6AXuASvTk3cYnB3dhCNLan2RZSvxIRtRntzMGPwm52yHkqwWrgTXKMXuRsCWZmcTHdAe8eZb_2Y4ejkNe9fZ4m7XShp6yaTv1z2mOJ-bG5aljQcQhkSSXMna_N4gBoPB7ut1fPzL3kfQpniTxOd5lKP2EE0CwaSXFqLETmLM0BOOhqI-oDOSrNAPusLdncCyqfGz4-sg-I2FAahvnlPag7L8I-1ZNLraFmAW1sIYayjrOQE_MfzL1n1qz771Hy4OJDSpymzr0lGeWmaVs",
            type = ReportedItemType.LARGE
        ),
        ReportedItem(
            id = "2",
            name = "Silver Chronograph",
            location = "Chelsea",
            status = "In Progress",
            progress = 0.5f,
            type = ReportedItemType.SMALL
        )
    )
)

data class ReportedItem(
    val id: String,
    val name: String,
    val location: String,
    val timeAgo: String? = null,
    val image: String? = null,
    val status: String? = null,
    val progress: Float? = null,
    val type: ReportedItemType
)

enum class ReportedItemType {
    LARGE, SMALL
}
