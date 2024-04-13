import React, { useEffect, useState } from 'react';
import httpClient from '../../services/http';
import User from "../../services/user";
import CreateInstructorModal from "../../components/People/CreateInstructorModal";
const People = () => {

    /**
 * State variables for customers, instructors, and modal visibility.
 * - customers: Array of customer data.
 * - instructors: Array of instructor data.
 * - showModal: Boolean indicating whether the modal is currently visible.
 */
    const [customers, setCustomers] = useState([]);
    const [instructors, setInstructors] = useState([]);
    const [showModal, setShowModal] = useState(true);
    // Fetch customers
    useEffect(() => {
        httpClient('/customers', 'GET')
            .then(res => {
                setCustomers(res.data);
            })
            .catch(err => {
                console.log('Error fetching customers:', err);
            });
    }, []);

    // Fetch instructors
    useEffect(() => {
        httpClient('/instructors', 'GET') // Adjust this endpoint as necessary
            .then(res => {
                setInstructors(res.data);
            })
            .catch(err => {
                console.log('Error fetching instructors:', err);
            });
    }, []);
    /**
     * Shows the modal when the "Add Instructor" button is clicked.
     */
    const handleAddInstructor = () => {
        setShowModal(true); // Show the modal when the button is clicked
    };

    /**
 * Hides the modal when the close button is clicked.
 */
    const handleCloseModal = () => {
        setShowModal(false); // Hide the modal
    };

    return (
        <div className="px-4 sm:px-6 lg:px-8 pt-5">
            <div className='flex justify-between'>
                <h1 className='text-2xl font-semibold text-gray-900'>
                    People
                </h1>
                <h5>
                </h5>
            <div className="flex justify-end">
                <CreateInstructorModal hide={!showModal} setInstructorId={() => {}} />
            </div>
            </div>
            <br/>
                
            <div className="flex">

                <div className="flex-1 min-w-0 mr-2 ring-1 ring-gray-300 rounded-lg">
                                        <h2 className="text-lg font-semibold leading-6 text-gray-900 pl-4 py-2">Customers</h2>

                    <table className="min-w-full divide-y divide-gray-300">
                        <thead>
                        <tr>
                            <th className="py-3.5 pl-4 pr-3 text-left text-sm font-semibold text-gray-900 sm:pl-6">Name</th>
                            <th className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900 lg:table-cell">Email</th>
                            <th className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900 lg:table-cell">Phone #</th>
                            {/* <th className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900 lg:table-cell">ID</th> */}

                        </tr>
                        </thead>
                        <tbody>
                        {customers.map((customer) => (
                            <tr key={customer.id}>
                                <td className="px-3 py-4">{customer.name}</td>
                                <td className="px-3 py-4">{customer.email}</td>
                                <td className="px-3 py-4">{customer.phoneNumber}</td>
                                {/* <td className="px-3 py-4">{customer.id}</td> */}

                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>


                <div className="flex-1 min-w-0 ml-2 ring-1 ring-gray-300 rounded-lg">
                    <h2 className="text-lg font-semibold leading-6 text-gray-900 pl-4 py-2">Instructors</h2>
                    <table className="min-w-full divide-y divide-gray-300">
                        <thead>
                        <tr>
                            <th className="py-3.5 pl-4 pr-3 text-left text-sm font-semibold text-gray-900 sm:pl-6">Name</th>
                            <th className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900 lg:table-cell">Email</th>
                            <th className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900 lg:table-cell">Phone #</th>
                            {/* <th className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900 lg:table-cell">ID</th> */}
                        </tr>
                        </thead>
                        <tbody>
                        {instructors.map((instructor) => (
                            <tr key={instructor.id}>

                                <td className="px-3 py-4">{instructor.name}</td>
                                <td className="px-3 py-4">{instructor.email}</td>
                                <td className="px-3 py-4">{instructor.phoneNumber}</td>
                                {/* <td className="px-3 py-4">{instructor.id}</td> */}
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};

export default People;
