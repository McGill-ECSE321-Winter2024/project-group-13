// CourseTable.tsx
import React, { useState, useMemo, useEffect } from "react";
import { CourseDTO } from "../../helpers/types";
import moment from "moment";
import httpClient from "../../services/http";
import User from "../../services/user";
import { CourseState } from "../../helpers/enums";
import CourseStatusBadge from "./CourseStatusBadge";
import RegisterToCourseModal from "./RegisterToCourseModal";
import { CheckBadgeIcon } from "@heroicons/react/24/outline";
import _ from "lodash";

function classNames(...classes) {
  return classes.filter(Boolean).join(" ");
}

const CourseTable = ({
  courses,
  setCourses,
  onCourseSelect,
  onCourseEdit,
}: {
  courses: CourseDTO[];
  setCourses: Function;
  onCourseSelect: any;
  onCourseEdit: (courseId: string) => void;
}) => {
  const [sortField, setSortField] = useState(null);
  const [sortOrder, setSortOrder] = useState("asc");
  const [instructors, setInstructors] = useState([]);
  const [registrations, setRegistrations] = useState<
    {
      id: number;
      rating: number;
      course: {
        id: string;
      };
    }[]
  >([]);
  const [dropdownVisible, setDropdownVisible] = useState<string | null>(null);

  const handleDropdown = (courseId: string) => {
    setDropdownVisible((prev) => (prev === courseId ? null : courseId));
  };

  useEffect(() => {
    httpClient("/instructors", "GET")
      .then((res) => {
        setInstructors(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
    if (User().personType === "Customer") {
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
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      setSortField(field);
      setSortOrder("asc");
    }
  };

  const approveCourse = (courseId) => {
    // approve course
    httpClient("/courses/" + courseId + "/approve", "POST")
      .then((res) => {
        window.location.reload();
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const sortedCourses = useMemo(() => {
    let sortedData = [...courses];
    if (sortField !== null) {
      sortedData.sort((a, b) => {
        if (a[sortField] < b[sortField]) {
          return sortOrder === "asc" ? -1 : 1;
        }
        if (a[sortField] > b[sortField]) {
          return sortOrder === "asc" ? 1 : -1;
        }
        return 0;
      });
    }
    return sortedData.map((course) => {
      return {
        ...course,
        room: course.roomDTO ? course.roomDTO.name : "No room assigned", // use RoomDTO.name if available
      };
    });
  }, [courses, sortField, sortOrder]);

  const updateCourseState = async (courseId, newState) => {
    try {
      const response = await httpClient(`/courses/${courseId}/state`, "PUT", {
        state: newState,
      });
      if (response.status === 200) {
        // Assuming successful state update, now update local state:
        setCourses((prevCourses) =>
          prevCourses.map((course) =>
            course.id === courseId
              ? { ...course, courseState: newState }
              : course
          )
        );
        setDropdownVisible(null); // Hide the dropdown
      } else {
        throw new Error(
          "Failed to update course state with response: " + response.status
        );
      }
    } catch (error) {
      console.error("Failed to update course state:", error);
      // Handle errors, e.g., by showing a notification
    }
  };

  const SortIcon = ({ isAsc }) => (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      viewBox="0 0 20 20"
      fill="currentColor"
      className="inline w-4 h-4 ml-2"
    >
      {isAsc ? (
        <path
          fillRule="evenodd"
          d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
          clipRule="evenodd"
        />
      ) : (
        <path
          fillRule="evenodd"
          d="M14.707 12.707a1 1 0 01-1.414 0L10 9.414 6.707 12.707a1 1 0 11-1.414-1.414l4-4a1 1 0 011.414 0l4 4a1 1 0 010 1.414z"
          clipRule="evenodd"
        />
      )}
    </svg>
  );

  return sortedCourses?.length ? (
    <div>
      <div className="-mx-4 mt-10 ring-1 ring-gray-300 sm:mx-0 sm:rounded-lg ">
        <table className="w-full divide-y divide-gray-300 table-auto">
          <thead>
            <tr>
              {[
                "name",
                "status",
                "level",
                "start Date",
                "end Date",
                "room",
                "instructor",
                "cost",
                "Actions"
              ].map((field) => (
                <th
                  scope="col"
                  className="py-3.5 text-left text-sm font-semibold text-gray-900 sm:pl-3"
                >
                  {_.capitalize(field)}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {sortedCourses
            .sort((a, b) => a.name.localeCompare(b.name))
            .map((course) => (
              <tr key={course.id} className="hover:bg-gray-100">
                <td
                  className={classNames(
                    " px-2 py-3.5 text-sm text-gray-500 lg:table-cell"
                  )}
                  onClick={() => onCourseSelect(course.id)}
                >
                  {course.name}
                </td>
                <td
                  className={classNames(
                    "",
                    "px-3 py-3.5 text-sm text-gray-500 lg:table-cell"
                  )}
                  
                >
                  {User().personType !== "Customer" && (
                    <div onBlur={() => setDropdownVisible(null)} tabIndex={0}>
                      <CourseStatusBadge
                        status={course.courseState}
                        onClick={(e) => {
                          e.stopPropagation();
                          setDropdownVisible(course.id);
                        }}
                      />
                      {dropdownVisible === course.id && (
                        <div className="absolute bg-white shadow-lg rounded p-2 mt-1">
                          {Object.values(CourseState).map((state) => (
                            <div
                              key={state}
                              className="p-1 hover:bg-gray-200 cursor-pointer"
                              onClick={(e) => {
                                e.stopPropagation();
                                updateCourseState(course.id, state);
                                setDropdownVisible(null);
                              }}
                            >
                              {state}
                            </div>
                          ))}
                        </div>
                      )}
                    </div>
                  )}
                  {User().personType === "Customer" &&
                  !registrations.find((r) => r.course.id === course.id) ? (
                    <RegisterToCourseModal
                      courseId={course.id}
                      costPerHour={course.hourlyRateAmount}
                    />
                  ) : null}
                  {User().personType === "Customer" &&
                  registrations.find((r) => r.course.id === course.id) ? (
                    <div className="flex items-center gap-1 text-green-900 text-sm font-semibold">
                      <CheckBadgeIcon className="h-6 w-6 text-green-500" />
                      Registered
                    </div>
                  ) : null}
                </td>
                <td
                  className={classNames(
                    "",
                    "px-3 py-3.5 text-sm text-gray-500 lg:table-cell"
                  )}
                  onClick={() => onCourseSelect(course.id)}
                >
                  {course.level}
                </td>

                <td
                  className={classNames(
                    "",
                    "px-0 py-3.5 text-sm text-gray-500 lg:table-cell"
                  )}
                  onClick={() => onCourseSelect(course.id)}
                >
                  {moment(course.courseStartDate).format("MMM DD, YYYY")}
                </td>
                <td
                  className={classNames(
                    "",
                    "px-0 py-3.5 text-sm text-gray-500 lg:table-cell"
                  )}
                  onClick={() => onCourseSelect(course.id)}
                >
                  {moment(course.courseEndDate).format("MMM DD, YYYY")}
                </td>
                <td
                  className={classNames(
                    "",
                    "px-3 py-3.5 text-sm text-gray-500 lg:table-cell"
                  )}
                  onClick={() => onCourseSelect(course.id)}
                >
                  {course.roomDTO ? course.roomDTO.name : "No room assigned"}
                </td>
                <td
                  className={classNames(
                    "",
                    "px-3 py-3.5 text-sm text-gray-500 lg:table-cell"
                  )}
                  onClick={() => onCourseSelect(course.id)}
                >
                  {instructors.find((i) => i.id === course.instructor)?.name ||
                    "Instructor not found"}
                </td>
                <td
                  className={classNames(
                    "",
                    "px-3 py-3.5 text-sm text-gray-500 lg:table-cell"
                  )}
                  onClick={() => onCourseSelect(course.id)}
                >{`$${course?.hourlyRateAmount?.toFixed(2)}`}</td>
                <td
                  className={classNames(
                    "",
                    "px-3 py-3.5 text-sm text-gray-500 lg:table-cell"
                  )}
                >
                  {User().personType !== "Customer" ? <button
                    onClick={() => onCourseEdit(course.id)}
                    className="text-blue-500 hover:text-blue-600"
                  >
                    Edit
                  </button>: null}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  ): <div className="text-center mt-10">
  <p className="text-gray-500">No courses found.</p>
</div>
};

export default CourseTable;
