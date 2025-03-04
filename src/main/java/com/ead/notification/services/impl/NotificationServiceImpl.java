package com.ead.notification.services.impl;

import com.ead.notification.dtos.NotificationRecordCommandDto;
import com.ead.notification.dtos.NotificationRecordDto;
import com.ead.notification.enums.NotificationStatus;
import com.ead.notification.exceptions.NotFoundException;
import com.ead.notification.models.NotificationModel;
import com.ead.notification.repositories.NotificationRepository;
import com.ead.notification.services.NotificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

/**
 * The Class NotificationServiceImpl
 *
 * @author Miguel Vilela Moraes Ribeiro
 * @since 10/02/2025
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public NotificationModel saveNotification(NotificationRecordCommandDto notificationRecordCommandDto) {
        var notification = new NotificationModel();
        BeanUtils.copyProperties(notificationRecordCommandDto, notification);
        notification.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        notification.setNotificationStatus(NotificationStatus.CREATED);
        return notificationRepository.save(notification);
    }

    @Override
    public Page<NotificationModel> findAllNotificationsByUser(UUID userId, Pageable pageable) {
        return notificationRepository
                .findAllByUserIdAndNotificationStatus(userId, NotificationStatus.CREATED, pageable);
    }

    @Override
    public Optional<NotificationModel> findByNotificationIdAndUserId(UUID notificationId, UUID userId) {
        Optional<NotificationModel> notificationModelOptional =
                notificationRepository.findByNotificationIdAndUserId(notificationId, userId);

        if (notificationModelOptional.isEmpty()) {
            throw new NotFoundException("Error: Notification for this user not found.");
        }

        return notificationModelOptional;
    }

    @Override
    public NotificationModel updateNotification(NotificationRecordDto notificationRecordDto, NotificationModel notificationModel) {
        notificationModel.setNotificationStatus(notificationRecordDto.notificationStatus());
        return notificationRepository.save(notificationModel);
    }
}
