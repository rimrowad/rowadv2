package mr.rowad.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A TeamInvitation.
 */
@Entity
@Table(name = "team_invitation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TeamInvitation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "jhi_date")
    private LocalDate date;

    @ManyToOne
    private TeamMember sender;

    @ManyToOne
    private TeamMember receiver;

    @ManyToOne
    private Team team;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public TeamInvitation status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public TeamInvitation date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TeamMember getSender() {
        return sender;
    }

    public TeamInvitation sender(TeamMember teamMember) {
        this.sender = teamMember;
        return this;
    }

    public void setSender(TeamMember teamMember) {
        this.sender = teamMember;
    }

    public TeamMember getReceiver() {
        return receiver;
    }

    public TeamInvitation receiver(TeamMember teamMember) {
        this.receiver = teamMember;
        return this;
    }

    public void setReceiver(TeamMember teamMember) {
        this.receiver = teamMember;
    }

    public Team getTeam() {
        return team;
    }

    public TeamInvitation team(Team team) {
        this.team = team;
        return this;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TeamInvitation teamInvitation = (TeamInvitation) o;
        if (teamInvitation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), teamInvitation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TeamInvitation{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
