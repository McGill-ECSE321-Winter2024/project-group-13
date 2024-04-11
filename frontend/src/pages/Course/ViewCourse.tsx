import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import httpClient from '../../services/http';
import { CourseDTO, Schedule } from '../../helpers/types';
import CourseStatusBadge from '../../components/Course/CourseStatusBadge';
import moment from 'moment';
import User from "../../services/user";
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import './FullCalendarStyles.css';
import {CourseState} from "../../helpers/enums"; // Adjust the path as necessary

interface ViewCourseProps {
    courseId: string;  // Adjust if needed based on how you're passing props
}

export default function ViewCourse() {
    const { courseId } = useParams<{ courseId: string }>();
    const [course, setCourse] = useState<CourseDTO | null>(null);
    const [schedule, setSchedule] = useState<Schedule | null>(null);
    const [viewMode, setViewMode] = useState('table'); // 'table' or 'calendar'
    const isOwner = User().personType === 'Owner';
    const isInstructor = User().personType === 'Instructor';
    const [dropdownVisible, setDropdownVisible] = useState(false);
    const [editableCourse, setEditableCourse] = useState<{ name: string; description: string }>({ name: '', description: '' });
    const [selectedInstructor, setSelectedInstructor] = useState('');
    const [courseState, setCourseState] = useState<CourseState>(CourseState.AwaitingApproval); // default state or fetched from the course


    useEffect(() => {
        const fetchInstructors = async () => {
            try {
                const res = await httpClient("/instructors", "GET");
                setInstructors(res.data);
            } catch (err) {
                console.log(err);
            }
        };

        const fetchCourseDetails = async () => {
            try {
                console.log("Fetching course data for ID:", courseId);
                const res = await httpClient(`/courses/${courseId}`, 'GET');
                console.log("Course data fetched:", res.data);
                if (res.data) {
                    const courseData = {
                        ...res.data,
                        courseStartDate: new Date(res.data.courseStartDate),
                        courseEndDate: new Date(res.data.courseEndDate)
                    };
                    setCourse(courseData);
                    // Set the state for the editable fields
                    setEditableCourse({ name: courseData.name, description: courseData.description });
                    setCourseState(courseData.courseState); // Assuming courseState is in the fetched data
                    setSelectedInstructor(courseData.instructor); // Assuming instructor ID is directly in the fetched data
                    // Fetch the schedule as soon as the course details are loaded
                    const scheduleRes = await httpClient(`/courses/${courseId}/schedule`, 'GET');
                    setSchedule(scheduleRes.data); // Assuming res.data contains the schedule
                } else {
                    console.error('Course data is not in the expected format:', res.data);
                    setCourse(null);
                }
            } catch (err) {
                console.error('Error fetching course details or schedule:', err);
                setCourse(null); // Handle errors by setting course to null
            }
        };

        fetchInstructors();

        if (courseId) {
            fetchCourseDetails();
        }

    }, [courseId]); // Only re-run the effect if courseId changes


    const [instructors, setInstructors] = useState<{
        id: string,
        name: string,
    }[]>([]);

    const handleInstructorChange = (event) => {
        setSelectedInstructor(event.target.value);
    };

    const handleEdit = (field: keyof CourseDTO, value: string) => {
        setEditableCourse(prev => ({ ...prev, [field]: value }));
    };

    const handleSave = async () => {

        try {
            // Start with a check to see if the course object exists
            if (!course) {
                throw new Error("No course data to save.");
            }

            // Update course details. Depending on your backend,
            // you could combine these into a single request.
            await httpClient(`/courses/${courseId}/name`, 'PUT', { name: editableCourse.name });
            await httpClient(`/courses/${courseId}/description`, 'PUT', { description: editableCourse.description });
            if (isOwner) {
                await httpClient(`/courses/${courseId}/instructor`, 'PUT', { instructor: selectedInstructor });
                await httpClient(`/courses/${courseId}/state`, 'PUT', { state: course.courseState });
            }

            // Additional updates go here
            // ...

            alert("Course updated successfully");
        } catch (error) {
            console.error("Failed to update course:", error);
            alert("Failed to update course: " + error.message);
        }
    };


    const handleChange = (newState: string) => {
        if (course && (isOwner || isInstructor) && Object.values(CourseState).includes(newState as CourseState)) {
            setCourse({
                ...course,
                courseState: newState as CourseState
            });
        } else {
            console.error("Invalid course state: " + newState);
            // Handle the error state appropriately
        }
    };


    const getEvents = () => {
        if (!course || !schedule) return [];
        let events = [];
        const startDate = moment(course.courseStartDate);
        const endDate = moment(course.courseEndDate);

        while (startDate.isBefore(endDate) || startDate.isSame(endDate, 'day')) {
            const dayOfWeek = startDate.format('dddd').toLowerCase(); // 'monday', 'tuesday', etc.
            const startKey = `${dayOfWeek}Start` as keyof Schedule;
            const endKey = `${dayOfWeek}End` as keyof Schedule;
            const sessionStart = schedule[startKey];
            const sessionEnd = schedule[endKey];

            if (sessionStart && sessionEnd) {
                events.push({
                    title: `${course.name} Session`,
                    start: startDate.format('YYYY-MM-DD') + 'T' + sessionStart,
                    end: startDate.format('YYYY-MM-DD') + 'T' + sessionEnd,
                    allDay: false
                });
            }
            startDate.add(1, 'days');
        }
        return events;
    };

    return course ? (
        <div className="bg-white p-5 rounded shadow">
            <div className="flex justify-between items-center mb-4">
                {isOwner ? (
                    <input
                        type="text"
                        value={editableCourse.name}
                        onChange={(e) => handleEdit('name', e.target.value)}
                        className="text-2xl font-bold text-gray-800 bg-transparent border-b-2 border-gray-300 focus:outline-none"
                    />
                ) : (
                    <h1 className="text-2xl font-bold text-gray-800">{course.name}</h1>
                )}
                <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded transition duration-300 ease-in-out"
                        onClick={() => setViewMode(viewMode === 'table' ? 'calendar' : 'table')}>
                    Switch to {viewMode === 'table' ? 'Calendar' : 'Table'} View
                </button>
            </div>
            {(isInstructor || isOwner) && (
                <textarea
                    value={editableCourse.description}
                    onChange={(e) => handleEdit('description', e.target.value)}
                    className="w-full mb-4 p-2 border rounded"
                />
            )}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                <div className="mb-4">
                    <p><strong>Start Date:</strong> {moment(course.courseStartDate).format('YYYY-MM-DD')}</p>
                    <p><strong>End Date:</strong> {moment(course.courseEndDate).format('YYYY-MM-DD')}</p>
                    <div className="sm:col-span-3">
                        <strong>Instructor:</strong>
                        <div className="mt-2">
                            <select
                                id="instructor"
                                disabled={User().personType !== "Owner"}
                                required
                                name="instructors"
                                value={selectedInstructor}
                                onChange={handleInstructorChange}
                                autoComplete="country-name"
                                className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:max-w-xs sm:text-sm sm:leading-6"
                            >
                                <option
                                    value="">{instructors.find(i => i.id === course.instructor)?.name || "Select an instructor"}</option>
                                {instructors.map(instructor => (
                                    <option key={instructor.id} value={instructor.id}>{instructor.name}</option>
                                ))}
                            </select>
                        </div>
                    </div>
                    <div className="flex align-items-center relative">
                        <span className="mr-2 font-semibold">Status:</span>
                        {isOwner ? (
                            // Allow owners to interact and change the course state
                            <div onBlur={() => setDropdownVisible(false)} tabIndex={0}>
                                <CourseStatusBadge status={course.courseState} onClick={(e) => {
                                    e.stopPropagation();
                                    setDropdownVisible(!dropdownVisible);
                                }}/>
                                {dropdownVisible && (
                                    <div className="absolute z-10 bg-white shadow-lg rounded p-2 mt-1">
                                        {Object.values(CourseState).map((state) => (
                                            <div key={state} className="p-1 hover:bg-gray-200 cursor-pointer"
                                                 onClick={(e) => {
                                                     e.stopPropagation();
                                                     handleChange(state as CourseState);  // Directly pass the new state
                                                     setDropdownVisible(false);
                                                 }}>
                                                {state}
                                            </div>
                                        ))}
                                    </div>
                                )}
                            </div>
                        ) : (
                            // For instructors, just display the status badge without interactivity
                            <div>
                                <CourseStatusBadge status={course.courseState}/>
                            </div>
                        )}
                    </div>

                </div>
                {(isInstructor || isOwner) && (
                    <button className="mt-4 bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
                            onClick={handleSave}>
                        Save
                    </button>
                )}

            </div>
            {viewMode === 'calendar' ? (
                <FullCalendar
                    plugins={[dayGridPlugin]}
                    initialView="dayGridMonth"
                    events={getEvents()}
                    timeZone='local'
                    headerToolbar={{
                        left: 'prev,next today',
                        center: 'title',
                        right: 'dayGridMonth,dayGridWeek,dayGridDay'
                    }}
                    buttonText={{
                        today: 'Today',
                        month: 'Month',
                        week: 'Week',
                        day: 'Day'
                    }}
                    eventColor='#007bff'
                    navLinks={true} // can click day/week names to navigate views
                    businessHours={true} // display business hours
                    editable={true}
                    selectable={true}
                    droppable={true} // allows things to be dropped onto the calendar
                />

            ) : (
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-100">
                    <tr>
                        <th
                            scope="col"
                            className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                        >
                            Date
                        </th>
                        <th
                            scope="col"
                            className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                        >
                            Session Time
                        </th>
                    </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                    {getEvents().map((event, index) => {
                        // Extract and format date and time
                        const [date, time] = event.start.split('T');
                        const formattedDate = moment(date).format('YYYY-MM-DD');
                        const formattedTime = moment(time, 'HH:mm:ss').format('hh:mm A'); // or 'HH:mm' for 24-hour time

                        return (
                            <tr key={index}>
                                <td className="px-6 py-4 whitespace-nowrap">{formattedDate}</td>
                                <td className="px-6 py-4 whitespace-nowrap">{formattedTime}</td>
                            </tr>
                        );
                    })}
                    </tbody>
                </table>

            )}
        </div>
    ) : <p>Loading...</p>;  // Improved loading and error handling
}
