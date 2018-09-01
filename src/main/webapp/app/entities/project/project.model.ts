import { BaseEntity } from './../../shared';

export class Project implements BaseEntity {
    constructor(
        public id?: number,
        public title?: string,
        public shortDescription?: string,
        public details?: string,
        public rendement?: number,
        public budget?: number,
        public startDate?: any,
        public estimatedEndDate?: any,
        public status?: string,
        public cible?: string,
        public type?: string,
        public team?: BaseEntity,
    ) {
    }
}
