import React, { useEffect, useState } from "react";
import CourseTable from "../../components/Course/CourseTable";
import CourseDetail from "./CourseDetail";
import User from "../../services/user";
import httpClient from "../../services/http";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSync } from '@fortawesome/free-solid-svg-icons';
import { CourseDTO } from "../../helpers/types";
import {useNavigate} from "react-router-dom";
import CreateCourseModal from "../../components/Course/CreateCourseModal";

export default function Courses() {
    const [selectedCourseId, setSelectedCourseId] = useState("");
    const [courses, setCourses] = useState<CourseDTO[]>([]);
    const [instructors, setInstructors] = useState([]);
    const [selectedInstructorId, setSelectedInstructorId] = useState("");
    const [courseNameFilter, setCourseNameFilter] = useState("");
    const [selectedCourseState, setSelectedCourseState] = useState("");
    // Add this near your other state declarations
    const [selectedCourseLevel, setSelectedCourseLevel] = useState("");
    const [rooms, setRooms] = useState([]);
    const navigate = useNavigate();

    const handleCourseClick = (courseId: string) => {
        navigate(`/courses/${courseId}`);
    };

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

    const fetchRooms = () => {
        httpClient("/rooms", "GET")
            .then(res => {
                setRooms(res.data); // Assuming the API returns an array of room objects
            })
            .catch(err => {
                console.log("Failed to fetch rooms:", err);
            });
    };

    const courseEdit = (courseId: string) => {
        setSelectedCourseId(courseId);
    }

    useEffect(() => {
        fetchCourses();
        fetchInstructors();
        fetchRooms(); // Fetch rooms on component mount
    }, []);


    const filteredCourses = courses.filter(course => {
        const filterByInstructor = !selectedInstructorId || course.instructor === selectedInstructorId;
        const filterByName = !courseNameFilter || course.name.toLowerCase().includes(courseNameFilter.toLowerCase());
        const filterByLevel = !selectedCourseLevel || course.level === selectedCourseLevel;
        const filterByState = !selectedCourseState || course.courseState === selectedCourseState;
        return filterByInstructor && filterByName && filterByLevel && filterByState;
    });

    const enhancedCourses = filteredCourses.map(course => ({
        ...course,
        room: rooms.find(room => room.id === course.room)?.name || "Room not found"
    }));

    return (
        <div className="p-10">
            <div className="rounded-lg ">
                <div className="sm:flex sm:items-center">
                    <div className="sm:flex-auto">
                        <h1 className="text-xl font-semibold leading-6 text-gray-900">Courses</h1>
                        <p className="mt-2 text-sm text-gray-700">
                            A list of all the courses in your account.
                        </p>
                    </div>
                    {User().personType !== "Customer" && (
                        <div className="mt-4 sm:ml-16 sm:mt-0 sm:flex-none">
                            {/* <button
                                type="button"
                                onClick={() => window.location.href = '/courses/create'}
                                className="inline-flex items-center rounded-md bg-indigo-600 px-4 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-700"
                            >
                                Add new course
                            </button> */}
                            <CreateCourseModal
                                courseId={selectedCourseId}
                                setCourseId={setSelectedCourseId}
                            />    
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
                        value={selectedCourseLevel}
                        onChange={(e) => setSelectedCourseLevel(e.target.value)}
                        className="rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
                    >
                        <option value="">All Levels</option>
                        <option value="Beginner">Beginner</option>
                        <option value="Intermediate">Intermediate</option>
                        <option value="Advanced">Advanced</option>
                    </select>
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
                    {User().personType !== "Customer" && (
                        <select
                        value={selectedCourseState}
                        onChange={(e) => setSelectedCourseState(e.target.value)}
                        className="rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
                    >
                        <option value="">All States</option>
                        <option value="Approved">Approved</option>
                        <option value="Denied">Denied</option>
                        <option value="Inactive">Inactive</option>
                        <option value="Awaiting Approval">Awaiting Approval</option>
                    </select>
                    )}

                    <button
                        onClick={fetchCourses}
                        className="rounded-full p-2 text-blue-500 hover:text-blue-600"
                        title="Reload Courses"
                    >
                        <FontAwesomeIcon icon={faSync} size="lg"/>
                    </button>
                </div>
            </div>
            {
                courses.length === 0 ? (
                    <div className="text-center mt-10">
                        <p className="text-gray-500">No courses found.</p>
                    </div>
                ) : <CourseTable courses={enhancedCourses} setCourses={setCourses} onCourseSelect={handleCourseClick} onCourseEdit={courseEdit} />
            }
            
        </div>
    );
}
