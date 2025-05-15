package com.quickmart.controller;

import com.quickmart.model.PrimeMember;
import com.quickmart.service.PrimeMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
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
        List<PrimeMember> members = primeMemberService.getAllMembers();
        model.addAttribute("totalMembers", members.size());
        model.addAttribute("silverCount", members.stream().filter(m -> "SILVER".equals(m.getTier())).count());
        model.addAttribute("goldCount", members.stream().filter(m -> "GOLD".equals(m.getTier())).count());
        model.addAttribute("platinumCount", members.stream().filter(m -> "PLATINUM".equals(m.getTier())).count());

        // Average points
        int avgPoints = members.isEmpty() ? 0 : (int) Math.round(members.stream().mapToInt(PrimeMember::getPoints).average().orElse(0));
        model.addAttribute("averagePoints", avgPoints);

        // Top 5 members by points
        List<PrimeMember> topMembers = members.stream()
                .sorted(Comparator.comparingInt(PrimeMember::getPoints).reversed())
                .limit(5)
                .collect(Collectors.toList());
        model.addAttribute("topMembers", topMembers);

        // Highest points holder
        PrimeMember topMember = members.stream().max(Comparator.comparingInt(PrimeMember::getPoints)).orElse(null);
        model.addAttribute("topMember", topMember);

        // New members this month (assuming ID or another field has a date, here we just show 0)
        model.addAttribute("newMembersThisMonth", 0); // Placeholder

        // Current month for display
        model.addAttribute("currentMonth", LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy")));

        return "reports";
    }
} 