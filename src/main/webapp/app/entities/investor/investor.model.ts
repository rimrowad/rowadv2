import { BaseEntity, User } from '../../shared';

export class Investor implements BaseEntity {
    constructor(
        public id?: number,
        public address?: string,
        public phone?: string,
        public dateOfBirth?: any,
        public shortDescription?: string,
        public description?: string,
        public user?: User,
    ) {
    }
}
