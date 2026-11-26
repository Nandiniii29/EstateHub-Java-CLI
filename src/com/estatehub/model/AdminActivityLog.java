
package com.estatehub.model;

import java.time.LocalDateTime;

public class AdminActivityLog {

    private int activityId;
    private String adminUserId;
    private Integer propertyId;
    private String action;
    private String description;
    private LocalDateTime activityTime;

    public AdminActivityLog(
            int activityId,
            String adminUserId,
            Integer propertyId,
            String action,
            String description,
            LocalDateTime activityTime
    ) {
        this.activityId = activityId;
        this.adminUserId = adminUserId;
        this.propertyId = propertyId;
        this.action = action;
        this.description = description;
        this.activityTime = activityTime;
    }

    public int getActivityId() {
        return activityId;
    }

    public String getAdminUserId() {
        return adminUserId;
    }

    public Integer getPropertyId() {
        return propertyId;
    }

    public String getAction() {
        return action;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getActivityTime() {
        return activityTime;
    }

    @Override
    public String toString() {
        return "\nActivity ID: " + activityId
                + "\nAdmin User ID: " + adminUserId
                + "\nProperty ID: "
                + (propertyId == null ? "N/A" : propertyId)
                + "\nAction: " + action
                + "\nDescription: " + description
                + "\nActivity Time: " + activityTime;
    }
}
