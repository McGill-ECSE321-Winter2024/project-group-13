// CourseTable.tsx
import React, { useState, useMemo } from 'react';

const CourseTable = ({ courses, onCourseSelect }) => {
    const [filterText, setFilterText] = useState('');
    const [sortField, setSortField] = useState(null);
    const [sortOrder, setSortOrder] = useState('asc');

    const handleSort = (field) => {
        if (field === sortField) {
            setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc');
        } else {
            setSortField(field);
            setSortOrder('asc');
        }
    };

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
                course[key].toString().toLowerCase().includes(filterText.toLowerCase())
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
                        {['name', 'description', 'level', 'startDate', 'endDate', 'room', 'instructor', 'cost'].map((field) => (
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
                    <tbody className="text-gray-700">
                    {filteredAndSortedCourses.map((course, index) => (
                        <tr key={course.id} onClick={() => onCourseSelect(course)} className="hover:bg-gray-100 cursor-pointer">
                            <td>{course.name}</td>
                            <td>{course.description}</td>
                            <td>{course.level}</td>
                            <td>{course.startDate}</td>
                            <td>{course.endDate}</td>
                            <td>{course.room}</td>
                            <td>{course.instructor}</td>
                            <td>{course.cost}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default CourseTable;