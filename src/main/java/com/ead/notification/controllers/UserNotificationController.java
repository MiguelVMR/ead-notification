package com.ead.notification.controllers;

import com.ead.notification.configs.security.AuthenticationCurrentUserService;
import com.ead.notification.configs.security.UserDetailsImpl;
import com.ead.notification.dtos.NotificationRecordDto;
import com.ead.notification.models.NotificationModel;
import com.ead.notification.services.NotificationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * The Class UserNotificationController
 *
 * @author Miguel Vilela Moraes Ribeiro
 * @since 04/03/2025
 */
@RestController
public class UserNotificationController {

    final NotificationService notificationService;
    final AuthenticationCurrentUserService authenticationCurrentUserService;

    public UserNotificationController(NotificationService notificationService, AuthenticationCurrentUserService authenticationCurrentUserService) {
        this.notificationService = notificationService;
        this.authenticationCurrentUserService = authenticationCurrentUserService;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/users/{userId}/notifications")
    public ResponseEntity<Page<NotificationModel>> getAllNotificationsByUser(@PathVariable(value = "userId") UUID userId,
                                                                             Pageable pageable) {
        UserDetailsImpl userDetails = authenticationCurrentUserService.getCurrentUser();
        if (userId.equals(userDetails.getUserId()) || userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(notificationService.findAllNotificationsByUser(userId, pageable));
        } else {
            throw new AccessDeniedException("Forbidden");
        }
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("/users/{userId}/notifications/{notificationId}")
    public ResponseEntity<Object> updateNotification(@PathVariable(value = "userId") UUID userId,
                                                     @PathVariable(value = "notificationId") UUID notificationId,
                                                     @RequestBody @Valid NotificationRecordDto notificationRecordDto) {
        UserDetailsImpl userDetails = authenticationCurrentUserService.getCurrentUser();
        if (userId.equals(userDetails.getUserId()) || userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(notificationService.updateNotification(notificationRecordDto, notificationService.findByNotificationIdAndUserId(notificationId, userId).get()));
        } else {
            throw new AccessDeniedException("Forbidden");
        }
    }
}
