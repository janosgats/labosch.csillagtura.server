package com.labosch.csillagtura.repo;

import com.labosch.csillagtura.entity.externalaccount.GithubExternalAccountDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GithubExternalAccountDetailRepository extends JpaRepository<GithubExternalAccountDetail, Long> {
    Optional<GithubExternalAccountDetail> findByGithubId(String githubId);
}
