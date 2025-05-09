package com.springboot.homework.actuators;

import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@ConditionalOnEnabledHealthIndicator("disk_space_health")
public class DiskSpaceHealthIndicator implements HealthIndicator {

    private static final long DISK_SPACE_THRESHOLD = 500 * 1024 * 1024; // 500 MB

    @Override
    public Health health() {
        File diskRoot = new File("/");
        long freeSpace = diskRoot.getFreeSpace();

        if (freeSpace >= DISK_SPACE_THRESHOLD) {
            return Health.up().withDetail("Disk Space", formatSize(freeSpace) + " free").build();
        } else {
            return Health.down().withDetail("Disk Space", formatSize(freeSpace) + " free")
                    .withDetail("Threshold", formatSize(DISK_SPACE_THRESHOLD)).build();
        }
    }

    private String formatSize(long bytes) {
        return bytes / (1024 * 1024) + " MB";
    }
}
