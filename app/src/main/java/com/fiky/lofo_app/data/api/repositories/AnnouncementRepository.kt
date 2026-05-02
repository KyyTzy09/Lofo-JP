package com.fiky.lofo_app.data.api.repositories

import com.fiky.lofo_app.data.api.dto.announcement.AnnouncementDetail
import com.fiky.lofo_app.data.api.dto.announcement.BulkAnnouncementDto
import com.fiky.lofo_app.data.api.dto.announcement.CreateAnnouncementRequest
import com.fiky.lofo_app.data.api.dto.announcement.CreateAnnouncementResponse
import com.fiky.lofo_app.data.api.dto.announcement.PendingAnnouncements
import com.fiky.lofo_app.data.api.dto.announcement.UpdateAnnouncementRequest
import com.fiky.lofo_app.data.api.dto.announcement.UpdateAnnouncementResponse
import com.fiky.lofo_app.data.api.services.ApiService

class AnnouncementRepository {
    suspend fun GetAllAnnouncements() : BulkAnnouncementDto {
        return ApiService.announcementService.getAllAnnouncements()
    }

    suspend fun GetAllPendingAnnouncements(): PendingAnnouncements {
        return ApiService.announcementService.getPendingAnnouncements()
    }

    suspend fun CreateAnnouncement( announcementDto: CreateAnnouncementRequest): CreateAnnouncementResponse {
        return ApiService.announcementService.createAnnouncement(announcementDto)
    }

    suspend fun GetAnnouncementDetail(id: String) : AnnouncementDetail {
        return ApiService.announcementService.getAnnouncementDetail(id)
    }

    suspend fun UpdateAnnounecment(id: String, announcementDto: UpdateAnnouncementRequest): UpdateAnnouncementResponse {
        return ApiService.announcementService.updateAnnouncementStatus(id, announcementDto)
    }

    suspend fun DeleteAnnouncement(id: String): AnnouncementDetail {
        return ApiService.announcementService.deleteAnnouncement(id)
    }
}