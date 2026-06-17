package org.example._555laba555.dataBase;

import org.example._555laba555.service.ServiceManager;
import org.example._555laba555.storage.Storage;
import java.sql.SQLException;

public class DatabaseStorage implements Storage {

    private final ReagentRepository reagentRepo;
    private final BatchRepository batchRepo;
    private final MoveRepository moveRepo;

    public DatabaseStorage() {
        this.reagentRepo = new ReagentRepository();
        this.batchRepo = new BatchRepository();
        this.moveRepo = new MoveRepository();
    }

    @Override
    public void save(ServiceManager services) throws Exception {
        System.out.println("Данные синхронизированы с БД");
    }

    @Override
    public void load(ServiceManager services) throws Exception {
        try {
            services.getReagentService().loadFromList(reagentRepo.findAll());
            services.getBatchService().loadFromList(batchRepo.findAll());
            services.getMoveService().loadFromList(moveRepo.findAll());
            System.out.println("Данные загружены из PostgreSQL");
        } catch (SQLException e) {
            System.err.println("Ошибка загрузки из БД: " + e.getMessage());
            throw e;
        }
    }
}