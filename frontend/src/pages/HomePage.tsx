import React from 'react';
import CourseTable from '../components/CourseTable'; // Make sure the path is correct

const sampleCourses = [
    // ... Array of course objects with id, name, description, level, start date, end date, room, instructor, and cost
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
