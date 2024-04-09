import React from 'react';
import CourseTable from '../components/CourseTable'; // Make sure the path is correct

const sampleCourses = [
    {
        id: 1,
        name: 'Course 1',
        description: 'Description 1',
        price: 100,
    },
    {
        id: 2,
        name: 'Course 2',
        description: 'Description 2',
        price: 200,
    },
    {
        id: 3,
        name: 'Course 3',
        description: 'Description 3',
        price: 300,
    }
];

export default function HomePage() {
    return (
        <main className="py-10">
            <div className="px-4 sm:px-6 lg:px-8">
                {/* ... other content ... */}
                <CourseTable courses={sampleCourses} />
            </div>
        </main>
    );
}
