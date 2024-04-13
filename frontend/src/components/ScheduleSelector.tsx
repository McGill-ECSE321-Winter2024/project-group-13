import { Dialog, Transition } from "@headlessui/react";
import { XMarkIcon } from "@heroicons/react/24/outline";
import React, { useEffect } from "react";
import { useState } from "react";
import { Fragment } from "react/jsx-runtime";
import { Schedule } from "../helpers/types";
import _, { set } from "lodash";

export default function ScheduleSelector({
  schedule,
  setSchedule,
}: {
  schedule: Schedule;
  setSchedule: (schedule: Schedule) => void;
}) {
  const [open, setOpen] = useState(false);
  const [unusedDays, setUnusedDays] = useState<string[]>([]);
  const [selectedDay, setSelectedDay] = useState<string>('monday');

  useEffect(() => {
    const days = [
      "monday",
      "tuesday",
      "wednesday",
      "thursday",
      "friday",
      "saturday",
      "sunday",
    ];
    const usedDays = Object.keys(schedule);
    const unusedDays = days.filter((day) => !usedDays.includes(day));
    setUnusedDays(unusedDays);
  }, [schedule]);

  useEffect(() => {
    setUnusedDays([
      "monday",
      "tuesday",
      "wednesday",
      "thursday",
      "friday",
      "saturday",
      "sunday",
    ]);
  }, []);

  return (
    <div
      style={{
        // zIndex: 9999,
      }}
    >
        <button
        onClick={() => setOpen(true)}
        type="button"
        className="block w-full rounded-md bg-white px-3.5 py-2.5 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50"
        
      >
        Set schedule    
      </button>
      <Transition.Root show={open} as={Fragment}>
        <Dialog as="div" className="relative z-10" onClose={setOpen}>
          <div className="fixed inset-0" />

          <div className="fixed inset-0 overflow-hidden">
            <div className="absolute inset-0 overflow-hidden">
              <div className="pointer-events-none fixed inset-y-0 right-0 flex max-w-full pl-10">
                <Transition.Child
                  as={Fragment}
                  enter="transform transition ease-in-out duration-500 sm:duration-700"
                  enterFrom="translate-x-full"
                  enterTo="translate-x-0"
                  leave="transform transition ease-in-out duration-500 sm:duration-700"
                  leaveFrom="translate-x-0"
                  leaveTo="translate-x-full"
                >
                  <Dialog.Panel className="pointer-events-auto w-screen max-w-md">
                    <div className="flex h-full flex-col overflow-y-scroll bg-white py-6 shadow-xl">
                      <div className="px-4 sm:px-6">
                        <div className="flex items-start justify-between">
                          <Dialog.Title className="text-base font-semibold leading-6 text-gray-900">
                            Panel title
                          </Dialog.Title>
                          <div className="ml-3 flex h-7 items-center">
                            <button
                              type="button"
                              className="relative rounded-md bg-white text-gray-400 hover:text-gray-500 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
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
                      </div>
                      <div className="relative mt-6 flex-1 px-4 sm:px-6">
                        <div>
                         <h3 className="text-lg font-semibold text-gray-900 py-5">
                            Schedule Builder
                        </h3>
                          {unusedDays.length ? <div className="pb-6 mb-6 border-b border-gray-200">
                            <div className="flex items-center justify-between mb-2">
                              <label
                                htmlFor="timezones"
                                className="text-sm font-medium text-gray-900"
                              >
                                Add a day to the schedule
                              </label>
                              
                            </div>
                            <div className="flex items-center">
                              <select
                                id="timezones"
                                name="timezone"
                                value={selectedDay}
                                onChange={(e) => setSelectedDay(e.target.value)}
                                className="flex-1 bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                                required
                              >
                                {unusedDays.map((day) => (
                                  <option value={day}>
                                    {_.capitalize(day)}
                                  </option>
                                ))}
                              </select>
                              <button
                                onClick={() => {
                                  setSchedule({
                                    ...schedule,
                                    [selectedDay]: {
                                      start: "09:00",
                                      end: "10:00",
                                    },
                                  });
                                  setUnusedDays(
                                    unusedDays.filter(
                                      (day) => day !== selectedDay
                                    )
                                  );
                                  if(unusedDays.length >= 1){
                                    setSelectedDay(unusedDays[0]);
                                  }
                                }}
                                type="button"
                                className="rounded-md bg-white px-3.5 py-2.5 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50 ml-2"
                              >
                                Add day
                              </button>
                            </div>
                          </div> : null}

                          {Object.entries(schedule)
                          .sort(([a], [b]) => {
                            const days = [
                              "monday",
                              "tuesday",
                              "wednesday",
                              "thursday",
                              "friday",
                              "saturday",
                              "sunday"
                            ];
                            return days.indexOf(a) - days.indexOf(b);
                          
                          })
                          .map(([day, hours]) => (
                            <div className="mb-6">
                              <div className="flex items-center justify-between">
                                <div className="flex items-center min-w-[4rem]">
                                  <input
                                    defaultChecked
                                    id="monday"
                                    name="days"
                                    type="checkbox"
                                    defaultValue="monday"
                                    className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 focus:border-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2"
                                  />
                                  <label
                                    htmlFor="monday"
                                    className="ms-2 text-sm font-medium text-gray-900 dark:text-gray-300"
                                  >
                                    {_.capitalize(day).slice(0, 3)}
                                  </label>
                                </div>
                                <div className="w-full max-w-[7rem]">
                                  <label
                                    htmlFor="start-time-monday"
                                    className="sr-only"
                                  >
                                    Start time:
                                  </label>
                                  <div className="relative">
                                    <div className="absolute inset-y-0 end-0 top-0 flex items-center pe-3.5 pointer-events-none">
                                      <svg
                                        className="w-4 h-4 text-gray-500 dark:text-gray-400"
                                        aria-hidden="true"
                                        xmlns="http://www.w3.org/2000/svg"
                                        fill="currentColor"
                                        viewBox="0 0 24 24"
                                      >
                                        <path
                                          fillRule="evenodd"
                                          d="M2 12C2 6.477 6.477 2 12 2s10 4.477 10 10-4.477 10-10 10S2 17.523 2 12Zm11-4a1 1 0 1 0-2 0v4a1 1 0 0 0 .293.707l3 3a1 1 0 0 0 1.414-1.414L13 11.586V8Z"
                                          clipRule="evenodd"
                                        />
                                      </svg>
                                    </div>
                                    <input
                                      type="time"
                                      id="start-time-monday"
                                      name="start-time-monday"
                                      className="bg-gray-50 border leading-none border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                                      defaultValue="00:00"
                                      value={hours.start}
                                    min="00:00"
                                    max="23:59"
                                    onChange={(e) => {
                                        setSchedule({
                                            ...schedule,
                                            [day]: {
                                                ...hours,
                                                start: e.target.value,
                                            },
                                        });
                                    }}
                                      required
                                    />
                                  </div>
                                </div>
                                <div className="w-full max-w-[7rem]">
                                  <label
                                    htmlFor="end-time-monday"
                                    className="sr-only"
                                  >
                                    End time:
                                  </label>
                                  <div className="relative">
                                    <div className="absolute inset-y-0 end-0 top-0 flex items-center pe-3.5 pointer-events-none">
                                      <svg
                                        className="w-4 h-4 text-gray-500 dark:text-gray-400"
                                        aria-hidden="true"
                                        xmlns="http://www.w3.org/2000/svg"
                                        fill="currentColor"
                                        viewBox="0 0 24 24"
                                      >
                                        <path
                                          fillRule="evenodd"
                                          d="M2 12C2 6.477 6.477 2 12 2s10 4.477 10 10-4.477 10-10 10S2 17.523 2 12Zm11-4a1 1 0 1 0-2 0v4a1 1 0 0 0 .293.707l3 3a1 1 0 0 0 1.414-1.414L13 11.586V8Z"
                                          clipRule="evenodd"
                                        />
                                      </svg>
                                    </div>
                                    <input
                                      type="time"
                                      id="end-time-monday"
                                      name="end-time-monday"
                                      className="bg-gray-50 border leading-none border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5"
                                      min={hours.start}
                                      defaultValue="00:00"
                                        value={hours.end}
                                    max="23:59"
                                    onChange={(e) => {
                                        setSchedule({
                                            ...schedule,
                                            [day]: {
                                                ...hours,
                                                end: e.target.value,
                                            },
                                        });
                                    }}
                                      required
                                    />
                                  </div>
                                </div>
                                <div>
                                  <button
                                    onClick={() => {
                                        setSchedule(_.omit(schedule, day));
                                        setUnusedDays([...unusedDays, day]);
                                    }}
                                    type="button"
                                    className="inline-flex items-center p-1.5 text-sm font-medium text-center text-gray-500 hover:text-gray-800 hover:bg-gray-200 rounded-lg focus:outline-none dark:text-gray-400 dark:hover:text-gray-100"
                                  >
                                    <svg
                                      className="w-5 h-5"
                                      aria-hidden="true"
                                      xmlns="http://www.w3.org/2000/svg"
                                      width={24}
                                      height={24}
                                      fill="currentColor"
                                      viewBox="0 0 24 24"
                                    >
                                      <path
                                        fillRule="evenodd"
                                        d="M8.586 2.586A2 2 0 0 1 10 2h4a2 2 0 0 1 2 2v2h3a1 1 0 1 1 0 2v12a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V8a1 1 0 0 1 0-2h3V4a2 2 0 0 1 .586-1.414ZM10 6h4V4h-4v2Zm1 4a1 1 0 1 0-2 0v8a1 1 0 1 0 2 0v-8Zm4 0a1 1 0 1 0-2 0v8a1 1 0 1 0 2 0v-8Z"
                                        clipRule="evenodd"
                                      />
                                    </svg>
                                    <span className="sr-only">Delete</span>
                                  </button>
                                </div>
                              </div>
                            </div>
                          ))}
                          <div className="grid grid-cols-2 gap-4 bottom-4 left-0 w-full md:px-4 md:absolute">
                            <button
                                onClick={() => {
                                    setSchedule({});
                                    setUnusedDays([
                                        "monday",
                                        "tuesday",
                                        "wednesday",
                                        "thursday",
                                        "friday",
                                        "saturday",
                                        "sunday",
                                    ]);
                                }}
                              type="button"
                              data-drawer-hide="drawer-timepicker"
                              className="py-2.5 px-5 text-sm font-medium text-gray-900 bg-gray-50 border border-gray-300 rounded-lg hover:bg-gray-100 focus:outline-none"
                            >
                              Clear selection
                            </button>
                            <button
                              type="button"
                              onClick={() => setOpen(false)}

                              className="text-white w-full py-2.5 px-5 text-sm font-medium bg-blue-700 hover:bg-blue-800 focus:outline-none focus:ring-4 focus:ring-blue-300 rounded-lg"
                            >
                              OK
                            </button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </Dialog.Panel>
                </Transition.Child>
              </div>
            </div>
          </div>
        </Dialog>
      </Transition.Root>
      {/* drawer init and show */}
    </div>
  );
}
