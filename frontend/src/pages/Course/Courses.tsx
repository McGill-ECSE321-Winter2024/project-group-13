import React, { useEffect, useState } from "react";
import CourseTable from "../../components/Course/CourseTable";
import CourseDetail from "./CourseDetail";
import User from "../../services/user";
import httpClient from "../../services/http";
import { CourseState } from "../../helpers/enums";
import { CourseDTO } from "../../helpers/types";

export default function Courses() {
  const [selectedCourse, setSelectedCourse] = useState(null);

  const [createCourseModalOpen, setCreateCourseModalOpen] = useState(false);
  const [courses, setCourses] = useState<CourseDTO[]>([]); // [1]
  const [instructors, setInstructors] = useState([]); // [2]

  const handleCourseSelect = (course) => {
    setSelectedCourse(course);
  };

  const closeDetailModal = () => {
    setSelectedCourse(null);
  };

  useEffect(() => {
   
    httpClient("/courses", "GET")
      .then((res) => {
        setCourses(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

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
          {User().personType !== "Customer" ? (
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
          courses={courses}
          onCourseSelect={handleCourseSelect}
        />
        {selectedCourse && (
          <CourseDetail course={selectedCourse} onClose={closeDetailModal} />
        )}
      </div>
    </div>
  );
}
