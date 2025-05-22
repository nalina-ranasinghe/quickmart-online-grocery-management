package com.admin.quickmart.controller;

import com.admin.quickmart.model.PrimeMember;
import com.admin.quickmart.service.PrimeMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ReportController {
    private final PrimeMemberService primeMemberService;

    @Autowired
    public ReportController(PrimeMemberService primeMemberService) {
        this.primeMemberService = primeMemberService;
    }

    @GetMapping("/reports")
    public String showReports(Model model) {
        List<PrimeMember> allMembers = primeMemberService.getAllMembers();
        
        // Calculate statistics
        long totalMembers = allMembers.size();
        Map<String, Long> membersByTier = allMembers.stream()
                .collect(Collectors.groupingBy(PrimeMember::getTier, Collectors.counting()));
        
        double averagePoints = allMembers.stream()
                .mapToInt(PrimeMember::getPoints)
                .average()
                .orElse(0.0);
        
        // Top 5 members by points
        List<PrimeMember> topMembers = allMembers.stream()
                .sorted(Comparator.comparingInt(PrimeMember::getPoints).reversed())
                .limit(5)
                .collect(Collectors.toList());
        
        // Add data to model
        model.addAttribute("totalMembers", totalMembers);
        model.addAttribute("membersByTier", membersByTier);
        model.addAttribute("averagePoints", String.format("%.2f", averagePoints));
        model.addAttribute("topMembers", topMembers);
        model.addAttribute("reportDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        
        return "reports";
    }
} 