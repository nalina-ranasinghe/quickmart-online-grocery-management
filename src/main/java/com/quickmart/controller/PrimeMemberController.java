// Completed Prime Member Controller

package com.quickmart.controller;

import com.quickmart.model.PrimeMember;
import com.quickmart.service.PrimeMemberService;
import com.quickmart.service.QRCodeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PrimeMemberController {

    private final PrimeMemberService primeMemberService;
    private final QRCodeService qrCodeService;

    @Autowired
    public PrimeMemberController(PrimeMemberService primeMemberService, QRCodeService qrCodeService) {
        this.primeMemberService = primeMemberService;
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/prime-members";
    }

    @GetMapping("/prime-members")
    public String listMembers(Model model) {
        List<PrimeMember> members = primeMemberService.getAllMembers();
        model.addAttribute("members", members);
        return "prime-members";
    }

    @GetMapping("/prime-members/{id}")
    public String getMemberDetails(@PathVariable String id, Model model) {
        return primeMemberService.getMemberById(id)
                .map(member -> {
                    model.addAttribute("member", member);
                    // Generate QR code with member information
                    String qrContent = String.format("Member ID: %s\nName: %s\nTier: %s\nPoints: %d", 
                        member.getId(), member.getName(), member.getTier(), member.getPoints());
                    String qrCode = qrCodeService.generateQRCode(qrContent);
                    model.addAttribute("qrCode", qrCode);
                    return "member-details";
                })
                .orElse("redirect:/prime-members");
    }

    @GetMapping("/prime-members/add")
    public String showAddMemberForm(Model model) {
        model.addAttribute("member", new PrimeMember());
        model.addAttribute("isNew", true);
        return "member-form";
    }

    @GetMapping("/prime-members/edit/{id}")
    public String showEditMemberForm(@PathVariable String id, Model model) {
        return primeMemberService.getMemberById(id)
                .map(member -> {
                    model.addAttribute("member", member);
                    model.addAttribute("isNew", false);
                    return "member-form";
                })
                .orElse("redirect:/prime-members");
    }

    @PostMapping("/prime-members/save")
    public String saveMember(@Valid @ModelAttribute PrimeMember member, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isNew", member.getId() == null);
            return "member-form";
        }
        try {
            if (member.getId() == null) {
                primeMemberService.createMember(member);
            } else {
                primeMemberService.updateMember(member.getId(), member);
            }
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("isNew", member.getId() == null);
            return "member-form";
        }
        return "redirect:/prime-members";
    }

    @GetMapping("/prime-members/delete/{id}")
    public String deleteMember(@PathVariable String id) {
        primeMemberService.deleteMember(id);
        return "redirect:/prime-members";
    }

    // API Endpoints for AJAX calls
    @GetMapping("/prime-members/api/list")
    @ResponseBody
    public List<PrimeMember> apiListMembers() {
        return primeMemberService.getAllMembers();
    }
}