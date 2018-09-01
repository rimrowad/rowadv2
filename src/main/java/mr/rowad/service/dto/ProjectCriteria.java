package mr.rowad.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;


import io.github.jhipster.service.filter.LocalDateFilter;



/**
 * Criteria class for the Project entity. This class is used in ProjectResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /projects?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProjectCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter title;

    private StringFilter shortDescription;

    private StringFilter details;

    private DoubleFilter rendement;

    private DoubleFilter budget;

    private LocalDateFilter startDate;

    private LocalDateFilter estimatedEndDate;

    private StringFilter status;

    private StringFilter cible;

    private StringFilter type;

    private LongFilter teamId;

    public ProjectCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(StringFilter shortDescription) {
        this.shortDescription = shortDescription;
    }

    public StringFilter getDetails() {
        return details;
    }

    public void setDetails(StringFilter details) {
        this.details = details;
    }

    public DoubleFilter getRendement() {
        return rendement;
    }

    public void setRendement(DoubleFilter rendement) {
        this.rendement = rendement;
    }

    public DoubleFilter getBudget() {
        return budget;
    }

    public void setBudget(DoubleFilter budget) {
        this.budget = budget;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEstimatedEndDate() {
        return estimatedEndDate;
    }

    public void setEstimatedEndDate(LocalDateFilter estimatedEndDate) {
        this.estimatedEndDate = estimatedEndDate;
    }

    public StringFilter getStatus() {
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public StringFilter getCible() {
        return cible;
    }

    public void setCible(StringFilter cible) {
        this.cible = cible;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public LongFilter getTeamId() {
        return teamId;
    }

    public void setTeamId(LongFilter teamId) {
        this.teamId = teamId;
    }

    @Override
    public String toString() {
        return "ProjectCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (shortDescription != null ? "shortDescription=" + shortDescription + ", " : "") +
                (details != null ? "details=" + details + ", " : "") +
                (rendement != null ? "rendement=" + rendement + ", " : "") +
                (budget != null ? "budget=" + budget + ", " : "") +
                (startDate != null ? "startDate=" + startDate + ", " : "") +
                (estimatedEndDate != null ? "estimatedEndDate=" + estimatedEndDate + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (cible != null ? "cible=" + cible + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (teamId != null ? "teamId=" + teamId + ", " : "") +
            "}";
    }

}
