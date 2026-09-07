package com.compus.placementmanagement;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByStudentId(Long studentId);

    List<Application> findByJobId(Long jobId);

    boolean existsByStudentIdAndJobId(Long studentId, Long jobId);

    long countByJobId(Long jobId);

    long countByStudentId(Long studentId);
}