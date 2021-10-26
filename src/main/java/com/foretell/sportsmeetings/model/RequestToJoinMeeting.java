package com.foretell.sportsmeetings.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "request_to_join_meeting")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class RequestToJoinMeeting extends AbstractEntity {

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @OneToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestToJoinMeetingStatus status = RequestToJoinMeetingStatus.CREATED;
}
