import {useState} from "react";
import httpClient from "../services/http";
import User from "../services/user";

export default function Signup() {

    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = async (e: any) => {
        e.preventDefault();
        console.log(email, password);
        httpClient('/auth/register/customer', 'POST',{
            email,
            password,
            name,
            phoneNumber
        })
        .then((res) => {
            httpClient('/auth/login', 'POST',{
                email,
                password
            }).then((res) => {
                localStorage.setItem('token', res.data.session);
                localStorage.setItem('user', JSON.stringify({
                    ...res.data,
                    session: undefined
                }));
                console.log(res.data);
                console.log(User())
                window.location.href = '/';
            });
        })
            .catch((err) => {
                alert(err.response.data.message);
            })
    }
    return (
        <>
            {/*
        This example requires updating your template:

        ```
        <html class="h-full bg-gray-50">
        <body class="h-full">
        ```
      */}
            <div className="flex min-h-full flex-1 flex-col justify-center py-12 sm:px-6 lg:px-8">
                <div className="sm:mx-auto sm:w-full sm:max-w-md">
                    <img
                        className="mx-auto h-10 w-auto"
                        src="icons/black.png"
                        alt="Your Company"
                    />

                </div>

                <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-[480px]">
                    <div className="bg-white px-6 py-12 shadow sm:rounded-lg sm:px-12">
                        <h2 className=" text-center text-2xl font-bold leading-10 tracking-tight text-gray-900">
                            Create a new account
                        </h2>
                        <form className="space-y-6" onSubmit={handleSubmit}>

                            <div>
                                <label htmlFor="name" className="block text-sm font-medium leading-6 text-gray-900">
                                    Name
                                </label>
                                <div className="mt-2">
                                    <input
                                        id="name"
                                        name="name"
                                        type="text"
                                        autoComplete="name"
                                        required
                                        onChange={(e) => setName(e.target.value)}
                                        value={name}
                                        className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                    />
                                </div>
                            </div>


                            <div>
                                <label htmlFor="email" className="block text-sm font-medium leading-6 text-gray-900">
                                    Email address
                                </label>
                                <div className="mt-2">
                                    <input
                                        id="email"
                                        name="email"
                                        type="email"
                                        autoComplete="email"
                                        required
                                        onChange={(e) => setEmail(e.target.value)}
                                        value={email}
                                        className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                    />
                                </div>
                            </div>

                            <div>
                                <label htmlFor="phone" className="block text-sm font-medium leading-6 text-gray-900">
                                    Phone Number
                                </label>
                                <div className="mt-2">
                                    <input
                                        id="phone"
                                        name="phone"
                                        type="text"
                                        autoComplete="phone"
                                        required
                                        onChange={(e) => setPhoneNumber(e.target.value)}
                                        value={phoneNumber}
                                        className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                    />
                                </div>
                            </div>

                            <div>
                                <label htmlFor="password" className="block text-sm font-medium leading-6 text-gray-900">
                                    Password
                                </label>
                                <div className="mt-2">
                                    <input
                                        id="password"
                                        name="password"
                                        type="password"
                                        autoComplete="current-password"
                                        required
                                        onChange={(e) => setPassword(e.target.value)}
                                        value={password}
                                        className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                    />
                                </div>
                            </div>

                            <div className="flex items-center justify-between">

                                <div className="text-sm leading-6">
                                    <a href="/login" className="font-semibold text-indigo-600 hover:text-indigo-500">
                                        Already have an account ? Login
                                    </a>
                                </div>
                            </div>

                            <div>
                                <button
                                    type="submit"
                                    className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                                >
                                    Sign up
                                </button>
                            </div>
                        </form>

                        
                    </div>

                </div>
            </div>
        </>
    )
}
