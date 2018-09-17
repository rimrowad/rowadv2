import { BaseEntity } from '../../shared';
import { TeamMember } from '../team-member';
import { Team } from '../team/team.model';

export class TeamInvitation implements BaseEntity {
    constructor(
        public id?: number,
        public status?: string,
        public date?: any,
        public sender?: TeamMember,
        public receiver?: TeamMember,
        public team?: Team,
    ) {
    }
}
