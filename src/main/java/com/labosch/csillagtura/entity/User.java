package com.labosch.csillagtura.entity;

import com.labosch.csillagtura.entity.externalaccount.ExternalAccountDetail;
import com.labosch.csillagtura.entity.externalaccount.GithubExternalAccountDetail;
import com.labosch.csillagtura.entity.externalaccount.GoogleExternalAccountDetail;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="user_")
public class User implements Serializable {//TODO: Get rid of these EAGER fetchings!!!! They have to be lazy!!! (currently hibernate exceptions don't let lazy work)
    static final long serialVersionUID = 42L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private Boolean enabled;

    @ManyToOne(fetch = FetchType.EAGER)
    private User joinedInto;//The account which this account is joined in. Null if this account was not joined in another one.

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = {CascadeType.REMOVE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<UserEmailAddress> userEmailAddresses = new ArrayList<>();


    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ExternalAccountDetail> externalAccountDetails = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<GoogleExternalAccountDetail> googleExternalAccountDetails = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<GithubExternalAccountDetail> githubExternalAccountDetails = new ArrayList<>();



    @OneToMany(fetch = FetchType.LAZY, mappedBy = "approverUser", cascade = {CascadeType.REMOVE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<AccountJoinInitiation> accountJoinInitiationsToApprove = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "initiatorUser", cascade = {CascadeType.REMOVE})
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

    public List<UserEmailAddress> getUserEmailAddresses() {
        return userEmailAddresses;
    }

    public User getJoinedInto() {
        return joinedInto;
    }

    public void setJoinedInto(User joinedInto) {
        this.joinedInto = joinedInto;
    }

    public List<AccountJoinInitiation> getAccountJoinInitiationsToApprove() {
        return accountJoinInitiationsToApprove;
    }

    public AccountJoinInitiation getInitiatedAccountJoinInitiation() {
        return initiatedAccountJoinInitiation;
    }

    public void setInitiatedAccountJoinInitiation(AccountJoinInitiation initiatedAccountJoinInitiationsToApprove) {
        this.initiatedAccountJoinInitiation = initiatedAccountJoinInitiationsToApprove;
    }

    public boolean equalsById(User otherUser) {
        return this.getId() != null
                && this.getId().equals(otherUser.getId());
    }

    public List<GoogleExternalAccountDetail> getGoogleExternalAccountDetails() {
        return googleExternalAccountDetails;
    }

    public List<GithubExternalAccountDetail> getGithubExternalAccountDetails() {
        return githubExternalAccountDetails;
    }

    public List<ExternalAccountDetail> getExternalAccountDetails() {
        return externalAccountDetails;
    }
}