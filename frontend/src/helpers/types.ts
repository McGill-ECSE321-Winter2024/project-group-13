import { CourseState, InvoiceStatus } from "./enums";

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
    roomDTO: RoomDTO;
    courseState: CourseState;
    schedule: string;
    sportCenter: string;
}

export type RoomDTO = {
    id: string;
    name: string;
    sportCenterId: string;
}

export type Invoice = {
    id: string;
    status: InvoiceStatus;
    amount: number;    
    customer?: {
        id: string;
        name: string;
    };
    course?: {
        id: string;
        name: string;
    };
}

export type RegistrationDTO = {
    customer: CustomerDTO;
    course: CourseDTO;
    rating?: number;
    id: string;
};

export type PersonDTO = {
    id: string;
    name: string;
};

export type CustomerDTO = PersonDTO & {
    sportCenterId: string;
};