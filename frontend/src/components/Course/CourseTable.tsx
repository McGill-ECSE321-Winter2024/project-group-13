// CourseTable.tsx
import React, { useState, useMemo, useEffect } from 'react';
import { CourseDTO } from '../../helpers/types';
import moment from 'moment';
import httpClient from '../../services/http';
import User from '../../services/user';
import ApproveCourseModal from './ApproveCourseModal';
import { CourseState } from '../../helpers/enums';
import CourseStatusBadge from './CourseStatusBadge';
import RegisterToCourseModal from './RegisterToCourseModal';
import { CheckBadgeIcon } from '@heroicons/react/24/outline';

const CourseTable = ({ courses, onCourseSelect }: {courses: CourseDTO[], onCourseSelect: any}) => {
    const [filterText, setFilterText] = useState('');
    const [sortField, setSortField] = useState(null);
    const [sortOrder, setSortOrder] = useState('asc');
    const [instructors, setInstructors] = useState([]); 
    const [registrations, setRegistrations] = useState<{
        id: number;
        rating: number;
        course: {
            id: string
        }
    }[]>([]);

    useEffect(() => {
        httpClient("/instructors", "GET")
        .then((res) => {
          setInstructors(res.data);
        })
        .catch((err) => {
          console.log(err);
        });
        if(User().personType === 'Customer') {
            httpClient("/registrations", "GET")
            .then((res) => {
              setRegistrations(res.data);
            })
            .catch((err) => {
              console.log(err);
            });
        }
    }, []);

    const handleSort = (field) => {
        if (field === sortField) {
            setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc');
        } else {
            setSortField(field);
            setSortOrder('asc');
        }
    };

    const approveCourse = (courseId) => {
        // approve course
        httpClient('/courses/' + courseId + '/approve', 'POST')
        .then((res) => {
            window.location.reload();
        })
        .catch((err) => {
            console.log(err);
        });
    }
    

    const sortedCourses = useMemo(() => {
        let sortedData = [...courses];
        if (sortField !== null) {
            sortedData.sort((a, b) => {
                if (a[sortField] < b[sortField]) {
                    return sortOrder === 'asc' ? -1 : 1;
                }
                if (a[sortField] > b[sortField]) {
                    return sortOrder === 'asc' ? 1 : -1;
                }
                return 0;
            });
        }
        return sortedData;
    }, [courses, sortField, sortOrder]);

    const filteredAndSortedCourses = useMemo(() => {
        return sortedCourses.filter((course) => {
            return Object.keys(course).some((key) =>
                course[key]?.toString().toLowerCase().includes(filterText.toLowerCase())
            );
        });
    }, [sortedCourses, filterText]);

    const SortIcon = ({ isAsc }) => (
        <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 20 20"
            fill="currentColor"
            className="inline w-4 h-4 ml-2"
        >
            {isAsc ? (
                <path fillRule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clipRule="evenodd" />
            ) : (
                <path fillRule="evenodd" d="M14.707 12.707a1 1 0 01-1.414 0L10 9.414 6.707 12.707a1 1 0 11-1.414-1.414l4-4a1 1 0 011.414 0l4 4a1 1 0 010 1.414z" clipRule="evenodd" />
            )}
        </svg>
    );

    return (
        <div>
            <div className="mb-4">
                <input
                    type="text"
                    placeholder="Filter courses..."
                    value={filterText}
                    onChange={(e) => setFilterText(e.target.value)}
                    className="px-4 py-2 border rounded-lg w-full"
                />
            </div>
            <div className="overflow-x-auto shadow-md">
                <table className="min-w-full bg-white w-auto">
                    <thead className="bg-gray-800 text-white">
                    <tr >
                        {['name', 'description', 'level', 'startDate', 'endDate', 'room', 'instructor', 'cost', 'state'].map((field) => (
                            <th
                                key={field}
                                onClick={() => handleSort(field)}
                                className={`cursor-pointer px-6 py-3 text-left text-xs font-medium uppercase tracking-wider hover:bg-gray-700 ${sortField === field ? 'bg-gray-600' : ''}`}
                            >
                                {field.charAt(0).toUpperCase() + field.slice(1)}
                                {sortField === field && (
                                    <SortIcon isAsc={sortOrder === 'asc'}/>
                                )}
                            </th>
                        ))}
                    </tr>
                    </thead>
                    <tbody className="text-gray-700 gap-10">
                    {filteredAndSortedCourses.map((course, index) => (
                        <tr key={course.id}  className="hover:bg-gray-100 cursor-pointer" style={{
                            padding: '10px'
                        }}>
                            <td onClick={() => onCourseSelect(course)}><b>{course.name}</b></td>
                            <td>{course.description}</td>
                            <td>{course.level}</td>
                            <td>{moment(course.courseStartDate).format('MMM DD, YYYY')}</td>
                            <td>{moment(course.courseEndDate).format('MMM DD, YYYY')}</td>
                            <td>{course.room}</td>
                            <td>{instructors.find(i => i.id === course.instructor)?.name}</td>
                            <td>{course.hourlyRateAmount}</td>
                            <td>
                                {User().personType !== "Customer" ? <CourseStatusBadge status={course.courseState} /> : null}
                                {User().personType === 'Owner' && course.courseState !== CourseState.Approved ?
                                    <ApproveCourseModal
                                        approveCourse={() => {
                                            approveCourse(course.id);
                                        }}
                                    /> : null    
                                 }
                                 {User().personType === 'Customer' && !registrations.find(r => r.course.id === course.id) ?
                                    <RegisterToCourseModal
                                        courseId={course.id}
                                        costPerHour={course.hourlyRateAmount}
                                    /> : null   
                                }
                                {User().personType === 'Customer' && registrations.find(r => r.course.id === course.id) ?
                                    <div
                                        className='flex items-center gap-1 text-green-900 text-sm font-semibold'
                                    >
                                        <CheckBadgeIcon className="h-6 w-6 text-green-500" /> 
                                        Registered
                                    </div>: null
                                }
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default CourseTable;