package com.ibrahimtugrul.cartservice.domain.repository;

import com.ibrahimtugrul.cartservice.domain.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    Optional<List<Campaign>> findByCategoryId(final Long categoryId);
}