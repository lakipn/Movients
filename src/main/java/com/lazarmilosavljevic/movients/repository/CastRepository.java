package com.lazarmilosavljevic.movients.repository;

import com.lazarmilosavljevic.movients.domain.Cast;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Cast entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CastRepository extends JpaRepository<Cast, Long>, JpaSpecificationExecutor<Cast> {

}
