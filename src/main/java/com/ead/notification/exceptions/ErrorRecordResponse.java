package com.ead.notification.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
* The Record ErrorRecordResponse
*
* @author Miguel Vilela Moraes Ribeiro
* @since 04/03/2025
*/
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorRecordResponse(int errorCode,
                                  String errorMessage,
                                  Map<String, String> errorsDetails
) {
}
