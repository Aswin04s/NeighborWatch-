package com.neighborhoodwatch.service;

import com.neighborhoodwatch.model.Report;
import com.neighborhoodwatch.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public List<Report> getAllReports() {
        return reportRepository.findAllOrderByCreatedAtDesc();
    }

    public Optional<Report> getReportById(Long id) {
        return reportRepository.findById(id);
    }

    public Report createReport(Report report) {
        // Set default priority based on issue type
        if (report.getPriority() == null) {
            if (report.getIssueType() == Report.IssueType.SUSPICIOUS_ACTIVITY) {
                report.setPriority(Report.Priority.HIGH);
            } else {
                report.setPriority(Report.Priority.MEDIUM);
            }
        }
        return reportRepository.save(report);
    }

    public Report updateReportStatus(Long id, Report.Status status) {
        Optional<Report> reportOpt = reportRepository.findById(id);
        if (reportOpt.isPresent()) {
            Report report = reportOpt.get();
            report.setStatus(status);
            return reportRepository.save(report);
        }
        return null;
    }

    public Report updateReport(Long id, Report updatedReport) {
        Optional<Report> reportOpt = reportRepository.findById(id);
        if (reportOpt.isPresent()) {
            Report report = reportOpt.get();
            report.setTitle(updatedReport.getTitle());
            report.setDescription(updatedReport.getDescription());
            report.setIssueType(updatedReport.getIssueType());
            report.setPriority(updatedReport.getPriority());
            report.setLocation(updatedReport.getLocation());
            report.setReporterName(updatedReport.getReporterName());
            report.setReporterContact(updatedReport.getReporterContact());
            return reportRepository.save(report);
        }
        return null;
    }

    public boolean deleteReport(Long id) {
        if (reportRepository.existsById(id)) {
            reportRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Report> getReportsByType(Report.IssueType issueType) {
        return reportRepository.findByIssueType(issueType);
    }

    public List<Report> getReportsByStatus(Report.Status status) {
        return reportRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    public List<Report> getReportsByLocation(String location) {
        return reportRepository.findByLocationContainingIgnoreCase(location);
    }

    public long getReportCountByStatus(Report.Status status) {
        return reportRepository.countByStatus(status);
    }

}