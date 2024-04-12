import {Fragment, useEffect, useRef, useState} from "react";
import { Dialog, Transition } from "@headlessui/react";
import { XMarkIcon } from "@heroicons/react/24/outline";
import React from "react";
import httpClient from "../../services/http";
import {ArrowUturnLeftIcon} from "@heroicons/react/24/solid";
import SuccessModal from "../SuccessModal";
import User from "../../services/user";

export default function CreateInstructorModal({
                                                  hide=false,
                                                  instructorId,
                                                  setInstructorId,
                                              }: {
    hide?: boolean;
    instructorId?: string;
    setInstructorId: (id: string) => void;
}) {
    const [open, setOpen] = useState(false);
    const [SuccessModalOpen, setSuccessModalOpen] = useState(false);
    const cancelButtonRef = useRef(null);

    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");

    useEffect(() => {
        if (instructorId) {
            fetchInstructor();
            setOpen(true);
        }
    }, [instructorId]);

    const fetchInstructor = () => {
        httpClient(`/instructors/${instructorId}`, "GET")
            .then((res) => {
                const instructor = res.data;
                setName(instructor.name);
                setEmail(instructor.email);
            })
            .catch((err) => {
                console.log(err);
            });

    }

    useEffect(() => {
        if (instructorId) {
            fetchInstructor();
        } else {
            setName("");
            setEmail("");
        }
    }, [instructorId]);

    const handleSubmit = async (e: any) => {
        e.preventDefault();
        const body = {
            name,
            email,
            password,
            phoneNumber,
            sportCenterId: User().personSportCenterId,
        };

        console.log(body);

        try {
            let id = instructorId;
            if (!instructorId) {

                const res = await httpClient("/instructors", "POST", body);
                id = res.data.message;

            }else {
                await httpClient(`/instructors/${id}/name`, "PUT", body.name);
                await httpClient(`/instructors/${id}/email`, "PUT", body.email);
                await httpClient(`/instructors/${id}/phone_number`, "PUT", body.phoneNumber);
                await httpClient(`/instructors/${id}/password`, "PUT", body.password);
            }
            setSuccessModalOpen(true);
            window.location.reload();
        } catch (error) {
            alert(error.response.data);
        }
    };

    return (

        <React.Fragment>
            {!hide ? <button
                type="button"
                onClick={() => {
                    setInstructorId(null);
                    setOpen(true)
                }}
                className="inline-flex items-center rounded-md bg-indigo-600 px-4 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-700"
            >
                Add new instructor
            </button> : null}
            <SuccessModal
                action={() => (window.location.href = "/people")}
                message={instructorId ? "Instructor updated successfully" : "Instructor created successfully"}
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
                                                            {instructorId ? `Edit ${name}` : `Create a new instructor`}
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
                                                                        Instructor name
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
                                                                            htmlFor="email"
                                                                            className="block text-sm font-medium leading-6 text-gray-900"
                                                                        >
                                                                            Email
                                                                        </label>
                                                                        <div className="relative mt-2 rounded-md shadow-sm">
                                                                            <input
                                                                                required
                                                                                type="email"
                                                                                name="email"
                                                                                id="email"
                                                                                value={email}
                                                                                onChange={(e) =>
                                                                                    setEmail(e.target.value)
                                                                                }
                                                                                className="block w-full rounded-md border-0 py-1.5 pl-7 pr-20 text-gray-900 ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6 [appearance:textfield] [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
                                                                                placeholder="example@example.com"
                                                                            />
                                                                        </div>
                                                                    </div>
                                                                    <label htmlFor="password" className="block text-sm font-medium leading-6 text-gray-900">
                                                                        Password
                                                                    </label>
                                                                    <div className="relative mt-2 rounded-md shadow-sm">
                                                                        <input
                                                                            required
                                                                            type="password"
                                                                            name="password"
                                                                            id="password"
                                                                            value={password}
                                                                            onChange={(e) => setPassword(e.target.value)}
                                                                            className="block w-full rounded-md border-0 py-1.5 pl-7 pr-20 text-gray-900 ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6 [appearance:textfield] [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
                                                                            placeholder="Enter password"
                                                                        />
                                                                    </div>
                                                                    <label htmlFor="phone_number" className="block text-sm font-medium leading-6 text-gray-900">
                                                                        Phone Number
                                                                    </label>
                                                                    <div className="relative mt-2 rounded-md shadow-sm">
                                                                        <input
                                                                            required
                                                                            type="tel"
                                                                            name="phone_number"
                                                                            id="phone_number"
                                                                            value={phoneNumber}
                                                                            onChange={(e) => setPhoneNumber(e.target.value)}
                                                                            className="block w-full rounded-md border-0 py-1.5 pl-7 pr-20 text-gray-900 ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6 [appearance:textfield] [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
                                                                            placeholder="Enter phone number"
                                                                        />
                                                                    </div>
                                                                </div>
                                                                <div className="mt-6 flex items-center justify-end gap-x-6">
                                                                    <button
                                                                        type="button"
                                                                        onClick={() =>
                                                                            (window.location.href = "/people")
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