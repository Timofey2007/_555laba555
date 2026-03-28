package org.example._555laba555.cli;

import org.example._555laba555.cli.InputHelper;
import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.fileManager.Conservation;

public interface Command {
    void justDOIT(ServiceManager services, InputHelper input,
                 Conservation storage, String args) throws Exception;
}