import React, { useEffect, useState } from "react";
import CourseTable from "../../components/Course/CourseTable";
import CourseDetail from "./CourseDetail";
import User from "../../services/user";
import httpClient from "../../services/http";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSync } from '@fortawesome/free-solid-svg-icons';
import { CourseDTO } from "../../helpers/types";

export default function Courses() {
    const [selectedCourse, setSelectedCourse] = useState<CourseDTO | null>(null);
    const [courses, setCourses] = useState<CourseDTO[]>([]);
    const [instructors, setInstructors] = useState([]);
    const [selectedInstructorId, setSelectedInstructorId] = useState("");
    const [courseNameFilter, setCourseNameFilter] = useState("");

    const fetchCourses = () => {
        httpClient("/courses", "GET")
            .then((res) => {
                setCourses(res.data);
            })
            .catch((err) => {
                console.log(err);
            });
    };

    const fetchInstructors = () => {
        httpClient("/instructors", "GET")
            .then((res) => {
                setInstructors(res.data);
            })
            .catch((err) => {
                console.log(err);
            });
    };

    useEffect(() => {
        fetchCourses();
        fetchInstructors();
    }, []);

    const filteredCourses = courses.filter(course => {
        return (!selectedInstructorId || course.instructor === selectedInstructorId) &&
            (!courseNameFilter || course.name.toLowerCase().includes(courseNameFilter.toLowerCase()));
    });

    return (
        <div className="p-10">
            <div className="px-4 sm:px-6 lg:px-8 rounded-lg">
                <div className="sm:flex sm:items-center">
                    <div className="sm:flex-auto">
                        <h1 className="text-xl font-semibold leading-6 text-gray-900">Courses</h1>
                        <p className="mt-2 text-sm text-gray-700">
                            A list of all the courses in your account.
                        </p>
                    </div>
                    {User().personType !== "Customer" && (
                        <div className="mt-4 sm:ml-16 sm:mt-0 sm:flex-none">
                            <button
                                type="button"
                                onClick={() => window.location.href = '/courses/create'}
                                className="inline-flex items-center rounded-md bg-indigo-600 px-4 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-700"
                            >
                                Add new course
                            </button>
                        </div>
                    )}
                </div>
            </div>
            <div className="my-4">
                <div className="flex items-center space-x-4">
                    <input
                        type="text"
                        placeholder="Filter by course name..."
                        value={courseNameFilter}
                        onChange={(e) => setCourseNameFilter(e.target.value)}
                        className="flex-grow rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
                    />
                    <select
                        value={selectedInstructorId}
                        onChange={(e) => setSelectedInstructorId(e.target.value)}
                        className="rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
                    >
                        <option value="">All Instructors</option>
                        {instructors.map((instructor) => (
                            <option key={instructor.id} value={instructor.id}>
                                {instructor.name}
                            </option>
                        ))}
                    </select>
                    <button
                        onClick={fetchCourses}
                        className="rounded-full p-2 text-blue-500 hover:text-blue-600"
                        title="Reload Courses"
                    >
                        <FontAwesomeIcon icon={faSync} size="lg" />
                    </button>
                </div>
            </div>
            <CourseTable courses={filteredCourses} onCourseSelect={setSelectedCourse} />
            {selectedCourse && (
                <CourseDetail course={selectedCourse} onClose={() => setSelectedCourse(null)} />
            )}
        </div>
    );
}
