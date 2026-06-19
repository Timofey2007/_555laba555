package org.example._555laba555.cli;

import org.example._555laba555.service.ServiceManager;

public interface Command {
    void justDOIT(ServiceManager services, InputHelper input, String args) throws Exception;
}