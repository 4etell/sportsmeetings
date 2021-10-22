package com.foretell.sportsmeetings.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
    @JoinColumn(name = "meeting_category_id")
    @NotNull
    private MeetingCategory category;

    @NotNull
    @Column(name = "description")
    private String description;

    @Column(name = "first_coordinate")
    private float firstCoordinate;

    @Column(name = "second_coordinate")
    private float secondCoordinate;

    @NotNull
    @Column(name = "date")
    private GregorianCalendar date;

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

    public boolean addParticipant(User user) {
        return participants.add(user);
    }
}
