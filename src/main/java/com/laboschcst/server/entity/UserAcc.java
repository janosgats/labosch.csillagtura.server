package com.laboschcst.server.entity;

import com.laboschcst.server.config.auth.authorities.Authority;
import com.laboschcst.server.config.auth.authorities.EnumBasedAuthority;
import com.laboschcst.server.entity.externalaccount.ExternalAccountDetail;
import com.laboschcst.server.entity.externalaccount.GithubExternalAccountDetail;
import com.laboschcst.server.entity.externalaccount.GoogleExternalAccountDetail;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "userAcc")
public class UserAcc implements Serializable {
    static final long serialVersionUID = 42L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private Boolean enabled;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserAcc joinedInto;//The account which this account is joined in. Null if this account was not joined in another one.

    @OneToMany(mappedBy = "userAcc", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<UserEmailAddress> userEmailAddresses = new HashSet<>();

    @ElementCollection(targetClass = Authority.class, fetch = FetchType.EAGER)
    @JoinTable(name = "granted_authority", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "granted_authority", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Authority> authorities = new HashSet<>();

    @OneToMany(mappedBy = "userAcc", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<ExternalAccountDetail> externalAccountDetails = new HashSet<>();

    @OneToMany(mappedBy = "userAcc", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<GoogleExternalAccountDetail> googleExternalAccountDetails = new HashSet<>();

    @OneToMany(mappedBy = "userAcc", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<GithubExternalAccountDetail> githubExternalAccountDetails = new HashSet<>();


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "approverUserAcc", cascade = {CascadeType.REMOVE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<AccountJoinInitiation> accountJoinInitiationsToApprove = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "initiatorUserAcc", cascade = {CascadeType.REMOVE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AccountJoinInitiation initiatedAccountJoinInitiation;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<UserEmailAddress> getUserEmailAddresses() {
        return userEmailAddresses;
    }

    public UserAcc getJoinedInto() {
        return joinedInto;
    }

    public void setJoinedInto(UserAcc joinedInto) {
        this.joinedInto = joinedInto;
    }

    public Set<AccountJoinInitiation> getAccountJoinInitiationsToApprove() {
        return accountJoinInitiationsToApprove;
    }

    public AccountJoinInitiation getInitiatedAccountJoinInitiation() {
        return initiatedAccountJoinInitiation;
    }

    public void setInitiatedAccountJoinInitiation(AccountJoinInitiation initiatedAccountJoinInitiationsToApprove) {
        this.initiatedAccountJoinInitiation = initiatedAccountJoinInitiationsToApprove;
    }

    public boolean equalsById(UserAcc otherUserAcc) {
        return this.getId() != null
                && this.getId().equals(otherUserAcc.getId());
    }

    public Set<GoogleExternalAccountDetail> getGoogleExternalAccountDetails() {
        return googleExternalAccountDetails;
    }

    public Set<GithubExternalAccountDetail> getGithubExternalAccountDetails() {
        return githubExternalAccountDetails;
    }

    public Set<ExternalAccountDetail> getExternalAccountDetails() {
        return externalAccountDetails;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public List<EnumBasedAuthority> getCopyOfAuthorities_AsEnumBasedAuthority() {
        return this.getAuthorities().stream().map(EnumBasedAuthority::new).collect(Collectors.toList());
    }

    public void setAuthorities_FromEnumBasedAuthority(Collection<EnumBasedAuthority> authorities) {
        this.setAuthorities(authorities.stream()
                .map((ga) -> Authority.fromStringValue(ga.getAuthority()))
                .collect(Collectors.toSet())
        );
    }
}