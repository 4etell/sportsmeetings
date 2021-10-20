package com.foretell.sportsmeetings.service.impl;

import com.foretell.sportsmeetings.dto.req.AdminMeetingCategoryReqDto;
import com.foretell.sportsmeetings.dto.res.MeetingCategoryResDto;
import com.foretell.sportsmeetings.exception.RoleNotFoundException;
import com.foretell.sportsmeetings.model.MeetingCategory;
import com.foretell.sportsmeetings.model.Role;
import com.foretell.sportsmeetings.model.User;
import com.foretell.sportsmeetings.repo.RoleRepo;
import com.foretell.sportsmeetings.repo.UserRepo;
import com.foretell.sportsmeetings.service.AdminService;
import com.foretell.sportsmeetings.service.MeetingCategoryService;
import com.foretell.sportsmeetings.service.ProfileCommentService;
import com.foretell.sportsmeetings.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private final UserService userService;
    private final MeetingCategoryService meetingCategoryService;
    private final ProfileCommentService profileCommentService;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    public AdminServiceImpl(UserService userService, MeetingCategoryService meetingCategoryService, ProfileCommentService profileCommentService, UserRepo userRepo, RoleRepo roleRepo) {
        this.userService = userService;
        this.meetingCategoryService = meetingCategoryService;
        this.profileCommentService = profileCommentService;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public boolean setUserRoleBanned(Long userId) {
        User user = userService.findById(userId);
        List<Role> roles = new ArrayList<Role>();
        Role roleBanned = roleRepo.findByName("ROLE_BANNED").
                orElseThrow(() -> new RoleNotFoundException("ROLE_BANNED not found"));
        roles.add(roleBanned);
        user.setRoles(roles);
        userRepo.save(user);
        return true;
    }

    @Override
    public boolean deleteUserRoleBanned(Long userId) {
        User user = userService.findById(userId);
        List<Role> roles = new ArrayList<Role>();
        Role roleUser = roleRepo.findByName("ROLE_USER").
                orElseThrow(() -> new RoleNotFoundException("ROLE_USER not found"));
        roles.add(roleUser);
        user.setRoles(roles);
        userRepo.save(user);
        return true;
    }

    @Override
    public MeetingCategoryResDto createMeetingCategory(AdminMeetingCategoryReqDto adminMeetingCategoryReqDto) {
        MeetingCategory meetingCategory = new MeetingCategory();
        meetingCategory.setName(adminMeetingCategoryReqDto.getName());
        return meetingCategoryService.create(meetingCategory);
    }

    @Override
    public boolean deleteProfileCommentById(Long id) {
        profileCommentService.deleteById(id);
        return true;
    }
}
