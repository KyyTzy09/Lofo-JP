package com.fiky.lofo_app.data.api.services

import com.fiky.lofo_app.data.api.dto.announcement.AnnouncementDetail
import com.fiky.lofo_app.data.api.dto.announcement.BulkAnnouncementDto
import com.fiky.lofo_app.data.api.dto.announcement.CreateAnnouncementRequest
import com.fiky.lofo_app.data.api.dto.announcement.CreateAnnouncementResponse
import com.fiky.lofo_app.data.api.dto.announcement.PendingAnnouncements
import com.fiky.lofo_app.data.api.dto.announcement.UpdateAnnouncementRequest
import com.fiky.lofo_app.data.api.dto.announcement.UpdateAnnouncementResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface AnnouncementService {
    @POST("announcements")
    suspend fun createAnnouncement(
        @Body announcementDto: CreateAnnouncementRequest
    ): CreateAnnouncementResponse

    @GET("announcements")
    suspend fun getAllAnnouncements(): BulkAnnouncementDto

    @GET("announcements/pending")
    suspend fun getPendingAnnouncements(): PendingAnnouncements

    @GET("announcements/{id}")
    suspend fun getAnnouncementDetail(
        @Path("id") id: String
    ): AnnouncementDetail

    @PATCH("announcements/{id}")
    suspend fun updateAnnouncementStatus(
        @Path("id") id: String,
        @Body announcementDto: UpdateAnnouncementRequest
    ): UpdateAnnouncementResponse

    @DELETE("announcements/{id}")
    suspend fun deleteAnnouncement(
        @Path("id") id: String
    ): AnnouncementDetail
}