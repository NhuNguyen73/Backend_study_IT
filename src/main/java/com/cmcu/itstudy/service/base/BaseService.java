package com.cmcu.itstudy.service.base;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Base abstract service providing shared helper methods.
 */
public abstract class BaseService {

    /**
     * Generate a random UUID.
     *
     * @return generated {@link UUID}
     */
    protected UUID generateId() {
        return UUID.randomUUID();
    }

    /**
     * Get current system time.
     *
     * @return current {@link LocalDateTime}
     */
    protected LocalDateTime now() {
        return LocalDateTime.now();
    }
}


