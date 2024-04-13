import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import httpClient from "../../services/http";
import { CourseDTO, Schedule } from "../../helpers/types";
import CourseStatusBadge from "../../components/Course/CourseStatusBadge";
import moment from "moment";
import User from "../../services/user";
import FullCalendar from "@fullcalendar/react";
import timeGridPlugin from '@fullcalendar/timegrid'
import dayGridPlugin from "@fullcalendar/daygrid";
import { CourseState } from "../../helpers/enums"; // Adjust the path as necessary
import CreateCourseModal from "../../components/Course/CreateCourseModal";


interface ViewCourseProps {
  courseId: string; // Adjust if needed based on how you're passing props
}

export default function ViewCourse() {
  const { courseId } = useParams<{ courseId: string }>();
  const [courseEditId, setCourseEditId] = useState<string | null>(null);
  const [course, setCourse] = useState<CourseDTO | null>(null);
  const [schedule, setSchedule] = useState<Schedule | null>(null);
  const [viewMode, setViewMode] = useState("table"); // 'table' or 'calendar'
  const isOwner = User().personType === "Owner";
  const isInstructor = User().personType === "Instructor";
  const [dropdownVisible, setDropdownVisible] = useState(false);
  const [editableCourse, setEditableCourse] = useState<{
    name: string;
    description: string;
  }>({ name: "", description: "" });
  const [selectedInstructor, setSelectedInstructor] = useState("");
  const [courseState, setCourseState] = useState<CourseState>(
    CourseState.AwaitingApproval
  ); // default state or fetched from the course
  const [customers, setCustomers] = useState([]);

  useEffect(() => {
    const fetchInstructors = async () => {
      try {
        const res = await httpClient("/instructors", "GET");
        setInstructors(res.data);
      } catch (err) {
        console.log(err);
      }
    };
    const fetchCustomers = async () => {
      try {
        const response = await httpClient(`/courses/${courseId}/customers`, "GET");
        if (response.status === 200) {
          setCustomers(response.data);
        } else {
          throw new Error('Failed to fetch customers');
        }
      } catch (error) {
        console.error("Error fetching customers:", error);
        // Handle the error state appropriately, e.g., set an error message state
      }
    };

    const fetchCourseDetails = async () => {
      try {
        console.log("Fetching course data for ID:", courseId);
        const res = await httpClient(`/courses/${courseId}`, "GET");
        console.log("Course data fetched:", res.data);
        if (res.data) {
          const courseData = {
            ...res.data,
            courseStartDate: new Date(res.data.courseStartDate),
            courseEndDate: new Date(res.data.courseEndDate),
          };
          setCourse(courseData);
          // Set the state for the editable fields
          setEditableCourse({
            name: courseData.name,
            description: courseData.description,
          });
          setCourseState(courseData.courseState); // Assuming courseState is in the fetched data
          setSelectedInstructor(courseData.instructor); // Assuming instructor ID is directly in the fetched data
          // Fetch the schedule as soon as the course details are loaded
          const scheduleRes = await httpClient(
            `/courses/${courseId}/schedule`,
            "GET"
          );
          setSchedule(scheduleRes.data); // Assuming res.data contains the schedule
        } else {
          console.error("Course data is not in the expected format:", res.data);
          setCourse(null);
        }
      } catch (err) {
        console.error("Error fetching course details or schedule:", err);
        setCourse(null); // Handle errors by setting course to null
      }
    };

    fetchInstructors();

    if (courseId) {
      fetchCourseDetails();
      fetchCustomers();
    }
  }, [courseId]); // Only re-run the effect if courseId changes

  const [instructors, setInstructors] = useState<
    {
      id: string;
      name: string;
    }[]
  >([]);


  const handleChange = (newState: string) => {
    if (
      course &&
      (isOwner || isInstructor) &&
      Object.values(CourseState).includes(newState as CourseState)
    ) {
      setCourse({
        ...course,
        courseState: newState as CourseState,
      });
    } else {
      console.error("Invalid course state: " + newState);
      // Handle the error state appropriately
    }
  };

  const getEvents = () => {
    if (!course || !schedule) return [];
    let events = [];
    const startDate = moment(course.courseStartDate);
    const endDate = moment(course.courseEndDate);

    while (startDate.isBefore(endDate) || startDate.isSame(endDate, "day")) {
      const dayOfWeek = startDate.format("dddd").toLowerCase(); // 'monday', 'tuesday', etc.
      const startKey = `${dayOfWeek}Start` as keyof Schedule;
      const endKey = `${dayOfWeek}End` as keyof Schedule;
      const sessionStart = schedule[startKey];
      const sessionEnd = schedule[endKey];

      if (sessionStart && sessionEnd) {
        events.push({
          title: `${course.name} Session`,
          start: startDate.format("YYYY-MM-DD") + "T" + sessionStart,
          end: startDate.format("YYYY-MM-DD") + "T" + sessionEnd,
          allDay: false,
        });
      }
      startDate.add(1, "days");
    }
    return events;
  };

  return course ? (
    <div className="bg-white p-5 rounded shadow">
      <div className="flex justify-between items-center mb-4">
        <CreateCourseModal
          hide={true}
          courseId={courseEditId}
          setCourseId={setCourseEditId}
        />
      </div>
      <div className="mx-auto ">
        <div className="mx-auto max-w-none">
          <div className="overflow-hidden bg-white sm:rounded-lg" style={{
             borderWidth: 1,
             borderColor: '#e8e8e8',
             borderStyle: 'solid',
             borderRadius: 10,
             marginBottom: -20
          }}>
            <div className=" bg-white px-4 py-5 sm:px-6">
              <div className="-ml-4 -mt-2 flex flex-wrap items-center justify-between sm:flex-nowrap">
                <div className="ml-4 mt-2">
                  <h3 className="text-base font-semibold leading-6 text-gray-900">
                    {isOwner ? (
                        // Allow owners to interact and change the course state
                        <div
                            onBlur={() => setDropdownVisible(false)}
                            tabIndex={0}
                        >
                          <CourseStatusBadge
                              status={course.courseState}
                              onClick={(e) => {
                                e.stopPropagation();
                                setDropdownVisible(!dropdownVisible);
                              }}
                          />
                          {dropdownVisible && (
                              <div className="absolute z-10 bg-white shadow-lg rounded p-2 mt-1">
                                {Object.values(CourseState).map((state) => (
                                    <div
                                        key={state}
                                        className="p-1 hover:bg-gray-200 cursor-pointer"
                                        onClick={(e) => {
                                          e.stopPropagation();
                                          handleChange(state as CourseState); // Directly pass the new state
                                          setDropdownVisible(false);
                                        }}
                                    >
                                      {state}
                                    </div>
                                ))}
                              </div>
                          )}
                        </div>
                    ) : (
                        // For instructors, just display the status badge without interactivity
                        <div>
                          <CourseStatusBadge status={course.courseState}/>
                        </div>
                    )}
                    <p className="mt-1 text-3xl font-bold tracking-tight text-gray-900 sm:text-4xl">
                      {course.name}
                    </p>
                    <p className="mt-6 text-lg leading-1 text-gray-600" style={{
                      marginTop: 0,
                      marginBottom: 10
                    }}>
                      {course.description}
                    </p><p>
                    <strong>Start Date:</strong>{" "}
                    {moment(course.courseStartDate).format("YYYY-MM-DD")}
                  </p>
                    <p>
                      <strong>End Date:</strong>{" "}
                      {moment(course.courseEndDate).format("YYYY-MM-DD")}
                    </p>
                    <p>
                      <strong>Instructor:</strong>{" "}
                      {instructors.find(i => i.id === course.instructor)?.name || 'Instructor not found'}
                    </p>


                  </h3>
                </div>
                <div className="ml-4 mt-2  gap-5 align-top">
                  <button
                      style={{ marginRight: "10px" }}
                    className="rounded-md bg-white px-3.5 py-2.5 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50"
                    onClick={() =>
                      setViewMode(viewMode === "table" ? "calendar" : "table")
                    }
                  >
                    Switch to {viewMode === "table" ? "Calendar" : "Table"} View
                  </button>

                  <button
                    className="rounded-md bg-indigo-600 px-3.5 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                    onClick={async () => {
                      setCourseEditId(null);
                      await new Promise((resolve) => setTimeout(resolve, 100)); // Wait for the modal to close
                      setCourseEditId(courseId);
                    }}
                  >
                    Edit course
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="lg:ml-auto lg:pl-4 lg:pt-4">
        <div className="lg:max-w-lg">
          <h2 className="text-base font-semibold leading-7 text-indigo-600">
            <div className="flex space-between w-full">
              <div className="flex-auto	"></div>
              <div className="flex-auto	justify-end"></div>
            </div>
          </h2>

          <dl className="mt-10 max-w-xl space-y-8 text-base leading-7 text-gray-600 lg:max-w-none">
            {/* {features.map((feature) => (
                  <div key={feature.name} className="relative pl-9">
                    <dt className="inline font-semibold text-gray-900">
                      <feature.icon className="absolute left-1 top-1 h-5 w-5 text-indigo-600" aria-hidden="true" />
                      {feature.name}
                    </dt>{' '}
                    <dd className="inline">{feature.description}</dd>
                  </div>
                ))} */}
          </dl>
        </div>
      </div>
      

      <div className="flex">
        {User().personType !== "Customer ?" ?
            <div className="w-1/3" style={{
              borderWidth: 1,
              borderColor: '#e8e8e8',
              borderStyle: 'solid',
              padding: 20,
              paddingTop: 15,
              borderRadius: 10,
              marginRight: 20
            }}>
              <h2
                  className="text-base font-semibold leading-7 " style={{fontSize: 20, marginBottom: 20}}
              >
                Participants
              </h2>
              <table className="w-full divide-y divide-gray-300 table-auto">
                <thead>
                <tr>
                </tr>
                </thead>
                <tbody>
                {customers.map((customer) => (
                    <tr key={customer.id}>
                      <td
                          className="px-2 py-3.5 text-sm text-gray-500"
                      >
                        {customer.name}
                      </td>
                    </tr>
                ))}
                </tbody>
              </table>
            </div> : null}
        <div className="w-2/3" style={{
          borderWidth: 1,
          borderColor: '#e8e8e8',
          borderStyle: 'solid',
          padding: 20,
          paddingTop: 15,
          borderRadius: 10
        }}>
            <h2 className="text-base font-semibold leading-7 " style={{
                fontSize: 20,
                marginBottom: 20
            }}>
                Course sessions schedule
            </h2>
          {viewMode === "calendar" ? (
            <FullCalendar
              plugins={[dayGridPlugin, timeGridPlugin]}
              initialView="timeGridWeek"
              events={getEvents()}
              timeZone="local"
              headerToolbar={{
                left: "prev,next today",
                center: "title",
                right: "dayGridMonth,dayGridWeek,dayGridDay",
              }}
              buttonText={{
                today: "Today",
                month: "Month",
                week: "Week",
                day: "Day",
              }}
              eventColor="#007bff"
            />
          ) : (
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-100">
                <tr>
                  <th
                    scope="col"
                    className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                  >
                    Date
                  </th>
                  <th
                    scope="col"
                    className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                  >
                    Session Time
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {getEvents().map((event, index) => {
                  // Extract and format date and time
                  const [date, time] = event.start.split("T");
                  const formattedDate = moment(date).format("YYYY-MM-DD");
                  const formattedTime = moment(time, "HH:mm:ss").format(
                    "hh:mm A"
                  ); // or 'HH:mm' for 24-hour time

                  return (
                    <tr key={index}>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {formattedDate}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {formattedTime}
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </div>
  ) : (
    <p>Loading...</p>
  ); // Improved loading and error handling
}
