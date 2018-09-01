import { BaseEntity } from './../../shared';

export class Parameter implements BaseEntity {
    constructor(
        public id?: number,
        public type?: string,
        public value?: string,
    ) {
    }
}
