// CourseDetail.tsx

import React from 'react';

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

const CourseDetail: React.FC<CourseDetailProps> = ({ course, onClose }) => {
    if (!course) return null;

    return (
        <div className="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center p-4">
            <div className="bg-white p-5 rounded-lg shadow-lg max-w-lg w-full">
                <h2 className="text-xl font-bold">{course.name}</h2>
                <ul>
                    <li>Description: {course.description}</li>
                    <li>Level: {course.level}</li>
                    <li>Start Date: {course.startDate}</li>
                    <li>End Date: {course.endDate}</li>
                    <li>Room: {course.room}</li>
                    <li>Instructor: {course.instructor}</li>
                    <li>Cost: ${course.cost}</li>
                </ul>
                <button onClick={onClose} className="mt-4 px-4 py-2 bg-red-500 text-white rounded hover:bg-red-700">Close</button>
            </div>
        </div>
    );
};

export default CourseDetail;
