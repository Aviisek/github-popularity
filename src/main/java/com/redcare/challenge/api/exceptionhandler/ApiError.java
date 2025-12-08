package com.redcare.challenge.api.exceptionhandler;

import java.util.List;

public record ApiError(int errorCode, List<String> errorMessage) {
}
