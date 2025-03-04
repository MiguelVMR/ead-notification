package com.ead.notification.dtos;

import com.ead.notification.enums.NotificationStatus;
import jakarta.validation.constraints.NotNull;

/**
 * The Record NotificationRecordDto
 *
 * @author Miguel Vilela Moraes Ribeiro
 * @since 04/03/2025
 */
public record NotificationRecordDto(@NotNull NotificationStatus notificationStatus) {
}
