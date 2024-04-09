import { CourseState } from "./enums";

export type Schedule = {
    monday?: {
        start: number;
        end: number;
    };
    tuesday?: {
        start: number;
        end: number;
    };
    wednesday?: {
        start: number;
        end: number;
    };
    thursday?: {
        start: number;
        end: number;
    };
    friday?: {
        start: number;
        end: number;
    };
    saturday?: {
        start: number;
        end: number;
    };
    sunday?: {
        start: number;
        end: number;
    };
}

export type CourseDTO = {
    id: string;
    name: string;
    description: string;
    level: string;
    courseStartDate: Date;
    courseEndDate: Date;
    room: string;
    instructor: string;
    hourlyRateAmount: number;
    courseState: CourseState;
}

export type RoomDTO = {
    id: string;
    name: string;
    sportCenterId: string;
}