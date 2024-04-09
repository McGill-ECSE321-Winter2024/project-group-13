import React, { useState } from 'react';
import CourseTable from '../components/CourseTable';
import CourseDetail from './CourseDetail';

const sampleCourses = [
    {
        id: 1,
        name: "Introduction to Psychology",
        description: "An introductory course into the world of Psychology.",
        level: "Beginner",
        startDate: "2023-01-10",
        endDate: "2023-04-10",
        room: "101",
        instructor: "Dr. Jane Smith",
        cost: 300
    },
    {
        id: 2,
        name: "Advanced Neuroscience",
        description: "Explore the complexities of the human brain.",
        level: "Advanced",
        startDate: "2023-02-15",
        endDate: "2023-06-15",
        room: "201",
        instructor: "Dr. John Doe",
        cost: 500
    },
    {
        id: 3,
        name: "Statistics for Biologists",
        description: "Statistical methods applicable to biological studies.",
        level: "Intermediate",
        startDate: "2023-03-01",
        endDate: "2023-07-01",
        room: "105",
        instructor: "Dr. Emily White",
        cost: 450
    }
];


export default function Courses() {
    const [selectedCourse, setSelectedCourse] = useState(null);

    const handleCourseSelect = (course) => {
        setSelectedCourse(course);
    };

    const closeDetailModal = () => {
        setSelectedCourse(null);
    };

    return (
        <main className="py-10">
            <div className="px-4 sm:px-6 lg:px-8">
                <CourseTable courses={sampleCourses} onCourseSelect={handleCourseSelect} />
                {selectedCourse && <CourseDetail course={selectedCourse} onClose={closeDetailModal} />}
            </div>
        </main>
    );
}
