package com.foretell.sportsmeetings.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractEntity {

    @Column(name = "username")
    @NonNull
    private String username;

    @Column(name = "first_name")
    @NonNull
    private String firstName;

    @Column(name = "last_name")
    @NonNull
    private String lastName;

    @Column(name = "email")
    @NonNull
    private String email;

    @Column(name = "password")
    @NonNull
    private String password;

    @Column(name = "email_notifications")
    private boolean emailNotifications = false;

    @Column(name = "telegram_bot_activation_code")
    private String telegramBotActivationCode;

    @Column(name = "telegram_bot_chat_id")
    private Long telegramBotChatId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

    @OneToMany(mappedBy = "recipient")
    private List<ProfileComment> profileComments = new ArrayList<ProfileComment>();


    @ManyToMany(mappedBy = "participants", fetch = FetchType.LAZY)
    private List<Meeting> meetings = new ArrayList<Meeting>();
}
