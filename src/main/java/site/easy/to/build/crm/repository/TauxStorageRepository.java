package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.TauxStorage;

@Repository
public interface TauxStorageRepository extends JpaRepository<TauxStorage, Integer> {

    // Récupérer le dernier taux créé
    TauxStorage findTopByOrderByCreatedAtDesc();
}
