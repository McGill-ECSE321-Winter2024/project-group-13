import React, { useEffect, useState } from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import httpClient from '../../services/http';
import User from '../../services/user';
import { CourseDTO, Schedule, RegistrationDTO } from '../../helpers/types';
import moment from "moment";
import {set} from "lodash"; // Assume these are your type definitions

export default function UserCalendar() {
    const [courses, setCourses] = useState<CourseDTO[]>([]);
    const [events, setEvents] = useState([]);
    const user = User(); // Assume this retrieves the current user's info

    function hashCode(str) {
        var hash = 0;
        for (var i = 0; i < str.length; i++) {
            var char = str.charCodeAt(i);
            hash = ((hash << 5) - hash) + char;
            hash = hash & hash; // Convert to 32bit integer
        }
        return hash;
    }

    function intToRGB(i){
        var c = (i & 0x00FFFFFF).toString(16).toUpperCase();
        return "00000".substring(0, 6 - c.length) + c;
    }

    useEffect(() => {
        async function fetchCoursesAndSchedules() {
            try {
                let fetchedCourses;
                if (user.personType === 'Customer') {
                    const registrationsResponse = await httpClient('/registrations', 'GET');
                    const registrations = registrationsResponse.data as RegistrationDTO[];
                    console.log('Registrations Data:', registrations);
                    fetchedCourses = registrations.filter(reg => reg.course.courseState === 'Approved').map(registration => registration.course);
                    console.log('Courses Data:', fetchedCourses);
                } else {
                    const coursesResponse = await httpClient('/courses', 'GET');
                    fetchedCourses = coursesResponse.data as CourseDTO[];
                }
                setCourses(fetchedCourses);
            } catch (error) {
                console.error('Error fetching courses:', error);
            }
        }

        fetchCoursesAndSchedules();
    }, [user.personType]);

    useEffect(() => {
        async function fetchSchedules() {
            try {
                const schedulesPromises = courses.map(course =>
                    httpClient(`/courses/${course.id}/schedule`, 'GET')
                );
                const schedulesResponses = await Promise.all(schedulesPromises);
                const newEvents = schedulesResponses.flatMap((response, index) => {
                    const schedule = response.data as Schedule;
                    const course = courses[index];
                    const color = '#' + intToRGB(hashCode(course.name));
                    return generateEventsForCourse(course, schedule, color);
                });
                setEvents(newEvents);
            } catch (error) {
                console.error('Error fetching schedules:', error);
            }
        }

        if (courses.length > 0) {
            fetchSchedules();
        }
    }, [courses]);

    function generateEventsForCourse(course, schedule, color) {
        let events = [];
        for (const day in schedule) {
            if (schedule[day]) {
                const sessionStart = schedule[day].start;
                const sessionEnd = schedule[day].end;
                const dayOfWeek = day.toLowerCase(); // e.g., "monday"
                let currentDate = moment(course.courseStartDate);
                while (currentDate.isSameOrBefore(course.courseEndDate)) {
                    if (currentDate.format('dddd').toLowerCase() === dayOfWeek) {
                        events.push({
                            title: course.name,
                            start: currentDate.format('YYYY-MM-DD') + 'T' + sessionStart,
                            end: currentDate.format('YYYY-MM-DD') + 'T' + sessionEnd,
                            allDay: false,
                            color: color
                        });
                    }
                    currentDate.add(1, 'days');
                }
            }
        }
        return events;
    }

    return (
        <div className="bg-white p-5 rounded shadow">
            <h2 className="text-base font-semibold leading-7 " style={{
                fontSize: 20,
                marginBottom: 20
            }}>
                {(user.personType === 'Customer' || user.personType === 'Instructor') ? 'My Courses' : 'Course Schedule'}</h2>
            <FullCalendar
                plugins={[dayGridPlugin]}
                initialView="dayGridMonth"
                events={events}
                timeZone="local"
                headerToolbar={{
                    left: "prev,next today",
                    center: "title",
                    right: "dayGridMonth,dayGridWeek",
                }}
                buttonText={{
                    today: "Today",
                    month: "Month",
                    week: "Week",
                }}
                eventColor="#007bff"
            />
        </div>
    );
}




