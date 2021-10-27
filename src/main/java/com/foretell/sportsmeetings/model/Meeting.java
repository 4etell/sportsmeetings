package com.foretell.sportsmeetings.model;

import com.foretell.sportsmeetings.exception.MaxNumbOfMeetingParticipantsException;
import com.vividsolutions.jts.geom.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "meetings")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Meeting extends AbstractEntity {

    @OneToOne
    @JoinColumn(name = "category_id")
    @NotNull
    private MeetingCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MeetingStatus status;

    @NotNull
    @Column(name = "description")
    private String description;

    @Column(columnDefinition = "geography")
    private Point geom;


    @NotNull
    @Column(name = "start_date")
    private GregorianCalendar startDate;

    @NotNull
    @Column(name = "end_date")
    private GregorianCalendar endDate;

    @Column(name = "max_num_of_participants")
    private int maxNumbOfParticipants = 2;

    @OneToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "meeting_participants",
            joinColumns = {@JoinColumn(name = "meeting_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "participant_id", referencedColumnName = "id")})
    private Set<User> participants = new HashSet<User>();

    public synchronized boolean addParticipant(User user) {
        if (maxNumbOfParticipants != participants.size()) {
            return participants.add(user);
        } else {
            throw new MaxNumbOfMeetingParticipantsException("All places of participants are taken");
        }
    }

    public synchronized boolean removeParticipant(User user) {
        return participants.remove(user);
    }

    public synchronized boolean isParticipant(User user) {return participants.contains(user);}
}
