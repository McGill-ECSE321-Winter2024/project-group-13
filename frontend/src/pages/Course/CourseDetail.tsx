// CourseDetail.tsx

import React, { useEffect, useState } from 'react';
import { CourseDTO } from '../../helpers/types';
import moment from 'moment';
import httpClient from '../../services/http';

interface CourseDetailProps {
    course: {
        id: number;
        name: string;
        description: string;
        level: string;
        startDate: string;
        endDate: string;
        room: string;
        instructor: string;
        cost: number;
    };
    onClose: () => void;
}
/**
     * This constant is used to display details of a course on the course page of our app.
     */
const CourseDetail = ({ course, onClose }: {course: CourseDTO, onClose: any}) => {
    

    const [instructors, setInstructors] = useState([]);
    
    useEffect(() => {
        httpClient("/instructors", "GET")
        .then((res) => {
          setInstructors(res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    }, []);

    return course  ? (
        <div className="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center p-4">
            <div className="bg-white p-5 rounded-lg shadow-lg max-w-lg w-full">
                <h2 className="text-xl font-bold">{course.name}</h2>
                <ul>
                    <li>Description: {course.description}</li>
                    <li>Level: {course.level}</li>
                    <li>Start Date: {moment(course.courseStartDate).format('MMMM Do YYYY')}</li> 
                    <li>End Date: {moment(course.courseEndDate).format('MMMM Do YYYY')}</li> 
                    <li>Room: {course.room}</li>
                    <li>Instructor: {instructors.find((instructor) => instructor.id === course.instructor)?.name}</li>
                    <li>Cost: ${course.hourlyRateAmount}</li>
                </ul>
                <button onClick={onClose} className="mt-4 px-4 py-2 bg-red-500 text-white rounded hover:bg-red-700">Close</button>
            </div>
        </div>
    ) : null;
};

export default CourseDetail;
