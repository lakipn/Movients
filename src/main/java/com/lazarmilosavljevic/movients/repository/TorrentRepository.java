package com.lazarmilosavljevic.movients.repository;

import com.lazarmilosavljevic.movients.domain.Torrent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Torrent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TorrentRepository extends JpaRepository<Torrent, Long>, JpaSpecificationExecutor<Torrent> {

}
