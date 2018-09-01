import { BaseEntity } from './../../shared';

export class ProjectFile implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public path?: string,
        public type?: string,
        public contentContentType?: string,
        public content?: any,
        public textContent?: any,
        public project?: BaseEntity,
    ) {
    }
}
