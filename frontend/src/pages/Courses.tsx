import React, { useState, useEffect } from 'react';
import CourseTable from '../components/CourseTable';
import CourseDetail from './CourseDetail';
import AddCourseModal from './AddCourseModal';
import User from '../services/user';
import httpClient from '../services/http';

export default function Courses() {
    const [courses, setCourses] = useState([]);
    const [selectedCourse, setSelectedCourse] = useState(null);
    const [showAddModal, setShowAddModal] = useState(false);
    const userType = User().personType;

    useEffect(() => {
        const fetchCourses = async () => {
            try {
                const response = await httpClient('/courses');
                setCourses(response.data);
            } catch (error) {
                console.error('Failed to fetch courses:', error);
                // Handle errors as appropriate
            }
        };

        fetchCourses();
    }, []);  // Empty dependency array means this effect runs once on mount

    const handleCourseSelect = (course) => {
        setSelectedCourse(course);
    };

    const closeDetailModal = () => {
        setSelectedCourse(null);
    };

    const toggleAddModal = () => {
        setShowAddModal(!showAddModal);
    };

    return (
        <main className="py-10">
            <div className="px-4 sm:px-6 lg:px-8">
                {userType === 'Instructor' || userType === 'Owner' ? (
                    <button onClick={toggleAddModal} className="mb-4 px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600">
                        Add Course
                    </button>
                ) : null}
                <CourseTable courses={courses} onCourseSelect={handleCourseSelect} />
                {selectedCourse && <CourseDetail course={selectedCourse} onClose={closeDetailModal} />}
                {showAddModal && <AddCourseModal onClose={toggleAddModal} />}
            </div>
        </main>
    );
}
