import { BaseEntity, User } from '../../shared';
import { Team } from '../team/team.model';

export class TeamMember implements BaseEntity {
    constructor(
        public id?: number,
        public address?: string,
        public phone?: string,
        public dateOfBirth?: any,
        public diplome?: string,
        public resume?: string,
        public team?: Team,
        public user?: User,
    ) {
    }
}
