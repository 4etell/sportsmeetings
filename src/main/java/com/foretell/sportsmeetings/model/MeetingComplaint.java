package com.foretell.sportsmeetings.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "meeting_complaint")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class MeetingComplaint extends AbstractEntity {

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "creator_id")
    @NotNull
    private User creator;

    @OneToOne
    @JoinColumn(name = "meeting_id")
    @NotNull
    private Meeting meeting;
}
