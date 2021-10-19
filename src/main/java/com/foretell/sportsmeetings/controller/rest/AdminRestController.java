package com.foretell.sportsmeetings.controller.rest;

import com.foretell.sportsmeetings.dto.req.AdminMeetingCategoryReqDto;
import com.foretell.sportsmeetings.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
public class AdminRestController {

    private final AdminService adminService;

    public AdminRestController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("ban-user/{userId}")
    public ResponseEntity<?> banUser(@PathVariable Long userId) {
        if (adminService.setUserRoleBanned(userId)) {
            return ResponseEntity.ok("Successfully banned");
        } else {
            return ResponseEntity.internalServerError().body("Something wrong on server");
        }
    }

    @PostMapping("unban-user/{userId}")
    public ResponseEntity<?> unbanUser(@PathVariable Long userId) {
        if (adminService.deleteUserRoleBanned(userId)) {
            return ResponseEntity.ok("Successfully unbanned");
        } else {
            return ResponseEntity.internalServerError().body("Something wrong on server");
        }
    }

    @PostMapping("meeting-categories")
    public ResponseEntity<?> createMeetingCategory(@RequestBody AdminMeetingCategoryReqDto adminMeetingCategoryReqDto) {
        if (adminService.createMeetingCategory(adminMeetingCategoryReqDto)) {
            return ResponseEntity.ok("Successfully created");
        } else {
            return ResponseEntity.internalServerError().body("Something wrong on server");
        }
    }

    @DeleteMapping("comments/{id}")
    public ResponseEntity<?> deleteCommentById(@PathVariable Long id) {
        if (adminService.deleteProfileCommentById(id)) {
            return ResponseEntity.ok("Successfully deleted");
        } else {
            return ResponseEntity.internalServerError().body("Something wrong on server");
        }
    }
}
