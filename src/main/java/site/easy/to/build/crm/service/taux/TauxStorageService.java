package site.easy.to.build.crm.service.taux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.TauxStorage;
import site.easy.to.build.crm.repository.TauxStorageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TauxStorageService implements TauxStorageServiceInterface {

    private final TauxStorageRepository tauxStorageRepository;

    @Autowired
    public TauxStorageService(TauxStorageRepository tauxStorageRepository) {
        this.tauxStorageRepository = tauxStorageRepository;
    }

    @Override
    public TauxStorage save(TauxStorage tauxStorage) {
        return tauxStorageRepository.save(tauxStorage);
    }


    @Override
    public List<TauxStorage> findAll() {
        return tauxStorageRepository.findAll();
    }

    @Override
    public TauxStorage getDefault() {
        return Optional.ofNullable(tauxStorageRepository.findTopByOrderByCreatedAtDesc())
                       .orElse(new TauxStorage(100.0)); // Retourne 0.0 par défaut si aucun taux n'est stocké
    }
}
