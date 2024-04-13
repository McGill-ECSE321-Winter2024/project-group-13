import React, { useEffect, useState } from "react";
import CourseTable from "../../components/Course/CourseTable";
import CourseDetail from "./CourseDetail";
import User from "../../services/user";
import httpClient from "../../services/http";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faSync } from "@fortawesome/free-solid-svg-icons";
import { CourseDTO } from "../../helpers/types";
import { useNavigate } from "react-router-dom";
import CreateCourseModal from "../../components/Course/CreateCourseModal";
import CsvDownloadButton from "react-json-to-csv";
import { ArrowDownTrayIcon } from "@heroicons/react/24/outline";

export default function Courses() {
  const [selectedCourseId, setSelectedCourseId] = useState("");
  const [courses, setCourses] = useState<CourseDTO[]>([]);
  const [instructors, setInstructors] = useState([]);
  const [selectedInstructorId, setSelectedInstructorId] = useState("");
  const [courseNameFilter, setCourseNameFilter] = useState("");
  const [selectedCourseState, setSelectedCourseState] = useState("");
  // Add this near your other state declarations
  const [selectedCourseLevel, setSelectedCourseLevel] = useState("");
  const [rooms, setRooms] = useState([]);
  const navigate = useNavigate();

  const handleCourseClick = (courseId: string) => {
    navigate(`/courses/${courseId}`);
  };

      /**
     * Fetches a list of courses from the server.
     * Uses the httpClient to make a GET request to the "/courses" endpoint.
     * If the request is successful, sets the fetched courses using the setCourses function.
     * If there is an error, logs the error to the console.
     */
  const fetchCourses = () => {
    httpClient("/courses", "GET")
      .then((res) => {
        setCourses(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  /**
 * Fetches a list of instructors from the server.
 * Uses the httpClient to make a GET request to the "/instructors" endpoint.
 * If the request is successful, sets the fetched instructors using the setInstructors function.
 * If there is an error, logs the error to the console.
 */
  const fetchInstructors = () => {
    httpClient("/instructors", "GET")
      .then((res) => {
        setInstructors(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  /**
 * Fetches a list of rooms from the server.
 * Uses the httpClient to make a GET request to the "/rooms" endpoint.
 * If the request is successful, sets the fetched rooms using the setRooms function.
 * If there is an error, logs the error to the console.
 */
  const fetchRooms = () => {
    httpClient("/rooms", "GET")
      .then((res) => {
        setRooms(res.data); // Assuming the API returns an array of room objects
      })
      .catch((err) => {
        console.log("Failed to fetch rooms:", err);
      });
  };

  /**
 * Sets the selected course ID for editing.
 * Clears the previously selected course ID, waits for the modal to close,
 * and then sets the new course ID as the selected one for editing.
 *
 * @param courseId The ID of the course to be edited.
 */
  const courseEdit = async (courseId: string) => {
    setSelectedCourseId(null);
    await new Promise((resolve) => setTimeout(resolve, 100)); // Wait for the modal to close
    setSelectedCourseId(courseId);
  };

  /**
 * Runs once when the component mounts.
 * Fetches a list of courses, instructors, and rooms from the server using the corresponding fetch methods.
 */
  useEffect(() => {
    fetchCourses();
    fetchInstructors();
    fetchRooms(); // Fetch rooms on component mount
  }, []);

  /**
 * Filters the list of courses based on selected filters.
 * Filters the courses by instructor, name, level, and state.
 * Returns a new array of courses that match the selected filters.
 *
 * @param courses The array of courses to filter.
 * @param selectedInstructorId The ID of the selected instructor to filter by.
 * @param courseNameFilter The name filter to apply.
 * @param selectedCourseLevel The selected course level to filter by.
 * @param selectedCourseState The selected course state to filter by.
 * @returns A new array of courses that match the selected filters.
 */
  const filteredCourses = courses.filter((course) => {
    const filterByInstructor =
      !selectedInstructorId || course.instructor === selectedInstructorId;
    const filterByName =
      !courseNameFilter ||
      course.name.toLowerCase().includes(courseNameFilter.toLowerCase());
    const filterByLevel =
      !selectedCourseLevel || course.level === selectedCourseLevel;
    const filterByState =
      !selectedCourseState || course.courseState === selectedCourseState;
    return filterByInstructor && filterByName && filterByLevel && filterByState;
  });

  /**
 * Enhances the filtered list of courses with room names.
 * Maps over the filtered courses array and adds the corresponding room name to each course.
 * If the room is not found, sets the room name to "Room not found".
 * Returns a new array of courses with enhanced room information.
 *
 * @param filteredCourses The array of filtered courses to enhance.
 * @param rooms The array of rooms to match with course rooms.
 * @returns A new array of courses with enhanced room information.
 */
  const enhancedCourses = filteredCourses.map((course) => ({
    ...course,
    room:
      rooms.find((room) => room.id === course.room)?.name || "Room not found",
  }));

  return (
    <div className="p-10">
      <div className="rounded-lg ">
        <div className="sm:flex sm:items-center">
          <div className="sm:flex-auto">
            <h1 className="text-xl font-semibold leading-6 text-gray-900">
              Courses
            </h1>
            <p className="mt-2 text-sm text-gray-700">
              A list of all the courses in your account.
            </p>
          </div>
          {User().personType !== "Customer" && (
            <div className="mt-4 sm:ml-16 sm:mt-0 sm:flex-none">
              {/* <button
                                type="button"
                                onClick={() => window.location.href = '/courses/create'}
                                className="inline-flex items-center rounded-md bg-indigo-600 px-4 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-700"
                            >
                                Add new course
                            </button> */}
              <button
                type="button"
                onClick={() => {}}
                style={{
                    marginRight: "10px",
                }}
                className="rounded-md bg-white px-3 py-2 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50"
                >
                   <p
                   className="flex items-center space-x-2 text-sm font-semibold text-gray-900 hover:text-gray-900"
                   ><ArrowDownTrayIcon title="Download report" className="h-3 w-3 text-gray-400" aria-hidden="true" />
                <CsvDownloadButton data={courses} /></p>
              </button>
              <CreateCourseModal
                courseId={selectedCourseId}
                setCourseId={setSelectedCourseId}
              />
            </div>
          )}
        </div>
      </div>
      <div className="my-4">
        <div className="flex items-center space-x-4">
          <input
            type="text"
            placeholder="Filter by course name..."
            value={courseNameFilter}
            onChange={(e) => setCourseNameFilter(e.target.value)}
            className="flex-grow rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
          />
          <select
            value={selectedCourseLevel}
            onChange={(e) => setSelectedCourseLevel(e.target.value)}
            className="rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
          >
            <option value="">All Levels</option>
            <option value="Beginner">Beginner</option>
            <option value="Intermediate">Intermediate</option>
            <option value="Advanced">Advanced</option>
          </select>
          <select
            value={selectedInstructorId}
            onChange={(e) => setSelectedInstructorId(e.target.value)}
            className="rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
          >
            <option value="">All Instructors</option>
            {instructors.map((instructor) => (
              <option key={instructor.id} value={instructor.id}>
                {instructor.name}
              </option>
            ))}
          </select>
          {User().personType !== "Customer" && (
            <select
              value={selectedCourseState}
              onChange={(e) => setSelectedCourseState(e.target.value)}
              className="rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            >
              <option value="">All States</option>
              <option value="Approved">Approved</option>
              <option value="Denied">Denied</option>
              <option value="Inactive">Inactive</option>
              <option value="Awaiting Approval">Awaiting Approval</option>
            </select>
          )}

          <button
            onClick={fetchCourses}
            className="rounded-full p-2 text-blue-500 hover:text-blue-600"
            title="Reload Courses"
          >
            <FontAwesomeIcon icon={faSync} size="lg" />
          </button>
        </div>
      </div>

      <CourseTable
        courses={enhancedCourses}
        setCourses={setCourses}
        onCourseSelect={handleCourseClick}
        onCourseEdit={courseEdit}
      />
    </div>
  );
}
