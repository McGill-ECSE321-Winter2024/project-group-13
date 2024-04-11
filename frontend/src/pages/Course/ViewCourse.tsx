import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import httpClient from '../../services/http';
import { CourseDTO, Schedule } from '../../helpers/types';
import CourseStatusBadge from '../../components/Course/CourseStatusBadge';
import moment from 'moment';
import User from "../../services/user";
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';

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

    useEffect(() => {
        httpClient("/instructors", "GET")
            .then((res) => {
                setInstructors(res.data);
            })
            .catch((err) => {
                console.log(err);
            });
        if (courseId) {
            console.log("Fetching course data for ID:", courseId);  // Log the ID being requested
            httpClient(`/courses/${courseId}`, 'GET')
                .then((res) => {
                    console.log("Course data fetched:", res.data);  // Log the data received
                    if (res.data) {
                        const courseData = {
                            ...res.data,
                            courseStartDate: new Date(res.data.courseStartDate),
                            courseEndDate: new Date(res.data.courseEndDate)
                        };
                        setCourse(courseData);
                    } else {
                        console.error('Course data is not in the expected format:', res.data);
                        setCourse(null);
                    }
                    // Fetch the schedule as soon as the course details are loaded
                    return httpClient(`/courses/${courseId}/schedule`, 'GET');
                })
                .then((res) => {
                    setSchedule(res.data); // Assuming res.data contains the schedule
                })
                .catch((err) => {
                    console.error('Error fetching course or schedule:', err);
                    setCourse(null);  // Handle errors by setting course to null
                });
        }
    }, [courseId]);

    const [instructors, setInstructors] = useState<{
        id: string,
        name: string,
    }[]>([]);

    const handleChange = (event: React.ChangeEvent<HTMLSelectElement>, field: keyof CourseDTO) => {
        if (course && (isOwner || isInstructor)) {
            setCourse({ ...course, [field]: event.target.value });
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
                <h1 className="text-2xl font-bold text-gray-800">{course.name}</h1>
                <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded transition duration-300 ease-in-out"
                        onClick={() => setViewMode(viewMode === 'table' ? 'calendar' : 'table')}>
                    Switch to {viewMode === 'table' ? 'Calendar' : 'Table'} View
                </button>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                <p><strong>Start Date:</strong> {moment(course.courseStartDate).format('YYYY-MM-DD')}</p>
                <p><strong>End Date:</strong> {moment(course.courseEndDate).format('YYYY-MM-DD')}</p>
                <p><strong>Instructor:</strong> {instructors.find(i => i.id === course.instructor)?.name || "Instructor not found"}</p>
                {isOwner && (
                    <div className="flex align-items-center">
                        <span className="mr-2 font-semibold">Status:</span>
                        <select
                            className="form-select appearance-none block w-full px-3 py-1.5 text-base font-normal text-gray-700 bg-white bg-clip-padding bg-no-repeat border border-solid border-gray-300 rounded transition ease-in-out m-0 focus:text-gray-700 focus:bg-white focus:border-blue-600 focus:outline-none"
                            value={course.courseState}
                            onChange={(e) => handleChange(e, 'courseState')}
                            disabled={!isOwner}
                        >
                            {/* Options here */}
                        </select>
                    </div>
                )}
            </div>
            {viewMode === 'calendar' ? (
                <FullCalendar
                    plugins={[dayGridPlugin]}
                    initialView="dayGridMonth"
                    events={getEvents()}
                    timeZone='local'
                />
            ) : (
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-100">
                    <tr>
                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Date
                        </th>
                        <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Session Time
                        </th>
                    </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                    {getEvents().map((event, index) => (
                        <tr key={index}>
                            <td className="px-6 py-4 whitespace-nowrap">{event.start}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{event.title.replace(course.name + ' ', '')}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    ) : <p>Loading...</p>;  // Improved loading and error handling
}
