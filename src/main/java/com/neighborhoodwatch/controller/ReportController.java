package com.neighborhoodwatch.controller;

import com.neighborhoodwatch.model.Report;
import com.neighborhoodwatch.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // Display all reports
    @GetMapping
    public String getAllReports(Model model) {
        model.addAttribute("reports", reportService.getAllReports());
        model.addAttribute("pendingCount", reportService.getReportCountByStatus(Report.Status.PENDING));
        model.addAttribute("inProgressCount", reportService.getReportCountByStatus(Report.Status.IN_PROGRESS));
        model.addAttribute("resolvedCount", reportService.getReportCountByStatus(Report.Status.RESOLVED));
        return "reports/list";
    }

    // Show create report form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("report", new Report());
        return "reports/create";
    }

    // Create new report
    @PostMapping
    public String createReport(@Valid @ModelAttribute Report report, BindingResult result) {
        if (result.hasErrors()) {
            return "reports/create";
        }
        reportService.createReport(report);
        return "redirect:/reports";
    }

    // Show report details
    @GetMapping("/{id}")
    public String getReportById(@PathVariable Long id, Model model) {
        Report report = reportService.getReportById(id).orElse(null);
        if (report == null) {
            return "redirect:/reports";
        }
        model.addAttribute("report", report);
        return "reports/detail";
    }

    // Show edit form
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Report report = reportService.getReportById(id).orElse(null);
        if (report == null) {
            return "redirect:/reports";
        }
        model.addAttribute("report", report);
        return "reports/edit";
    }

    // Update report
    @PostMapping("/{id}/edit")
    public String updateReport(@PathVariable Long id, @Valid @ModelAttribute Report report, BindingResult result) {
        if (result.hasErrors()) {
            return "reports/edit";
        }
        reportService.updateReport(id, report);
        return "redirect:/reports/" + id;
    }

    // Update status
    @PostMapping("/{id}/status")
    public String updateReportStatus(@PathVariable Long id, @RequestParam Report.Status status) {
        reportService.updateReportStatus(id, status);
        return "redirect:/reports/" + id;
    }

    // Delete report
    @PostMapping("/{id}/delete")
    public String deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return "redirect:/reports";
    }

    // Filter reports by type
    @GetMapping("/type/{issueType}")
    public String getReportsByType(@PathVariable Report.IssueType issueType, Model model) {
        List<Report> reports = reportService.getReportsByType(issueType);
        model.addAttribute("reports", reports);
        model.addAttribute("filterType", issueType.toString());
        return "reports/list";
    }

    // Filter reports by status
    @GetMapping("/status/{status}")
    public String getReportsByStatus(@PathVariable Report.Status status, Model model) {
        List<Report> reports = reportService.getReportsByStatus(status);
        model.addAttribute("reports", reports);
        model.addAttribute("filterStatus", status.toString());
        return "reports/list";
    }

    // Search reports by location
    @GetMapping("/search")
    public String searchReportsByLocation(@RequestParam String location, Model model) {
        List<Report> reports = reportService.getReportsByLocation(location);
        model.addAttribute("reports", reports);
        model.addAttribute("searchLocation", location);
        return "reports/list";
    }
}