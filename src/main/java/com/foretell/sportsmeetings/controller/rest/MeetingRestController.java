package com.foretell.sportsmeetings.controller.rest;

import com.foretell.sportsmeetings.dto.req.MeetingReqDto;
import com.foretell.sportsmeetings.dto.res.MeetingResDto;
import com.foretell.sportsmeetings.service.MeetingService;
import com.foretell.sportsmeetings.util.jwt.JwtProvider;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class MeetingRestController {

    private final JwtProvider jwtProvider;
    private final MeetingService meetingService;

    public MeetingRestController(JwtProvider jwtProvider, MeetingService meetingService) {
        this.jwtProvider = jwtProvider;
        this.meetingService = meetingService;
    }

    @RequestMapping(value = "/meetings", method = RequestMethod.POST)
    public MeetingResDto createMeeting(@RequestBody @Valid MeetingReqDto meetingReqDto,
                                       HttpServletRequest httpServletRequest) {
        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));
        return meetingService.createMeeting(meetingReqDto, usernameFromToken);
    }
}
