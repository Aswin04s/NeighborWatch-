package com.neighborhoodwatch.repository;

import com.neighborhoodwatch.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByIssueType(Report.IssueType issueType);

    List<Report> findByStatus(Report.Status status);

    List<Report> findByPriority(Report.Priority priority);

    List<Report> findByLocationContainingIgnoreCase(String location);

    @Query("SELECT r FROM Report r ORDER BY r.createdAt DESC")
    List<Report> findAllOrderByCreatedAtDesc();

    List<Report> findByStatusOrderByCreatedAtDesc(Report.Status status);

    long countByStatus(Report.Status status);
}