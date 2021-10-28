package com.foretell.sportsmeetings.controller.rest;

import com.foretell.sportsmeetings.dto.req.MeetingReqDto;
import com.foretell.sportsmeetings.dto.req.UpdateParticipantReqDto;
import com.foretell.sportsmeetings.dto.res.MeetingResDto;
import com.foretell.sportsmeetings.dto.res.page.extnds.PageMeetingResDto;
import com.foretell.sportsmeetings.service.MeetingService;
import com.foretell.sportsmeetings.util.jwt.JwtProvider;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
    })
    @RequestMapping(value = "/meetings", method = RequestMethod.GET)
    public PageMeetingResDto getMeetings(
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam double userLatitude,
            @RequestParam double userLongitude,
            @RequestParam int distanceInMeters,
            @PageableDefault(size = 3) Pageable pageable) {

        return meetingService.getAllByCategoryAndDistance(pageable, categoryIds, userLatitude, userLongitude, distanceInMeters);
    }

    @RequestMapping(value = "/meetings/{id}", method = RequestMethod.GET)
    public MeetingResDto getById(@PathVariable Long id) {
        return meetingService.getById(id);
    }

    @RequestMapping(value = "/meetings/{id}", method = RequestMethod.PUT)
    public MeetingResDto updateParticipantInMeeting(@PathVariable Long id,
                                                    @RequestBody @Valid UpdateParticipantReqDto updateParticipantReqDto,
                                                    HttpServletRequest httpServletRequest) {
        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));

        return meetingService.updateParticipantsInMeeting(id, updateParticipantReqDto, usernameFromToken);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
    })
    @RequestMapping(value = "my-created-meetings", method = RequestMethod.GET)
    public PageMeetingResDto getMyCreatedMeetings(
            HttpServletRequest httpServletRequest,
            @PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {

        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));

        return meetingService.getAllByCreatorUsername(pageable, usernameFromToken);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
    })
    @RequestMapping(value = "my-attended-meetings", method = RequestMethod.GET)
    public PageMeetingResDto getMyAttendedMeetings(
            HttpServletRequest httpServletRequest,
            @PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {

        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));

        return meetingService.getAllWhereParticipantNotCreatorByParticipantUsername(pageable, usernameFromToken);
    }


}
