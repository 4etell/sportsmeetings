package com.foretell.sportsmeetings.controller.rest;

import com.foretell.sportsmeetings.dto.req.RequestToJoinMeetingReqDto;
import com.foretell.sportsmeetings.dto.res.RequestToJoinMeetingResDto;
import com.foretell.sportsmeetings.service.RequestToJoinMeetingService;
import com.foretell.sportsmeetings.util.jwt.JwtProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("requests-to-join-meeting")
public class RequestToJoinMeetingController {

    private final JwtProvider jwtProvider;
    private final RequestToJoinMeetingService requestToJoinMeetingService;

    public RequestToJoinMeetingController(JwtProvider jwtProvider, RequestToJoinMeetingService requestToJoinMeetingService) {
        this.jwtProvider = jwtProvider;
        this.requestToJoinMeetingService = requestToJoinMeetingService;
    }

    @PostMapping("create")
    public ResponseEntity<?> createRequestToJoinMeeting(@RequestBody @Valid RequestToJoinMeetingReqDto requestToJoinMeetingReqDto,
                                                        HttpServletRequest httpServletRequest) {
        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));

        if (requestToJoinMeetingService.create(requestToJoinMeetingReqDto, usernameFromToken)) {
            return ResponseEntity.ok().body("Successfully created");
        } else {
            return ResponseEntity.internalServerError().body("Something wrong on server");
        }
    }

    @GetMapping
    public List<RequestToJoinMeetingResDto> getRequestsByMeetingId(@RequestParam Long meetingId,
                                                                   HttpServletRequest httpServletRequest) {
        String usernameFromToken =
                jwtProvider.getUsernameFromToken(jwtProvider.getTokenFromRequest(httpServletRequest));

        return requestToJoinMeetingService.getByMeetingId(meetingId, usernameFromToken);
    }
}
