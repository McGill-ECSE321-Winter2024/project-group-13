import React, { useState } from "react";
import CourseTable from "../../components/Course/CourseTable";
import CourseDetail from "./CourseDetail";
import User from "../../services/user";

const sampleCourses = [
  {
    id: 1,
    name: "Introduction to Psychology",
    description: "An introductory course into the world of Psychology.",
    level: "Beginner",
    startDate: "2023-01-10",
    endDate: "2023-04-10",
    room: "101",
    instructor: "Dr. Jane Smith",
    cost: 300,
  },
  {
    id: 2,
    name: "Advanced Neuroscience",
    description: "Explore the complexities of the human brain.",
    level: "Advanced",
    startDate: "2023-02-15",
    endDate: "2023-06-15",
    room: "201",
    instructor: "Dr. John Doe",
    cost: 500,
  },
  {
    id: 3,
    name: "Statistics for Biologists",
    description: "Statistical methods applicable to biological studies.",
    level: "Intermediate",
    startDate: "2023-03-01",
    endDate: "2023-07-01",
    room: "105",
    instructor: "Dr. Emily White",
    cost: 450,
  },
];

export default function Courses() {
  const [selectedCourse, setSelectedCourse] = useState(null);

  const [createCourseModalOpen, setCreateCourseModalOpen] = useState(false);

  const handleCourseSelect = (course) => {
    setSelectedCourse(course);
  };

  const closeDetailModal = () => {
    setSelectedCourse(null);
  };

  return (
    <div className="p-10">
      <div className="px-4 sm:px-6 lg:px-8  rounded-lg">
        <div className="sm:flex sm:items-center">
          <div className="sm:flex-auto">
            <h1 className="text-base font-semibold leading-6 text-gray-900">
              Users
            </h1>
            <p className="mt-2 text-sm text-gray-700">
              A list of all the users in your account including their name,
              title, email and role.
            </p>
          </div>
          {User().personType === "Owner" ? (
            <div className="mt-4 sm:ml-16 sm:mt-0 sm:flex-none">
              <button
                type="button"
                onClick={() => window.location.href = '/courses/create'}
                className="block rounded-md bg-indigo-600 px-3 py-2 text-center text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
              >
                Add new course
              </button>
            </div>
          ) : null}
        </div>
      </div>
      <div className="px-4 sm:px-6 lg:px-8 mt-4 rounded-lg">
        <CourseTable
          courses={sampleCourses}
          onCourseSelect={handleCourseSelect}
        />
        {selectedCourse && (
          <CourseDetail course={selectedCourse} onClose={closeDetailModal} />
        )}
      </div>
    </div>
  );
}
