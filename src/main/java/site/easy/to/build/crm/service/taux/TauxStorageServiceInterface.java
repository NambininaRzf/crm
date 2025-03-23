package site.easy.to.build.crm.service.taux;

import java.util.List;

import site.easy.to.build.crm.entity.TauxStorage;

public interface TauxStorageServiceInterface {
    TauxStorage save(TauxStorage tauxStorage);
    List<TauxStorage> findAll();
    TauxStorage getDefault(); // Récupérer le dernier taux stocké
}