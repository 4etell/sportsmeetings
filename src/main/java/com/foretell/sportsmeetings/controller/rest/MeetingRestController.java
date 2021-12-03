package com.foretell.sportsmeetings.controller.rest;

import com.foretell.sportsmeetings.dto.req.MeetingCategoryReqDto;
import com.foretell.sportsmeetings.dto.req.MeetingReqDto;
import com.foretell.sportsmeetings.dto.req.RequestToJoinMeetingReqDto;
import com.foretell.sportsmeetings.dto.req.RequestToJoinMeetingStatusReqDto;
import com.foretell.sportsmeetings.dto.req.UpdateParticipantReqDto;
import com.foretell.sportsmeetings.dto.res.MeetingCategoryResDto;
import com.foretell.sportsmeetings.dto.res.MeetingResDto;
import com.foretell.sportsmeetings.dto.res.RequestToJoinMeetingResDto;
import com.foretell.sportsmeetings.dto.res.page.extnds.PageMeetingCategoryResDto;
import com.foretell.sportsmeetings.dto.res.page.extnds.PageMeetingResDto;
import com.foretell.sportsmeetings.dto.res.page.extnds.PageRequestToJoinMeetingResDto;
import com.foretell.sportsmeetings.model.MeetingStatus;
import com.foretell.sportsmeetings.security.jwt.JwtProvider;
import com.foretell.sportsmeetings.service.MeetingCategoryService;
import com.foretell.sportsmeetings.service.MeetingService;
import com.foretell.sportsmeetings.service.RequestToJoinMeetingService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("meetings")
public class MeetingRestController {

    private final JwtProvider jwtProvider;
    private final MeetingService meetingService;
    private final MeetingCategoryService meetingCategoryService;
    private final RequestToJoinMeetingService requestToJoinMeetingService;


    public MeetingRestController(JwtProvider jwtProvider, MeetingService meetingService, MeetingCategoryService meetingCategoryService, RequestToJoinMeetingService requestToJoinMeetingService) {
        this.jwtProvider = jwtProvider;
        this.meetingService = meetingService;
        this.meetingCategoryService = meetingCategoryService;
        this.requestToJoinMeetingService = requestToJoinMeetingService;
    }

    @PostMapping
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
    @GetMapping
    public PageMeetingResDto getAllMeetings(
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam double userLatitude,
            @RequestParam double userLongitude,
            @RequestParam int distanceInMeters,
            @PageableDefault(size = 3) Pageable pageable) {

        return meetingService.getAllByCategoryAndDistance(pageable, categoryIds, userLatitude, userLongitude, distanceInMeters);
    }

    @GetMapping("{meetingId}")
    public MeetingResDto getMeetingById(@PathVariable Long meetingId) {
        return meetingService.getById(meetingId);
    }

    @PutMapping("{meetingId}")
    public MeetingResDto updateParticipantInMeeting(@PathVariable Long meetingId,
                                                    @RequestBody @Valid UpdateParticipantReqDto updateParticipantReqDto,
                                                    HttpServletRequest httpServletRequest) {
        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));

        return meetingService.updateParticipantsInMeeting(meetingId, updateParticipantReqDto, usernameFromToken);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
    })
    @GetMapping("created")
    public PageMeetingResDto getMyCreatedMeetings(
            HttpServletRequest httpServletRequest,
            @RequestParam MeetingStatus meetingStatus,
            @PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {

        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));

        return meetingService.getAllByCreatorUsername(pageable, usernameFromToken, meetingStatus);
    }

    @PostMapping("{meetingId}/requests")
    public ResponseEntity<?> createRequestToJoinMeeting(@PathVariable Long meetingId,
                                                        @RequestBody @Valid RequestToJoinMeetingReqDto requestToJoinMeetingReqDto,
                                                        HttpServletRequest httpServletRequest) {
        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));

        if (requestToJoinMeetingService.create(meetingId, requestToJoinMeetingReqDto, usernameFromToken)) {
            return ResponseEntity.ok().body("Successfully created");
        } else {
            return ResponseEntity.internalServerError().body("Something wrong on server");
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
    })
    @GetMapping("{meetingId}/requests")
    public PageRequestToJoinMeetingResDto getRequestsByMeetingId(
            @PathVariable Long meetingId,
            HttpServletRequest httpServletRequest,
            @PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {

        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));

        return requestToJoinMeetingService.getByMeetingId(meetingId, usernameFromToken, pageable);
    }

    @PutMapping("{meetingId}/requests/{requestId}")
    public RequestToJoinMeetingResDto updateStatus(
            @PathVariable Long meetingId,
            @PathVariable Long requestId,
            @RequestBody @Valid RequestToJoinMeetingStatusReqDto requestToJoinMeetingStatusReqDto,
            HttpServletRequest httpServletRequest) {
        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));

        return requestToJoinMeetingService.updateStatus(requestId, meetingId, requestToJoinMeetingStatusReqDto, usernameFromToken);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
    })
    @GetMapping("attended")
    public PageMeetingResDto getMyAttendedMeetings(
            HttpServletRequest httpServletRequest,
            @RequestParam MeetingStatus meetingStatus,
            @PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {

        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));

        return meetingService.getAllWhereParticipantNotCreatorByParticipantUsername(pageable, usernameFromToken, meetingStatus);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("categories")
    public MeetingCategoryResDto createMeetingCategory(@RequestBody MeetingCategoryReqDto meetingCategoryReqDto) {
        return meetingCategoryService.create(meetingCategoryReqDto);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
    })
    @GetMapping("categories")
    public PageMeetingCategoryResDto getAllCategories(
            @PageableDefault(size = 20, sort = {"name"}) Pageable pageable) {
        return meetingCategoryService.getAll(pageable);
    }

    @GetMapping("categories/{categoryId}")
    public MeetingCategoryResDto getCategoriesById(@PathVariable Long categoryId) {
        return meetingCategoryService.getById(categoryId);
    }


}
