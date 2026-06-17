package org.example._555laba555.storage;

import org.example._555laba555.service.ServiceManager;

public interface Storage {
    void save(ServiceManager services) throws Exception;
    void load(ServiceManager services) throws Exception;
}