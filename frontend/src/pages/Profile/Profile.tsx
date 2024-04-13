import { PhotoIcon, UserCircleIcon } from '@heroicons/react/24/solid'
import User from '../../services/user'
import { useState } from 'react';
import httpClient from '../../services/http';

export default function Profile() {

    const [name, setName] = useState(User().personName);
    const [email, setEmail] = useState(User().personEmail);
    const [phoneNumber, setPhoneNumber] = useState(User().personPhoneNumber);
    const [password, setPassword] = useState('');

    const formSubmit = async (e: any) => {
        e.preventDefault();
        try {
            await httpClient('/auth/email', 'PUT', {
                email
            });
            localStorage.setItem('user', JSON.stringify({
                personName: name,
                personEmail: email,
                personPhoneNumber: User().personPhoneNumber,
                personId: User().personId,
                personType: User().personType,
                personSportCenterId: User().personSportCenterId
            }));
        } catch (e) {
            alert(e.response.data.message);
            return;
        }
        try {
            await httpClient('/auth/phoneNumber', 'PUT', {
                phoneNumber
            });
            localStorage.setItem('user', JSON.stringify({
                personName: name,
                personEmail: email,
                personPhoneNumber: phoneNumber,
                personId: User().personId,
                personType: User().personType,
                personSportCenterId: User().personSportCenterId
            }));
        } catch (e) {
            alert(e.response.data.message);
            return;
        }

        try {
            await httpClient('/auth/password', 'PUT', {
                password
            });
        } catch (e) {
            alert(e.response.data.message);
            return;
        }

        alert('Profile updated successfully');
        window.location.reload();
    }
  return (
    <div className="space-y-10 divide-y divide-gray-900/10 p-10 pt-0">

      <div className="grid grid-cols-1 gap-x-8 gap-y-8 pt-10 md:grid-cols-3">
        <div className="px-4 sm:px-0">
        <div className="flex items-center">
  <h2 className="text-base font-semibold leading-7 text-gray-900">Your profile</h2>
  <span className="inline-flex items-center rounded-full bg-gray-100 px-2 py-1 text-xs font-medium text-gray-600 ml-1">
    {User().personType}
  </span>
</div>

          <p className="mt-1 text-sm leading-6 text-gray-600">You can edit your profile information here.</p>
          
        </div>

        <form onSubmit={formSubmit} className="bg-white shadow-sm ring-1 ring-gray-900/5 sm:rounded-xl md:col-span-2">
  <div className="px-4 py-6 sm:p-8">
    <div className="grid max-w-2xl grid-cols-1 gap-x-6 gap-y-8">
      <div>
        <label htmlFor="first-name" className="block text-sm font-medium leading-6 text-gray-900">
          Name
        </label>
        <div className="mt-2">
          <input
            type="text"
            name="name"
            id="name"
            autoComplete="given-name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            disabled
            className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6 bg-gray-100"
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
            value={email}
            onChange={(e) => setEmail(e.target.value)}
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
            value={phoneNumber}
            onChange={(e) => setPhoneNumber(e.target.value)}
            className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
          />
        </div>
      </div>
      <hr/>
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
            value={password}
            placeholder="********"
            onChange={(e) => setPassword(e.target.value)}
            className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
          />
        </div>
      </div>
      <div>
        <label htmlFor="country" className="block text-sm font-medium leading-6 text-gray-900">
          Country
        </label>
        <div className="mt-2">
          <select
            id="country"
            name="country"
            autoComplete="country-name"
            disabled
            className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:max-w-xs sm:text-sm sm:leading-6"
          >
            <option>Canada</option>
          </select>
        </div>
      </div>
    </div>
  </div>
  <div className="flex items-center justify-end gap-x-6 border-t border-gray-900/10 px-4 py-4 sm:px-8">
    <button
      type="submit"
      className="rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
    >
      Save
    </button>
  </div>
</form>

      </div>
    </div>
  )
}
