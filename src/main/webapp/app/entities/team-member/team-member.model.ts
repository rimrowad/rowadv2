import { BaseEntity, User } from '../../shared';

export class TeamMember implements BaseEntity {
    constructor(
        public id?: number,
        public address?: string,
        public phone?: string,
        public dateOfBirth?: any,
        public diplome?: string,
        public resume?: string,
        public team?: BaseEntity,
        public user?: User,
    ) {
    }
}
