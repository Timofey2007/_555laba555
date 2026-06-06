package org.example._555laba555.ui.commands;

import org.example._555laba555.domain.BatchUnit;
import org.example._555laba555.domain.ReagentBatch;
import org.example._555laba555.service.ServiceManager;
import java.time.Instant;

public class BatchAddUI {
    private final ServiceManager services;

    public BatchAddUI(ServiceManager services) {
        this.services = services;
    }

    public void execute(long reagentId, String label, double quantity, BatchUnit unit,
                        String location, Instant expiresAt) {
        ReagentBatch batch = new ReagentBatch();
        String login = services.getUserService().getCurrentUserLogin();

        batch.setReagentId(reagentId);
        batch.setLabel(label);
        batch.setQuantityCurrent(quantity);
        batch.setUnit(unit);
        batch.setLocation(location);
        batch.setExpiresAt(expiresAt);
        batch.setOwnerUsername(login);

        services.getBatchService().add(batch);
    }
}