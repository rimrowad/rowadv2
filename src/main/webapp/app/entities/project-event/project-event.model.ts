import { BaseEntity, User } from './../../shared';

export class ProjectEvent implements BaseEntity {
    constructor(
        public id?: number,
        public type?: string,
        public creationDate?: any,
        public description?: string,
        public project?: BaseEntity,
        public user?: User,
    ) {
    }
}
