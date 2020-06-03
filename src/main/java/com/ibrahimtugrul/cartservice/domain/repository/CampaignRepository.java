package com.ibrahimtugrul.cartservice.domain.repository;

import com.ibrahimtugrul.cartservice.domain.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
}