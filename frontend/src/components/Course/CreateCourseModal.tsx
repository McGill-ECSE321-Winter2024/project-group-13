/*
  This example requires some changes to your config:
  
  ```
  // tailwind.config.js
  module.exports = {
    // ...
    plugins: [
      // ...
      require('@tailwindcss/forms'),
    ],
  }
  ```
*/
import { Fragment, useEffect, useRef, useState } from "react";
import { Dialog, Transition } from "@headlessui/react";
import { ArrowUturnLeftIcon, XMarkIcon } from "@heroicons/react/24/outline";
import {
} from "@heroicons/react/20/solid";
import React from "react";
import User from "../../services/user";
import { CourseDTO, RoomDTO, Schedule } from "../../helpers/types";
import httpClient from "../../services/http";
import { Level } from "../../helpers/enums";
import Datepicker, { DateRangeType } from "react-tailwindcss-datepicker";
import ScheduleSelector from "../ScheduleSelector";
import SuccessModal from "../SuccessModal";
import { h } from "@fullcalendar/core/preact";
import { set } from "lodash";


export default function CreateCourseModal({
    hide=false,
    courseId,
    setCourseId,
}: {
    hide?: boolean;
    courseId?: string;
    setCourseId: (id: string) => void;
}) {
  const [open, setOpen] = useState(false);

  const cancelButtonRef = useRef(null);

  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [level, setLevel] = useState<Level>(Level.Beginner);
  const [rate, setRate] = useState("");
  const [instructor, setInstructor] = useState(
    User().personType === "Instructor" ? User().personId : ""
  );
  const [room, setRoom] = useState<string>("");
  const [schedule, setSchedule] = useState<Schedule>({}); // [1
  const [SuccessModalOpen, setSuccessModalOpen] = useState(false);

  const [dateRange, setDateRange] = useState<DateRangeType>({
    startDate: new Date(),
    // @ts-ignore
    endDate: new Date(),
  });

  const handleValueChange = (newValue) => {
    console.log("newValue:", newValue);
    setDateRange(newValue);
  };

  const [instructors, setInstructors] = useState<
    {
      id: string;
      name: string;
    }[]
  >([]);

  const [rooms, setRooms] = useState<RoomDTO[]>([]);

  useEffect(() => {
    if (courseId) {
        fetchCourse();
        setOpen(true);
    }
  }, [courseId]);

  const fetchCourse = () => {
    httpClient(`/courses/${courseId}`, "GET")
      .then((res) => {
        const course = res.data as CourseDTO;
        setName(course.name);
        setDescription(course.description);
        setLevel(course.level as Level);
        setRate(course.hourlyRateAmount.toString());
        setInstructor(course.instructor);
        setRoom(course.room);
        setDateRange({
          startDate: new Date(course.courseStartDate),
          endDate: new Date(course.courseEndDate),
        });
      })
      .catch((err) => {
        console.log(err);
      });
  
  }

  useEffect(() => {
    if (courseId) {
      fetchCourse();
    } else {
      setName("");
      setDescription("");
      setLevel(Level.Beginner);
      setRate("");
      setInstructor(
        User().personType === "Instructor" ? User().personId : ""
      );
      setRoom("");
      setSchedule({});
      setDateRange({
        startDate: new Date(),
        // @ts-ignore
        endDate: new Date(),
      });
    }
  }, [courseId]);

  useEffect(() => {
    setName("");
    setDescription("");
    setLevel(Level.Beginner);
    setRate("");
    setInstructor("");
    httpClient("/instructors", "GET")
      .then((res) => {
        setInstructors(res.data);
      })
      .catch((err) => {
        console.log(err);
      });

    httpClient("/rooms", "GET")
      .then((res) => {
        setRooms(res.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  const handleSubmit = async (e: any) => {
    e.preventDefault();
    const body = {
      name,
      description,
      level,
      rate,
      startDate: dateRange.startDate,
      endDate: dateRange.endDate,
      instructor,
    };

    console.log(body);

    const bodySchedule = {};

    Object.entries(schedule).forEach(([key, value]) => {
      const startKey = key + "Start";
      const endKey = key + "End";
      bodySchedule[startKey] = value.start + ":00";
      bodySchedule[endKey] = value.end + ":00";
    });

    console.log(schedule, bodySchedule);
    // return;
    try {
    let id = courseId;
    if (!courseId) {

        const res = await httpClient("/courses", "POST", body.name);
        id = res.data.message;

    }
      await httpClient(`/courses/${id}/name`, "PUT", body.name);
      await httpClient(`/courses/${id}/description`, "PUT", body.description);
      await httpClient(`/courses/${id}/level`, "PUT", body.level);
      await httpClient(`/courses/${id}/rate`, "PUT", body.rate);
      await httpClient(`/courses/${id}/startDate`, "PUT", body.startDate);
      await httpClient(`/courses/${id}/endDate`, "PUT", body.endDate);
      await httpClient(`/courses/${id}/instructor`, "PUT", body.instructor);
      await httpClient(`/courses/${id}/room`, "PUT", room);
      await httpClient(
        `/courses/${id}/update-schedule-and-sessions`,
        "PUT",
        bodySchedule
      );

      setSuccessModalOpen(true);
    } catch (error) {
      alert(error.response.data);
    }
  };

  /*
  name, description, level, rate, startDate, endDate, instructor
  */

  return (
    <React.Fragment>
      {!hide ? <button
        type="button"
        onClick={() => {
            setCourseId(null);
            setOpen(true)
        }}
        className="inline-flex items-center rounded-md bg-indigo-600 px-4 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-700"
      >
        Add new course
      </button> : null}
      <SuccessModal
        action={() => (window.location.href = "/courses")}
        message={courseId ? "Course updated successfully" : "Course created successfully"}
        open={SuccessModalOpen}
        setOpen={setSuccessModalOpen}
      />

      <Transition.Root show={open} as={Fragment}>
        <Dialog as="div" className="relative z-10" onClose={setOpen}>
          <div className="fixed inset-0" />

          <div className="fixed inset-0 overflow-hidden">
            <div className="absolute inset-0 overflow-hidden">
              <div className="pointer-events-none fixed inset-y-0 right-0 flex max-w-full pl-10 sm:pl-16">
                <Transition.Child
                  as={Fragment}
                  enter="transform transition ease-in-out duration-500 sm:duration-700"
                  enterFrom="translate-x-full"
                  enterTo="translate-x-0"
                  leave="transform transition ease-in-out duration-500 sm:duration-700"
                  leaveFrom="translate-x-0"
                  leaveTo="translate-x-full"
                >
                  <Dialog.Panel className="pointer-events-auto w-screen max-w-2xl">
                    <form onSubmit={handleSubmit} className="flex h-full flex-col divide-y divide-gray-200 bg-white shadow-xl ">
                      <div className="h-0 flex-1 overflow-y-auto">
                        <div
                          className="bg-indigo-700 px-4 py-6 sm:px-6"
                          style={{
                            marginTop: "4.0rem",
                          }}
                        >
                          <div className="flex items-center justify-between">
                            <Dialog.Title className="text-base font-semibold leading-6 text-white">
                              {courseId ? `Edit ${name}` : `Create a new course`}
                            </Dialog.Title>
                            <div className="ml-3 flex h-7 items-center">
                              <button
                                type="button"
                                className="relative rounded-md bg-indigo-700 text-indigo-200 hover:text-white focus:outline-none focus:ring-2 focus:ring-white"
                                onClick={() => setOpen(false)}
                              >
                                <span className="absolute -inset-2.5" />
                                <span className="sr-only">Close panel</span>
                                <XMarkIcon
                                  className="h-6 w-6"
                                  aria-hidden="true"
                                />
                              </button>
                            </div>
                          </div>
                          <div className="mt-1">
                            <p className="text-sm text-indigo-300">
                              {User().personType !== "Instructor"
                                ? "We will send you an email notification when the course is approved."
                                : null}
                            </p>
                          </div>
                        </div>
                        <div className="flex flex-1 flex-col justify-between">
                          <div className="divide-y divide-gray-200 px-4 sm:px-6">
                            <div className="space-y-1 pb-0 pt-3">
                              <div className="grid gap-2">
                                 <div className="sm:col-span-6">
                                  <label
                                    htmlFor="first-name"
                                    className="block text-sm font-medium leading-6 text-gray-900"
                                  >
                                    Course name
                                  </label>

                                  <div className="mt-2">
                                    <input
                                      required
                                      type="text"
                                      name="name"
                                      id="name"
                                      value={name}
                                      onChange={(e) => setName(e.target.value)}
                                      className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                    />
                                  </div>
                                </div>
                                <br />
                                <div className="sm:col-span-6" style={{
                                    marginTop: "-1.5rem",
                                }}>
                                  <div>
                                    <label
                                      htmlFor="price"
                                      className="block text-sm font-medium leading-6 text-gray-900"
                                    >
                                      Price
                                    </label>
                                    <div className="relative mt-2 rounded-md shadow-sm">
                                      <div className="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3">
                                        <span className="text-gray-500 sm:text-sm">
                                          $
                                        </span>
                                      </div>
                                      <input
                                        required
                                        type="number"
                                        name="price"
                                        id="price"
                                        min={0}
                                        value={rate}
                                        onChange={(e) =>
                                          setRate(e.target.value)
                                        }
                                        className="block w-full rounded-md border-0 py-1.5 pl-7 pr-20 text-gray-900 ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6 [appearance:textfield] [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
                                        placeholder="0.00"
                                      />
                                      <div className="absolute inset-y-0 right-0 flex items-center">
                                        <label
                                          htmlFor="currency"
                                          className="sr-only"
                                        >
                                          Currency
                                        </label>
                                        <select
                                          required
                                          disabled
                                          id="currency"
                                          name="currency"
                                          className="h-full rounded-md border-0 bg-transparent py-0 pl-2 pr-7 text-gray-500 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm"
                                        >
                                          <option>CAD</option>
                                        </select>
                                      </div>
                                    </div>
                                  </div>
                                </div>

                                <div className="sm:col-span-6">
                                  <label
                                    htmlFor="email"
                                    className="block text-sm font-medium leading-6 text-gray-900"
                                  >
                                    Description
                                  </label>
                                  <div className="mt-2">
                                    <textarea
                                      required
                                      id="description"
                                      value={description}
                                      onChange={(e) =>
                                        setDescription(e.target.value)
                                      }
                                      className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                    />
                                  </div>
                                </div>

                                <div className="sm:col-span-6">
                                  <label
                                    htmlFor="country"
                                    className="block text-sm font-medium leading-6 text-gray-900"
                                  >
                                    Instructor
                                  </label>
                                  <div className="mt-2">
                                    <select
                                      disabled={
                                        User().personType === "Instructor"
                                      }
                                      required
                                      name="instructors"
                                      value={instructor}
                                      onChange={(e) =>
                                        setInstructor(e.target.value)
                                      }
                                      autoComplete="country-name"
                                      className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                    >
                                      <option value="">
                                        Select an instructor
                                      </option>
                                      {instructors.map((instructor) => (
                                        <option
                                          key={instructor.id}
                                          value={instructor.id}
                                        >
                                          {instructor.name}
                                        </option>
                                      ))}
                                    </select>
                                  </div>
                                </div>
                                <div className="sm:col-span-6">
                                  <label
                                    htmlFor="country"
                                    className="block text-sm font-medium leading-6 text-gray-900"
                                  >
                                    Level
                                  </label>
                                  <div className="mt-2">
                                    <select
                                      required
                                      name="instructors"
                                      value={level}
                                      onChange={(e) =>
                                        setLevel(e.target.value as Level)
                                      }
                                      autoComplete="country-name"
                                      className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                    >
                                      <option value="">Select a level</option>
                                      {Object.values(Level).map((level) => (
                                        <option key={level} value={level}>
                                          {level}
                                        </option>
                                      ))}
                                    </select>
                                  </div>
                                </div>

                                <div className="sm:col-span-6">
                                  <label
                                    htmlFor="city"
                                    className="block text-sm font-medium leading-6 text-gray-900 pb-2"
                                  >
                                    Select a start and end date
                                  </label>
                                  <Datepicker
                                    minDate={new Date()}
                                    value={dateRange}
                                    onChange={handleValueChange}
                                  />
                                </div>
                                <div className="sm:col-span-6">
                                  <label className="block text-sm font-medium leading-6 text-gray-900">
                                    Room
                                  </label>
                                  <div className="mt-2">
                                    <select
                                      required
                                      name="rooms"
                                      value={room}
                                      onChange={(e) => setRoom(e.target.value)}
                                      className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                    >
                                      <option value="">Select a room</option>
                                      {rooms.map((room) => (
                                        <option key={room.id} value={room.id}>
                                          {room.name}
                                        </option>
                                      ))}
                                    </select>
                                  </div>
                                  <br />
                                  {courseId ? null : <ScheduleSelector
                                    schedule={schedule}
                                    setSchedule={setSchedule}
                                  />}
                                  <div className="mt-6 flex items-center justify-end gap-x-6">
                                    <button
                                      type="button"
                                      onClick={() =>
                                        (window.location.href = "/courses")
                                      }
                                      className="flex gap-1 justify-center w-full rounded-md bg-white px-3.5 py-2.5 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50"
                                    >
                                      <ArrowUturnLeftIcon
                                        className="h-5 w-5"
                                        aria-hidden="true"
                                      />
                                      Cancel
                                    </button>
                                    <button
                                      type="submit"
                                      className="block w-full rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                                    >
                                      Save
                                    </button>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                        
                      </div>
                    </form>
                  </Dialog.Panel>
                </Transition.Child>
              </div>
            </div>
          </div>
        </Dialog>
      </Transition.Root>
    </React.Fragment>
  );
}
