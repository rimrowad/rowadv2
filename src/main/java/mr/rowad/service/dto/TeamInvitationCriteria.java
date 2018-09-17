package mr.rowad.service.dto;

import java.io.Serializable;

import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;



/**
 * Criteria class for the TeamInvitation entity. This class is used in TeamInvitationResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /team-invitations?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TeamInvitationCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter status;

    private LocalDateFilter date;

    private LongFilter senderId;

    private LongFilter receiverId;

    private LongFilter teamId;

    private String senderLogin;

    private String receiverLogin;

    public TeamInvitationCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getStatus() {
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public LongFilter getSenderId() {
        return senderId;
    }

    public void setSenderId(LongFilter senderId) {
        this.senderId = senderId;
    }

    public LongFilter getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(LongFilter receiverId) {
        this.receiverId = receiverId;
    }

    public LongFilter getTeamId() {
        return teamId;
    }

    public void setTeamId(LongFilter teamId) {
        this.teamId = teamId;
    }

    public String getSenderLogin() {
        return senderLogin;
    }

    public void setSenderLogin(String senderLogin) {
        this.senderLogin = senderLogin;
    }

    public String getReceiverLogin() {
        return receiverLogin;
    }

    public void setReceiverLogin(String receiverLogin) {
        this.receiverLogin = receiverLogin;
    }

    @Override
    public String toString() {
        return "TeamInvitationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (senderId != null ? "senderId=" + senderId + ", " : "") +
                (receiverId != null ? "receiverId=" + receiverId + ", " : "") +
                (teamId != null ? "teamId=" + teamId + ", " : "") +
                (senderLogin != null ? "senderLogin=" + senderLogin + ", " : "") +
                (receiverLogin != null ? "receiverLogin=" + receiverLogin + ", " : "") +
            "}";
    }
}
