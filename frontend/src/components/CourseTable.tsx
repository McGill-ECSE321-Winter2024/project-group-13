import React, { useState, useMemo } from 'react';

const CourseTable = ({ courses }) => {
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
            <div className="overflow-x-auto">
                <table className="min-w-full bg-white">
                    <thead>
                    <tr className="bg-gray-800 text-white">
                        <th onClick={() => handleSort('name')} className="cursor-pointer px-6 py-3 text-left text-xs font-medium uppercase tracking-wider">
                            Name
                        </th>
                        <th onClick={() => handleSort('description')} className="cursor-pointer px-6 py-3 text-left text-xs font-medium uppercase tracking-wider">
                            Description
                        </th>
                        <th onClick={() => handleSort('level')} className="cursor-pointer px-6 py-3 text-left text-xs font-medium uppercase tracking-wider">
                            Level
                        </th>
                        <th onClick={() => handleSort('startDate')} className="cursor-pointer px-6 py-3 text-left text-xs font-medium uppercase tracking-wider">
                            Start Date
                        </th>
                        <th onClick={() => handleSort('endDate')} className="cursor-pointer px-6 py-3 text-left text-xs font-medium uppercase tracking-wider">
                            End Date
                        </th>
                        <th onClick={() => handleSort('room')} className="cursor-pointer px-6 py-3 text-left text-xs font-medium uppercase tracking-wider">
                            Room
                        </th>
                        <th onClick={() => handleSort('instructor')} className="cursor-pointer px-6 py-3 text-left text-xs font-medium uppercase tracking-wider">
                            Instructor
                        </th>
                        <th onClick={() => handleSort('cost')} className="cursor-pointer px-6 py-3 text-left text-xs font-medium uppercase tracking-wider">
                            Cost
                        </th>
                    </tr>
                    </thead>
                    <tbody className="text-gray-700">
                    {filteredAndSortedCourses.map((course, index) => (
                        <tr key={index}>
                            <td className="px-6 py-4 whitespace-nowrap">{course.name}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{course.description}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{course.level}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{course.startDate}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{course.endDate}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{course.room}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{course.instructor}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{course.cost}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default CourseTable;
